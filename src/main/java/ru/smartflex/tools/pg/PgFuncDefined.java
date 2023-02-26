package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

public class PgFuncDefined {
    private String funcName;
    private ReturnTypeEnum returnType;
    private String dataTypeName;
    @Deprecated
    private List<FuncParameter> parList = new ArrayList<>();
    private List<TableRetParameter> tableRetParlist = new ArrayList<>();
    private String funcBody;
//    private FunctionBlockStatement functionBlockStatement = new FunctionBlockStatement();

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

    List<FuncParameter> getParList() {
        return parList;
    }

    @Deprecated
    void addFuncParameter(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionContext parCtx) {
        ru.smartflex.tools.pg.PgSQLIncludeParser.ArgModeContext argMode = parCtx.argMode();
        ru.smartflex.tools.pg.PgSQLIncludeParser.IdentifierContext ident = parCtx.identifier();
        PgPlSqlElEnum type = ParserHelper.defineDataType(parCtx.pgTypeFull());

        parList.add(new FuncParameter((argMode != null ? argMode.getText() : null),
                (ident != null ? ident.getText() : null), type));
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
/*
    void setFunctionBlockStatementIndexes(int indexStart, int indexEnd) {
        functionBlockStatement.indexStart = indexStart;
        functionBlockStatement.indexEnd = indexEnd;
    }

    FunctionBlockStatement getFunctionBlockStatement() {
        return functionBlockStatement;
    }
*/
/*
    class FunctionBlockStatement {
        int indexStart = 0;
        int indexEnd = 0;
    }
*/
    @Deprecated
    class FuncParameter {
        String argMode;
        String argName;
        PgPlSqlElEnum argType;

        FuncParameter(String argMode, String argName, PgPlSqlElEnum argType) {
            this.argMode = argMode;
            this.argName = argName;
            this.argType = argType;
        }

        String getArgName() {
            return argName;
        }
    }
}
