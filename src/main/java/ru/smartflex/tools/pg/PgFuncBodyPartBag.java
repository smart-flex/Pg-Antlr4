package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

public class PgFuncBodyPartBag {

    private List<FuncBodyPart> listPart = new ArrayList();

    PgFuncBodyPartBag(PgTreeNode node) {
        // create PgFuncBodyPartBag without children
        String funcBody = node.getFuncDefined().getFuncBody();
        listPart.add(new FuncBodyPart(funcBody));
    }

    PgFuncBodyPartBag(String bodyPart) {
        listPart.add(new FuncBodyPart(bodyPart));
    }

    void addBodyPart(String bodyPart) {
        listPart.add(new FuncBodyPart(bodyPart));
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

    class FuncBodyPart {

        private String funcPart;
        private int rowOffsetBody = 0;

        FuncBodyPart(String funcPart) {
            this.funcPart = funcPart;
        }

        int getRowOffsetBody() {
            return rowOffsetBody;
        }

        String getFuncPart() {
            return funcPart;
        }
    }
}
