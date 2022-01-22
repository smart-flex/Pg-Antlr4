CREATE FUNCTION p08_cursor()
    RETURNS refcursor AS
$$
DECLARE
    ref refcursor;
BEGIN
   OPEN ref FOR SELECT 1;
   RETURN ref;
END;
$$
LANGUAGE 'plpgsql';
