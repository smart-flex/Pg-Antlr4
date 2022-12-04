package ru.smartflex.tools.pg;

import java.util.List;

public class PgGenGlueFunctions {

    public void glue(PgTreeNode root) {
        ITreeHandler<PgTreeNode> ith = getITreeHandler();
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