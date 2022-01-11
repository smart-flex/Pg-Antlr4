parser grammar PgSQLParserDataType;

pgTypeEnum
    : SMALLINT
    | INT2
    | INTEGER
    | INT
    | INT4
    | BIGINT
    | INT8
    | DECIMAL
    | NUMERIC
    | REAL
    | DOUBLE_PRECISION
    | SERIAL
    | SERIAL4
    | BIGSERIAL
    | SERIAL8
    | FLOAT8
    | FLOAT4

    | MONEY

    | CHARACTER_VARYING
    | VARCHAR
    | CHARACTER
    | CHAR
    | TEXT

    | BYTEA

    | DATE
    | TIME
    | TIME_WITHOUT_TIME_ZONE
    | TIME_WITH_TIME_ZONE
    | TIMETZ
    | TIMESTAMP
    | TIMESTAMP_WITHOUT_TIME_ZONE
    | TIMESTAMP_WITH_TIME_ZONE
    | TIMESTAMPTZ
    | INTERVAL

    | BOOLEAN
    | BOOL

    | CIRCLE
    | POINT
    | POLYGON
    | LINE
    | LSEG
    | BOX
    | PATH

    | CIDR
    | INET
    | MACADDR

    | BIT
    | BIT_VARYING
    | VARBIT

    | TSQUERY
    | TSVECTOR

    | UUID
    |
    | XML

    | TXID_SNAPSHOT

    | VOID;
