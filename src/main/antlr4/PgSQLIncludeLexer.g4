lexer grammar PgSQLIncludeLexer;

import PgSQLLexerDataType, PgSQLLexerOperators, PgSQLLexerControl, PgSQLLexerSelect, PgSQLLexerEnd;

@header {
package ru.smartflex.tools.pg;
}


BEGIN       :   [Bb][Ee][Gg][Ii][Nn];
END         :   [Ee][Nn][Dd];
CREATE      :   [Cc][Rr][Ee][Aa][Tt][Ee];
REPLACE     :   [Rr][Ee][Pp][Ll][Aa][Cc][Ee];
FUNCTION    :   [Ff][Uu][Nn][Cc][Tt][Ii][Oo][Nn];
RETURNS     :   [Rr][Ee][Tt][Uu][Rr][Nn][Ss];
RETURN      :   [Rr][Ee][Tt][Uu][Rr][Nn];
LANGUAGE    :   [Ll][Aa][Nn][Gg][Uu][Aa][Gg][Ee];
AS          :   [Aa][Ss];
NULL        :   [Nn][Uu][Ll][Ll];
PERFORM     :   [Pp][Ee][Rr][Ff][Oo][Rr][Mm];
RAISE       :   [Rr][Aa][Ii][Ss][Ee];

DECLARE     :   [Dd][Ee][Cc][Ll][Aa][Rr][Ee];
ROWTYPE     :   [Rr][Oo][Ww][Tt][Yy][Pp][Ee];
CONSTANT    :   [Cc][Oo][Nn][Ss][Tt][Aa][Nn][Tt];
NOT_NULL    :   [Nn][Oo][Tt]WS[Nn][Uu][Ll][Ll];
DEFAULT     :   [Dd][Ee][Ff][Aa][Uu][Ll][Tt];
TYPE        :   [Tt][Yy][Pp][Ee];
CURSOR      :   [Cc][Uu][Rr][Ss][Oo][Rr];
REFCURSOR   :   [Rr][Ee][Ff][Cc][Uu][Rr][Ss][Oo][Rr];
NO          :   [Nn][Oo];
SCROLL      :   [Ss][Cc][Rr][Oo][Ll][Ll];

LANGUAGES : '\'plpgsql\'' | 'plpgsql';
LANGUAGE_IDENT : LANGUAGE WS LANGUAGES;

//modes
//CALLED ON NULL INPUT
MD_NULL_1   :   [Cc][Aa][Ll][Ll][Ee][Dd] WS [Oo][Nn] WS [Nn][Uu][Ll][Ll] WS [Ii][Nn][Pp][Uu][Tt];
//RETURNS NULL ON NULL INPUT
MD_NULL_2   :   [Rr][Ee][Tt][Uu][Rr][Nn][Ss] WS [Nn][Uu][Ll][Ll] WS [Oo][Nn] WS [Nn][Uu][Ll][Ll] WS [Ii][Nn][Pp][Uu][Tt];
//STRICT
MD_NULL_3   :   [Ss][Tt][Rr][Ii][Cc][Tt];

SEMI    : ';' ;
LPAREN  : '(' ;
RPAREN  : ')' ;

COMMA   : ',' ;
DOT     : '.';

//QUERY_TEXT  : (FOR | IS) (IDENTF | DOT | ASTERISK | COMMA | LPAREN | RPAREN | .*?[;]) ;

DECL_DOLLAR : '$$' | '$BODY$';


