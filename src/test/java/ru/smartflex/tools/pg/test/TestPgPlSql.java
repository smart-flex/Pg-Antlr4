package ru.smartflex.tools.pg.test;

import org.junit.Test;
import ru.smartflex.tools.pg.*;
import ru.smartflex.tools.pg.PgSQLIncludeParser;

import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;

public class TestPgPlSql {

    private static int amount = 1;

    private void testValidityPlPgSql(String pgSql) {
        System.out.println(String.format("*** %2d *** " + pgSql, amount++));
        PgSQLIncludeParserWrapper wrapper = ParserBuilder.makeParser(pgSql);
        PgSQLIncludeParser.FunctionDefinitionContext func = wrapper.functionDefinition();

        assertEquals(false, wrapper.isParsingErrorHappened());
    }

    @Test
    public void testParsing() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources();
        stream.forEach(c -> testValidityPlPgSql(c.getSqlName()));
    }

    @Test
    public void testGeneratingAll() {
        System.out.println();
        System.out.println();

        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources();
        new PgGenFunctions().genFromEnum(stream);
    }

    @Test
    public void testGeneratingOne() {
        //p01_void_perform.sql
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p01_void_perform.sql");
        new PgGenFunctions().genFromEnum(stream);
    }

    @Test
    public void testMakeTree() {
        /*
        PgParsingResult pr1 = new PgParsingResult();
        pr1.setFunctionName("func_1");

        PgGenResultBag bag = new PgGenResultBag();
        bag.addResult(pr1);
*/

        // TODO make tree
    }
}
