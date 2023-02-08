package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.ParserRuleContext;

class PgFuncInvoked extends PgFuncReplacementPart {
    private String funcName;
    private PgTreeNode childNode = null;

    public PgFuncInvoked(PgPlSqlElEnum elementType, String funcName, ParserRuleContext prc) {
        super(elementType, funcName, prc);
        this.funcName = funcName;
    }

    String getFuncName() {
        return funcName;
    }

    void addParameter(PgPlSqlElEnum elementSubType, String value, ParserRuleContext prc) {
        addSubPart(elementSubType, value, prc);
    }

    PgTreeNode getChildNode() {
        return childNode;
    }

    void setChildNode(PgTreeNode childNode) {
        this.childNode = childNode;
    }
}
