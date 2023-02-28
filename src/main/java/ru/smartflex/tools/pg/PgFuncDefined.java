package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

public class PgFuncDefined {
    private String funcName;
    private ReturnTypeEnum returnType;
    private String dataTypeName;
    private List<TableRetParameter> tableRetParlist = new ArrayList<>();
    private String funcBody;

    private PgFuncBodyPartsBag funcBodyPartsBag = new PgFuncBodyPartsBag();

    PgFuncDefined() {
    }

    void addPart(PgFuncReplacementPart part) {
        funcBodyPartsBag.addPart(part);
    }

    List<PgFuncReplacementPart> getParts() {
        return funcBodyPartsBag.getParts();
    }

    PgFuncReplacementPart getPart(int indexStart) {
        return funcBodyPartsBag.getPart(indexStart);
    }

    PgFuncReplacementPart getPart(int indexStart, PgPlSqlElEnum elementType) {
        return funcBodyPartsBag.getPart(indexStart, elementType);
    }

    List<PgFuncReplacementPart> getFuncParamParts() {
        return funcBodyPartsBag.getFuncParamParts();
    }
/*
    String getFunctionBlockStatementAsString() {
        return funcBody.substring(functionBlockStatement.indexStart, functionBlockStatement.indexEnd + 1);
    }
*/
    public String getFuncName() {
        return funcName;
    }

    void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    void setFuncBody(String funcBody) {
        this.funcBody = funcBody;
    }

    String getFuncBody() {
        return funcBody;
    }

    void setFuncReturnSetOf(String dataTypeName) {
        returnType = ReturnTypeEnum.SETOF;
        this.dataTypeName = dataTypeName;
    }

    void setFuncReturnRefcursor() {
        returnType = ReturnTypeEnum.REFCURSOR;
    }

    void addFuncReturnTableParameter(String parName, String parType) {
        returnType = ReturnTypeEnum.TABLE;
        tableRetParlist.add(new TableRetParameter(parName, parType));
    }

    void setFuncReturnUsual(String dataTypeName) {
        returnType = ReturnTypeEnum.USUAL;
        this.dataTypeName = dataTypeName;
    }

    public TableRetParameter getTableRetParameter(int i) {
        return tableRetParlist.get(i);
    }

    public ReturnTypeEnum getReturnType() {
        return returnType;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public enum ReturnTypeEnum {
        SETOF, REFCURSOR, TABLE, USUAL;
    }

    public class TableRetParameter {
        String parName = null;
        String parType = null;

        public TableRetParameter(String parName, String parType) {
            this.parName = parName;
            this.parType = parType;
        }

        public String getParName() {
            return parName;
        }

        public String getParType() {
            return parType;
        }
    }
}
