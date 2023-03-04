package ru.smartflex.tools.pg;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PgGenGlueFunctions {

    public void glue(PgTreeNode root) {
        ITreeHandler<PgTreeNode> ith = getITreeHandler();
        root.walkingTree(root, ith);
    }

    private ITreeHandler<PgTreeNode> getITreeHandler() {
        ITreeHandler<PgTreeNode> ith = (node) -> {
            if (!node.isPossibleGenerateBody()) {
                return;
            }

            generateBody(node);
        };
        return ith;
    }

    private void generateBody(PgTreeNode node) {

        // выставляется в true если были обращения к ХП
        AtomicBoolean wasGenerated = new AtomicBoolean(false);
        StringBuilder sb = new StringBuilder();

        generateBody(node, null, null, null, wasGenerated, sb, 0, null);

        writeGeneratedSQL(sb, node.getFuncDefined().getFuncName(), wasGenerated.get());
        if (wasGenerated.get()) {
            PgParseFunctions.addGeneratedBody(sb.toString());
        }
    }

    private void generateBody(PgTreeNode node, PgPlSqlElEnum typeOfCall, PgFuncInvoked inv, String assignPartFunc,
                              AtomicBoolean wasGenerated, StringBuilder sb, int indexNested, TransferParameters outerParameters) {
        boolean rootNodeMode = true;
        if (typeOfCall != null) {
            rootNodeMode = false;
        }

        Set<Integer> usedLines = new HashSet<>();

        // строку в массив char и режем на части, после цикла клеим и сраниваем с оригиналом
        String funcBody = node.getFuncDefined().getFuncBody();

        IndexBag ind = null;
        List<PgFuncReplacementPart> list = node.getParts();
        for (PgFuncReplacementPart part : list) {
            if (part.getElementType() == PgPlSqlElEnum.FUNCTION_DECLARE_BLOCK) {
                ind = new IndexBag(part);
                break;
            }
        }

        IndexBag indClone = null;
        try {
            indClone = (IndexBag) ind.clone();
        } catch (Exception e) {
        }

        if (rootNodeMode) {
            ind.moveToEnd();;
            // блок begin ХП
            glue(sb, funcBody, ind, indexNested);
            ind.moveToStart();
        } else {
            insertBeforeBody(sb, node.getPgFuncName());
        }

        for (PgFuncReplacementPart part : list) {
            switch (part.getElementType()) {
                case PERFORM_STATEMENT:
                    handlePerformStatement(part, sb, ind, funcBody, wasGenerated, indexNested, outerParameters);
                    break;
                case ASSIGN_STATEMENT:
                    handleAssignStatement(part, sb, ind, funcBody, wasGenerated, indexNested, outerParameters);
                    break;
                case ANONYMOUS_PARAMETER:
                    handleAnonymousParameter(part, inv, sb, ind, funcBody, indexNested, usedLines, outerParameters);
                    break;
                case VARIABLE_USAGE:
                    handleVariableUsage(node, part, inv, sb, ind, funcBody, indexNested, usedLines, outerParameters);
                    break;
                case RETURN_STATEMENT:
                    handleReturnStatement(part, inv, sb, ind, funcBody, assignPartFunc, indexNested, usedLines);
                    break;
            }
        }

        // блок end ХП (+1 чтобы захватиь D в операторе END)
        ind.indexEnd = indClone.indexEnd + 1;
        ind.lineEnd = indClone.lineEnd;
        if (rootNodeMode) {
            glue(sb, funcBody, ind, indexNested);
        } else {
            glue(sb, funcBody, ind, indexNested, usedLines);
        }

        if (rootNodeMode) {
            // пишем хвост
            ind.indexStart = ind.indexEnd;
            ind.indexEnd = funcBody.length();
            glue(sb, funcBody, ind, indexNested);
        } else {
            // нужен завершающий ;
            sb.append(";");
            insertAfterBody(sb, node.getPgFuncName());
        }
    }

    /**
     * Работа с RETURN оператором.
     */
    private void handleReturnStatement(PgFuncReplacementPart part, PgFuncInvoked inv, StringBuilder sb,
                                       IndexBag ind, String funcBody, String assignPartFunc, int indexNested,
                                       Set<Integer> usedLines) {
        if (inv == null) {
            return;
        }

        ind.fillEndFromStart(part);
        glue(sb, funcBody, ind, indexNested, usedLines);
        if (inv.getElementType() == PgPlSqlElEnum.PERFORM_STATEMENT) {
            ind.lineStart = part.getLineStart();
            ind.indexStart = returnHandlingPerform(part, sb, funcBody, ind, indexNested, usedLines) + 1;
        } else if (inv.getElementType() == PgPlSqlElEnum.FUNC_INVOKE_STATEMENT) {
            // смещаем индекс для сокрытия RETURN
            ind.indexStart = ind.indexEnd + "RETURN".length() + 1;
            ind.lineStart = part.getLineStart();
            // добавляем часть assign statement из головного ХП
            append(sb, assignPartFunc, indexNested, ind, usedLines);
        }
    }

    /**
     * Работа с заменой параметров ХП в выражениях типа: p2 := p1 + p2 + 200; на переменные с суффиксами.
     */
    private void handleVariableUsage(PgTreeNode node, PgFuncReplacementPart part, PgFuncInvoked inv, StringBuilder sb,
                                     IndexBag ind, String funcBody, int indexNested, Set<Integer> usedLines,
                                     TransferParameters outerParameters) {
        if (inv == null) {
            return;
        }

        List<PgFuncReplacementPart> list = inv.getChildNode().getParts();

        if (isReplacementAllowed(inv, part, list)) {
            if (part.isOverLoaded()) {
                // переменная переопределена в declare, не трогаем, вписываем как есть
                ind.fillEndFromStart(part);
                glue(sb, funcBody, ind, indexNested, usedLines);

                append(sb, part.getValue(), indexNested, ind, usedLines);
                ind.indexStart = part.getIndexEnd() + 1;
                ind.lineStart = part.getLineEnd();;
            } else {
                // ищем в параметрах

                Integer order = null;
                List<PgFuncReplacementPart> lp = node.getFuncDefined().getFuncParamParts();
                for (PgFuncReplacementPart par : lp) {
                    PgVarDefinition vPar = (PgVarDefinition) par;
                    if (!vPar.isAnonymous()) {
                        if (vPar.getIdentifier().equalsIgnoreCase(part.getValue())) {
                            // наш параметр
                            order = vPar.getOrder();
                        }
                    }
                }

                if (order != null) {
                    PgVarDefinition var = outerParameters.getVarByOrder(order);
                    String identifier = var.getIdentifier();

                    ind.fillEndFromStart(part);
                    glue(sb, funcBody, ind, indexNested, usedLines);

                    append(sb, identifier, indexNested, ind, usedLines);
                    ind.indexStart = part.getIndexEnd() + 1;
                    ind.lineStart = part.getLineEnd();
                }
            }
        }
    }

    /**
     * Работа с анонимными переменными типа $1. Замена на именованные переменные.
     */
    private void handleAnonymousParameter(PgFuncReplacementPart partAnonPar, PgFuncInvoked inv, StringBuilder sb, IndexBag ind,
                                          String funcBody, int indexNested, Set<Integer> usedLines, TransferParameters outerParameters) {
        if (inv == null) {
            return;
        }

        List<PgFuncReplacementPart> list = inv.getChildNode().getParts();

        if (isReplacementAllowed(inv, partAnonPar, list)) {
            ind.fillEndFromStart(partAnonPar);
            glue(sb, funcBody, ind, indexNested, usedLines);

            PgAnonymousParameter aPar = (PgAnonymousParameter) partAnonPar;

            PgVarDefinition var = outerParameters.getVarByOrder(aPar.getOrder());
            String identifier = var.getIdentifier();
            append(sb, identifier, indexNested, ind, usedLines);
            ind.indexStart = partAnonPar.getIndexEnd() + 1;
            ind.lineStart = partAnonPar.getLineEnd();
        }
    }

    /**
     * Работа с выражениями присваивания, например: m_res := p02_int4_v4(m_par1, m_par2);
     * Подразумевается. что в ASSIGN_STATEMENT присутствует вызов ХП
     */
    private void handleAssignStatement(PgFuncReplacementPart part, StringBuilder sb, IndexBag ind, String funcBody,
                                       AtomicBoolean wasGenerated, int indexNested, TransferParameters outerParameters) {

        // пишем хвост
        ind.fillEndFromStart(part);
        glue(sb, funcBody, ind, indexNested);

        // склеиваем в цикле
        PgFuncReplacementPart assignPart = part;
        int iStart = assignPart.getIndexStart();
        List<PgFuncReplacementPart> listSp = assignPart.getListSubPart(); // 1+x вызовов ХП
        for (PgFuncReplacementPart pp : listSp) {
            PgFuncInvoked pgi = (PgFuncInvoked) pp;
            PgTreeNode nd = pgi.getChildNode();
            if (nd.isPossibleGenerateBody()) {
                int iEnd = pp.getIndexStart();
                String assignPartFunc = funcBody.substring(iStart, iEnd);

                TransferParameters downOuter = makeOuterParameters(pgi, outerParameters);

                generateBody(nd, PgPlSqlElEnum.FUNC_INVOKE_STATEMENT, pgi, assignPartFunc, wasGenerated, sb, ++indexNested,
                        downOuter);

                iStart = pp.getIndexEnd() + 1;

                wasGenerated.set(true);
            }
        }
        ind.indexStart = part.getIndexEnd() + 1;
    }

    private TransferParameters makeOuterParameters(PgFuncInvoked pgi, TransferParameters upOuterParameters) {
        TransferParameters downOuter = new TransferParameters();
        for (PgFuncReplacementPart pr : pgi.getListSubPart()) {
            PgVarDefinition var = pr.getLinkedVariable();
            switch (var.getElementType()) {
                case VARIABLE_DECLARE:
                    downOuter.add(var);
                    break;
                case FUNCTION_PARAMETER_DECLARE:
                    if (upOuterParameters != null) {
                        int order = var.getOrder();
                        PgVarDefinition varUp = upOuterParameters.getVarByOrder(order);
                        downOuter.add(varUp);
                    } else {
                        downOuter.add(var);
                    }
                    break;
            }
        }
        return downOuter;
    }

    /**
     * Работа с PERFORM оператором: рекурсивный вызов generate сверху-вниз
     */
    private void handlePerformStatement(PgFuncReplacementPart part, StringBuilder sb, IndexBag ind, String funcBody,
                                        AtomicBoolean wasGenerated, int indexNested, TransferParameters outerParameters) {
        PgFuncInvoked inv = (PgFuncInvoked) part;
        PgTreeNode nd = inv.getChildNode();

        if (nd.isPossibleGenerateBody()) {
            ind.fillEndFromStart(part);
            glue(sb, funcBody, ind, indexNested);

            TransferParameters downOuter = makeOuterParameters(inv, outerParameters);

            generateBody(nd, PgPlSqlElEnum.PERFORM_STATEMENT, inv, null, wasGenerated, sb, ++indexNested,
                    downOuter);

            ind.indexStart = part.getIndexEnd() + 1;
            ind.lineStart = part.getLineEnd();

            wasGenerated.set(true);
        }
    }

    class IndexBag implements  Cloneable {
        int indexStart = 0;
        int indexEnd = 0;
        int lineStart = 0;
        int lineEnd = 0;

        IndexBag(PgFuncReplacementPart part) {
            indexStart = part.getIndexStart();
            indexEnd = part.getIndexEnd();
            lineStart = part.getLineStart();
            lineEnd = part.getLineEnd();
        }

        void fillEndFromStart(PgFuncReplacementPart part) {
            indexEnd = part.getIndexStart();
            lineEnd = part.getLineStart();
        }

        void fillStartFromEnd(PgFuncReplacementPart part) {
            indexStart = part.getIndexEnd();
            lineStart = part.getLineEnd();
        }

        void moveToEnd() {
            indexEnd = indexStart;
            lineEnd = lineStart;
            indexStart = 0;
            lineStart = 0;
        }

        void moveToStart() {
            indexStart = indexEnd;
            lineStart = lineEnd;
            indexEnd = 0;
            lineEnd = 0;
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public String toString() {
            return "IndexBag{" +
                    "indexStart=" + indexStart +
                    ", indexEnd=" + indexEnd +
                    ", lineStart=" + lineStart +
                    ", lineEnd=" + lineEnd +
                    '}';
        }
    }
    private void writeGeneratedSQL(StringBuilder sb, String fileName, boolean wasGenerated) {
        if (!wasGenerated) {
            return;
        }
        String dir = System.getProperty("user.dir").toLowerCase();
        File outCat = new File(dir, "out");
        outCat.mkdir();
        File outSql = new File(outCat, fileName + "_sf_result.sql");
        outSql.delete();
        try (FileOutputStream fos = new FileOutputStream(outSql)) {
            fos.write(sb.toString().getBytes());
        } catch (Exception e){
            // TODO transfer exception to logger or something else
        }
    }

    private void insertBeforeBody(StringBuilder sb, String funcName) {
        sb.append("\n");
        sb.append("--");
        sb.append("\n");
        sb.append("-- ***** insert sub-body BEGIN *****: \"");
        sb.append(funcName);
        sb.append("\"");
        sb.append("\n");
        sb.append("--");
        sb.append("\n");
    }

    private void insertAfterBody(StringBuilder sb, String funcName) {
        sb.append("\n");
        sb.append("--");
        sb.append("\n");
        sb.append("-- ***** insert sub-body END *****: \"");
        sb.append(funcName);
        sb.append("\"");
        sb.append("\n");
        sb.append("--");
        sb.append("\n");
    }

    private void glue(StringBuilder sb, String body, IndexBag ind, int indexNested) {
        glue(sb, body, ind, indexNested, null);
    }

    private void glue(StringBuilder sb, String body, IndexBag ind, int indexNested, Set<Integer> usedLines) {
        String str = body.substring(ind.indexStart, ind.indexEnd);
        sb.append(getOffsetPartBody(str, indexNested, ind,usedLines));
    }

    private void append(StringBuilder sb, String body, int indexNested) {
        append(sb, body, indexNested, null, null);
    }

    private void append(StringBuilder sb, String body, int indexNested, IndexBag ind, Set<Integer> usedLines) {
        sb.append(getOffsetPartBody(body, indexNested, ind, usedLines));
    }

    private String getOffsetPartBody(String partBody, int indexNested, IndexBag ind, Set<Integer> usedLines) {
        if (indexNested == 0) {
            // смещение не нужно, возвращаем как есть
            return partBody;
        }
        boolean oneLineBlock = true;
        if (ind.lineEnd > ind.lineStart) {
            // многострочный блок
            oneLineBlock = false;
        }
        if (usedLines != null) {
            if (oneLineBlock) {
                if (usedLines.contains(ind.lineStart)) {
                    // смещение уже было применено, отказываем
                    return partBody;
                }
            }
        }
        String offsett = "      ";
        if (indexNested > 1) {
            StringBuilder sbl = new StringBuilder();
            for (int i=0; i<indexNested; i++) {
                sbl.append(offsett);
            }
            offsett = sbl.toString();
        }
        char lf = '\n';
        char cr = '\r';
        char[] chars = partBody.toCharArray();
        int indexStartLine = 0;
        int indexEndLine = -1;
        int index = -1;
        int lfCounter = 0;
        int crCounter = 0;
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<chars.length; i++) {
            index = i;
            char ch = chars[i];
            if (ch == lf || ch == cr) {
                indexEndLine = i;
                if (ch == lf) {
                    lfCounter++;
                }
                if (ch == cr) {
                    crCounter++;
                }
            } else {
                if (indexEndLine != -1) {
                    String part = new String(chars, indexStartLine, indexEndLine - indexStartLine + 1);
                    doOffset(ind, usedLines, sb, offsett, lfCounter, crCounter, true);
                    sb.append(part);
                    indexStartLine = indexEndLine + 1;
                    indexEndLine = -1;
                }
            }
        }
        if (indexStartLine < index) {
            String part = new String(chars, indexStartLine, index - indexStartLine + 1);
            doOffset(ind, usedLines, sb, offsett, lfCounter, crCounter, false);
            sb.append(part);
        }
        return sb.toString();
    }

    private void doOffset(IndexBag ind, Set<Integer> usedLines, StringBuilder sb, String offsett,
                          int lfCounter, int crCounter, boolean inCycle) {
        if (usedLines != null) {
            // проверяем каждую строчку
            int lfCounter_ = lfCounter;
            int crCounter_ = crCounter;
            if (inCycle) {
                if (lfCounter_ > 0) {
                    lfCounter_--;
                }
                if (crCounter_ > 0) {
                    crCounter_--;
                }
            }
            int currLine = ind.lineStart + Math.max(lfCounter_, crCounter_);
            if (usedLines.contains(currLine) == false) {
                // вносим смещение
                sb.append(offsett);
                usedLines.add(currLine);
            }
        } else {
            // вносим смещение т.к. проверки не нужны
            sb.append(offsett);
        }
    }

    private boolean isReplacementAllowed(PgFuncInvoked inv, PgFuncReplacementPart part, List<PgFuncReplacementPart> list) {
        boolean fok = true;

        if (inv.getElementType() == PgPlSqlElEnum.PERFORM_STATEMENT) {
            int index = part.getIndexStart();
            for (PgFuncReplacementPart rp : list) {
                if (rp.getElementType() == PgPlSqlElEnum.RETURN_STATEMENT) {
                    if (rp.getIndexStart() < index && index < rp.getIndexEnd()) {
                        // находимся в зоне return
                        fok = false;
                    }
                }
            }
        }

        if (fok) {
            if (inv.getElementType() == PgPlSqlElEnum.FUNC_INVOKE_STATEMENT) {
                // возможно этот if надо убрать и проверять всегда
                if (part.getElementType() == PgPlSqlElEnum.ANONYMOUS_PARAMETER) {
                    int index = part.getIndexStart();
                    for (PgFuncReplacementPart rp : list) {
                        if (!part.equals(rp)) {
                            if (rp instanceof  PgFuncInvoked) {
                                if (rp.getIndexStart() < index && index < rp.getIndexEnd()) {
                                    // анонимынй параметр принадлежит вызову ХП
                                    fok = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return fok;
    }

    private int returnHandlingPerform(PgFuncReplacementPart part, StringBuilder sb, String funcBody, IndexBag ind,
                                      int indexNested, Set<Integer> usedLines) {
        String retStatement = funcBody.substring(part.getIndexStart(), part.getIndexEnd() + 1);
        append(sb, "/* RETURN disabled because PERFORM: ", indexNested, ind, usedLines);
        append(sb, retStatement, indexNested, ind, usedLines);
        append(sb, "*/", indexNested, ind, usedLines);

        return part.getIndexEnd();
    }

}