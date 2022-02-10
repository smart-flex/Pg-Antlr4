parser grammar PgSQLParserExpression;

constantExpressionList
    : constantExpression (COMMA constantExpression)*
    ;

constantExpression
    : string
    | intValue
    | realValue
    | escapeString
    | bitString;

bitString
   : BIT_STRING_LITERAL
   ;

escapeString
   : ESC_STRING_LITERAL
   ;

string
   : STRING_LITERAL
   ;

realValue
   : sign? NUM_REAL ;

intValue
   : sign? NUM_INT
   ;

sign
   : PLUS
   | MINUS
   ;

anonymousParameter
   : ANONYMOUS_PAR
   ;

operators
   : PLUS
   | MINUS
   | OR
   | AND
   | NOT
   | IS
   | (IS NULL)
   | (IS NOT NULL)
   | BETWEEN
   | IN
   | EQUAL
   | NOT_EQUAL
   | NOT_EQUAL2
   | ASSIGN
   | PERCENT
   | ASTERISK
   | DIVIDE
   | LESS_THAN
   | GREATER_THAN
   | CONCAT
   ;

// TODO to implement many cases: 38.8. Errors and Messages
raiseStatement
   : (RAISE SEMI)
   | (RAISE NOTICE? STRING_LITERAL SEMI)
   | (RAISE NOTICE? STRING_LITERAL COMMA identifier (DOT identifier)? SEMI)
   ;

labelClause
   : SHIFT_LEFT identifier SHIFT_RIGHT
   ;

// 38.7.3.1. FETCH
fetchDirection
   : NEXT
   | PRIOR
   | FIRST
   | LAST
   | (ABSOLUTE NUM_INT)
   | (RELATIVE NUM_INT)
   | NUM_INT
   | ALL
   | FORWARD
   | BACKWARD
   ;

