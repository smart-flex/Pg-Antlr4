package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    List<PgFuncReplacementPart> getFuncParamParts() {
        if (!sorted) {
            Collections.sort(listPart);
            sorted = true;
        }
        return listPart.stream().filter(it -> it.getElementType() == PgPlSqlElEnum.FUNCTION_PARAMETER_DECLARE).collect(Collectors.toList());
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
