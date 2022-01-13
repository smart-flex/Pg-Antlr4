parser grammar PgSQLIncludeParser;

import PgSQLParserDataType, PgSQLParserExpression;

@header {
package ru.smartflex.tools.pg;
}
options { tokenVocab=PgSQLIncludeLexer; }

functionDefinition
    : fuunctionCreateDef identifier functionParamsDef functionReturns
    (functionAttributes)*
    AS
    DECL_DOLLAR
    BEGIN (seqOfStatements)? (returnStatement)?
    END
    ( (SEMI DECL_DOLLAR functionAttributes SEMI)
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
    : (statement)+
    ;

statement
    : nullStatement
    | performStatement
    | raiseStatement
    | ifDef
    ;

nullStatement
    : NULL SEMI
    ;

performStatement
    : PERFORM functionInvocation SEMI
    ;

returnStatement
    : RETURN constantExpression SEMI
    | RETURN anonymousParameter SEMI
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

functionParamsDef
    : LPAREN functionParamDefinitionList RPAREN
    ;

functionParamDefinitionList
    : functionParamDefinition (COMMA functionParamDefinition)*
    ;

functionParamDefinition
    : identifier? pgTypeEnum
    ;

// Unfortunately, antlr4 does not allow permutation (to combain statements in any order)
functionAttributes
    : LANGUAGE_IDENT
    | MD_NULL_1 
    | MD_NULL_2 
    | MD_NULL_3
    ;

ifDef
    : IF complexExpression THEN
      (seqOfStatements)?
      END_IF SEMI
    ;

complexExpression
    : (anonymousParameter | identifier | constantExpression)
    operators?
    (anonymousParameter | identifier | constantExpression)?
    ;
/*
selectStatement
   : (SELECT | WITH) etc....
   ;
*/