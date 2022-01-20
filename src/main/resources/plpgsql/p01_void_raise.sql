CREATE FUNCTION p01_void_raise(text)
    RETURNS void AS
$$
BEGIN
   RAISE NOTICE 'Called p01_void_raise';
END;
$$
LANGUAGE 'plpgsql';

