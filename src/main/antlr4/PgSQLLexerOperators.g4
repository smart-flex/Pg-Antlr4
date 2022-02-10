lexer grammar PgSQLLexerOperators;

// Logical Operators
OR          :   [Oo][Rr];
AND         :   [Aa][Nn][Dd];
NOT         :   [Nn][Oo][Tt];

IS          :   [Ii][Ss];
BETWEEN     :   [Bb][Ee][Tt][Ww][Ee][Ee][Nn];
IN          :   [Ii][Nn];

// 9.2. Comparison Operators

LESS_THAN    :   '<';
GREATER_THAN :   '>';
EQUAL        :   '=';
NOT_EQUAL    :   '!=';
NOT_EQUAL2   :   '<>';
ASSIGN       :   ':=';

// 9.3. Mathematical Functions and Operators

PERCENT     :   '%';
ASTERISK    :   '*';
DIVIDE      :   '/';
SHIFT_LEFT  :   '<<';
SHIFT_RIGHT :   '>>';

// 9.4. String Functions and Operators

CONCAT      :   '||';


// other

TYPE_CAST
   : '::'
   ;
