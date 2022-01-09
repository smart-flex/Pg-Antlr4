CREATE FUNCTION p02_void ()
    RETURNS void AS
$$
BEGIN

   NULL;
   PERFORM p01_void();

END;
$$
LANGUAGE 'plpgsql';