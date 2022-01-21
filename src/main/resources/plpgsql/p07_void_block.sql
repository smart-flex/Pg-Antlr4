CREATE OR REPLACE FUNCTION p07_void_block ()
    RETURNS void AS
$$
DECLARE
   m_int int4 := 1;
BEGIN

   BEGIN
      NULL;
   END;

   DECLARE
      m_int int4 := 1;
   BEGIN
      m_int := m_int + 1;
   END;

   BEGIN
      m_int := 1 / 0;
   EXCEPTION 
      WHEN division_by_zero THEN
         RAISE notice 'There is division by zero';
      WHEN others THEN
         RAISE notice 'It is never reachable';
   END;

END;
$$
LANGUAGE 'plpgsql';

