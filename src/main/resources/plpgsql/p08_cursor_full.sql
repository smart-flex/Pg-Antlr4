CREATE OR REPLACE FUNCTION p08_cursor_full(int4)
    RETURNS void AS
$$
DECLARE
   curs1 refcursor;
   curs2 refcursor;
   curs3 CURSOR (key integer) IS SELECT 1 WHERE key != key;
   curs4 refcursor;
   rec4 record;
   m_text text;
   curs5 CURSOR (key1 integer) IS SELECT key1 as k1 WHERE key1 = key1;
   rec5 record;
BEGIN
   OPEN curs1 NO SCROLL FOR SELECT $1 WHERE $1 = $1;
   CLOSE curs1;
   RAISE NOTICE 'Cursor 1 is done';

   OPEN curs2 NO SCROLL FOR EXECUTE 'SELECT 1';
   CLOSE curs2;
   RAISE NOTICE 'Cursor 2 is done';

   OPEN curs3(10);
   CLOSE curs3;
   RAISE NOTICE 'Cursor 3 is done';

   OPEN curs4 SCROLL FOR SELECT 'Cursor 4 is done; Param = '||TO_CHAR($1,'FM000000') as t1 WHERE $1 = $1;
   FETCH curs4 INTO rec4;
   RAISE NOTICE '%', rec4.t1;
   FETCH LAST FROM curs4 INTO rec4;
   RAISE NOTICE '%', rec4.t1;
   FETCH FIRST IN curs4 INTO rec4;
   RAISE NOTICE '%', rec4.t1;
   FETCH 0 IN curs4 INTO rec4;
   RAISE NOTICE '%', rec4.t1;
   FETCH BACKWARD IN curs4 INTO rec4;
   RAISE NOTICE '%', rec4.t1;
   FETCH ABSOLUTE 1 IN curs4 INTO rec4;
   RAISE NOTICE '%', rec4.t1;
   FETCH RELATIVE 0 FROM curs4 INTO rec4;
   RAISE NOTICE '%', rec4.t1;

   MOVE curs4;
   RAISE NOTICE 'MOVE curs4';
   MOVE LAST FROM curs4;
   RAISE NOTICE 'MOVE LAST FROM curs4';
   
   CLOSE curs4;

   <<c1>> 
   FOR rec5 IN curs5($1) LOOP
      RAISE NOTICE '%', rec5.k1;
   END LOOP c1;
END;
$$
LANGUAGE 'plpgsql';
