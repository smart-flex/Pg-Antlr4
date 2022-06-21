CREATE OR REPLACE FUNCTION p02_int4_v2(int4, int2)
RETURNS int4
LANGUAGE plpgsql AS $$
BEGIN
   RAISE NOTICE 'Called p02_int4_v2(int4, int2)';
   RETURN $1 + $2;
END; $$;
