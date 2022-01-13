parser grammar PgSQLParserExpression;

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
   : sign? NUM_INT ;

sign
   : PLUS
   | MINUS;

anonymousParameter
   : ANONYMOUS_PAR;

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
   | DEVIDE
   ;

raiseStatement
   : RAISE STRING_LITERAL SEMI
   ;
