package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PgFuncBodyPartsBag {

    private List<PgFuncReplacementPart> listPart = new ArrayList();
    private boolean sorted = false;

    void addPart(PgFuncReplacementPart part) {
        listPart.add(part);
    }

    List<PgFuncReplacementPart> getParts() {
        if (!sorted) {
            Collections.sort(listPart);
            sorted = true;
        }
        return listPart;
    }
}
