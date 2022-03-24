package ru.smartflex.tools.pg;

import java.util.stream.Stream;

public enum PgPlSQLEnums {
    P01_VOID("plpgsql\\p01_void.sql"),
    P01_VOID_V1("plpgsql\\p01_void_v1.sql"),
    P01_VOID_PERFORM("plpgsql\\p01_void_perform.sql"),
    P01_VOID_PERFORM_V1("plpgsql\\p01_void_perform_v1.sql"),
    P02_INT4("plpgsql\\p02_int4.sql"),
    P02_INT4_V1("plpgsql\\p02_int4_v1.sql"),
    P03_TABLE("plpgsql\\p03_table.sql"),
    P04_INT8("plpgsql\\p04_int8.sql"),
    P04_INT8_NULL_INPUT("plpgsql\\p04_int8_null_input.sql"),
    P04_INT8_STRICT("plpgsql\\p04_int8_strict.sql"),
    P05_VOI_DECLARE("plpgsql\\p05_void_declare.sql"),
    P06_INT4_IF("plpgsql\\p06_int4_if.sql"),
    P06_INT4_CASE("plpgsql\\p06_int4_case.sql"),
    P06_INT4_CASE_V2("plpgsql\\p06_int4_case_v2.sql"),
    P06_INT4_LOOP("plpgsql\\p06_int4_loop.sql"),
    P06_INT4_LOOP_QUERY("plpgsql\\p06_int4_loop_query.sql"),
    P01_VOID_EXECUTE("plpgsql\\p01_void_execute.sql"),
    P06_INT4_LOOP_EXECUTE("plpgsql\\p06_int4_loop_execute.sql"),
    P07_VOID_BLOCK("plpgsql\\p07_void_block.sql"),
    P02_INT4_V2("plpgsql\\p02_int4_v2.sql"),
    P08_CURSOR("plpgsql\\p08_cursor.sql"),
    P08_CURSOR_FULL("plpgsql\\p08_cursor_full.sql"),
    P09_CALL("plpgsql\\p09_call.sql");

    private String sqlName;

    private PgPlSQLEnums(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getSqlName() {
        return sqlName;
    }

    public static Stream<PgPlSQLEnums> getPlPgSQLResources() {
        Stream<PgPlSQLEnums> stream = Stream.of(PgPlSQLEnums.values());
        return stream;
    }

    public static Stream<PgPlSQLEnums> getPlPgSQLResources(String sqlName) {
        Stream<PgPlSQLEnums> stream = Stream.of(PgPlSQLEnums.values()).filter(pge -> pge.getSqlName().contains(sqlName));
        return stream;
    }
}
