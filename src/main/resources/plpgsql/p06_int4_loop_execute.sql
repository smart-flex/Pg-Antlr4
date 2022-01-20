CREATE OR REPLACE FUNCTION p06_int4_loop_execute(int4)
    RETURNS int4 AS
$$
DECLARE
   m_amn int4 NOT NULL DEFAULT 100;
   rec RECORD;
BEGIN
   <<c1>>
   FOR rec IN EXECUTE 'SELECT $1 as ybd' USING $1+1 LOOP
      m_amn := m_amn + rec.ybd;
   END LOOP c1;
   RETURN m_amn;
END;
$$
LANGUAGE 'plpgsql';

