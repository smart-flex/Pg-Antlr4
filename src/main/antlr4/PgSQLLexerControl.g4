lexer grammar PgSQLLexerControl;

//38.6.2. Conditionals

IF                   :   [Ii][Ff];
THEN                 :   [Tt][Hh][Ee][Nn];
ELSE                 :   [Ee][Ll][Ss][Ee];
ELSIF                :   [Ee][Ll][Ss][Ii][Ff];
END_IF               :   [Ee][Nn][Dd]WS[Ii][Ff];
CASE                 :   [Cc][Aa][Ss][Ee];
WHEN                 :   [Ww][Hh][Ee][Nn];
END_CASE             :   [Ee][Nn][Dd]WS[Cc][Aa][Ss][Ee];
LOOP                 :   [Ll][Oo][Oo][Pp];
END_LOOP             :   [Ee][Nn][Dd]WS[Ll][Oo][Oo][Pp];
EXIT                 :   [Ee][Xx][Ii][Tt];
CONTINUE             :   [Cc][Oo][Nn][Tt][Ii][Nn][Uu][Ee];
WHILE                :   [Ww][Hh][Ii][Ll][Ee];
FOR                  :   [Ff][Oo][Rr];

//38.6.5. Trapping Errors
EXCEPTION            :   [Ee][Xx][Cc][Ee][Pp][Tt][Ii][Oo][Nn];

//38.7.3.1. FETCH
FETCH                :   [Ff][Ee][Tt][Cc][Hh];
INTO                 :   [Ii][Nn][Tt][Oo];
FROM                 :   [Ff][Rr][Oo][Mm];
MOVE                 :   [Mm][Oo][Vv][Ee];

NEXT                 :   [Nn][Ee][Xx][Tt];
PRIOR                :   [Pp][Rr][Ii][Oo][Rr];
FIRST                :   [Ff][Ii][Rr][Ss][Tt];
LAST                 :   [Ll][Aa][Ss][Tt];
ABSOLUTE             :   [Aa][Bb][Ss][Oo][Ll][Uu][Tt][Ee];
RELATIVE             :   [Rr][Ee][Ll][Aa][Tt][Ii][Vv][Ee];
ALL                  :   [Aa][Ll][Ll];
FORWARD              :   [Ff][Oo][Rr][Ww][Aa][Rr][Dd];
BACKWARD             :   [Bb][Aa][Cc][Kk][Ww][Aa][Rr][Dd];

/* the following constructions does not work in Pg 8.4
FORWARD count
FORWARD ALL
BACKWARD count
BACKWARD ALL
*/
WS  : [ \t\r\n]+ -> skip ;
