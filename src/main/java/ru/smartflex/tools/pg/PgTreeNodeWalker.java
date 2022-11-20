package ru.smartflex.tools.pg;

import java.util.List;

public class PgTreeNodeWalker {
    private PgTreeNode node;
    private ITreeState state;

    PgTreeNodeWalker(PgTreeNode node, ITreeState state) {
        this.node = node;
        this.state = state;
    }

    PgTreeNodeWalker next(PgTreeNode nodeNext) {
        PgTreeNodeWalker next = null;

        ITreeState statePrev = getState();
        ITreeState stateNextLevel = statePrev.doState();

        next = new PgTreeNodeWalker(nodeNext, stateNextLevel);

        return next;
    }

    boolean isChildEmpty() {
        return node.getChildListsize() == 0 ? true : false;
    }

    List<PgTreeNode> getChildList() {
        return node.getChildList();
    }

    PgTreeNode getNode() {
        return node;
    }

    ITreeState getState() {
        return state;
    }
}
