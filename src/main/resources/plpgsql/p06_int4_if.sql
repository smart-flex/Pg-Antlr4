CREATE OR REPLACE FUNCTION p06_int4_if (p_p1 int4)
    RETURNS int4 AS
$$
BEGIN
   IF p_p1 = 1 THEN
      RETURN p_p1;
   ELSIF p_p1 = 2 THEN
      RETURN p_p1;
   ELSE
      RAISE notice 'Other value';
      RETURN p_p1;
   END IF;
END;
$$
LANGUAGE 'plpgsql';

