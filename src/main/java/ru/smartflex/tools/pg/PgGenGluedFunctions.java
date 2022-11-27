package ru.smartflex.tools.pg;

public class PgGenGluedFunctions {

    public void glue(PgTreeNode root) {

        PgTreeNodeWalker ptw = new PgTreeNodeWalker(root, new GlueTreeStateNode());
        ITreeHandler<PgTreeNodeWalker> ith = getITreeHandler();

        root.walkingTree(ptw, ith);

    }

    private ITreeHandler<PgTreeNodeWalker> getITreeHandler() {
        ITreeHandler<PgTreeNodeWalker> ith = (w) -> {
            PgTreeNode node = w.getNode();
            if (node.getFuncDefined() == null) {
                // it is root node
                return;
            }
            // child can be not exists, but for sameness we have to build PgFuncBodyPartBag
            if (node.getChildListsize() == 0) {
                node.setFuncBodyPartBag(new PgFuncBodyPartBag(node));
                return;
            }
            // create body parts
            // строку в массив char и режем на части, после цикла клеим и сраниваем с оригиналом
            String funcBody = node.getFuncDefined().getFuncBody();
            char[] chars = funcBody.toCharArray();
            int rowStart= 0;
            int colStart = 0;
            int rowEnd = 0;
            int colEnd = 0;

            for (PgTreeNode nd : node.getChildList()) {

                // вырезаем часть главной ХП до вызова подчиненной ХП
                rowEnd = nd.getFuncInvoked().getLineStart() - 1;
                colEnd = nd.getFuncInvoked().getColStart() - 1;
                cutBody(node, chars, rowStart, colStart, rowEnd, colEnd);

                rowStart = rowEnd;
                colStart = colEnd;
                rowEnd = nd.getFuncInvoked().getLineEnd() - 1;
                colEnd = nd.getFuncInvoked().getColEnd() - 1;
                cutBody(node, chars, rowStart, colStart, rowEnd, colEnd);

                rowStart = rowEnd;
                colStart = colEnd;
            }

            // пишем хвост
            cutBody(node, chars, rowEnd, colEnd);

            checkGlue(node);

            return;
        };

        return ith;
    }

    private void checkGlue(PgTreeNode node) {
        if (!node.getFuncBodyPartBag().checkGlue((node))) {
            throw new PgSQLIncludeException("Glued function is not equals original body");
        }
    }

    private void cutBody(PgTreeNode node, char[] chars, int rowStart, int colStart, int rowEnd, int colEnd) {
        char lf = '\n';
        char cr = '\r';
        boolean signNewLine = false;
        boolean rowStartMatched = true;
        int amountReadLines = 0;
        int indexBegin = 0;
        int indexEnd = 0;

        for (int i=0; i<chars.length; i++) {
            char ch = chars[i];
            if (ch == lf || ch == cr) {
                signNewLine = true;
            } else {
                if (signNewLine) {
                    amountReadLines++;
                }
                signNewLine = false;
            }
            if (amountReadLines == rowStart && rowStartMatched) {
                indexBegin = i + colStart;
                rowStartMatched = false;
            }
            if (amountReadLines == rowEnd) {
                indexEnd = i + colEnd;
                break;
            }
        }

        char[] partChar = new char[indexEnd - indexBegin];
        System.arraycopy(chars, indexBegin, partChar, 0, indexEnd - indexBegin);
        String partString = new String(partChar);
        node.addBodyPart(partString);
    }

    private void cutBody(PgTreeNode node, char[] chars, int rowStart, int colStart) {
        char lf = '\n';
        char cr = '\r';
        boolean signNewLine = false;
        int amountReadLines = 0;
        int indexBegin = 0;

        for (int i=0; i<chars.length; i++) {
            char ch = chars[i];
            if (ch == lf || ch == cr) {
                signNewLine = true;
            } else {
                if (signNewLine) {
                    amountReadLines++;
                }
                signNewLine = false;
            }
            if (amountReadLines == rowStart) {
                indexBegin = i + colStart;
                break;
            }
        }

        char[] partChar = new char[chars.length - indexBegin];
        System.arraycopy(chars, indexBegin, partChar, 0, partChar.length);
        String partString = new String(partChar);
        node.addBodyPart(partString);
    }

    class GlueTreeStateNode implements ITreeState {

        public GlueTreeStateNode() {
        }

        @Override
        public ITreeState doState() {
            return new GlueTreeStateNode();
        }
    }

}
