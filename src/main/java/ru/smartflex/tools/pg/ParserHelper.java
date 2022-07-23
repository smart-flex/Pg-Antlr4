package ru.smartflex.tools.pg;

public class ParserHelper {

    public static PgTreeNode makeTree(PgParsingResultBag pgParsingResultBag) {
        PgTreeNode root = PgTreeNode.createRoot();
        for (PgParsingResult res : pgParsingResultBag.getResultList()) {
            PgFuncDefined funcDefined = res.getFuncDefined();
            PgTreeNode node = new PgTreeNode(funcDefined);

            for (PgFuncInvoked inv : res.getFunctionInvocationsList()) {
                PgTreeNode child = new PgTreeNode(node, inv);
                node.addChild(child);
            }

            root.putInPlaceNode(node);
        }
        root.packTree();

        return root;
    }

    static PgFuncBodyPartBag.FuncBodyPart getLastFuncBodyPart(PgTreeNode node) {
        PgFuncDefined funcDefined = node.getParentNode().getFuncDefined();

        if (funcDefined == null) {
            // это второй и более уровни вложенности.
            // TODO Надо найти и сопоставить определение ХП с учетом полиморфизма
            return null; // so far
        }

        return funcDefined.getFuncBodyPartBag().getLastFuncBodyPart();
    }
}
