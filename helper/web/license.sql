CREATE TABLE License (
    clientName  varchar(255) not null,
    cpuId varchar(255) not null,
    days int not null,
    token varchar(255) not null,
    start_date DATE not null,
    end_date DATE,
    PRIMARY KEY (cpuId)
);
