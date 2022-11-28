package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

public class PgFuncInvoked {

    private String funcName;
    private int indexStart = 0;
    private int indexEnd = 0;

    private List<FuncInvokedParameter> paramList = new ArrayList<>();

    public PgFuncInvoked(String funcName, int indexStart, int indexEnd) {
        this.funcName = funcName;
        this.indexStart = indexStart;
        this.indexEnd = indexEnd;
    }

    String getFuncName() {
        return funcName;
    }

    void addParameter(String value) {
        paramList.add(new FuncInvokedParameter((value)));
    }

    int getIndexStart() {
        return indexStart;
    }

    int getIndexEnd() {
        return indexEnd;
    }

    class FuncInvokedParameter {
        private String value;

        public FuncInvokedParameter(String value) {
            this.value = value;
        }
    }
}
