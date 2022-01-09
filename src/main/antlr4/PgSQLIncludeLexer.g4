lexer grammar PgSQLIncludeLexer;

import PgSQLLexerDataType, PgSQLLexerOperators, PgSQLLexerEnd;

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

LANGUAGES : '\'plpgsql\'' | 'plpgsql';
LANGUAGE_IDENT : LANGUAGE WS LANGUAGES;


SEMI    : ';' ;
LPAREN  : '(' ;
RPAREN  : ')' ;

// WOW: IDENTF brokes VOID. VOID must be defined before IDENTF
////IDENTF  : ('a' .. 'z' | 'A' .. 'Z') ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_')* ;
//VOID        :   [Vv][Oo][Ii][Dd];

COMMA   : ',' ;


DECL_DOLLAR : '$$' | '$BODY$';

//WS  : [ \t\r\n]+ -> skip ;
