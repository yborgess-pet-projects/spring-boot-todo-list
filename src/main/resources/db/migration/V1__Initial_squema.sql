create sequence todo_seq increment by 1;

create table TM_TODOS
(
    id            bigint                      not null primary key,
    title         varchar(255)                not null,
    completed     boolean                     not null,
    modified_date timestamp(6) with time zone DEFAULT now(),
    created_date  timestamp(6) with time zone not null DEFAULT now()
);

CREATE OR REPLACE FUNCTION todo_pk_function()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.id := nextval('todo_seq');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER todo_insert_trigger
    BEFORE INSERT ON TM_TODOS
    FOR EACH ROW
    WHEN (new.id is null)
EXECUTE FUNCTION todo_pk_function();


CREATE OR REPLACE FUNCTION todo_modified_date_function()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.modified_date := now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER todo_update_trigger
    BEFORE UPDATE ON TM_TODOS
    FOR EACH ROW
EXECUTE FUNCTION todo_modified_date_function();
