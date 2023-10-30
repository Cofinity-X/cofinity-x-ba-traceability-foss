-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into alert_notification
    (id, contract_agreement_id, edc_url, notification_reference_id, send_to, created_by, alert_id, target_date, severity, created_by_name, send_to_name, edc_notification_id, status, created, updated, message_id, is_initial)
values
    (${alertNotificationId1}, 1, 'https://some.edc.url', 1, 'send_to', 'created_by', ${alertId1}, '2023-12-04T13:48:54', 1, 'created_by_name', 'send_to_name', 1, '2', '2023-10-04T13:48:54', null, 1, true),
    (${alertNotificationId2}, 1, 'https://some.edc.url', 1, 'send_to', 'created_by', ${alertId2}, '2023-12-04T13:48:54', 1, 'created_by_name', 'send_to_name', 1, '3', '2023-10-04T13:48:54', null, 1, true),
    (${alertNotificationId3}, 1, 'https://some.edc.url', 1, 'send_to', 'created_by', ${alertId3}, '2023-12-04T13:48:54', 1, 'created_by_name', 'send_to_name', 1, '0', '2023-10-04T13:48:54', null, 1, true),
    (${alertNotificationId4}, 1, 'https://some.edc.url', 1, 'send_to', 'created_by', ${alertId4}, '2023-12-04T13:48:54', 1, 'created_by_name', 'send_to_name', 1, '1', '2023-10-04T13:48:54', null, 1, true);



