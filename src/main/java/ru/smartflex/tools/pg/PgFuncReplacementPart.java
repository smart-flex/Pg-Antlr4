package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

class PgFuncReplacementPart implements Comparable {
    private int indexStart = 0;
    private int indexEnd = 0;
    private int lineStart = 0;
    private int lineEnd = 0;
    private String value = null;
    private PgPlSqlElEnum elementType = null;
    private int suffixInt = 0;
    private PgFuncReplacementPart abovePart = null;

    private List<PgFuncReplacementPart> listSub = new ArrayList<>();

    PgFuncReplacementPart (PgPlSqlElEnum elementType, String value, ParserRuleContext prc) {
        this.elementType = elementType;
        this.value = value;
        this.indexStart = prc.start.getStartIndex();
        this.indexEnd = prc.stop.getStopIndex();
        this.lineStart = prc.start.getLine();
        this.lineEnd = prc.stop.getLine();
    }

    void addSubPart(PgPlSqlElEnum elementSubType, String value, ParserRuleContext prc) {
        PgFuncReplacementPart part = new PgFuncReplacementPart(elementSubType, value, prc);
        listSub.add(part);
    }

    void addSubPart(PgFuncReplacementPart part) {
        listSub.add(part);
    }

    List<PgFuncReplacementPart> getListSubPart() {
        return listSub;
    }

    PgFuncReplacementPart getPgFuncReplacementPart(int ind) {
        return listSub.get(ind);
    }

    int getIndexStart() {
        return indexStart;
    }

    int getIndexEnd() {
        return indexEnd;
    }

    int getLineStart() {
        return lineStart;
    }

    int getLineEnd() {
        return lineEnd;
    }

    PgPlSqlElEnum getElementType() {
        return elementType;
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

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof PgFuncReplacementPart)) return false;
        PgFuncReplacementPart castOther = (PgFuncReplacementPart) other;
        boolean fok = false;
        if (getIndexStart() == castOther.getIndexStart() && getIndexEnd() == castOther.getIndexEnd() &&
                getLineStart() == castOther.getLineStart() && getLineEnd() == castOther.getLineEnd()) {
            fok = true;
        }
        return fok;
    }

    }
