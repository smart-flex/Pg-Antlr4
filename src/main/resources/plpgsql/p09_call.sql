CREATE OR REPLACE FUNCTION p09_call(p_period integer)
   RETURNS SETOF t09_yyyymm AS
$$
DECLARE
   ret t09_yyyymm%ROWTYPE;
BEGIN
   ret := p09_call_sub(coalesce(p_period, 196802));

   RETURN NEXT ret;
END;
$$
  LANGUAGE 'plpgsql';

