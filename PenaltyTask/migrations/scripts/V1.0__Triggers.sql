--TODO Delete this section
DROP TABLE IF EXISTS temp_table;
DROP TABLE IF EXISTS temp_count_table;
DROP TABLE IF EXISTS temp_rang_table;

CREATE TEMP TABLE temp_table AS
SELECT carOwner.lastName, carOwner.firstName, carOwner.middleName, car.make, car.model,
       stateNumber.number, stateNumber.series, stateNumber.regionCode, stateNumber.country,
       penaltyEvent.eventDate, fine.id as fine_id, fine.type, fine.charge
FROM penaltyEvent
         JOIN car ON car.id = penaltyEvent.carID
         JOIN stateNumber ON stateNumber.id = car.stateNumberID
         JOIN carOwner ON carOwner.id = car.carOwnerID
         JOIN fine ON fine.id = penaltyEvent.fineID
ORDER BY carOwner.lastName;

CREATE TEMP TABLE temp_count_table AS
SELECT temp_table.fine_id, COUNT(*) cnt
FROM temp_table
GROUP BY fine_id
ORDER BY cnt DESC;

CREATE TEMP TABLE temp_rang_table AS
select * from
    (select *, dense_rank() over (order by cnt desc) as fine_rank
     from temp_count_table) subquery;

CREATE TABLE statistics AS
SELECT fine.id as fine_id, fine.type as fine_type,
       fine.charge as fine_charge, temp_rang_table.fine_rank as fine_top_place, temp_rang_table.cnt as fine_occurrences
FROM fine, temp_rang_table
WHERE fine.id = temp_rang_table.fine_id
ORDER BY temp_rang_table.fine_rank ASC;

--EndOfSection!!!

CREATE OR REPLACE FUNCTION calculate_statistics() RETURNS TRIGGER AS $$
    --DECLARE
    --fine_occurrences bigint;

BEGIN
    IF  TG_OP = 'INSERT' THEN
        UPDATE statistics
        SET fine_occurrences = fine_occurrences + 1
        WHERE fine_id = NEW.fineID;

        DROP TABLE IF EXISTS temp_rang_table;
        CREATE TEMP TABLE temp_rang_table AS
        SELECT fine_occurrences as cnt, dense_rank() over (order by fine_occurrences desc) as fine_rank
        FROM statistics;

        UPDATE statistics
        SET fine_top_place = temp_rang_table.fine_rank
        FROM temp_rang_table
        WHERE fine_occurrences = temp_rang_table.cnt;

        RETURN NEW;

    ELSIF TG_OP = 'UPDATE' THEN

        IF NEW.fineID != OLD.fineID THEN
            UPDATE statistics
            SET fine_occurrences = fine_occurrences + 1
            WHERE fine_id = NEW.fineID;

            UPDATE statistics
            SET fine_occurrences = fine_occurrences - 1
            WHERE fine_id = OLD.fineID;
        END IF;

        DROP TABLE IF EXISTS temp_rang_table;
        CREATE TEMP TABLE temp_rang_table AS
        SELECT fine_occurrences as cnt, dense_rank() over (order by fine_occurrences desc) as fine_rank
        FROM statistics;

        UPDATE statistics
        SET fine_top_place = temp_rang_table.fine_rank
        FROM temp_rang_table
        WHERE fine_occurrences = temp_rang_table.cnt;

        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        UPDATE statistics
        SET fine_occurrences = fine_occurrences - 1
        WHERE fine_id = OLD.fineID;

        DROP TABLE IF EXISTS temp_rang_table;
        CREATE TEMP TABLE temp_rang_table AS
        SELECT fine_occurrences as cnt, dense_rank() over (order by fine_occurrences desc) as fine_rank
        FROM statistics;

        UPDATE statistics
        SET fine_top_place = temp_rang_table.fine_rank
        FROM temp_rang_table
        WHERE fine_occurrences = temp_rang_table.cnt;

        RETURN OLD;
    END IF;
END;

$$ LANGUAGE plpgsql;

CREATE TRIGGER t_statistics
    AFTER INSERT OR UPDATE OR DELETE ON penaltyEvent FOR EACH ROW EXECUTE PROCEDURE calculate_statistics();

--------------------------------------------------------------------------------------------------


CREATE OR REPLACE FUNCTION add_delete_fine_to_statistics() RETURNS TRIGGER AS $$
DECLARE
    new_top_place bigint;

BEGIN
    IF  TG_OP = 'INSERT' THEN
        new_top_place = (SELECT max(fine_top_place) FROM statistics);

        IF (SELECT count(fine_occurrences) FROM statistics WHERE fine_occurrences = 0) = 0 THEN
            new_top_place = new_top_place + 1;
        END IF;

        INSERT INTO statistics (fine_top_place, fine_occurrences, fine_id)
        VALUES (new_top_place, 0, NEW.id);

        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        DELETE FROM statistics
        WHERE fine_id = OLD.id;

        DROP TABLE IF EXISTS temp_rang_table;
        CREATE TEMP TABLE temp_rang_table AS
        SELECT fine_occurrences as cnt, dense_rank() over (order by fine_occurrences desc) as fine_rank
        FROM statistics;

        UPDATE statistics
        SET fine_top_place = temp_rang_table.fine_rank
        FROM temp_rang_table
        WHERE fine_occurrences = temp_rang_table.cnt;

        RETURN OLD;
    END IF;
END;

$$ LANGUAGE plpgsql;

CREATE TRIGGER t_fine
    AFTER INSERT OR DELETE ON fine FOR EACH ROW EXECUTE PROCEDURE add_delete_fine_to_statistics();
