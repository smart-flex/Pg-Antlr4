package ru.smartflex.tools.pg;

import java.util.ArrayList;

public class PgParsingResult {

    private String functionName;
    private ArrayList<String> functionInvocationsList = new ArrayList<>();
    private boolean parsingErrorHappened = false;

    public PgParsingResult() {
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public void addElementToFunctionInvocationsList(String currentInvocation) {
        functionInvocationsList.add(currentInvocation);
    }

    public void setParsingErrorHappened(boolean parsingErrorHappened) {
        this.parsingErrorHappened = parsingErrorHappened;
    }

    public boolean isParsingErrorHappened() {
        return parsingErrorHappened;
    }

    @Override
    public String toString() {
        return "PgParsingResult{" +
                "functionName='" + functionName + '\'' +
                ", functionInvocationsList=" + functionInvocationsList +
                '}';
    }
}
