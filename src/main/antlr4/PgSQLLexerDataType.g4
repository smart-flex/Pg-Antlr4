lexer grammar PgSQLLexerDataType;


// datatypes PL/pgSQL

// Numeric Types
SMALLINT	:	[Ss][Mm][Aa][Ll][Ll][Ii][Nn][Tt];
INT2	:	[Ii][Nn][Tt][2];
INTEGER	:	[Ii][Nn][Tt][Ee][Gg][Ee][Rr];
INT	:	[Ii][Nn][Tt];
INT4	:	[Ii][Nn][Tt][4];
BIGINT	:	[Bb][Ii][Gg][Ii][Nn][Tt];
INT8	:	[Ii][Nn][Tt][8];
DECIMAL	:	[Dd][Ee][Cc][Ii][Mm][Aa][Ll];
NUMERIC	:	[Nn][Uu][Mm][Ee][Rr][Ii][Cc];
REAL	:	[Rr][Ee][Aa][Ll];
DOUBLE_PRECISION	:	[Dd][Oo][Uu][Bb][Ll][Ee]WS[Pp][Rr][Ee][Cc][Ii][Ss][Ii][Oo][Nn];
SERIAL	:	[Ss][Ee][Rr][Ii][Aa][Ll];
SERIAL4	:	[Ss][Ee][Rr][Ii][Aa][Ll][4];
BIGSERIAL	:	[Bb][Ii][Gg][Ss][Ee][Rr][Ii][Aa][Ll];
SERIAL8	:	[Ss][Ee][Rr][Ii][Aa][Ll][8];
FLOAT8	:	[Ff][Ll][Oo][Aa][Tt][8];
FLOAT4	:	[Ff][Ll][Oo][Aa][Tt][4];

//Monetary Types
MONEY	:	[Mm][Oo][Nn][Ee][Yy];

//Character Types
CHARACTER_VARYING	:	[Cc][Hh][Aa][Rr][Aa][Cc][Tt][Ee][Rr]WS[Vv][Aa][Rr][Yy][Ii][Nn][Gg];
VARCHAR	:	[Vv][Aa][Rr][Cc][Hh][Aa][Rr];
CHARACTER	:	[Cc][Hh][Aa][Rr][Aa][Cc][Tt][Ee][Rr];
CHAR	:	[Cc][Hh][Aa][Rr];
TEXT	:	[Tt][Ee][Xx][Tt];

//Binary Data Types
BYTEA	:	[Bb][Yy][Tt][Ee][Aa];

//Date/Time Types
DATE	:	[Dd][Aa][Tt][Ee];
TIME	:	[Tt][Ii][Mm][Ee];
TIME_WITHOUT_TIME_ZONE	:	[Tt][Ii][Mm][Ee]WS[Ww][Ii][Tt][Hh][Oo][Uu][Tt]WS[Tt][Ii][Mm][Ee]WS[Zz][Oo][Nn][Ee];
TIME_WITH_TIME_ZONE	:	[Tt][Ii][Mm][Ee]WS[Ww][Ii][Tt][Hh]WS[Tt][Ii][Mm][Ee]WS[Zz][Oo][Nn][Ee];
TIMETZ	:	[Tt][Ii][Mm][Ee][Tt][Zz];
TIMESTAMP	:	[Tt][Ii][Mm][Ee][Ss][Tt][Aa][Mm][Pp];
TIMESTAMP_WITHOUT_TIME_ZONE	:	[Tt][Ii][Mm][Ee][Ss][Tt][Aa][Mm][Pp]WS[Ww][Ii][Tt][Hh][Oo][Uu][Tt]WS[Tt][Ii][Mm][Ee]WS[Zz][Oo][Nn][Ee];
TIMESTAMP_WITH_TIME_ZONE	:	[Tt][Ii][Mm][Ee][Ss][Tt][Aa][Mm][Pp]WS[Ww][Ii][Tt][Hh]WS[Tt][Ii][Mm][Ee]WS[Zz][Oo][Nn][Ee];
TIMESTAMPTZ	:	[Tt][Ii][Mm][Ee][Ss][Tt][Aa][Mm][Pp][Tt][Zz];
INTERVAL	:	[Ii][Nn][Tt][Ee][Rr][Vv][Aa][Ll];

