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

DECL_DOLLAR : '$$' | '$BODY$';


