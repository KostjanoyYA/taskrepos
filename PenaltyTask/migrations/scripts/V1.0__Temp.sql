--TODO Не использовать этот скрипт!!! Delete this file in the future
ALTER TABLE statistics
    ADD id bigserial not null;

alter table statistics
    add constraint statistics_pk primary key (id);

--

ALTER TABLE pdbs.penaltyevent
    RENAME TO penalty_event;

ALTER TABLE pdbs.penalty_event
    RENAME eventdate TO event_date;

ALTER TABLE pdbs.penalty_event
    RENAME fineid TO fine_id;

ALTER TABLE pdbs.penalty_event
    RENAME carid TO car_id;

ALTER TABLE pdbs.carowner
    RENAME TO car_owner;

ALTER TABLE pdbs.car_owner
    RENAME firstname TO first_name;

ALTER TABLE pdbs.car_owner
    RENAME middlename TO middle_name;

ALTER TABLE pdbs.car_owner
    RENAME lastname TO last_name;

ALTER TABLE pdbs.car
    RENAME statenumberid TO state_number_id;

ALTER TABLE pdbs.car
    RENAME carownerid TO car_owner_id;

ALTER TABLE pdbs.statenumber
    RENAME TO state_number;

ALTER TABLE pdbs.state_number
    RENAME regioncode TO region_code;