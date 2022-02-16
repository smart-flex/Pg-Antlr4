CREATE FUNCTION p01_void_perform ()
    RETURNS void AS
$$
BEGIN

   NULL;
   PERFORM p01_void();

END;
$$
LANGUAGE 'plpgsql';