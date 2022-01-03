parser grammar PgSQLIncludeParser;

@header {
package ru.smartflex.tools.pg;
}
options { tokenVocab=PgSQLIncludeLexer; }

functionDefinition
    : CREATE FUNCTION identifier LPAREN RPAREN RETURNS VOID AS
      DECL_DOLLAR BEGIN (seq_of_statements)? END SEMI DECL_DOLLAR
      LANGUAGE_IDENT SEMI
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

functionInvocation
    : identifier functionParams?
    ;

functionParams
    : LPAREN functionParamList? RPAREN
    ;

functionParamList
    : identifier (COMMA identifier)*
    ;

