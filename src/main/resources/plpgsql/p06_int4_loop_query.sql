CREATE OR REPLACE FUNCTION p06_int4_loop_query(int4)
    RETURNS int4 AS
$$
DECLARE
   m_amn int4 NOT NULL DEFAULT 100;
   rec RECORD;
BEGIN
   <<c1>>
   FOR rec IN SELECT $1 as ybd LOOP
      m_amn := m_amn + rec.ybd;
   END LOOP c1;
   RETURN m_amn;
END;
$$
LANGUAGE 'plpgsql';

