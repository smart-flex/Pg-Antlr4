package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.*;

import java.io.InputStream;

public class ParserBuilder {

    private static InputStream getSqlBody(String name) {
        return ParserBuilder.class.getClassLoader().getResourceAsStream(name);
    }

    public static PgSQLIncludeParserWrapper makeParser(String resourceName) {
        InputStream is = getSqlBody(resourceName);
        return makeParser(is);
    }

    public static PgSQLIncludeParserWrapper makeParser(InputStream is) {
        PgSQLIncludeParserWrapper wrapper = null;

        try {
            CharStream сharStream = CharStreams.fromStream(is);
            PgSQLIncludeLexerWrapper lexer = new PgSQLIncludeLexerWrapper(сharStream);

            wrapper = new PgSQLIncludeParserWrapper(new CommonTokenStream(lexer));
            wrapper.setLexer(lexer);
        } catch (Exception e) {
            throw new PgSQLQjuException("Parser creation exception", e);
        }

        return wrapper;
    }

    public static void printErrorMessage(int line, int pos, String msg) {
        String text = "line " + String.valueOf(line) + ":" + String.valueOf(pos) + " " + msg;
        System.err.println(text);
    }


}
