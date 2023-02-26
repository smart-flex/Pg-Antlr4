CREATE FUNCTION p01_void_perform ()
    RETURNS void AS
$$
DECLARE
   m_par1 int4 := 10;
   m_par2 int2 := 20;
   m_par3 int4 := 30;
BEGIN

   PERFORM p02_int4_v2(m_par1, 
                       m_par3) ; NULL;

   PERFORM p02_int4_v2(m_par1, 
                       m_par2) ; NULL;


END;
$$
LANGUAGE 'plpgsql';