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
    BEGIN (seqOfStatements)? (returnStatement)?
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

seqOfStatements
    : (statement ';')+
    ;

statement
    : nullStatement
    | performStatement
    ;

nullStatement
    : NULL
    ;

performStatement
    : PERFORM functionInvocation
    ;

returnStatement
    : RETURN expression SEMI
    | RETURN QUERY // TODO here has to be selectStatement rule
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

fuunctionCreateDef
    : CREATE (OR REPLACE)? FUNCTION;

functionReturns
    : RETURNS pgTypeEnum
    | RETURNS TABLE tableParams
    ;

tableParams
    : LPAREN tableParamDefinitionList RPAREN
    ;

tableParamDefinitionList
   : tableParamDefinition (COMMA tableParamDefinition)*
   ;

tableParamDefinition
   : identifier pgTypeEnum
   ;

/*
selectStatement
   : (SELECT | WITH) etc....
   ;
*/