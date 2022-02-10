package ru.smartflex.tools.pg;

public class PgParsingResult {

    private String functionName;

    public PgParsingResult() {
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return "PgParsingResult{" +
                "functionName='" + functionName + '\'' +
                '}';
    }
}
