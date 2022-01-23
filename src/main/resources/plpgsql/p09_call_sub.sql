CREATE OR REPLACE FUNCTION p09_call_sub(p_period integer)
   RETURNS SETOF t09_yyyymm AS
$$
DECLARE
   ret t09_yyyymm%ROWTYPE;
BEGIN
   ret.t_year := p_period/100;
   ret.t_mm   := p_period%100;

   RETURN NEXT ret;
END;
$$
  LANGUAGE 'plpgsql';

