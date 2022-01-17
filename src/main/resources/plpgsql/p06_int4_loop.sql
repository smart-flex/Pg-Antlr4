CREATE OR REPLACE FUNCTION p06_int4_loop (p_p1 int4)
    RETURNS int4 AS
$$
DECLARE
   m_amn int4 NOT NULL DEFAULT 0;
BEGIN
   <<c1>>
   LOOP
      m_amn := m_amn * 2 + 1;
      EXIT c1 WHEN m_amn > p_p1;
   END LOOP c1;
   RETURN m_amn;
END;
$$
LANGUAGE 'plpgsql';

