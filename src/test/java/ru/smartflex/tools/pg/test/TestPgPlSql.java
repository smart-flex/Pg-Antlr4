package ru.smartflex.tools.pg.test;

import org.junit.Test;
import ru.smartflex.tools.pg.ParserBuilder;
import ru.smartflex.tools.pg.PgSQLIncludeParserWrapper;


import static junit.framework.TestCase.assertEquals;

public class TestPgPlSql {

    private void testValidityPlPgSql(String pgSql) {
        System.out.println("*** " + pgSql);
        PgSQLIncludeParserWrapper wrapper = ParserBuilder.makeParser(pgSql);
        wrapper.functionDefinition();

        assertEquals(false, wrapper.isErrorHappened());
    }

    @Test
    public void testSamples() {
        testValidityPlPgSql("plpgsql\\p01_void.sql");
        testValidityPlPgSql("plpgsql\\p01_void_v1.sql");
        testValidityPlPgSql("plpgsql\\p01_void_perform.sql");
        testValidityPlPgSql("plpgsql\\p01_void_perform_v1.sql");
        testValidityPlPgSql("plpgsql\\p02_int4.sql");
        testValidityPlPgSql("plpgsql\\p02_int4_v1.sql");
        testValidityPlPgSql("plpgsql\\p03_table.sql");
        testValidityPlPgSql("plpgsql\\p04_int8.sql");
        testValidityPlPgSql("plpgsql\\p04_int8_null_input.sql");
        testValidityPlPgSql("plpgsql\\p04_int8_strict.sql");
        testValidityPlPgSql("plpgsql\\p05_void_declare.sql");
        testValidityPlPgSql("plpgsql\\p06_int4_if.sql");
        testValidityPlPgSql("plpgsql\\p06_int4_case.sql");
        testValidityPlPgSql("plpgsql\\p06_int4_case_v2.sql");
        testValidityPlPgSql("plpgsql\\p06_int4_loop.sql");
        testValidityPlPgSql("plpgsql\\p06_int4_loop_query.sql");
        testValidityPlPgSql("plpgsql\\p01_void_execute.sql");
        testValidityPlPgSql("plpgsql\\p06_int4_loop_execute.sql");
        testValidityPlPgSql("plpgsql\\p07_void_block.sql");
        testValidityPlPgSql("plpgsql\\p02_int4_v2.sql");
        testValidityPlPgSql("plpgsql\\p08_cursor.sql");
    }
}
