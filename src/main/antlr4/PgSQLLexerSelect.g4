lexer grammar PgSQLLexerSelect;

SELECT      :   [Ss][Ee][Ll][Ee][Cc][Tt];
WITH        :   [Ww][Ii][Tt][Hh];

SELECT_END  :   ~[;] ;

QUERY       :   [Qq][Uu][Ee][Rr][Yy];

QUERY_TEXT  :   (SELECT | WITH) .*? (LOOP | ';');



// temporary rule: just to hide select .*?
//SELECT_HIDE :  (SELECT | WITH) .*? ';' -> channel(HIDDEN);

