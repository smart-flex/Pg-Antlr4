package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

public class PgFuncBodyPartBag {

    private List<FuncBodyPart> listPart = new ArrayList();

    PgFuncBodyPartBag(String bodyPart, PgTreeNode nodeInvoked) {
        listPart.add(new FuncBodyPart(bodyPart, nodeInvoked));
    }

    void addBodyPart(String bodyPart, PgTreeNode nodeInvoked) {
        listPart.add(new FuncBodyPart(bodyPart, nodeInvoked));
    }

    boolean checkGlue(PgTreeNode node) {
        StringBuilder sb = new StringBuilder();
        for (FuncBodyPart fb : listPart) {
            sb.append(fb.getFuncPart());
        }
        String gluedBody = sb.toString();
        String funcBody = node.getFuncDefined().getFuncBody();

        return gluedBody.equals(funcBody);
    }

    List<FuncBodyPart> getListPart() {
        return listPart;
    }

    class FuncBodyPart {
        private String funcPart;
        private PgTreeNode nodeInvoked = null;

        FuncBodyPart(String funcPart) {
            this.funcPart = funcPart;
        }

        FuncBodyPart(String funcPart, PgTreeNode nodeInvoked) {
            this.funcPart = funcPart;
            this.nodeInvoked = nodeInvoked;
        }

        String getFuncPart() {
            return funcPart;
        }

        PgTreeNode getNodeInvoked() {
            return nodeInvoked;
        }
    }
}
