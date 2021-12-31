package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class PgSQLIncludeLexerWrapper extends ru.smartflex.tools.pg.PgSQLIncludeLexer {

    boolean lexerErrorHappened = false;

    public PgSQLIncludeLexerWrapper(CharStream сharStream) {
        super(сharStream);
        init();
    }

    private void init() {
        removeErrorListeners();
        addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int pos, String msg, RecognitionException e) {
                lexerErrorHappened = true;
                ParserBuilder.printErrorMessage(line, pos, msg);

            }
        });
    }

    boolean isLexerErrorHappened() {
        return lexerErrorHappened;
    }
}
