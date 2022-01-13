CREATE OR REPLACE FUNCTION p04_int8_null_input(int8)
RETURNS int8
LANGUAGE plpgsql 
RETURNS NULL ON NULL INPUT 
AS $$
BEGIN
    IF $1 IS NULL THEN
       RAISE 'Achtung: NULL argument';
    END IF;
    RETURN $1;
END$$;
