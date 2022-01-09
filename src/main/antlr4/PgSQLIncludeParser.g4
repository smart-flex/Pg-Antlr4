parser grammar PgSQLIncludeParser;

import PgSQLParserDataType, PgSQLParserExpression;

@header {
package ru.smartflex.tools.pg;
}
options { tokenVocab=PgSQLIncludeLexer; }

functionDefinition
    : fuunctionCreateDef identifier LPAREN RPAREN functionReturns
    (LANGUAGE_IDENT)?
    AS
    DECL_DOLLAR
    BEGIN (seq_of_statements)? (return_statement)?
    END
    ( (SEMI DECL_DOLLAR LANGUAGE_IDENT SEMI)
      |
      (DECL_DOLLAR SEMI)
      |
      (SEMI DECL_DOLLAR SEMI)
    )
    ;

identifier
   : IDENTF
   ;

seq_of_statements
    : (statement ';')+
    ;

statement
    : null_statement
    | perform_statement
    ;

null_statement
    : NULL
    ;

perform_statement
    : PERFORM functionInvocation
    ;

return_statement
    : RETURN expression SEMI;

functionInvocation
    : identifier functionParams?
    ;

functionParams
    : LPAREN functionParamList? RPAREN
    ;

functionParamList
    : identifier (COMMA identifier)*
    ;

fuunctionCreateDef
    : CREATE (OR REPLACE)? FUNCTION;

functionReturns
    : RETURNS pg_type_enum;

