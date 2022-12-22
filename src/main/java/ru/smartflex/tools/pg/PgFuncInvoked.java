package ru.smartflex.tools.pg;

class PgFuncInvoked extends PgFuncReplacementPart {
    private String funcName;
    private PgTreeNode childNode = null;

    public PgFuncInvoked(String funcName, int indexStart, int indexEnd) {
        super(PgPlSqlElEnum.PERFORM, funcName, indexStart, indexEnd);
        this.funcName = funcName;
    }

    String getFuncName() {
        return funcName;
    }

    void addParameter(PgPlSqlElEnum elementSubType, String value, int indexStart, int indexEnd) {
        addSubPart(elementSubType, value, indexStart, indexEnd);
    }

    PgTreeNode getChildNode() {
        return childNode;
    }

    void setChildNode(PgTreeNode childNode) {
        this.childNode = childNode;
    }
}
