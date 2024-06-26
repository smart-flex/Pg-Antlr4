CREATE OR REPLACE FUNCTION p04_int8(int8)
RETURNS int8
CALLED ON NULL INPUT
LANGUAGE plpgsql AS $$
BEGIN
    IF $1 IS NULL THEN
       RAISE 'Achtung: NULL argument';
    END IF;
    RETURN $1;
END$$;
