CREATE FUNCTION p03_table()
RETURNS TABLE(quantity int, total numeric) AS $$
BEGIN
    RETURN QUERY SELECT 1968, 3400000::numeric;
END;
$$ LANGUAGE plpgsql;
