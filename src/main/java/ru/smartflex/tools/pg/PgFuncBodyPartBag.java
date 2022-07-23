package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

public class PgFuncBodyPartBag {

    private List<FuncBodyPart> listPart = new ArrayList();

    FuncBodyPart getLastFuncBodyPart() {
        if (listPart.size() > 0) {
            return listPart.get(listPart.size() - 1);
        }
        return null;
    }

    class FuncBodyPart {

    }
}
