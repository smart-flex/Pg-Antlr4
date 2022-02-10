package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.InputStream;
import java.util.concurrent.Callable;

public class ThreadPgParser implements Callable<PgParsingResult> {
    private InputStream is;

    public ThreadPgParser(InputStream is) {
        this.is = is;
    }

    @Override
    public PgParsingResult call() throws Exception {
        PgParsingResult pgParsingResult = new PgParsingResult();

        PgSQLIncludeParserWrapper wrapper = ParserBuilder.makeParser(is);
        ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionDefinitionContext func = wrapper.functionDefinition();

        PgSqlIncludeListener listener = new PgSqlIncludeListener(pgParsingResult);

        ParseTreeWalker.DEFAULT.walk(listener, func);

        //is.close();

        return pgParsingResult;
    }
}
