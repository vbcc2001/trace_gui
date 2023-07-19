CREATE TABLE License_2023 (
    hard_fingerprint varchar(255) not null,
    clientName  varchar(255) not null,
    token varchar(255) not null,
    start_date DATE not null,
    end_date DATE,
    authorize_type varchar(255),
    project_name varchar(255) not null,
    PRIMARY KEY (hard_fingerprint)
);
