package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

class PgFuncReplacementPart implements Comparable {
    private int indexStart = 0;
    private int indexEnd = 0;
    private String value = null;
    private PgPlSqlElEnum elementType = null;
    private PgPlSqlElEnum elementSubType = null;
    private int suffixInt = 0;
    private PgFuncReplacementPart abovePart = null;

    private List<PgFuncReplacementPart> listSub = new ArrayList<>();

    PgFuncReplacementPart (PgPlSqlElEnum elementType, String value, int indexStart, int indexEnd) {
        this.elementType = elementType;
        this.value = value;
        this.indexStart = indexStart;
        this.indexEnd = indexEnd;
    }

    PgFuncReplacementPart (PgPlSqlElEnum elementType, PgPlSqlElEnum elementSubType, String value, int indexStart, int indexEnd) {
        this.elementType = elementType;
        this.elementSubType = elementSubType;
        this.value = value;
        this.indexStart = indexStart;
        this.indexEnd = indexEnd;
    }

    void addSubPart(String value, int indexStart, int indexEnd) {
        PgFuncReplacementPart part = new PgFuncReplacementPart(this.elementType, value, indexStart, indexEnd);
        listSub.add(part);
    }

    void addSubPart(PgPlSqlElEnum elementSubType, String value, int indexStart, int indexEnd) {
        PgFuncReplacementPart part = new PgFuncReplacementPart(this.elementType, elementSubType, value, indexStart, indexEnd);
        listSub.add(part);
    }

    List<PgFuncReplacementPart> getListSubPart() {
        return listSub;
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

    PgPlSqlElEnum getElementSubType() {
        return elementSubType;
    }

    String getValue() {
        return value;
    }

    String getNameWithSuffix() {
        String ret = null;

        if (suffixInt == 0) {
            suffixInt = PgParseFunctions.nextSuffix();
        }
        ret = getValue() + "_sf" + String.valueOf(suffixInt);

        return ret;
    }

    void setAbovePart(PgFuncReplacementPart abovePart) {
        if (this.abovePart == null) {
            this.abovePart = abovePart;
        }
    }

    PgFuncReplacementPart getAbovePart() {
        return abovePart;
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
