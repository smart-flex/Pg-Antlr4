package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

public class PgFuncBodyPartBag {

    private PgFuncDefined funcDefined;

    private List<FuncBodyPart> listPart = new ArrayList();

    PgFuncBodyPartBag(PgFuncDefined funcDefined) {
        this.funcDefined = funcDefined;
    }

    FuncBodyPart getLastFuncBodyPart() {
        if (listPart.size() == 0) {
            // first call
            String funcBody = funcDefined.getFuncBody();
            listPart.add(new FuncBodyPart(funcBody));
        }

        if (listPart.size() > 0) {
            return listPart.get(listPart.size() - 1);
        }
        return null;
    }

    class FuncBodyPart {

        String funcPart;

        FuncBodyPart(String funcPart) {
            this.funcPart = funcPart;
        }
    }
}
