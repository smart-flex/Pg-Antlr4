lexer grammar PgSQLLexerEnd;

// WOW: IDENTF brokes VOID. VOID must be defined before IDENTF
IDENTF  : ('a' .. 'z' | 'A' .. 'Z') ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_')* ;

IDENFT_LIST     : IDENTF (COMMA IDENTF)*;

LINE_COMMENT    :	'--' ~[\r\n]* -> channel(HIDDEN) ;

BLOCK_COMMENT
	:	(	'/*'
			(	'/'* BLOCK_COMMENT
			|	~[/*]
			|	'/'+ ~[/*]
			|	'*'+ ~[/*]
			)*
			'*'*
			'*/'
		) -> channel(HIDDEN)
	;
