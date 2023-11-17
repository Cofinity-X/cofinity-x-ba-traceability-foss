create table assets_as_built_alerts
(
    alert_id int8         not null,
    asset_id varchar(255) not null
);

create table asset_as_built_alert_notifications
(
    alert_notification_id varchar(255) not null,
    asset_id              varchar(255) not null
);

create table if not exists alert
(
    id             int8 generated by default as identity,
    bpn            varchar(255),
    close_reason   varchar(1000),
    created        timestamp,
    description    varchar(1000),
    status         int4,
    side           int4,
    accept_reason  varchar(1000),
    decline_reason varchar(1000),
    updated        timestamp,
    primary key (id)
);

create table alert_notification
(
    id                         varchar(255) not null,
    contract_agreement_id      varchar(255),
    edc_url                    varchar(255),
    notification_reference_id  varchar(255),
    receiver_bpn_number        varchar(255),
    sender_bpn_number          varchar(255),
    alert_id                   int8,
    target_date                timestamp,
    severity                   int4,
    sender_manufacturer_name   varchar(255),
    receiver_manufacturer_name varchar(255),
    edc_notification_id        varchar(255),
    status                     varchar(255),
    created                    timestamp with time zone,
    updated                    timestamp with time zone,
    message_id                 varchar(255),
    is_initial                 boolean,
    primary key (id)
);

alter table if exists assets_as_built_alerts
    add constraint fk_asset_entity foreign key (asset_id) references assets_as_built;

alter table if exists assets_as_built_alerts
    add constraint fk_alert foreign key (alert_id) references alert;

alter table if exists alert_notification
    add constraint fk_alert foreign key (alert_id) references alert;
