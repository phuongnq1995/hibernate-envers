CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE TABLE IF NOT EXISTS addresses
(
    id          uuid         NOT NULL,
    name        varchar(255) NOT NULL,
    date_created timestamp(6),
    last_updated timestamp(6),
    CONSTRAINT pk_addresses PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS languages
(
    id          uuid         NOT NULL,
    name        varchar(255) NOT NULL,
    date_created timestamp(6),
    last_updated timestamp(6),
    CONSTRAINT pk_languages PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS stores
(
    id          uuid         NOT NULL,
    name        varchar(255) NOT NULL,
    master_address_id uuid,
    delivery_address_id uuid,
    date_created timestamp(6),
    last_updated timestamp(6),
    CONSTRAINT pk_stores PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS campaigns
(
    id          uuid         NOT NULL,
    name        varchar(255) NOT NULL,
    store_id uuid,
    date_created timestamp(6),
    last_updated timestamp(6),
    CONSTRAINT pk_campaigns PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS stores_languages
(
    store_id          uuid         NOT NULL,
    language_id       uuid         NOT NULL,
    CONSTRAINT pk_stores_languages PRIMARY KEY (store_id, language_id)
);