package ru.smartflex.tools.pg;

import java.util.List;

public class PgGenGlueFunctions {

    public void glue(PgTreeNode root) {
        ITreeHandler<PgTreeNode> ith = getITreeHandler1();
        root.walkingTree(root, ith);
    }

    private ITreeHandler<PgTreeNode> getITreeHandler() {
        ITreeHandler<PgTreeNode> ith = (node) -> {
            if (!node.isPossibleCreateBodyForInsert()) {
                return;
            }

            PgFuncBodyPartBag funcBodyPartBag = node.getFuncBodyPartBag();
            List<PgFuncBodyPartBag.FuncBodyPart> list =  funcBodyPartBag.getListPart();

            StringBuilder sb = new StringBuilder();

            for (PgFuncBodyPartBag.FuncBodyPart part : list) {
                sb.append(getPart(part));
            }

            System.out.println(sb.toString()+ "\n*************\n");


        };
        return ith;
    }

    private ITreeHandler<PgTreeNode> getITreeHandler1() {
        ITreeHandler<PgTreeNode> ith = (node) -> {
            if (!node.isPossibleGenerateBody()) {
                return;
            }

            // проход с применением getParent
            // - регистриуем верхний begin end блок
            // - вставляем тело вызываемой ф-ции
            String gen = generateBody(node);
            System.out.println(gen);
            /*
            List<PgFuncReplacementPart> list = node.getParts();
            for (PgFuncReplacementPart part : list) {
                if (part.getElementType() == PgPlSqlElEnum.PERFORM) {
                    PgFuncInvoked inv = (PgFuncInvoked) part;
                    PgTreeNode nd = inv.getChildNode();
                    String pstr = getBodyPart(nd);
                    System.out.println(nd.getPgFuncName()+"\n"+ pstr+"\n*************\n");

                }

            }
            */
/*
            PgFuncBodyPartBag funcBodyPartBag = node.getFuncBodyPartBag();
            List<PgFuncBodyPartBag.FuncBodyPart> list =  funcBodyPartBag.getListPart();

            StringBuilder sb = new StringBuilder();

            for (PgFuncBodyPartBag.FuncBodyPart part : list) {
                sb.append(getPart(part));
            }

            System.out.println(sb.toString()+ "\n*************\n");

*/
        };
        return ith;
    }

    private String generateBody(PgTreeNode node) {
        // строку в массив char и режем на части, после цикла клеим и сраниваем с оригиналом
        String funcBody = node.getFuncDefined().getFuncBody();
        char[] chars = funcBody.toCharArray();

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
            if (part.getElementType() == PgPlSqlElEnum.PERFORM) {
                indexEnd = part.getIndexStart();
                glue(sb, funcBody, indexStart, indexEnd);

                PgFuncInvoked inv = (PgFuncInvoked) part;
                PgTreeNode nd = inv.getChildNode();
                String bodyInvoked = getBodyPart(nd, inv);
                sb.append("\n");
                sb.append("--");
                sb.append("\n");
                sb.append("-- ***** insert sub-body BEGIN ***** ");
                sb.append("\n");
                sb.append("--");
                sb.append("\n");
                sb.append(bodyInvoked);
                sb.append("\n");
                sb.append("--");
                sb.append("\n");
                sb.append("-- ***** insert sub-body END ***** ");
                sb.append("\n");
                sb.append("--");
                sb.append("\n");

                indexStart = part.getIndexEnd() + 1;
            } else if (part.getElementType() == PgPlSqlElEnum.VAR_DECLARE_BLOCK) {
                indexEnd = part.getIndexStart();
                glue(sb, funcBody, indexStart, indexEnd);

                List<PgFuncReplacementPart> lsp = part.getListSubPart();
                for (PgFuncReplacementPart pdcl : lsp) {
                    if (pdcl.getElementSubType() == PgPlSqlElEnum.DECL_IDENT) {
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

            }

        }

        // блок end ХП (+1 чтобы захватиь D в операторе END)
        indexEnd = indexEndFDBlock + 1;
        glue(sb, funcBody, indexStart, indexEnd);

        // пишем хвост
        indexStart = indexEnd;
        glue(sb, funcBody, indexStart, funcBody.length());

        return sb.toString();
    }

    private void glue(StringBuilder sb, String body, int indexStart, int indexEnd) {
        sb.append(body.substring(indexStart, indexEnd));
    }

    // TODO рекурсивный вызов дописать (учесть под-вызовы ХП)
    private String getBodyPart(PgTreeNode nd, PgFuncInvoked inv) {

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

            } else if (part.getElementType() == PgPlSqlElEnum.VARIABLE_USAGE) {

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
        }

        glue(sb, funcBody, indexStart, indexEndFDBlock + 1);

        sb.append(";");

        return sb.toString();
    }

    private String getPart(PgFuncBodyPartBag.FuncBodyPart part) {
        String pt = part.getFuncPart();

        if (part.getNodeInvoked() != null) {
            pt = part.getNodeInvoked().getFunctionBlockStatement() + ";";
        }

        return pt;
    }

    private void genBodiesAggregate(PgTreeNode node) {
        if (!node.isPossibleCreateBodyForInsert()) {
            return;
        }

        for (PgTreeNode nd : node.getChildList()) {
            genBodiesAggregate(nd);
        }

    }
}