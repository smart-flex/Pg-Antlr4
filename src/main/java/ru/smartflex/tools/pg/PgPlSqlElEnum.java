package ru.smartflex.tools.pg;

public enum PgPlSqlElEnum {

    PERFORM, FUNCTION_DECLARE_BLOCK, VAR_DECLARE_BLOCK, VARIABLE_USAGE, RETURN_STATEMENT,
    ANONYMOUS_PARAMETER,

    DECL_IDENT, DECL_CONST, DECL_ANON_PAR, DECL_REF_EXPR, DECL_FUNC,
    DT_BOOLEAN, DT_CURSOR, DT_INT2, DT_INT4, DT_INT8, DT_FLOAT, DT_DOUBLE, DT_NUMERIC, DT_CHAR, DT_VARCHAR,
    DT_DATE, DT_TIME,

    // TODO удалить по завершении
    DT_TODO;
}
