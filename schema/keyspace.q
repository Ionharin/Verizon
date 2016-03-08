CREATE KEYSPACE verizon WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'}  AND durable_writes = true;

CREATE TABLE verizon.subscriber (
    account_id text,
    subscriber_id text,
    phone_number text,
    PRIMARY KEY (account_id, subscriber_id)
) WITH CLUSTERING ORDER BY (subscriber_id ASC);

CREATE TABLE verizon.account (
    account_id text PRIMARY KEY,
    city text,
    email text,
    first_name text,
    last_name text,
    name_prefix text,
    name_suffix text,
    state text,
    street_address text,
    subscribers set<text>,
    zip text
);

CREATE TABLE verizon.data_usage (
    subscriber_id text,
    date timestamp,
    bytes_dn double,
    bytes_total double,
    bytes_up double,
    day int,
    hour int,
    month int,
    url text,
    year int,
    PRIMARY KEY (subscriber_id, date)
) WITH CLUSTERING ORDER BY (date ASC);

CREATE TABLE verizon.account_by_phone (
    phone_number text,
    account_id text,
    first_name text,
    last_name text,
    PRIMARY KEY (phone_number, account_id)
) WITH CLUSTERING ORDER BY (account_id ASC);

CREATE TABLE verizon.account_by_email (
    email text,
    account_id text,
    first_name text,
    last_name text,
    PRIMARY KEY (email, account_id)
) WITH CLUSTERING ORDER BY (account_id ASC);