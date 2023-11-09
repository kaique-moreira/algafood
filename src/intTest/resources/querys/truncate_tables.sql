DO '
    DECLARE
    r RECORD;
    BEGIN
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP
       IF (NOT r.tablename = ''flyway_schema_history'') THEN
          EXECUTE ''TRUNCATE TABLE '' || quote_ident(r.tablename) || '' CASCADE'';
   END IF;
    END LOOP;
END;'
