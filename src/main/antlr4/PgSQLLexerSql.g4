lexer grammar PgSQLLexerSql;

SELECT      :   [Ss][Ee][Ll][Ee][Cc][Tt];
WITH        :   [Ww][Ii][Tt][Hh];
DISTINCT    :   [Dd][Ii][Ss][Tt][Ii][Nn][Cc][Tt];
FROM        :   [Ff][Rr][Oo][Mm];
QUERY       :   [Qq][Uu][Ee][Rr][Yy];
WHERE       :   [Ww][Hh][Ee][Rr][Ee];
ALL         :   [Aa][Ll][Ll];
ON          :   [Oo][Nn];
RECURSIVE   :   [Rr][Ee][Cc][Uu][Rr][Ss][Ii][Vv][Ee]; 

UPDATE      :   [Uu][Pp][Dd][Aa][Tt][Ee];

UPDATE_TEXT :   UPDATE .*? ';';


// temporary rule: just to hide select .*?
//SELECT_HIDE :  (SELECT | WITH) .*? ';' -> channel(HIDDEN);

