CREATE FUNCTION p01_void_perform_v1 ()
    RETURNS void AS
$$
BEGIN

   PERFORM p01_void();
   NULL;

END;
$$
LANGUAGE 'plpgsql';