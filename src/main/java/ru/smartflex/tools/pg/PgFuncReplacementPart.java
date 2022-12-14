package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

class PgFuncReplacementPart implements Comparable {
    private int indexStart = 0;
    private int indexEnd = 0;
    private String value = null;
    private PgPlSqlElEnum elementType = null;

    private List<PgFuncReplacementPart> listSub = new ArrayList<>();

    PgFuncReplacementPart (PgPlSqlElEnum elementType, String value, int indexStart, int indexEnd) {
        this.elementType = elementType;
        this.value = value;
        this.indexStart = indexStart;
        this.indexEnd = indexEnd;
    }

    void addSubPart(String value, int indexStart, int indexEnd) {
        PgFuncReplacementPart part = new PgFuncReplacementPart(this.elementType, value, indexStart, indexEnd);
        listSub.add(part);
    }
    int getIndexStart() {
        return indexStart;
    }

    int getIndexEnd() {
        return indexEnd;
    }

    PgPlSqlElEnum getElementType() {
        return elementType;
    }

    @Override
    public int compareTo(Object o) {
        int ret = 0;

        PgFuncReplacementPart p1 = (PgFuncReplacementPart) o;

        if (getIndexStart() < p1.getIndexStart()) {
            ret = -1;
        } else if (getIndexStart() > p1.getIndexStart()) {
            ret = 1;
        }

        return ret;
    }
}
