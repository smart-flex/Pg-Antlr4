package ru.smartflex.tools.pg;

public class PgSQLQjuException extends RuntimeException {

    public PgSQLQjuException() {
    }

    public PgSQLQjuException(String msg) {
        super(msg);
    }

    public PgSQLQjuException(String string, Throwable root) {
        super(string, root);
    }

}
