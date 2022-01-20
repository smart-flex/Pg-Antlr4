CREATE FUNCTION p02_void_execute()
    RETURNS void AS
$$
BEGIN

   EXECUTE p01_void_raise('test');

END;
$$
LANGUAGE 'plpgsql';