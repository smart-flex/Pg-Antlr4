CREATE OR REPLACE FUNCTION p02_int4_v2(int4, int4)
RETURNS int4
LANGUAGE plpgsql AS $$
DECLARE
   m_res int4 := 0;
BEGIN
   RAISE NOTICE 'Called p02_int4_v2(int4, int4)';
   m_res := $1 + $2 + 100;
   RETURN m_res;
END; $$;
