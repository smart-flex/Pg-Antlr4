package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenStream;

public class PgSQLIncludeParserWrapper extends ru.smartflex.tools.pg.PgSQLIncludeParser {

    private boolean parserErrorHappened = false;
    private PgSQLIncludeLexerWrapper lexer = null;

    public PgSQLIncludeParserWrapper(TokenStream input) {
        super(input);

        init();
    }

    private void init() {
        removeErrorListeners();

        addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int pos, String msg, RecognitionException e) {
                parserErrorHappened = true;
                ParserBuilder.printErrorMessage(line, pos, msg);
            }
        });

    }

    public void setLexer(PgSQLIncludeLexerWrapper lexer) {
        this.lexer = lexer;
    }

    public boolean isParsingErrorHappened() {
        boolean fok = false;

        if (lexer != null) {
            fok = lexer.isLexerErrorHappened();
        }
        if (!fok) {
            fok = parserErrorHappened;
        }

        return fok;
    }

}
