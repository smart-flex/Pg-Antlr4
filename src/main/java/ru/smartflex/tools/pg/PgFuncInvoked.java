package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

public class PgFuncInvoked {

    private String funcName;
    private int lineStart = 0;
    private int colStart = 0;
    private int lineEnd = 0;
    private int colEnd = 0;

    private List<FuncInvokedParameter> paramList = new ArrayList<>();


    PgFuncInvoked(String funcName, int lineStart, int colStart, int lineEnd, int colEnd) {
        this.funcName = funcName;
        this.lineStart = lineStart;
        this.colStart = colStart;
        this.lineEnd = lineEnd;
        this.colEnd = colEnd;
    }

    String getFuncName() {
        return funcName;
    }

    void addParameter(String value) {
        paramList.add(new FuncInvokedParameter((value)));
    }

    int getLineStart() {
        return lineStart;
    }

    int getColStart() {
        return colStart;
    }

    int getLineEnd() {
        return lineEnd;
    }

    int getColEnd() {
        return colEnd;
    }

    class FuncInvokedParameter {
        private String value;

        public FuncInvokedParameter(String value) {
            this.value = value;
        }
    }
}
