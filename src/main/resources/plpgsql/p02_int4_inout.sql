CREATE OR REPLACE FUNCTION p02_int4_inout(IN p1 int4, INOUT p2 int4)
RETURNS int4
LANGUAGE plpgsql AS $$
BEGIN
   RAISE NOTICE 'Called p02_int4_inout(IN p1 int4, INOUT p2 int4)';
   p2 := p1 + p2 + 200;
   RETURN;
END; $$;
