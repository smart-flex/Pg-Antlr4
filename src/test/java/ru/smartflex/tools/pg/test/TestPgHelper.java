package ru.smartflex.tools.pg.test;

import org.junit.Test;
import ru.smartflex.tools.pg.ParserBuilder;
import ru.smartflex.tools.pg.PgSQLIncludeParserWrapper;


import static junit.framework.TestCase.assertEquals;

public class TestPgHelper {

    @Test
    public void testSamples() {
        PgSQLIncludeParserWrapper wrapper = ParserBuilder.makeParser("plpgsql\\p01_void.sql");
        wrapper.functionDefinition();

        System.out.println(wrapper);
        assertEquals(false, wrapper.isErrorHappened());

    }
}
