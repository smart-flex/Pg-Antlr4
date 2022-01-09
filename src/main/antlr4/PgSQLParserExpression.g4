parser grammar PgSQLParserExpression;

expression
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

