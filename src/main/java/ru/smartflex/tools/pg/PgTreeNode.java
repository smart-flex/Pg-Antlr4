package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

public class PgTreeNode<T> {
    private static final String ROOT_NODE_NAME = "^--ROOT--*";

    private String pgFuncName;
    private T invokeInfo;
    private List<PgTreeNode<T>> childList = new ArrayList<>();

    private PgTreeNode() {
    }

    public PgTreeNode(String nameNode) {
        pgFuncName = nameNode;
    }

    public static PgTreeNode createRoot() {
        PgTreeNode root = new PgTreeNode();
        root.pgFuncName = ROOT_NODE_NAME;
        return root;
    }

    public void addChild(PgTreeNode<T> child) {
        childList.add(child);
    }

    public void putInPlaceNode(PgTreeNode node) {
        if (childList.size() == 0) {
            childList.add(node);
            return;
        }

        if (!moveDownToPut(node, this.childList)) {
            childList.add(node);
        }
    }

    public void drawTree() {
        drawTree(this, 10);
    }

    private void drawTree(PgTreeNode node, int depth) {
        System.out.println(String.format("%" + depth + "s", node.getPgFuncName()));

        for (int i = 0; i < node.childList.size(); i++) {
            PgTreeNode<T> nd = (PgTreeNode<T>) node.childList.get(i);
            drawTree(nd, depth + 10);
        }

    }

    private boolean moveDownToPut(PgTreeNode node, List<PgTreeNode<T>> list) {
        boolean fok = false;
        for (int i = 0; i < list.size(); i++) {
            PgTreeNode<T> nd = list.get(i);
            if (nd.equals(node)) {
                fok = replaceNode(nd, node);
            } else if (isCanReplaceChildNode(nd, node)) {
                fok = true;
                // емняем узел на поддерево
                list.set(i, node);
            } else {
                fok = moveDownToPut(node, nd.childList);
            }
        }
        return fok;
    }

    /**
     * замена хвоста
     */
    private boolean isCanReplaceChildNode(PgTreeNode srcNode, PgTreeNode destNode) {
        for (int i = 0; i < destNode.childList.size(); i++) {
            PgTreeNode<T> nd = (PgTreeNode<T>) destNode.childList.get(i);
            if (nd.equals(srcNode)) {
                checkForChildren(srcNode);
                return true;
            }
        }
        return false;
    }

    private void checkForChildren(PgTreeNode srcNode) {
        if (srcNode.childList.size() > 0) {
            throw new RuntimeException("Replaced node: " + srcNode.getPgFuncName() + " has a children. Replacement is impossible.");
        }
    }

    private boolean replaceNode(PgTreeNode srcNode, PgTreeNode destNode) {
        checkForChildren(srcNode);

        srcNode.childList = destNode.childList;
        return true;
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
}
