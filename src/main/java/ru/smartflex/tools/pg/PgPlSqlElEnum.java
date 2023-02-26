package ru.smartflex.tools.pg;

public enum PgPlSqlElEnum {

    PERFORM_STATEMENT,
    // самый верхний блок DECLARE-BEGIN-END в ХП, определяющий тело ХП
    FUNCTION_DECLARE_BLOCK,
    VAR_DECLARE_BLOCK, VARIABLE_USAGE, RETURN_STATEMENT, ASSIGN_STATEMENT,
    @Deprecated
    FUNCTION_PARAM_DECLARE_BLOCK,

    FUNCTION_PARAMETER_DECLARE,
    VARIABLE_DECLARE,

    FUNC_INVOKE_STATEMENT,
    ANONYMOUS_PARAMETER,

    DECL_IDENT, DECL_CONST, DECL_ANON_PAR, DECL_REF_EXPR, DECL_FUNC,
    DT_BOOLEAN, DT_CURSOR, DT_INT2, DT_INT4, DT_INT8, DT_FLOAT, DT_DOUBLE, DT_NUMERIC, DT_CHAR, DT_VARCHAR,
    DT_DATE, DT_TIME,

    // TODO удалить по завершении
    @Deprecated
    DT_TODO;
}
