--TODO схему нужно создавать из-под пользователя pdbs
-- psql -U pdbs penaltydb;

create schema if not exists pdbs;
SET search_path TO pdbs;
create table car_owner
(
    id          bigserial    not null,
    first_name  varchar(256) not null,
    middle_name varchar(256) not null,
    last_name   varchar(256) not null
);

alter table car_owner
    add constraint carOwner_pk primary key (id);

create table state_number
(
    id          bigserial                not null,
    country     varchar(3) DEFAULT 'RUS' not null,
    region_code int                      not null,
    series      varchar(3)               not null,
    number      int                      not null
);

alter table state_number
    add constraint stateNumber_pk primary key (id);

create table car
(
    id              bigserial    not null,
    make            varchar(256) not null,
    model           varchar(256) not null,
    state_number_id bigint       not null,
    car_owner_id    bigint       not null
);

alter table car
    add constraint car_pk primary key (id);

alter table car
    add constraint car_stateNumber_id_fk foreign key (state_Number_id) references state_number (id);

alter table car
    add constraint car_carOwner_id_fk foreign key (car_owner_id) references car_owner (id);


create table fine
(
    id     bigserial not null,
    type   varchar   not null,
    charge numeric(9, 2)
);

alter table fine
    add constraint fine_pk primary key (id);

create table penalty_event
(
    id         bigserial not null,
    event_date timestamp not null,
    fine_id    bigint    not null,
    car_id     bigint    not null
);

alter table penalty_event
    add constraint penaltyEvent_pk primary key (id);

alter table penalty_event
    add constraint penaltyEvent_fine_id_fk foreign key (fine_id) references fine (id);

alter table penalty_event
    add constraint penaltyEvent_car_id_fk foreign key (car_id) references car (id);

create table statistics
(
    id               bigserial not null,
    fine_top_place   bigint    not null,
    fine_occurrences bigint    not null default 0,
    fine_id          bigint    not null
);

alter table statistics
    add constraint statistics_pk primary key (id);

alter table statistics
    add constraint statistics_fine_id_fk foreign key (fine_id) references fine (id);

--Triggers----------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION calculate_statistics() RETURNS TRIGGER AS
$$
    --DECLARE
    --fine_occurrences bigint;

BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE statistics
        SET fine_occurrences = fine_occurrences + 1
        WHERE fine_id = NEW.fine_id;

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

        IF NEW.fine_id != OLD.fine_id THEN
            UPDATE statistics
            SET fine_occurrences = fine_occurrences + 1
            WHERE fine_id = NEW.fine_id;

            UPDATE statistics
            SET fine_occurrences = fine_occurrences - 1
            WHERE fine_id = OLD.fine_id;
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
        WHERE fine_id = OLD.fine_id;

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
    AFTER INSERT OR UPDATE OR DELETE
    ON penalty_event
    FOR EACH ROW
EXECUTE PROCEDURE calculate_statistics();

--------------------------------------------------------------------------------------------------


CREATE OR REPLACE FUNCTION add_delete_fine_to_statistics() RETURNS TRIGGER AS
$$
DECLARE
    new_top_place bigint;

BEGIN
    IF TG_OP = 'INSERT' THEN
        new_top_place = (SELECT max(fine_top_place) FROM statistics);

        IF (SELECT count(fine_occurrences) FROM statistics WHERE fine_occurrences = 0) = 0 THEN
            new_top_place = new_top_place + 1;
        END IF;

        INSERT INTO statistics (fine_top_place, fine_occurrences, fine_id)
        VALUES (new_top_place, 0, NEW.id);

        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        DELETE
        FROM statistics
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
    AFTER INSERT OR DELETE
    ON fine
    FOR EACH ROW
EXECUTE PROCEDURE add_delete_fine_to_statistics();

--Table fulfilment---------------------------------------------------

INSERT INTO fine (type, charge)
VALUES ('Driving under the influence of alcohol', 30000);

INSERT INTO fine (type, charge)
VALUES ('Driving without seat belt on', 1000);

INSERT INTO fine (type, charge)
VALUES ('Speeding', 500);

INSERT INTO fine (type, charge)
VALUES ('Violation of child transportation requirements', 5000);

INSERT INTO fine (type, charge)
VALUES ('Stop line crossing', 2000);

