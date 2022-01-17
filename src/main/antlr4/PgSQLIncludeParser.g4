parser grammar PgSQLIncludeParser;

import PgSQLParserDataType, PgSQLParserExpression;

@header {
package ru.smartflex.tools.pg;
}
options { tokenVocab=PgSQLIncludeLexer; }

functionDefinition
    : fuunctionCreateDef identifier functionParamsDef functionReturns
    (seqOfFunctionAttributes)*
    AS
    DECL_DOLLAR
    labelClause?
    DECLARE? variableDefinitions
    BEGIN (seqOfStatements)? (returnStatement)?
    END
    ( (SEMI DECL_DOLLAR seqOfFunctionAttributes SEMI)
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
    | returnStatement
    | ifDef
    | caseDef
    | exitDef
    | loopDef
    | assignedStatement
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
    | RETURN identifier SEMI
    | RETURN QUERY QUERY_TEXT
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

seqOfFunctionAttributes
    : (functionAttribute)+
    ;

functionAttribute
    : LANGUAGE_IDENT
    | (MD_NULL_1 | MD_NULL_2 | MD_NULL_3)
    ;

// 38.6.2. Conditionals
ifDef
    : IF complexExpression THEN
      (seqOfStatements)?
      (ELSIF complexExpression THEN (seqOfStatements)?)?
      (ELSE (seqOfStatements)?)?
      END_IF SEMI
    ;

caseDef
    : ( CASE complexExpression WHEN constantExpressionList THEN (seqOfStatements)?
        (WHEN constantExpressionList THEN (seqOfStatements)?)*
        (ELSE (seqOfStatements)?)*
        END_CASE SEMI
      )
    | ( CASE WHEN complexExpression THEN (seqOfStatements)?
        (WHEN complexExpression THEN (seqOfStatements)?)*
        (ELSE (seqOfStatements)?)*
        END_CASE SEMI
      )
    ;

// 38.6.3. Simple Loops
exitDef
    : EXIT identifier?  
      (WHEN complexExpression)
      SEMI
    ;

loopDef
    : labelClause?
      LOOP (seqOfStatements)?
      END_LOOP
      identifier?
      SEMI
    ;

assignedStatement
    : identifier ASSIGN complexExpression SEMI
    ;

complexExpression
    : (anonymousParameter | identifier | constantExpression)
      operators?
      seqOfRightPartExpression?
    ;

seqOfRightPartExpression
    : (rightPartExpression)+
    ;

rightPartExpression
    : LPAREN
    | anonymousParameter
    | identifier
    | constantExpression
    | AND
    | COMMA
    | RPAREN
    ;

// types definition

variableDefinitions
    : (variableDefinition)*
    ;

variableDefinition
   : identifier (CONSTANT)? dataType
   ;

dataType
    : usualType
    | booleanType
    | cursorType
    ;

booleanType : BOOL_VAL_START BOOL_ASSIGN? BOOL_VAL? BOOL_VAL_END;

usualType
    : (   pgTypeEnum
        | (identifier PERCENT ROWTYPE)
        | (identifier DOT identifier PERCENT TYPE)
      )
      precisionClause?
      NOT_NULL?
      (DEFAULT | ASSIGN)?
      (identifier | string | intValue | realValue | escapeString | bitString | functionInvocation)?
      SEMI
     ;

precisionClause
    : LPAREN NUM_INT (COMMA NUM_INT)* RPAREN
    ;

// cursor definition

cursorType
    : (REFCURSOR SEMI)
    | (NO? SCROLL? CURSOR (FOR | IS) sqlQuery) 
    | (CURSOR cursorParams? (FOR | IS) sqlQuery) 
    ;

sqlQuery
    : QUERY_TEXT
    ;

cursorParams
    : LPAREN cursorParamDefinitionList RPAREN
    ;

cursorParamDefinitionList
   : cursorParamDefinition (COMMA cursorParamDefinition)*
   ;

cursorParamDefinition
    : identifier pgTypeEnum
    ;


/*
precisionPart
    : LPAREN (unsignedInteger) (COMMA unsignedInteger)? RPAREN
    ;

unsignedInteger
   : NUM_INT
   ;
*/

/*
selectStatement
   : (SELECT | WITH) etc....
   ;
*/