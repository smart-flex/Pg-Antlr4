CREATE OR REPLACE FUNCTION p02_void_perform ()
    RETURNS void AS
$$
DECLARE
   m_par1 int4 := 10;
   m_par2 int4 := 30;
BEGIN

   NULL;PERFORM p02_int4_v2(m_par1, 
                       m_par2);NULL;
   RAISE NOTICE 'Called p02_void_perform';

END;
$$
LANGUAGE 'plpgsql';