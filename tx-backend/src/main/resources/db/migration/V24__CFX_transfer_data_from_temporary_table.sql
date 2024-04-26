-- Insert data from temp_alert to notification table
INSERT INTO notification (id, bpn, close_reason, created, description, "status",  side, accept_reason, decline_reason, updated,"type")
SELECT id, bpn, close_reason, created, description, "status", side, accept_reason, decline_reason, updated,'ALERT' AS "type"
FROM temp_alert;

-- Insert data from temp_investigation to notification table
INSERT INTO public.notification(id, title, bpn, close_reason, created, description, "status",  side, accept_reason, decline_reason,updated, "type")
SELECT id, '' AS title, bpn, close_reason, created, description, "status", side, accept_reason, decline_reason, updated,'INVESTIGATION' AS "type"
FROM temp_investigation;

-- Insert data from temp_alert_notification to notification_message table
INSERT INTO public.notification_message
(id, contract_agreement_id,notification_reference_id, send_to, created_by, notification_id, target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated, error_message, message_id,"severity")
select id, contract_agreement_id, notification_reference_id, send_to, created_by, alert_id as notification_id, target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated, error_message,message_id, "severity"
from temp_alert_notification;

-- Insert data from temp_investigation_notification to notification_message table
INSERT INTO public.notification_message
(id, contract_agreement_id,notification_reference_id, send_to, created_by, notification_id, target_date,  created_by_name, send_to_name, edc_notification_id, "status", created, updated, error_message, message_id,"severity")
select id, contract_agreement_id, notification_reference_id, send_to, created_by, investigation_id as notification_id, target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated,error_message, message_id, "severity"
from temp_investigation_notification;

-- Insert data from temp_asset_as_built_alert_notifications to assets_as_built_notification_messages table
INSERT INTO public.assets_as_built_notification_messages
(notification_message_id, asset_id)
select alert_notification_id as notification_message_id, asset_id
from temp_asset_as_built_alert_notifications;

-- Insert data from temp_assets_as_built_notifications to assets_as_built_notifications table
INSERT INTO public.assets_as_built_notifications
(notification_id, asset_id)
select cast(notification_id AS INTEGER) as notification_id, asset_id
from temp_assets_as_built_notifications;

-- Insert data from temp_assets_as_built_investigations to assets_as_built_notifications table
INSERT INTO public.assets_as_built_notifications
(notification_id, asset_id)
select cast(investigation_id AS INTEGER) as notification_id, asset_id
from temp_assets_as_built_investigations;

-- Drop temporary tables
DROP TABLE IF EXISTS temp_alert;
DROP TABLE IF EXISTS temp_investigation;
DROP TABLE IF EXISTS temp_alert_notification;
DROP TABLE IF EXISTS temp_investigation_notification;
DROP TABLE IF EXISTS temp_asset_as_built_alert_notifications;
DROP TABLE IF EXISTS temp_assets_as_built_notifications;
DROP TABLE IF EXISTS temp_assets_as_built_investigations;
