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

        try (InputStream ist = is;) {

            PgSQLIncludeParserWrapper wrapper = ParserBuilder.makeParser(ist);
            ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionDefinitionContext func = wrapper.functionDefinition();

            PgSqlIncludeListenerResult listener = new PgSqlIncludeListenerResult(pgParsingResult);

            ParseTreeWalker.DEFAULT.walk(listener, func);

            pgParsingResult.setParsingErrorHappened(wrapper.isParsingErrorHappened());

        } catch (Exception e) {
            pgParsingResult.setParsingErrorHappened(true);
            e.printStackTrace();
            // TODO transfer exception to logger or something else
        }

        is.close();

        return pgParsingResult;
    }
}
