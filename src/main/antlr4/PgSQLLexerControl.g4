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

WS  : [ \t\r\n]+ -> skip ;
