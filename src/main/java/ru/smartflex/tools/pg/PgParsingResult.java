package ru.smartflex.tools.pg;

import java.util.ArrayList;

public class PgParsingResult {

    private PgFuncDefined funcDefined = null;
    private ArrayList<PgFuncInvoked> functionInvocationsList = new ArrayList<>();
    private boolean parsingErrorHappened = false;

    PgParsingResult() {
    }

    void setFunctionName(String functionName) {
        funcDefined = new PgFuncDefined(functionName);
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

    void addFunctionInvocationsName(String funcName, int lineStart, int colStart, int lineEnd, int colEnd) {
        functionInvocationsList.add(new PgFuncInvoked(funcName, lineStart, colStart, lineEnd, colEnd));
    }

    PgFuncInvoked getLastFunctionInvoked() {
        return functionInvocationsList.get(functionInvocationsList.size() - 1);
    }

    void setParsingErrorHappened(boolean parsingErrorHappened) {
        this.parsingErrorHappened = parsingErrorHappened;
    }

    public boolean isParsingErrorHappened() {
        return parsingErrorHappened;
    }

    @Override
    public String toString() {
        return "PgParsingResult{" +
                "functionName='" + (funcDefined != null ? funcDefined.getFuncName() : null) + '\'' +
                ", functionInvocationsList=" + functionInvocationsList +
                '}';
    }
}
