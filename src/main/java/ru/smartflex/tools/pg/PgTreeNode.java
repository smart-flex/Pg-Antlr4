package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PgTreeNode {
    private static final String ROOT_NODE_NAME = "^--ROOT--*";

    private PgFuncDefined funcDefined = null;
    private PgFuncInvoked funcInvoked = null;
    private String pgFuncName;
    private List<PgTreeNode> childList = new ArrayList<>();

    private PgTreeNode parentNode = null;
    private boolean wasUsedAsChild = false;
    private PgFuncBodyPartBag funcBodyPartBag = null;

    private PgTreeNode() {
    }

    public PgTreeNode(PgFuncDefined funcDefined) {
        this.funcDefined = funcDefined;
        this.pgFuncName = funcDefined.getFuncName();
    }

    public PgTreeNode(PgTreeNode parentNode, PgFuncInvoked funcInvoked) {
        this.parentNode = parentNode;
        this.funcInvoked = funcInvoked;
        this.pgFuncName = funcInvoked.getFuncName();
    }

    public PgTreeNode(String nameNode) {
        pgFuncName = nameNode;
    }

    public static PgTreeNode createRoot() {
        PgTreeNode root = new PgTreeNode();
        root.pgFuncName = ROOT_NODE_NAME;
        return root;
    }

    boolean isPossibleCreateBodyForInsert() {
        if (funcDefined != null && funcBodyPartBag != null) {
            return true;
        }
        return false;
    }

    int getChildListsize() {
        return childList.size();
    }

    List<PgTreeNode> getChildList() {
        return childList;
    }

    String getFunctionBlockStatement() {
        if (funcDefined != null) {
            return funcDefined.getFunctionBlockStatementAsString();
        }
        return null;
    }

    public void addChild(PgTreeNode child) {
        childList.add(child);
    }

    /**
     * Метод для визуального контроля построения дерева вызовов
     */
    public void drawTree() {
        PgTreeNodeWalker ptw = new PgTreeNodeWalker(this, new PrintTreeStateNode(10, 0));

        ITreeHandler<PgTreeNodeWalker> ith = (a) -> {
            if (((PrintTreeStateNode) a.getState()).indexNested == 0) {
                System.out.println(String.format("%" + ((PrintTreeStateNode) a.getState()).depth + "s",
                        a.getNode().getPgFuncName()));
            } else {
                System.out.println(String.format("%" + ((PrintTreeStateNode) a.getState()).depth + "s",
                        ((PrintTreeStateNode) a.getState()).indexNested) + ": " + a.getNode().getPgFuncName());
            }
            return;
        };

        walkingTree(ptw, ith);
    }

    public void walkingTree(PgTreeNodeWalker ptw, ITreeHandler th) {
        th.apply(ptw);

        if (ptw.isChildEmpty()) {
            return;
        }

        for (PgTreeNode nd : ptw.getChildList()) {
            PgTreeNodeWalker ptwNext = ptw.next(nd);
            walkingTree(ptwNext, th);
        }

    }

    public void walkingTree(PgTreeNode node, ITreeHandler th) {
        th.apply(node);

        if (node.isChildEmpty()) {
            return;
        }

        for (PgTreeNode nd : node.getChildList()) {
            walkingTree(nd, th);
        }

    }

    boolean isChildEmpty() {
        return getChildListsize() == 0 ? true : false;
    }


    class PrintTreeStateNode implements ITreeState {
        int depth;
        int indexNested;

        public PrintTreeStateNode(int depth, int indexNested) {
            this.depth = depth;
            this.indexNested = indexNested;
        }

        @Override
        public ITreeState doState() {
            return new PrintTreeStateNode(depth + 10, indexNested + 1);
        }
    }

    private void checkForChildren(PgTreeNode srcNode) {
        if (srcNode.childList.size() > 0) {
            throw new RuntimeException("Replaced node: " + srcNode.getPgFuncName() + " has a children. Replacement is impossible.");
        }
    }

    void replaceNodeWithBody(PgTreeNode destNode) {
        checkForChildren(this);

        childList = destNode.childList;
        setFuncDefined(destNode.getFuncDefined());
    }

    public boolean equals(PgTreeNode node) {
        boolean fok = false;

        if (getPgFuncName().equals(node.getPgFuncName())) {
            fok = true;
        }
        if (fok) {
            // TODO дополнительная проверка по возвращаемуму типу и сигнатуре
        }

        return fok;
    }

    public String getPgFuncName() {
        return pgFuncName;
    }

    PgFuncInvoked getFuncInvoked() {
        return funcInvoked;
    }

    PgTreeNode getParentNode() {
        return parentNode;
    }

    PgFuncDefined getFuncDefined() {
        return funcDefined;
    }

    public void setFuncDefined(PgFuncDefined funcDefined) {
        this.funcDefined = funcDefined;
    }

    public boolean isWasUsedAsChild() {
        return wasUsedAsChild;
    }

    public void setWasUsedAsChild(boolean wasUsedAsChild) {
        this.wasUsedAsChild = wasUsedAsChild;
    }

    void setFuncBodyPartBag(PgFuncBodyPartBag funcBodyPartBag) {
        this.funcBodyPartBag = funcBodyPartBag;
    }

    PgFuncBodyPartBag getFuncBodyPartBag() {
        return funcBodyPartBag;
    }

    void addBodyPart(String bodyPart) {
        addBodyPart(bodyPart, null);
    }

    void addBodyPart(String bodyPart, PgTreeNode nodeInvoked) {
        if (funcBodyPartBag == null) {
            funcBodyPartBag = new PgFuncBodyPartBag(bodyPart, nodeInvoked);
        } else {
            funcBodyPartBag.addBodyPart(bodyPart, nodeInvoked);
        }
    }



    @Override
    public String toString() {
        return "PgTreeNode{" +
                "pgFuncName='" + pgFuncName + '\'' +
                '}';
    }
}
