package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

public class ThreadPgParser implements Callable<PgParsingResult> {

    private static int BUFFER_SIXE = 2048;
    private InputStream is;

    public ThreadPgParser(InputStream is) {
        this.is = is;
    }

    @Override
    public PgParsingResult call() throws Exception {
        PgParsingResult pgParsingResult = new PgParsingResult();

        byte[] body = readInputStreamIntoArray();

        pgParsingResult.setFuncBody(new String(body));

        try (InputStream ist = new ByteArrayInputStream(body);) {

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

        return pgParsingResult;
    }

    private byte[] readInputStreamIntoArray() {
        byte[] out = null;

        byte[] part = new byte[BUFFER_SIXE];

        try (InputStream ist = is;) {

            int amount = 0;
            do {
                int newSize = 0;
                amount = ist.read(part);
                if (amount != -1) {
                    if (out != null) {
                        newSize = out.length;
                    }
                    byte[] outPart = new byte[amount + newSize];
                    if (out != null) {
                        System.arraycopy(out, 0, outPart, 0, newSize);
                    }
                    System.arraycopy(part, 0, outPart, newSize, amount);
                    out = outPart;
                }
            } while (amount != -1);

        } catch (Exception e) {
            throw new PgSQLIncludeException("There is error with SQL reading", e);
        }

        return out;
    }
}
