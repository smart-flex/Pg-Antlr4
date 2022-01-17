CREATE OR REPLACE FUNCTION p06_int4_case (p_p1 int4)
    RETURNS int4 AS
$$
BEGIN
   CASE p_p1
   WHEN 1 THEN
      RETURN p_p1;
   WHEN 2,3 THEN
      RETURN p_p1;
   ELSE
      RAISE notice 'Other value';
      RETURN p_p1;
   END CASE;
END;
$$
LANGUAGE 'plpgsql';

