package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PgFuncBodyPartsBag {

    private List<PgFuncReplacementPart> listPart = new ArrayList();
    private boolean sorted = false;

    void addPart(PgFuncReplacementPart part) {
        sorted = false;
        listPart.add(part);
    }

    List<PgFuncReplacementPart> getParts() {
        if (!sorted) {
            Collections.sort(listPart);
            sorted = true;
        }
        return listPart;
    }

    PgFuncReplacementPart getPart(int indexStart) {
        return getPart(indexStart, null);
    }

    PgFuncReplacementPart getPart(int indexStart, PgPlSqlElEnum elementType) {
        PgFuncReplacementPart part = null;

        for (PgFuncReplacementPart p : listPart) {
            if (p.getIndexStart() == indexStart) {
                if (elementType != null) {
                    if (p.getElementType() == elementType) {
                        part = p;
                        break;
                    }
                } else {
                    part = p;
                    break;
                }
            }
        }

        return part;
    }

}
