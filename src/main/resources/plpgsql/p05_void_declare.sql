CREATE OR REPLACE FUNCTION p05_void_declare ()
    RETURNS void AS
$$
DECLARE
-- string, int, cursor
   m_char1 CONSTANT char(10) := 'Hello';
   m_curs1 refcursor;
   m_vchar2 varchar(10) DEFAULT 'Varchar';
   m_vchar3 varchar(20) NOT NULL DEFAULT 'Varchar not null';
   m_int1 int4;
   m_curs2 CURSOR FOR SELECT * FROM tenk1;
   m_curs3 scroll CURSOR FOR SELECT * FROM tenk1;
   m_curs4 no scroll CURSOR FOR SELECT * FROM tenk1;
   m_curs5 CURSOR (key integer) IS SELECT * FROM tenk1 WHERE unique1 = key;
   m_int2 int8 := 0;
   m_int3 int := +1; m_int4 int := -10; m_int5 int4 := 20;

-- boolean definitions
   m_bool1 bool := TRUE; m_bool2 bool := 1; m_bool3 boolean := true; m_bool4 bool := 't';
   m_bool5 boolean := 'true'; m_bool6 boolean := 'on'; m_bool7 boolean := '1';
   m_bool8 bool := 'yes';
   m_bool9 boolean := 'off'; m_bool10 boolean := false;

-- float, double, real
   m_float1 float8 := '+Infinity'; m_float2 float4 := 1.17e-2; m_float3 float4 := -1.17e+2; m_float4 float4 := +1.17e+2;

-- money, decimal, numeric
   m_money1 money; m_money2 money := -12.45; m_money3 money := +12.45;
   m_num1 numeric(10,2);

-- binary, bytea
    m_bytea1 bytea := E'\\047'; m_bytea2 bytea := E'\\176\047'; m_bytea3 bytea := E'\\176047';
--    m_bytea4 bytea := 'abc \153\154\155 \052\251\124';

-- bit
    m_bit1 bit := B'1'; m_bit2 bit(4) := b'1001'; m_bit3 bit(12) :=  X'1FF';

-- date, timestamp, interval
    m_date1 date := '1968-02-12'; m_date2 date := now(); m_date3 date := CURRENT_DATE;

    m_time1 time := '04:05:06.789'; m_time2 time with time zone := '04:05:06 PST'; m_time3 time without time zone  := '04:05:06';
    m_time4 time := CURRENT_TIME;

    m_timestamp1 timestamp := CURRENT_TIMESTAMP; m_timestamp2 timestamp := '2004-10-19 10:23:54 + 02';
    m_timestamp3 timestamp with time zone := '2004-10-19 10:23:54 - 5'; m_timestamp4 timestamp with time zone := '2004-10-19 10:23:54 MSK';
    m_timestamp5 timestamp without time zone := CURRENT_TIMESTAMP;

    m_interval interval := '1 day';

-- internet
    m_inet inet := '192.168.56.1';
    m_cidr cidr := '192.168.100.128/25';
    m_macaddr macaddr := '08:00:2b:01:02:03';

-- geom
    m_point point := '(0,0)'; m_line line := '( ( -1,0 ) , ( 1,0 ) )';
    m_lseg lseg := '((2,0),(0,2))'; m_box box := '((0,0),(1,1))';
    m_path path := '((1,0),(0,1),(-1,0))'; m_polygon polygon := '((0,0),(1,1))';
    m_circle circle := '((0,0),2)';

BEGIN

END;
$$
LANGUAGE 'plpgsql';

