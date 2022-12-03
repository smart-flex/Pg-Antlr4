package ru.smartflex.tools.pg;

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

    void addFuncParameter(String argMode, String argName, String argType) {
        funcDefined.addFuncParameter(argMode, argName, argType);
    }

    PgFuncInvoked addFunctionInvocationsName(String funcName, int indexStart, int indexEnd) {
        PgFuncInvoked invoked = new PgFuncInvoked(funcName, indexStart, indexEnd);
        functionInvocationsList.add(invoked);
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

    void setFunctionBlockStatementIndexes(int indexStart, int indexEnd) {
        funcDefined.setFunctionBlockStatementIndexes(indexStart, indexEnd);
    }

    @Override
    public String toString() {
        return "PgParsingResult{" +
                "functionName='" + (funcDefined != null ? funcDefined.getFuncName() : null) + '\'' +
                ", functionInvocationsList=" + functionInvocationsList +
                '}';
    }
}
