lexer grammar PgSQLLexerEnd;

// WOW: IDENTF brokes VOID. VOID must be defined before IDENTF
IDENTF  : ('a' .. 'z' | 'A' .. 'Z') ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_')* ;
