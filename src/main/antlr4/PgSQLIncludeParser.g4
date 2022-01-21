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
    | loopDefQuery
    | executeStatement
    | loopDefExecute
    | blockStatement
    | cursorOpenStatement
    ;

nullStatement
    : NULL SEMI
    ;

performStatement
    : PERFORM functionInvocation SEMI
    ;

executeStatement
    : EXECUTE functionInvocation SEMI
    ;

returnStatement
    : RETURN constantExpression SEMI
    | RETURN anonymousParameter SEMI
    | RETURN identifier SEMI
    | RETURN QUERY QUERY_TEXT
    | RETURN rightPartExpressionList SEMI
    ;

functionInvocation
    : identifier functionParams?
    ;

functionParams
    : LPAREN functionParamList? RPAREN
    ;

functionParamList
    : (identifier | constantExpression) (COMMA (identifier | constantExpression))*
    ;

fuunctionCreateDef
    : CREATE (OR REPLACE)? FUNCTION;

functionReturns
    : RETURNS pgTypeEnum
    | RETURNS TABLE tableParams
    | RETURNS REFCURSOR
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

// 38.6.4. Looping Through Query Results

loopDefQuery
    : labelClause?
      FOR identifier
      IN sqlQuery
      (seqOfStatements)?
      END_LOOP
      identifier?
      SEMI
    ;

loopDefExecute
    : labelClause?
      FOR identifier
      IN EXECUTE string (USING complexExpressionList)?
      LOOP (seqOfStatements)?
      END_LOOP
      identifier?
      SEMI
    ;

// 38.2. Structure of PL/pgSQL 38.6.5. Trapping Errors
blockStatement
   : labelClause?
     (DECLARE variableDefinitions)?
     BEGIN
     (seqOfStatements)?
     ( EXCEPTION trappingErrorList )?
     END identifier?
     SEMI
   ;

trappingErrorList
    : trappingError (trappingError)*
    ;

trappingError
   : WHEN identifier THEN seqOfStatements
   ;

complexExpressionList
   : complexExpression (COMMA complexExpression)*
   ;

assignedStatement
    : identifier ASSIGN complexExpression SEMI
    ;

// Maybe to replace seqOfRightPartExpression by rightPartExpressionList
complexExpression
    : (anonymousParameter | identifier | constantExpression | refExpression)
      operators?
      seqOfRightPartExpression?
    ;

refExpression
    : identifier DOT identifier
    ;

seqOfRightPartExpression
    : (rightPartExpression)+
    ;

rightPartExpressionList
    : (rightPartExpression)*
    ;

rightPartExpression
    : LPAREN
    | anonymousParameter
    | identifier
    | constantExpression
    | AND
    | PLUS
    | COMMA
    | refExpression
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

// 38.7.2.1. OPEN FOR query
cursorOpenStatement
    : OPEN identifier NO? SCROLL? FOR sqlQuery
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

unsignedInteger
   : NUM_INT
   ;
*/

/*
selectStatement
   : (SELECT | WITH) etc....
   ;
*/