//Boolean Type
BOOLEAN	:	[Bb][Oo][Oo][Ll][Ee][Aa][Nn];
BOOL	:	[Bb][Oo][Oo][Ll];

//Enumerated Types

//Geometric Types
CIRCLE	:	[Cc][Ii][Rr][Cc][Ll][Ee];
POINT	:	[Pp][Oo][Ii][Nn][Tt];
POLYGON	:	[Pp][Oo][Ll][Yy][Gg][Oo][Nn];
LINE	:	[Ll][Ii][Nn][Ee];
LSEG	:	[Ll][Ss][Ee][Gg];
BOX	:	[Bb][Oo][Xx];
PATH	:	[Pp][Aa][Tt][Hh];

//Network Address Types
CIDR	:	[Cc][Ii][Dd][Rr];
INET	:	[Ii][Nn][Ee][Tt];
MACADDR	:	[Mm][Aa][Cc][Aa][Dd][Dd][Rr];

//Bit String Types
BIT	    :	[Bb][Ii][Tt];
BIT_VARYING	:	[Bb][Ii][Tt]WS[Vv][Aa][Rr][Yy][Ii][Nn][Gg];
VARBIT	:	[Vv][Aa][Rr][Bb][Ii][Tt];

//Text Search Types
TSQUERY	:	[Tt][Ss][Qq][Uu][Ee][Rr][Yy];
TSVECTOR	:	[Tt][Ss][Vv][Ee][Cc][Tt][Oo][Rr];

//UUID Type
UUID	:	[Uu][Uu][Ii][Dd];

//XML Type
XML	:	[Xx][Mm][Ll];

//Transaction IDs and snapshots
TXID_SNAPSHOT	:	[Tt][Xx][Ii][Dd][_][Ss][Nn][Aa][Pp][Ss][Hh][Oo][Tt];

//Pseudo-Types
VOID        :   [Vv][Oo][Ii][Dd];

//any // TODO
//anyarray
//anyelement
//anyenum
//anynonarray
//cstring
//internal
//language_handler

RECORD      :   [Rr][Ee][Cc][Oo][Rr][Dd];

//trigger

//Other types
TABLE       :   [Tt][Aa][Bb][Ll][Ee];

//expressions

// NUM_INT MUST BE before NUM_REAL or we got crash
NUM_INT : ('0' .. '9') +;

NUM_REAL    : ('0' .. '9') + (('.' ('0' .. '9') + (EXPONENT)?)? | EXPONENT) ;
ESC_STRING_LITERAL    : [Ee] '\'' (~'\'')+ '\'';
BIT_STRING_LITERAL    : ([Bb] | [Xx]) '\'' (~'\'')+ '\'';
fragment EXPONENT   : ('e') ('+' | '-')? ('0' .. '9') + ;
PLUS    : '+' ;
MINUS   : '-' ;
STRING_LITERAL  : '\'' ('\'\'' | ~ ('\''))* '\'' ;

ANONYMOUS_PAR : '$' ('0' .. '9') +;

WS  : [ \t\r\n]+ -> skip ;

// boolean
fragment
TRUE_true   : [Tt][Rr][Uu][Ee];
fragment
BOOL_TRUE_frag   : TRUE_true | ('\'' TRUE_true '\'') | '\'t\'' | '\'y\'' | '\'yes\'' | '\'on\'' | '\'1\'' | '1';
fragment
FALSE_false : [Ff][Aa][Ll][Ss][Ee];
fragment
BOOL_FALSE_frag : FALSE_false | ('\'' FALSE_false '\'') | '\'f\'' | '\'n\'' | '\'no\'' |  '\'off\'' | '\'0\'' | '0';

BOOL_VAL_START  : WS (BOOL | BOOLEAN)  -> pushMode(INSIDE_BOOL_VAL) ;

mode INSIDE_BOOL_VAL;
BOOL_VAL_END    : SEMI   -> popMode ;
BOOL_ASSIGN     : ASSIGN ;
BOOL_VAL        : (BOOL_TRUE_frag | BOOL_FALSE_frag);
BOLL_WS         : [ \t\r\n]+ -> skip ;
