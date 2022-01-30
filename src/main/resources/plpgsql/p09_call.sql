CREATE OR REPLACE FUNCTION p09_call(p_period integer)
   RETURNS SETOF t09_yyyymm AS
$$
DECLARE
   ret t09_yyyymm%ROWTYPE;
   m_a1 int4;
   m_a2 int4;
   m_a3 int4;
   m_a4 int4;
BEGIN
   
   ret := p09_call_sub(coalesce(p_period, 196802));

   PERFORM p09_call_sub(coalesce(p_period, 196803));

   SELECT * INTO ret FROM p09_call_sub(196804);

   SELECT p02_int4 INTO m_a1 FROM p02_int4();

   SELECT 1 INTO m_a1;

   SELECT ALL a1 INTO m_a1 FROM (SELECT 1 as a1,2 as a2) as s1;

   SELECT DISTINCT a1 INTO m_a1 FROM (SELECT 1 as a1,2 as a2) as s2;

   SELECT DISTINCT a1, a2 INTO m_a1, m_a2 FROM (SELECT 1 as a1,2 as a2) as s3;

   SELECT DISTINCT on (a1, a2) a1, a2, a3, a4 INTO m_a1, m_a2, m_a3, m_a4 FROM (SELECT 1 as a1,2 as a2, 3 as a3, 4 as a4) as s1;

   WITH d1 AS (
      SELECT 1 as a1
   )   
   SELECT a1, 2 INTO m_a1, m_a2
   FROM d1;

   RETURN NEXT ret;
END;
$$
  LANGUAGE 'plpgsql';

