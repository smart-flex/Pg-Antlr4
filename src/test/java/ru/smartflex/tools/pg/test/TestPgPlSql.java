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
        assertEquals("19505aab2eba8d868ade0c203a18d7e6", hash);

    }

    @Test
    public void testGeneratingOutPerformSub() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p02_int4_v3.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);

        PgTreeNode root = ParserHelper.makeTree(pgParsingResultBag);
        root.drawTree();

        new PgGenGlueFunctions().glue(root);

//        String bodies = PgParseFunctions.getGeneratedBodies();
//        String hash = ParserHelper.getHash(bodies);
//        assertEquals("19505aab2eba8d868ade0c203a18d7e6", hash);

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
        assertEquals("9012bb06c47f2a0c71729e210967ce9b", hash);
    }


    @Test
    public void testGeneratingOutThreeLevel() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p01_void_v2.sql",
                "p02_int4_v4.sql", "p02_void_call_perf.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);

        PgTreeNode root = ParserHelper.makeTree(pgParsingResultBag);
        root.drawTree();

        new PgGenGlueFunctions().glue(root);

//        String bodies = PgParseFunctions.getGeneratedBodies();
//        String hash = ParserHelper.getHash(bodies);
//        System.out.println(bodies);
//        assertEquals("277185d5e56040679b83a0d905e8fa01", hash);
    }

    @Test
    public void testGeneratingOutThreeLevelSub() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p01_void_v2.sql",
                "p02_int4_v4.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);

        PgTreeNode root = ParserHelper.makeTree(pgParsingResultBag);
        root.drawTree();

        new PgGenGlueFunctions().glue(root);

//        String bodies = PgParseFunctions.getGeneratedBodies();
//        String hash = ParserHelper.getHash(bodies);
//        System.out.println(bodies);
//        assertEquals("277185d5e56040679b83a0d905e8fa01", hash);
    }

    @Test
    public void testGeneratingOutPerformSimple() {
        Stream<PgPlSQLEnums> stream = PgPlSQLEnums.getPlPgSQLResources("p01_void_perform.sql",
                "p02_int4_v2_int4.sql");
        PgParsingResultBag pgParsingResultBag = new PgParseFunctions().parseFromEnum(stream);

        PgTreeNode root = ParserHelper.makeTree(pgParsingResultBag);
        root.drawTree();

        new PgGenGlueFunctions().glue(root);

//        String bodies = PgParseFunctions.getGeneratedBodies();
//        String hash = ParserHelper.getHash(bodies);
//        assertEquals("037923206d7affac13572943ad55bcaf", hash);
    }

}
