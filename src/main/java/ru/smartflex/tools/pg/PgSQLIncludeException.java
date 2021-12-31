package ru.smartflex.tools.pg;

public class PgSQLIncludeException extends RuntimeException {

    public PgSQLIncludeException() {
    }

    public PgSQLIncludeException(String msg) {
        super(msg);
    }

    public PgSQLIncludeException(String string, Throwable root) {
        super(string, root);
    }

}
