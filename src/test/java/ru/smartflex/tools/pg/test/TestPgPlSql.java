package ru.smartflex.tools.pg.test;

import org.junit.Test;
import ru.smartflex.tools.pg.*;

import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;

public class TestPgPlSql {

    private static int amount = 1;

    private void testValidityPlPgSql(String pgSql) {
        System.out.println(String.format("*** %2d *** " + pgSql, amount++));
        PgSQLIncludeParserWrapper wrapper = ParserBuilder.makeParser(pgSql);
        wrapper.functionDefinition();

        assertEquals(false, wrapper.isParsingErrorHappened());
    }

    @Test
    public void testParsing() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources();
        stream.forEach(c -> testValidityPlPgSql(c.getSqlName()));
    }

    @Test
    public void testGeneratingAll() {
        System.out.println("\n\n");

        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources();
        new PgParseFunctions().parseFromEnum(stream);
    }

    @Test
    public void testGeneratingOne() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p01_void_perform.sql");
        new PgParseFunctions().parseFromEnum(stream);
    }

    @Test
    public void testGeneratingTwo() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p02_void_perform.sql");
        new PgParseFunctions().parseFromEnum(stream);
    }

    @Test
    public void testParsingReturnSeToF() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p09_call.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);
        PgParsingResult result = pgParsingResultBag.getResultFirst();
        PgFuncDefined funcDef = result.getFuncDefined();
        PgFuncDefined.ReturnTypeEnum retType = funcDef.getReturnType();
        assertEquals(PgFuncDefined.ReturnTypeEnum.SETOF, retType);
        assertEquals("t09_yyyymm", funcDef.getDataTypeName());
    }

    @Test
    public void testParsingReturnRefcursor() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p08_cursor.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);
        PgParsingResult result = pgParsingResultBag.getResultFirst();
        PgFuncDefined funcDef = result.getFuncDefined();
        PgFuncDefined.ReturnTypeEnum retType = funcDef.getReturnType();
        assertEquals(PgFuncDefined.ReturnTypeEnum.REFCURSOR, retType);
    }

    @Test
    public void testParsingReturnTable() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p03_table.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);
        PgParsingResult result = pgParsingResultBag.getResultFirst();
        PgFuncDefined funcDef = result.getFuncDefined();
        PgFuncDefined.ReturnTypeEnum retType = funcDef.getReturnType();
        assertEquals(PgFuncDefined.ReturnTypeEnum.TABLE, retType);
        assertEquals("quantity", funcDef.getTableRetParameter(0).getParName());
        assertEquals("numeric", funcDef.getTableRetParameter(1).getParType());
    }

    @Test
    public void testParsingReturnUsual() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p01_void.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);
        PgParsingResult result = pgParsingResultBag.getResultFirst();
        PgFuncDefined funcDef = result.getFuncDefined();
        PgFuncDefined.ReturnTypeEnum retType = funcDef.getReturnType();
        assertEquals(PgFuncDefined.ReturnTypeEnum.USUAL, retType);
        assertEquals("void", funcDef.getDataTypeName());
    }

    @Test
    public void testGeneratingOutPerform() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p02_int4_v2_int4.sql",
                "p02_int4_v2.sql", "p02_void_perform.sql", "p02_int4_inout.sql", "p01_void.sql", "p02_int4_v3.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);

        PgTreeNode root = ParserHelper.makeTree(pgParsingResultBag);
        root.drawTree();

        new PgGenGlueFunctions().glue(root);

        String bodies = PgParseFunctions.getGeneratedBodies();
        String hash = ParserHelper.getHash(bodies);
        assertEquals("94d281dd8ce77a3338cc23c780273d0c", hash);

    }

    @Test
    public void testGeneratingOutAssign() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p02_int4_v2.sql",
                "p02_void_call.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);

        PgTreeNode root = ParserHelper.makeTree(pgParsingResultBag);
        root.drawTree();

        new PgGenGlueFunctions().glue(root);

        String bodies = PgParseFunctions.getGeneratedBodies();
        String hash = ParserHelper.getHash(bodies);
        assertEquals("8b225d7516337e65db257aac45de9cf8", hash);
    }


    @Test
    public void testGeneratingOutThreeLevel() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p01_void_v2.sql",
                "p02_int4_v4.sql", "p02_void_call_perf.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);

        PgTreeNode root = ParserHelper.makeTree(pgParsingResultBag);
        root.drawTree();

        new PgGenGlueFunctions().glue(root);

        String bodies = PgParseFunctions.getGeneratedBodies();
        String hash = ParserHelper.getHash(bodies);
        assertEquals("cbbef0980bc632599199cc390b345b15", hash);
    }


    @Test
    public void testGeneratingOutPerformSimple() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p01_void_perform.sql",
                "p02_int4_v2_int4.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);

        PgTreeNode root = ParserHelper.makeTree(pgParsingResultBag);
        root.drawTree();

        new PgGenGlueFunctions().glue(root);

        String bodies = PgParseFunctions.getGeneratedBodies();
        String hash = ParserHelper.getHash(bodies);
        assertEquals("85a27e2f9849e104dfb211670498c795", hash);
    }

}
