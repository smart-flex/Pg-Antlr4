package ru.smartflex.tools.pg;

public class PgGenGlueFunctions {

    private ITreeHandler<PgTreeNode> getITreeHandler() {
        ITreeHandler<PgTreeNode> ith = (node) -> {
            if (node.getFuncDefined() == null) {
                // it is root node
                return;
            }

        };
        return ith;
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