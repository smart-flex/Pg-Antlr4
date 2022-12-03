package ru.smartflex.tools.pg;

public class PgGenCutFunctions {

    public void cut(PgTreeNode root) {
        ITreeHandler<PgTreeNode> ith = getITreeHandler();
        root.walkingTree(root, ith);
    }

    private ITreeHandler<PgTreeNode> getITreeHandler() {
        ITreeHandler<PgTreeNode> ith = (node) -> {
            if (node.getFuncDefined() == null) {
                // it is root node
                return;
            }

            // create body parts
            // строку в массив char и режем на части, после цикла клеим и сраниваем с оригиналом
            String funcBody = node.getFuncDefined().getFuncBody();
            char[] chars = funcBody.toCharArray();

            int indexStart = 0;
            int indexEnd = node.getFuncDefined().getFunctionBlockStatement().indexStart;
            // блок begin ХП
            cutBody(node, chars, indexStart, indexEnd);
            indexStart = indexEnd;

            for (PgTreeNode nd : node.getChildList()) {

                // вырезаем часть главной ХП до вызова подчиненной ХП
                indexEnd = nd.getFuncInvoked().getIndexStart();
                cutBody(node, chars, indexStart, indexEnd);

                indexStart = nd.getFuncInvoked().getIndexStart();
                indexEnd = nd.getFuncInvoked().getIndexEnd() + 1;
                cutBody(node, chars, indexStart, indexEnd);

                indexStart = indexEnd;
            }

            indexEnd = node.getFuncDefined().getFunctionBlockStatement().indexEnd + 1;
            // блок end ХП (+1 чтобы захватиь D в операторе END)
            cutBody(node, chars, indexStart, indexEnd);
            indexStart = indexEnd;

            // пишем хвост
            cutBody(node, chars, indexStart, chars.length);

            checkGlue(node);
        };

        return ith;
    }

    private void checkGlue(PgTreeNode node) {
        if (!node.getFuncBodyPartBag().checkGlue((node))) {
            throw new PgSQLIncludeException("Glued function is not equals original body");
        }
    }

    private void cutBody(PgTreeNode node, char[] chars, int indexStart, int indexEnd) {
        char[] partChar = new char[indexEnd - indexStart];
        System.arraycopy(chars, indexStart, partChar, 0, partChar.length);
        String partString = new String(partChar);
System.out.println("************\n"+partString+"\n");
        node.addBodyPart(partString);
    }

}
