parser grammar PgSQLIncludeParser;

import PgSQLParserDataType, PgSQLParserExpression;

@header {
package ru.smartflex.tools.pg;
}
options { tokenVocab=PgSQLIncludeLexer; }

functionDefinition
    : functionTitle
    (seqOfFunctionAttributes)*
    AS
    DECL_DOLLAR
    functionBlockStatement
    ( (SEMI DECL_DOLLAR seqOfFunctionAttributes SEMI)
      |
      (DECL_DOLLAR SEMI)
      |
      (SEMI DECL_DOLLAR SEMI)
    )
    ;

functionTitle
   : functionCreateDef identifier functionParamsDef functionReturns
   ;

functionBlockStatement
   : labelClause?
     (DECLARE variableDefinitions)?
     BEGIN (seqOfStatements)?
     ( EXCEPTION trappingErrorList )?
     END
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
    | cursorCloseStatement
    | fetchStatement
    | moveStatement
    | selectStatement
    | updateStatement
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
    | RETURN QUERY inlineQuery
    | RETURN rightPartExpressionList SEMI
    | RETURN NEXT identifier SEMI
    ;

functionInvocation
    : identifier LPAREN functionInvocationParamList? RPAREN
    ;

functionInvocationParamList
    : functionInvocationParameter (COMMA functionInvocationParameter)*
    ;

functionInvocationParameter
    : (identifier | constantExpression | anonymousParameter | refExpression | functionInvocation)
    ;

functionCreateDef
    : CREATE (OR REPLACE)? FUNCTION;

functionReturns
    : functionReturnsUsualType
    | functionReturnsTable
    | functionReturnsRefcursor
    | functionReturnsSetOf
    ;

functionReturnsUsualType
    : RETURNS (pgTypeEnum | tableRefColumnType)
    ;

functionReturnsTable
    : RETURNS TABLE tableParams
    ;

functionReturnsRefcursor
    : RETURNS REFCURSOR
    ;

functionReturnsSetOf
    : RETURNS SETOF identifier
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
    : argMode? identifier? pgTypeFull
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

// 38.6.4. Looping Through Query Results; 38.7.4. Looping Through a Cursor's Result

loopDefQuery
    : labelClause?
      FOR identifier
      IN (inlineQuery | identifier cursorParamList LOOP)
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
    : (functionInvocation | anonymousParameter | identifier | constantExpression | refExpression)
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

// with semi at end
booleanType : BOOL_VAL_START BOOL_ASSIGN? BOOL_VAL? BOOL_VAL_END;

pgTypeFull
    : pgTypeEnum
    | (identifier PERCENT ROWTYPE)
    | tableRefColumnType
    ;

usualType
    : pgTypeFull
      precisionClause?
      NOT_NULL?
      (DEFAULT | ASSIGN)?
      (identifier | string | intValue | realValue | escapeString | bitString | functionInvocation | NULL)?
      SEMI
     ;

tableRefColumnType
    : identifier DOT identifier PERCENT TYPE
    ;

precisionClause
    : LPAREN NUM_INT (COMMA NUM_INT)* RPAREN
    ;

// cursor definition

cursorType
    : (REFCURSOR SEMI)
    | (NO? SCROLL? CURSOR (FOR | IS) inlineQuery)
    | (CURSOR cursorParamsDef? (FOR | IS) inlineQuery)
    ;

// 38.7.2.1. OPEN FOR query; 38.7.2.2. OPEN FOR EXECUTE
cursorOpenStatement
    : (OPEN identifier NO? SCROLL? FOR inlineQuery)
    | (OPEN identifier NO? SCROLL? FOR EXECUTE string SEMI)
    | (OPEN identifier cursorParamList SEMI)
    ;

// 38.7.3.4. CLOSE
cursorCloseStatement
    : CLOSE identifier SEMI
    ;

// 38.7.3.1. FETCH
fetchStatement
    : FETCH (fetchDirection (FROM | IN))? identifier (INTO identifier)? SEMI
    ;

// 38.7.3.2. MOVE
moveStatement
    : MOVE (fetchDirection (FROM | IN))? identifier SEMI
    ;

selectStatement
    : (WITH RECURSIVE? identifier AS .*? SEMI)
      |
      (SELECT queryColClauseList INTO intoList FROM? (functionInvocation | .*?) SEMI)
    ;

updateStatement
    : UPDATE_TEXT
    ;

inlineQuery
    : SELECT queryColClauseList (FROM | WHERE)? .*? (LOOP | ';')
    ;

queryColClauseList
    : (ALL | DISTINCT (ON LPAREN intoList RPAREN)? )?
      queryColClause (COMMA queryColClause)*
    ;

queryColClause
    : (queryColumnAs operators?)* (AS identifier)?
    ;

queryColumnAs
    : (functionInvocation | castClause | cursorParam | ASTERISK | STRING_LITERAL)
    ;

castClause
    : cursorParam TYPE_CAST pgTypeEnum
    ;

intoList
    : identifier (COMMA identifier)*
    ;

cursorParamList
    : LPAREN cursorParam (COMMA cursorParam)* RPAREN
    ;

cursorParam
    : (identifier | constantExpression | anonymousParameter)
    ;

cursorParamsDef
    : LPAREN cursorParamDefinitionList RPAREN
    ;

cursorParamDefinitionList
   : cursorParamDefinition (COMMA cursorParamDefinition)*
   ;

cursorParamDefinition
    : identifier pgTypeEnum
    ;

argMode
    : (IN | OUT | INOUT | VARIADIC)
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