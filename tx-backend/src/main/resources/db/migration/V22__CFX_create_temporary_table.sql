-- Create a temporary alert table to hold the data
CREATE TABLE temp_alert AS
SELECT * FROM public.alert;

-- Insert data from alert to temp_alert table
INSERT INTO temp_alert (id, bpn, close_reason, created, description, status, side, accept_reason, decline_reason, updated)
SELECT id, bpn, close_reason, created, description, "status", side, accept_reason, decline_reason, updated
FROM public.alert;

-- Create a temporary investigation table to hold the data
CREATE TABLE temp_investigation AS
SELECT * FROM public.investigation;

-- Insert data from investigation to temp_investigation table
INSERT INTO temp_investigation
(id, bpn, close_reason, created, description, "status", updated, side, accept_reason, decline_reason)
SELECT id, bpn, close_reason, created, description, "status", updated, side, accept_reason, decline_reason
FROM public.investigation;

-- Create a temporary alert_notification table to hold the data
CREATE TABLE temp_alert_notification AS
SELECT * FROM public.alert_notification;

-- Insert data from alert_notification to temp_alert_notification table
INSERT INTO temp_alert_notification
(id, contract_agreement_id, notification_reference_id, send_to, created_by, alert_id, target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated, message_id, "severity", error_message)
select id, contract_agreement_id, notification_reference_id, send_to, created_by, alert_id, target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated, message_id, "severity", error_message
from public.alert_notification;

-- Create a temporary investigation_notification table to hold the data
CREATE TABLE temp_investigation_notification AS
SELECT * FROM public.investigation_notification;

-- Insert data from investigation_notification to temp_investigation_notification table
INSERT INTO temp_investigation_notification
(id, contract_agreement_id, notification_reference_id, send_to, created_by, investigation_id, target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated, message_id, "severity", error_message)
select id, contract_agreement_id, notification_reference_id, send_to, created_by, investigation_id, target_date, created_by_name, send_to_name, edc_notification_id, "status", created, updated, message_id, "severity", error_message
from public.investigation_notification;

-- Create a temporary asset_as_built_alert_notifications table to hold the data
CREATE TABLE temp_asset_as_built_alert_notifications AS
SELECT * FROM public.asset_as_built_alert_notifications;

-- Insert data from asset_as_built_alert_notifications to temp_asset_as_built_alert_notifications table
INSERT INTO temp_asset_as_built_alert_notifications
(alert_notification_id, asset_id)
select alert_notification_id, asset_id
from public.asset_as_built_alert_notifications;

-- Create a temporary assets_as_built_notifications table to hold the data
CREATE TABLE temp_assets_as_built_notifications AS
SELECT * FROM public.assets_as_built_notifications;

-- Insert data from assets_as_built_notifications to temp_assets_as_built_notifications table
INSERT INTO temp_assets_as_built_notifications
(notification_id, asset_id)
select notification_id, asset_id
from public.assets_as_built_notifications;

-- Create a temporary assets_as_built_investigations table to hold the data
CREATE TABLE temp_assets_as_built_investigations AS
SELECT * FROM public.assets_as_built_investigations;

-- Insert data from assets_as_built_investigations to temp_assets_as_built_investigations table
INSERT INTO temp_assets_as_built_investigations
(investigation_id, asset_id)
select investigation_id, asset_id
from public.assets_as_built_investigations;

-- Create a temporary assets_as_built_alerts table to hold the data
CREATE TABLE temp_assets_as_built_alerts AS
SELECT * FROM public.assets_as_built_alerts;

-- Insert data from assets_as_built_alerts to temp_assets_as_built_alerts table
INSERT INTO temp_assets_as_built_alerts
(alert_id, asset_id)
select alert_id, asset_id
from public.assets_as_built_alerts;


