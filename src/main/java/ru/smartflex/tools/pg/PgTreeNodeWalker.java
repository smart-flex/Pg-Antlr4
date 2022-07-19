package ru.smartflex.tools.pg;

public class PgTreeNodeWalker {
    private PgTreeNode node;
    private ITreeState info;

    public PgTreeNodeWalker(PgTreeNode node, ITreeState info) {
        this.node = node;
        this.info = info;
    }

    public PgTreeNode getNode() {
        return node;
    }

    public ITreeState getInfo() {
        return info;
    }
}
