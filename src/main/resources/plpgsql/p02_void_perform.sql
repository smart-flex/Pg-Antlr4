CREATE OR REPLACE FUNCTION p02_void_perform ()
    RETURNS void AS
$$
DECLARE
   m_par1 int4 := 10;
   m_par2 int2 := 30;
   m_par3 int4 := 30;
BEGIN

   NULL;PERFORM p02_int4_v2(m_par1, 
                       m_par2);NULL;
   NULL;PERFORM p02_int4_v2(m_par1, 
                       m_par3);NULL;
   NULL;PERFORM p02_int4_inout(m_par1, 
                       m_par3);NULL;
   NULL;PERFORM  p01_void();NULL;
   NULL;PERFORM p02_int4_v3(20);NULL;
   RAISE NOTICE 'Called p02_void_perform';

END;
$$
LANGUAGE 'plpgsql';