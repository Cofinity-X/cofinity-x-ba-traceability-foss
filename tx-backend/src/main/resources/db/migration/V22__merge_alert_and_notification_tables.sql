-- DROP TABLE AND VIEW;
DROP VIEW IF EXISTS assets_as_built_view;

CREATE TABLE public.temp_assets_as_built_notifications (
	notification_id varchar(255) NOT NULL,
	asset_id varchar(255) NOT NULL
);

INSERT INTO temp_assets_as_built_notifications
(notification_id                                          , asset_id)
select notification_id , asset_id
from assets_as_built_notifications;

DROP TABLE public.assets_as_built_notifications;

-- CREATE TABLE notification;
CREATE TABLE public.notification
(
    id             int8          NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE),
    title          varchar(255)  NULL,
    bpn            varchar(255)  NULL,
    close_reason   varchar(1000) NULL,
    created        timestamp     NULL,
    description    varchar(1000) NULL,
    status         varchar(50)   NULL,
    updated        timestamp     NULL,
    side           varchar(50)   NULL,
    accept_reason  varchar(1000) NULL,
    decline_reason varchar(1000) NULL,
    type           varchar(50)   NULL,
    CONSTRAINT notification_pk PRIMARY KEY (id)
);

CREATE TABLE public.notification_message
(
    id                        varchar(255) NOT NULL,
    contract_agreement_id     varchar(255) NULL,
    edc_url                   varchar(255) NULL,
    notification_reference_id varchar(255) NULL,
    send_to                   varchar(255) NULL,
    created_by                varchar(255) NULL,
    notification_id           int8         NULL,
    target_date               timestamp    NULL,
    severity                  varchar(255) NULL,
    created_by_name           varchar(255) NULL,
    send_to_name              varchar(255) NULL,
    edc_notification_id       varchar(255) NULL,
    status                    varchar(255) NULL,
    created                   timestamptz  NULL,
    updated                   timestamptz  NULL,
    error_message             varchar      NULL,
    message_id                varchar(255) NULL,
    is_initial                bool         NULL,
    CONSTRAINT notification_message_pkey PRIMARY KEY (id),
    CONSTRAINT fk_notification_message_notification FOREIGN KEY (notification_id) REFERENCES public.notification (id)
);

CREATE TABLE public.assets_as_planned_notifications
(
    notification_id int8         NOT NULL,
    asset_id        varchar(255) NOT NULL
);

CREATE TABLE public.assets_as_planned_notification_messages
(
    notification_message_id varchar(255) NOT NULL,
    asset_id                varchar(255) NOT NULL
);

CREATE TABLE public.assets_as_built_notifications
(
    notification_id int8         NOT NULL,
    asset_id        varchar(255) NOT NULL,
    CONSTRAINT fk_asset_entity FOREIGN KEY (asset_id) REFERENCES public.assets_as_built (id),
    CONSTRAINT fk_assets_as_built_notifications FOREIGN KEY (notification_id) REFERENCES public.notification (id)
);

CREATE TABLE public.assets_as_built_notification_messages
(
    notification_message_id varchar(255) NOT NULL,
    asset_id                varchar(255) NOT NULL,
    CONSTRAINT fk_notification FOREIGN KEY (notification_message_id) REFERENCES public.notification_message (id)
);

-- transfer data from old table to new tables
-- Insert data from alert to notification table
INSERT INTO notification (id,bpn, close_reason, created, description, "status", side, accept_reason, decline_reason, updated, "type")
SELECT id,bpn, close_reason, created, description, "status", side, accept_reason, decline_reason, updated, 'ALERT' AS "type"
FROM alert;


-- Insert data from alert_notification to notification_message table
INSERT INTO notification_message
(id       , contract_agreement_id, notification_reference_id, send_to, created_by, notification_id            , target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated, error_message, message_id, "severity")
select  id, contract_agreement_id, notification_reference_id, send_to, created_by, alert_id as notification_id, target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated, error_message, message_id, "severity"
from alert_notification;


-- Create a sequence
CREATE SEQUENCE investigation_uid_seq_temp;

-- Set the starting value of the sequence to be the next value after the maximum UID found in the 'notification' table
SELECT setval('investigation_uid_seq_temp', COALESCE((SELECT MAX(id) FROM notification), 0) + 1);


-- CREATE TABLE investigation_id_history to map old id to new id;
CREATE TABLE investigation_id_history (
 old_investigation_id INT NOT NULL,
 new_investigation_id INT NOT NULL
);

INSERT INTO investigation_id_history (old_investigation_id,new_investigation_id)
SELECT id,nextval('investigation_uid_seq_temp')
FROM investigation;

-- Drop the foreign key constraint
ALTER TABLE assets_as_built_investigations DROP CONSTRAINT fk_investigation;
ALTER TABLE investigation_notification DROP CONSTRAINT fk_investigation;

-- Update the investigation_id
UPDATE investigation
SET id = investigation_id_history.new_investigation_id
    FROM investigation_id_history
WHERE investigation.id = investigation_id_history.old_investigation_id;

UPDATE assets_as_built_investigations
SET investigation_id = investigation_id_history.new_investigation_id
    FROM investigation_id_history
WHERE assets_as_built_investigations.investigation_id = investigation_id_history.old_investigation_id;

UPDATE investigation_notification
SET investigation_id = investigation_id_history.new_investigation_id
    FROM investigation_id_history
WHERE investigation_notification.investigation_id = investigation_id_history.old_investigation_id;

-- Insert data from investigation to notification table
INSERT INTO notification(id,bpn, close_reason, created, description, "status", side, accept_reason, decline_reason, updated, "type")
SELECT id,bpn, close_reason, created, description, "status", side, accept_reason, decline_reason, updated, 'INVESTIGATION' AS "type"
FROM investigation;

-- Insert data from investigation_notification to notification_message table
INSERT INTO notification_message
(id      , contract_agreement_id, notification_reference_id, send_to, created_by, notification_id                    , target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated, error_message, message_id, "severity")
select id, contract_agreement_id, notification_reference_id, send_to, created_by, investigation_id as notification_id, target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated, error_message, message_id, "severity"
from investigation_notification;

---- Insert data from asset_as_built_alert to assets_as_built_notifications table
INSERT INTO assets_as_built_notifications (notification_id, asset_id)
SELECT alert_id AS notification_id, asset_id
FROM assets_as_built_alerts;

---- Insert data from asset_as_built_investigation to assets_as_built_notifications table
INSERT INTO assets_as_built_notifications (notification_id, asset_id)
SELECT investigation_id AS notification_id, asset_id
FROM assets_as_built_investigations;

-- Insert data from asset_as_built_alert_notifications to assets_as_built_notification_messages table
INSERT INTO assets_as_built_notification_messages
(notification_message_id                               , asset_id)
select alert_notification_id as notification_message_id, asset_id
from asset_as_built_alert_notifications;

-- Insert data from temp_assets_as_built_notifications to assets_as_built_notification_messages table
INSERT INTO assets_as_built_notification_messages
(notification_message_id                               , asset_id)
select notification_id as notification_message_id, asset_id
from temp_assets_as_built_notifications;

-- Drop all tables
DROP TABLE public.asset_as_built_alert_notifications;
DROP TABLE public.investigation_notification;
DROP TABLE public.assets_as_built_investigations;
DROP TABLE public.alert_notification;
DROP TABLE public.assets_as_built_alerts;
DROP TABLE public.alert;
DROP TABLE public.investigation;
DROP TABLE public.investigation_id_history;
DROP SEQUENCE public.alert_id_seq;
DROP SEQUENCE public.investigation_id_seq;
