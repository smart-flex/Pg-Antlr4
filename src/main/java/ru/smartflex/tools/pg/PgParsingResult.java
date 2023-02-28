package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class PgParsingResult {

    private PgFuncDefined funcDefined = new PgFuncDefined();
    private List<PgFuncInvoked> functionInvocationsList = new ArrayList<>();
    private boolean parsingErrorHappened = false;

    PgParsingResult() {
    }

    void setFunctionName(String functionName) {
        funcDefined.setFuncName(functionName);
    }

    void setFuncBody(String funcBody) {
        funcDefined.setFuncBody(funcBody);
    }

    void setFuncReturnSetOf(String dataTypeName) {
        funcDefined.setFuncReturnSetOf(dataTypeName);
    }

    void setFuncReturnRefcursor() {
        funcDefined.setFuncReturnRefcursor();
    }

    void addFuncReturnTableParameter(String parName, String parType) {
        funcDefined.addFuncReturnTableParameter(parName, parType);
    }

    void setFuncReturnUsual(String dataTypeName) {
        funcDefined.setFuncReturnUsual(dataTypeName);
    }

    public PgFuncDefined getFuncDefined() {
        return funcDefined;
    }

    PgFuncInvoked addFunctionInvocation(PgPlSqlElEnum elementType, String funcName, ParserRuleContext prc) {
        PgFuncInvoked invoked = new PgFuncInvoked(elementType, funcName, prc);
        functionInvocationsList.add(invoked);
        funcDefined.addPart(invoked);
        return invoked;
    }

    public List<PgFuncInvoked> getFunctionInvocationsList() {
        return functionInvocationsList;
    }

    void setParsingErrorHappened(boolean parsingErrorHappened) {
        this.parsingErrorHappened = parsingErrorHappened;
    }

    public boolean isParsingErrorHappened() {
        return parsingErrorHappened;
    }

    void setFunctionBlockStatementIndexes(ParserRuleContext prc) {
        //funcDefined.setFunctionBlockStatementIndexes(indexStart, indexEnd);
        PgFuncReplacementPart part = new PgFuncReplacementPart(PgPlSqlElEnum.FUNCTION_DECLARE_BLOCK, null, prc);
        funcDefined.addPart(part);
    }

    PgFuncReplacementPart getPart(int indexStart) {
        return funcDefined.getPart(indexStart);
    }

    PgFuncReplacementPart getPart(int indexStart, PgPlSqlElEnum elementType) {
        return funcDefined.getPart(indexStart, elementType);
    }

    void addPart(PgFuncReplacementPart part) {
        funcDefined.addPart(part);
    }

    @Override
    public String toString() {
        return "PgParsingResult{" +
                "functionName='" + (funcDefined != null ? funcDefined.getFuncName() : null) + '\'' +
                ", functionInvocationsList=" + functionInvocationsList +
                '}';
    }
}
