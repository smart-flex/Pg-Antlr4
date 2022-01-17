CREATE OR REPLACE FUNCTION p06_int4_case_v2 (p_p1 int4)
    RETURNS int4 AS
$$
BEGIN
   CASE
   WHEN p_p1 = 1 THEN
      RETURN p_p1;
   WHEN p_p1 IN (2,4) THEN
      RETURN p_p1;
   WHEN p_p1 BETWEEN 5 AND 7 THEN
      RETURN p_p1;
   ELSE
      RAISE notice 'Other value';
      RETURN p_p1;
   END CASE;
END;
$$
LANGUAGE 'plpgsql';

