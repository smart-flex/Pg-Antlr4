package ru.smartflex.tools.pg;

import java.io.InputStream;
import java.util.stream.Stream;

public class PgGenFunctions {

    public void genFromEnum(Stream<PgPlSQLEnums> stream) {

    }
    public void genFromIs(Stream<InputStream> streaam) {
        // parsing each file
        // write result to pojo
        // create nested dependencies
        // generates output from down to up
        // writes output


        /*        System.out.println(String.format("*** %2d *** " + pgSql, amount++));
        PgSQLIncludeParserWrapper wrapper = ParserBuilder.makeParser(pgSql);
        PgSQLIncludeParser.FunctionDefinitionContext func = wrapper.functionDefinition();

        PgSQLIncludeListener listener = new PgSQLIncludeListener(wrapper);

        PgSqlIncludeListener listener2 = new PgSqlIncludeListener();

        ParseTreeWalker.DEFAULT.walk(listener2, func);

        System.out.println(func.toStringTree(Arrays.asList(wrapper.getRuleNames())));

 */
//        System.out.println(listener.toString());

    }
}
