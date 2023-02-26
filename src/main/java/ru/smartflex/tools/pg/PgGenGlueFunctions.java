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

        generateBody(node, null, null, null, wasGenerated, sb, 0, Collections.emptyList());

        writeGeneratedSQL(sb, node.getFuncDefined().getFuncName(), wasGenerated.get());
        if (wasGenerated.get()) {
            PgParseFunctions.addGeneratedBody(sb.toString());
        }
    }

    private void generateBody(PgTreeNode node, PgPlSqlElEnum typeOfCall, PgFuncInvoked inv, String assignPartFunc,
                              AtomicBoolean wasGenerated, StringBuilder sb, int indexNested, List<String> outerParameters) {
        boolean rootNodeMode = true;
        if (typeOfCall != null) {
            rootNodeMode = false;
        }

        Set<Integer> usedLines = new HashSet<>();

        // строку в массив char и режем на части, после цикла клеим и сраниваем с оригиналом
        String funcBody = node.getFuncDefined().getFuncBody();
if (typeOfCall == null) {
    System.out.println("****** "+node.getFuncDefined().getFuncName()+ " "+typeOfCall);
} else {
    System.out.println("****** "+node.getFuncDefined().getFuncName()+ " "+typeOfCall);
}

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
                    handlePerformStatement(part, sb, ind, funcBody, wasGenerated, indexNested);
                    break;
                case VAR_DECLARE_BLOCK:
                    handleVarDeclareBlock(part, sb, ind, funcBody, indexNested);
                    break;
                case ASSIGN_STATEMENT:
                    handleAssignStatement(part, sb, ind, funcBody, wasGenerated, indexNested);
                    break;
                case ANONYMOUS_PARAMETER:
                    handleAnonymousParameter(part, inv, sb, ind, funcBody, indexNested, usedLines);
                    break;
                case VARIABLE_USAGE:
                    handleVariableUsage(node, part, inv, sb, ind, funcBody, indexNested, usedLines);
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
                                     IndexBag ind, String funcBody, int indexNested, Set<Integer> usedLines) {
        if (inv == null) {
            return;
        }

        List<PgFuncReplacementPart> list = inv.getChildNode().getParts();

        if (isReplacementAllowed(inv, part, list)) {
            int indexFound = 0;
            PgFuncReplacementPart invokedPart = null;
            List<PgFuncDefined.FuncParameter> parList = node.getFuncDefined().getParList();
            for (PgFuncDefined.FuncParameter par : parList) {
                if (par.getArgName() != null) {
                    if (par.getArgName().equalsIgnoreCase(part.getValue())) {
                        invokedPart = inv.getPgFuncReplacementPart(indexFound);
                        break;
                    }
                }
                indexFound++;
            }

            if (invokedPart != null) {
                PgFuncReplacementPart above = invokedPart.getAbovePart();
                if (above != null) {
                    ind.fillEndFromStart(part);
                    glue(sb, funcBody, ind, indexNested, usedLines);

                    append(sb, above.getNameWithSuffix(), indexNested, ind, usedLines);
                    ind.indexStart = part.getIndexEnd() + 1;
                }
            }
        }
    }

    /**
     * Работа с анонимными переменными типа $1. Замена на именованные переменные.
     */
    private void handleAnonymousParameter(PgFuncReplacementPart partAnonPar, PgFuncInvoked inv, StringBuilder sb, IndexBag ind,
                                          String funcBody, int indexNested, Set<Integer> usedLines) {
        if (inv == null) {
            return;
        }

        List<PgFuncReplacementPart> list = inv.getChildNode().getParts();

        if (isReplacementAllowed(inv, partAnonPar, list)) {
            ind.fillEndFromStart(partAnonPar);
            glue(sb, funcBody, ind, indexNested, usedLines);

            PgAnonymousParameter aPar = (PgAnonymousParameter) partAnonPar;

            int i = 1;
            for (PgFuncReplacementPart invokedPart : inv.getListSubPart()) {
                if (i == aPar.getOrder()) {
                    PgFuncReplacementPart above = invokedPart.getAbovePart();
                    append(sb, above.getNameWithSuffix(), indexNested, ind, usedLines);
                    ind.indexStart = partAnonPar.getIndexEnd() + 1;
                    break;
                }
                i++;
            }
        }
    }

    /**
     * Работа с выражениями присваивания, например: m_res := p02_int4_v4(m_par1, m_par2);
     * Подразумевается. что в ASSIGN_STATEMENT присутствует вызов ХП
     */
    private void handleAssignStatement(PgFuncReplacementPart part, StringBuilder sb, IndexBag ind, String funcBody,
                                       AtomicBoolean wasGenerated, int indexNested) {

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

                List<String> pars = makeOuterParametersList(pp);

                generateBody(nd, PgPlSqlElEnum.FUNC_INVOKE_STATEMENT, pgi, assignPartFunc, wasGenerated, sb, ++indexNested,
                        pars);

                iStart = pp.getIndexEnd() + 1;

                wasGenerated.set(true);
            }
        }
        ind.indexStart = part.getIndexEnd() + 1;
    }

    private List<String> makeOuterParametersList(PgFuncReplacementPart prp) {
        List<String> pars = new ArrayList<>();
        for (PgFuncReplacementPart p : prp.getListSubPart()) {
            String pName = p.getAbovePart().getNameWithSuffix();
            pars.add(pName);
        }
        return pars;
    }

    /**
     * Работа с переменными внутри DECLARE BEGIN блока: добавление сцффикса _sf к имени при необходимости
     */
    private void handleVarDeclareBlock(PgFuncReplacementPart part, StringBuilder sb, IndexBag ind, String funcBody,
                                       int indexNested) {
        ind.fillEndFromStart(part);
        glue(sb, funcBody, ind, indexNested);

        List<PgFuncReplacementPart> lsp = part.getListSubPart();
        for (PgFuncReplacementPart pdcl : lsp) {
            if (pdcl.getElementType() == PgPlSqlElEnum.DECL_IDENT) {
                String varName = part.getNameWithSuffix();
                append(sb, varName, indexNested);
                ind.fillStartFromEnd(pdcl);
            } else {
                // хвост от объявления переменной
                ind.indexStart++;
                ind.indexEnd = pdcl.getIndexEnd() + 1; //+ 1 для дозаписи ;
                ind.lineEnd = pdcl.getLineEnd();
                glue(sb, funcBody, ind, indexNested);
            }
        }

        ind.indexStart = ind.indexEnd + 1;
    }

    /**
     * Работа с PERFORM оператором: рекурсивный вызов generate сверху-вниз
     */
    private void handlePerformStatement(PgFuncReplacementPart part, StringBuilder sb, IndexBag ind, String funcBody,
                                        AtomicBoolean wasGenerated, int indexNested) {
        PgFuncInvoked inv = (PgFuncInvoked) part;
        PgTreeNode nd = inv.getChildNode();

        if (nd.isPossibleGenerateBody()) {
            ind.fillEndFromStart(part);
            glue(sb, funcBody, ind, indexNested);

            generateBody(nd, PgPlSqlElEnum.PERFORM_STATEMENT, inv, null, wasGenerated, sb, ++indexNested,
                    Collections.emptyList());

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