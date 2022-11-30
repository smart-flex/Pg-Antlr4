CREATE OR REPLACE FUNCTION p02_int4_v3(IN p1 int4)
RETURNS int4
LANGUAGE plpgsql AS $$
DECLARE
   m_par2 int2 := 17;
BEGIN
   RAISE NOTICE 'Called p02_int4_v3(int4)';
   RAISE NOTICE 'm_par2 = %', m_par2;
   IF p1 = 10 THEN
      RETURN p1 + m_par2;
   END IF;

  RAISE NOTICE 'original parameter p1 = %', p1;

   DECLARE
      p1 int2 := 155;
   BEGIN
      RAISE NOTICE 'overrided parameter p1 = %', p1;
   END;

   DECLARE
      m_par2 int2 := 177;
   BEGIN
      IF p1 = 20 THEN
         RETURN p1 + m_par2;
      END IF;
   END;

   RETURN 222;
END; $$;
