-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https: //documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

-- This creates an investigation in state ACKNOWLEDGED in Severity Life-threatening for asBuilt asset Schott Glass bulb which is sent from BPNL00000000GNDU to BPNL00SUPPLIER21

---
insert into notification
    (id                     , title, bpn      , close_reason, created                              , description                          , status        , side    , accept_reason, decline_reason, updated                              , type)
values
    (${investigationSentId2}, ''   , ${bpnOwn}, null        , current_timestamp - interval '3 days', 'Investigation on Schott Glass bulb.', 'ACKNOWLEDGED', 'SENDER', null         , null          , current_timestamp - interval '1 hour', 'INVESTIGATION');

---
-- reset sequence to highest next-val
select setval('notification_id_seq', (select max(n.id) from notification n), true);

---
insert into notification_message
    (id                                  , notification_id        , contract_agreement_id, edc_url, notification_reference_id, created_by, send_to         , target_date                          , severity          , created_by_name, send_to_name        , edc_notification_id                   , status        , created                              , updated                              , message_id, is_initial, error_message)
values
    (${investigationNotificationSentId2a}, ${investigationSentId2}, 'contractAgreementId', null   , null                     , ${bpnOwn} , ${bpnSupplier21}, current_timestamp + interval '4 days', 'LIFE_THREATENING', ${bpnOwnName}  , ${bpnSupplier21Name}, '92559ce9-d71e-46b3-989f-791c9970877c', 'ACKNOWLEDGED', current_timestamp - interval '3 days', current_timestamp - interval '1 hour', null      , true      , null);

---
-- join notification to asset
insert into assets_as_built_notification_messages
    (notification_message_id             , asset_id)
values
    (${investigationNotificationSentId2a}, ${assetAsBuiltId09});

---
-- join notification to asset
insert into assets_as_built_notifications
    (notification_id        , asset_id)
values
    (${investigationSentId2}, ${assetAsBuiltId09});

---
-- ACK by receiver notification message
insert into notification_message
    (id                                  , notification_id        , contract_agreement_id, edc_url, notification_reference_id             , created_by      , send_to  , target_date                          , severity          , created_by_name     , send_to_name , edc_notification_id                   , status        , created                              , updated, message_id, is_initial, error_message)
values
    (${investigationNotificationSentId2b}, ${investigationSentId2}, 'contractAgreementId', null   , '92559ce9-d71e-46b3-989f-791c9970877c', ${bpnSupplier21}, ${bpnOwn}, current_timestamp + interval '4 days', 'LIFE_THREATENING', ${bpnSupplier21Name}, ${bpnOwnName}, '92559ce9-d71e-46b3-989f-791c9970877c', 'ACKNOWLEDGED', current_timestamp - interval '2 days', null   , null      , false     , null);

---
-- join ACK notification to asset
insert into assets_as_built_notification_messages
    (notification_message_id             , asset_id)
values
    (${investigationNotificationSentId2b}, ${assetAsBuiltId09});