INSERT INTO fine (type, charge)
VALUES ('Double solid crossing', 2500);

INSERT INTO fine (type, charge)
VALUES ('Resistance to a traffic police officer', 150000);

--------------------------------------------------------------------

INSERT INTO car_owner (first_name, middle_name, last_name)
VALUES ('JORDAN', 'ROSS', 'BELFORT');

INSERT INTO car_owner (first_name, middle_name, last_name)
VALUES ('IPPOLIT', 'GEORGIEVICH', 'YAKOVLEV');

INSERT INTO car_owner (first_name, middle_name, last_name)
VALUES ('FLASH', 'BLITZ', 'SLOTHMORE');

INSERT INTO car_owner (first_name, middle_name, last_name)
VALUES ('BEN', 'FANTASTIC', 'CASH');

INSERT INTO car_owner (first_name, middle_name, last_name)
VALUES ('NIKOLAY', 'EVGRAFOVICH', 'PETROV');

--------------------------------------------------------------------

INSERT INTO state_number (country, region_code, series, number)
VALUES ('RUS', 75, 'MMM', 402);

INSERT INTO state_number (country, region_code, series, number)
VALUES ('RUS', 70, 'OPC', 503);

INSERT INTO state_number (country, region_code, series, number)
VALUES ('RUS', 39, 'PPK', 604);

INSERT INTO state_number (country, region_code, series, number)
VALUES ('RUS', 17, 'CKC', 705);

INSERT INTO state_number (country, region_code, series, number)
VALUES ('RUS', 117, 'XXX', 806);

INSERT INTO state_number (country, region_code, series, number)
VALUES ('RUS', 5, 'OPC', 907);

--------------------------------------------------------------------

INSERT INTO car (make, model, state_number_id, car_owner_id)
VALUES ('LAMBORGHINI', 'COUNTACH', 1, 1);

INSERT INTO car (make, model, state_number_id, car_owner_id)
VALUES ('VAZ', '2101', 2, 2);

INSERT INTO car (make, model, state_number_id, car_owner_id)
VALUES ('FERRARI', '550 MARANELLO', 3, 3);

INSERT INTO car (make, model, state_number_id, car_owner_id)
VALUES ('THOMAS BUILT', 'SAF-T-LINER', 4, 4);

INSERT INTO car (make, model, state_number_id, car_owner_id)
VALUES ('KIA', 'RIO', 5, 5);

INSERT INTO car (make, model, state_number_id, car_owner_id)
VALUES ('LADA', 'PRIORA', 6, 5);

--------------------------------------------------------------------

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 00:00:00', 1, 1);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-07 00:00:00', 6, 2);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2019-12-31 20:20:20', 7, 1);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2019-12-31 23:23:23', 2, 1);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 00:00:15', 3, 1);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 02:11:00', 2, 2);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 11:14:00', 3, 3);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 20:20:20', 4, 4);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 07:00:00', 3, 5);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-15 10:10:10', 5, 4);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-16 08:00:00', 5, 5);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 08:28:00', 4, 5);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 08:29:00', 4, 5);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 08:30:00', 4, 5);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 09:00:00', 4, 5);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 13:12:00', 1, 5);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-02-07 15:00:00', 2, 5);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-10 17:32:15', 3, 6);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-01 09:10:00', 3, 5);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-02 13:12:00', 1, 1);

INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-03 15:15:00', 2, 2);
--
INSERT INTO penalty_event (event_date, fine_id, car_id)
VALUES ('2020-01-15 12:10:10', 6, 4);

--
--INSERT INTO penalty_event (event_date, fine_id, car_id)
--VALUES ('2020-02-16 00:05:25', 3, 1);

-- UPDATE penalty_event
-- SET fine_id = 4
-- WHERE event_date = '2020-02-16 00:05:25';

-- DELETE FROM penalty_event
-- WHERE event_date='2020-02-16 00:05:25';

-- INSERT INTO fine (type, charge)
-- VALUES ('Tinted car windows', 999);

-- DELETE FROM fine
-- WHERE type = 'Tinted car windows';

-- INSERT INTO fine (type, charge)
-- VALUES ('Unreadable state number', 777);

-- DELETE FROM fine
-- WHERE type = 'Unreadable state number';