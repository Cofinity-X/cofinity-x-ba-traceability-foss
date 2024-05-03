-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https: //documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

-- This creates an investigation in state RECEIVED in Severity Critical for asBuilt asset Osram Front Right-AX400 which is sent from BPNL000000000001 to BPNL000SUPPLIER1

---
insert into notification
    (id                         , title, bpn            , close_reason, created                              , description                                                            , status    , side      , accept_reason, decline_reason, updated, type)
values
    (${investigationReceivedId1}, ''   , ${bpnCustomer1}, null        , current_timestamp - interval '2 days', 'Investigation on Osram Front Right-AX400 due to excessive brightness.', 'RECEIVED', 'RECEIVER', null         , null          , null   , 'INVESTIGATION' );

---
-- reset sequence to highest next-val
select setval('notification_id_seq', (select max(n.id) from notification n), true);

---
insert into notification_message
    (id                                     , notification_id            , contract_agreement_id, edc_url, notification_reference_id             , created_by     , send_to  , target_date                          , severity  , created_by_name    , send_to_name , edc_notification_id                   , status    , created                             , updated                               , message_id                            , is_initial, error_message)
values
    (${investigationNotificationReceivedId1}, ${investigationReceivedId1}, null                 , null   , '8925f21f-09eb-4789-81fb-ec221e9e1561', ${bpnCustomer1}, ${bpnOwn}, current_timestamp + interval '1 week', 'CRITICAL', ${bpnCustomer1Name}, ${bpnOwnName}, '8925f21f-09eb-4789-81fb-ec221e9e1561', 'RECEIVED', current_timestamp - interval '1 day', current_timestamp - interval '2 hours', 'e04f75e8-d37b-42e4-8cf7-6127f35f3ed5', false     , null);

---
-- join notificaiton to asset
insert into assets_as_built_notification_messages
    (notification_message_id                , asset_id)
values
    (${investigationNotificationReceivedId1}, ${assetAsBuiltId06});

---
-- join notification to asset
insert into assets_as_built_notifications
    (notification_id            , asset_id)
values
    (${investigationReceivedId1}, ${assetAsBuiltId06});
