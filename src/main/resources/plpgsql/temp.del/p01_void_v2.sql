CREATE OR REPLACE FUNCTION p01_void_v2(int2)
    RETURNS void AS
$BODY$
DECLARE
   m_res int4;
BEGIN
   m_res := $1;
   RAISE NOTICE 'Result p01_void_v2 = %', m_res;
END;
$BODY$
LANGUAGE plpgsql;
