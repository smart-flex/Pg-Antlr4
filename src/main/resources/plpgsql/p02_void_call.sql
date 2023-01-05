CREATE OR REPLACE FUNCTION p02_void_call()
    RETURNS void AS
$$
DECLARE
   m_par1 int4 := 10;
   m_par2 int2 := 30;
   m_res int4 := null;
BEGIN
   -- TODO fix some parsing error: m_res := p02_int4_v2(m_par1,m_par2);
   m_res := p02_int4_v2(m_par1, m_par2);
   
   RAISE NOTICE 'Result = %', m_res;

EXCEPTION
   WHEN others THEN RAISE NOTICE 'Catch exeption for p02_void_call';
END;
$$
LANGUAGE 'plpgsql';