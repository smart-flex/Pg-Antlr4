package ru.smartflex.tools.pg;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

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

            String gen = generateBody(node);
            System.out.println(gen);
//            System.out.println(node.getPgFuncName());

        };
        return ith;
    }

    private String generateBody(PgTreeNode node) {
        boolean wasGenerated = false;
        // строку в массив char и режем на части, после цикла клеим и сраниваем с оригиналом
        String funcBody = node.getFuncDefined().getFuncBody();

        int indexStartFDBlock = 0;
        int indexEndFDBlock = 0;
        List<PgFuncReplacementPart> list = node.getParts();
        for (PgFuncReplacementPart part : list) {
            if (part.getElementType() == PgPlSqlElEnum.FUNCTION_DECLARE_BLOCK) {
                indexStartFDBlock = part.getIndexStart();;
                indexEndFDBlock = part.getIndexEnd();
            }
        }

        StringBuilder sb = new StringBuilder();
        int indexStart = 0;
        int indexEnd = indexStartFDBlock;
        // блок begin ХП
        glue(sb, funcBody, indexStart, indexEnd);
        indexStart = indexEnd;

        for (PgFuncReplacementPart part : list) {
            if (part.getElementType() == PgPlSqlElEnum.PERFORM_STATEMENT) {
                indexEnd = part.getIndexStart();
                glue(sb, funcBody, indexStart, indexEnd);

                PgFuncInvoked inv = (PgFuncInvoked) part;
                PgTreeNode nd = inv.getChildNode();
                String bodyInvoked = getBodyPart(nd, inv);
                insertBody(sb, bodyInvoked, inv.getFuncName());

                indexStart = part.getIndexEnd() + 1;

                wasGenerated = true;
            } else if (part.getElementType() == PgPlSqlElEnum.VAR_DECLARE_BLOCK) {
                indexEnd = part.getIndexStart();
                glue(sb, funcBody, indexStart, indexEnd);

                List<PgFuncReplacementPart> lsp = part.getListSubPart();
                for (PgFuncReplacementPart pdcl : lsp) {
                    if (pdcl.getElementType() == PgPlSqlElEnum.DECL_IDENT) {
                        String varName = part.getNameWithSuffix();
                        sb.append(varName);
                        indexStart = pdcl.getIndexEnd();
                    } else {
                        // хвост от объявления переменной
                        indexStart++;
                        indexEnd = pdcl.getIndexEnd();
                        glue(sb, funcBody, indexStart, indexEnd);
                    }
                }

                indexStart = indexEnd + 1;
            } else if (part.getElementType() == PgPlSqlElEnum.ANONYMOUS_PARAMETER) {
                indexEnd = part.getIndexStart();
                glue(sb, funcBody, indexStart, indexEnd);
                indexStart = indexEnd + 1;

            }  else if (part.getElementType() == PgPlSqlElEnum.ASSIGN_STATEMENT) {
                // подразумевается. что в ASSIGN_STATEMENT присутствует вызов ХП

                // пишем хвост
                indexEnd = part.getIndexStart();
                glue(sb, funcBody, indexStart, indexEnd);

                // склеиваем в цикле
                PgFuncReplacementPart assignPart = part;
                int iStart = assignPart.getIndexStart();
                List<PgFuncReplacementPart> listSp = assignPart.getListSubPart(); // 1+x вызовов ХП
                for (PgFuncReplacementPart pp : listSp) {
                    int iEnd = pp.getIndexStart();
                    String assignPartFunc = funcBody.substring(iStart, iEnd);
                    PgFuncInvoked pgi = (PgFuncInvoked) pp;
                    PgTreeNode nd = pgi.getChildNode();
                    String bodyInvoked = getBodyPart(nd, pgi, assignPartFunc);
                    iStart = pp.getIndexEnd() + 1;
                    insertBody(sb, bodyInvoked, pgi.getFuncName());
                }
                indexStart = part.getIndexEnd() + 1;

                wasGenerated = true;
            }

        }

        // блок end ХП (+1 чтобы захватиь D в операторе END)
        indexEnd = indexEndFDBlock + 1;
        glue(sb, funcBody, indexStart, indexEnd);

        // пишем хвост
        indexStart = indexEnd;
        glue(sb, funcBody, indexStart, funcBody.length());

        writeGeneratedSQL(sb, node.getFuncDefined().getFuncName(), wasGenerated);

        return sb.toString();
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

    private void insertBody(StringBuilder sb, String partBody, String funcName) {
        sb.append("\n");
        sb.append("--");
        sb.append("\n");
        sb.append("-- ***** insert sub-body BEGIN *****: \"");
        sb.append(funcName);
        sb.append("\"");
        sb.append("\n");
        sb.append("--");
        sb.append("\n");

        String offsetPartBody = getOffsetPartBody(partBody);
        sb.append(offsetPartBody);

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

    private String getOffsetPartBody(String partBody) {
        String offsett = "      ";
        char lf = '\n';
        char cr = '\r';
        char[] chars = partBody.toCharArray();
        int indexStartLine = 0;
        int indexEndLine = -1;
        int index = -1;
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<chars.length; i++) {
            index = i;
            char ch = chars[i];
            if (ch == lf || ch == cr) {
                indexEndLine = i;
            } else {
                if (indexEndLine != -1) {
                    String part = new String(chars, indexStartLine, indexEndLine - indexStartLine + 1);
                    sb.append(offsett);
                    sb.append(part);
                    indexStartLine = indexEndLine + 1;
                    indexEndLine = -1;
                }
            }
        }
        if (indexStartLine < index) {
            String part = new String(chars, indexStartLine, index - indexStartLine + 1);
            sb.append(offsett);
            sb.append(part);
        }
        return sb.toString();
    }
    private void glue(StringBuilder sb, String body, int indexStart, int indexEnd) {
        sb.append(body.substring(indexStart, indexEnd));
    }

    private String getBodyPart(PgTreeNode nd, PgFuncInvoked inv) {
        return  getBodyPart(nd, inv, null);
    }

    // TODO рекурсивный вызов дописать (учесть под-вызовы ХП)

    private String getBodyPart(PgTreeNode nd, PgFuncInvoked inv, String assignPartFunc) {

        String funcBody = nd.getFuncDefined().getFuncBody();

        int indexStartFDBlock = 0;
        int indexEndFDBlock = 0;
        List<PgFuncReplacementPart> list = nd.getParts();
        for (PgFuncReplacementPart part : list) {
            if (part.getElementType() == PgPlSqlElEnum.FUNCTION_DECLARE_BLOCK) {
                indexStartFDBlock = part.getIndexStart();;
                indexEndFDBlock = part.getIndexEnd();
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        int indexStart = indexStartFDBlock;
        int indexEnd = indexEndFDBlock;

        for (PgFuncReplacementPart part : list) {
            if (part.getElementType() == PgPlSqlElEnum.ANONYMOUS_PARAMETER) {

                if (isReplacementAllowed(inv, part, list)) {
                    indexEnd = part.getIndexStart();
                    glue(sb, funcBody, indexStart, indexEnd);

                    PgAnonymousParameter aPar = (PgAnonymousParameter) part;

                    int i = 1;
                    for (PgFuncReplacementPart invokedPart : inv.getListSubPart()) {
                        if (i == aPar.getOrder()) {
                            PgFuncReplacementPart above = invokedPart.getAbovePart();
                            sb.append(above.getNameWithSuffix());
                            indexStart = part.getIndexEnd() + 1;
                            break;
                        }
                        i++;
                    }
                }
            } else if (part.getElementType() == PgPlSqlElEnum.VARIABLE_USAGE) {

                if (isReplacementAllowed(inv, part, list)) {
                    int indexFound = 0;
                    PgFuncReplacementPart invokedPart = null;
                    List<PgFuncDefined.FuncParameter> parList = nd.getFuncDefined().getParList();
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
                            indexEnd = part.getIndexStart();
                            glue(sb, funcBody, indexStart, indexEnd);

                            sb.append(above.getNameWithSuffix());
                            indexStart = part.getIndexEnd() + 1;
                        }
                    }
                }

            } else if (part.getElementType() == PgPlSqlElEnum.RETURN_STATEMENT) {
                indexEnd = part.getIndexStart();
                glue(sb, funcBody, indexStart, indexEnd);
                if (inv.getElementType() == PgPlSqlElEnum.PERFORM_STATEMENT) {
                    indexStart = returnHandlingPerform(part, sb, funcBody) + 1;
                } else if (inv.getElementType() == PgPlSqlElEnum.FUNC_INVOKE_STATEMENT) {
                    // смещаем индекс для сокрытия RETURN
                    indexStart = indexEnd + "RETURN".length() + 1;
                    // добавляем часть assign statement из головного ХП
                    sb.append(assignPartFunc);
                }
            }
        }

        glue(sb, funcBody, indexStart, indexEndFDBlock + 1);

        sb.append(";");

        return sb.toString();
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

        return fok;
    }

    private int returnHandlingPerform(PgFuncReplacementPart part, StringBuilder sb, String funcBody) {
        String retStatement = funcBody.substring(part.getIndexStart(), part.getIndexEnd() + 1);
        sb.append("/* RETURN disabled because PERFORM: ");
        sb.append(retStatement);
        sb.append("*/");

        return part.getIndexEnd();
    }

}