-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https: //documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

-- This creates an investigation in state ACCEPTED in Severity Minor for asBuilt asset Würth W-FogLight which is sent from BPNL000000000001 to BPNL000SUPPLIER3

---
insert into notification
    (id                                  , title                                 , bpn                  , close_reason, created                               , description                                                  , status         , side                                  , accept_reason         , decline_reason     , updated                              , type)
values
    (${investigationSentId4}             , ''                                    , ${bpnOwn}            , null        , current_timestamp - interval '4 days' , 'Investigation on Würth W-FogLight due to damaged packaging.', 'ACCEPTED'     , 'SENDER'                              , 'Damn, it''s damaged!', null               , current_timestamp - interval '1 hour', 'INVESTIGATION');

---
-- reset sequence to highest next-val
select setval('notification_id_seq'      , (select max(n.id) from notification n), true);

---
insert into notification_message
 (id                                     , notification_id                       , contract_agreement_id, edc_url     , notification_reference_id             , created_by                                                   , send_to        , target_date                           , severity              , created_by_name    , send_to_name                         , edc_notification_id                   , status    , created                              , updated                              , message_id, is_initial, error_message)
values
    (${investigationNotificationSentId4a}, ${investigationSentId4}               , 'contractAgreementId', null        , null                                  , ${bpnOwn}                                                    , ${bpnSupplier3}, current_timestamp + interval '1 month', 'MINOR'               , ${bpnOwnName}      , ${bpnSupplier3Name}                  , '10454df4-c04d-4e48-ab86-6aeb568fe64a', 'ACCEPTED', current_timestamp - interval '4 days', current_timestamp - interval '1 hour', null      , true      , null);

---
-- join investigation to asset
insert into assets_as_built_notification_messages
    (notification_message_id             , asset_id)
values
    (${investigationNotificationSentId4a}, ${assetAsBuiltId23});

---
-- join investigation to asset
insert into assets_as_built_notifications
    (notification_id                     , asset_id)
values
    (${investigationSentId4}             , ${assetAsBuiltId23});

---
-- ACCEPTED by receiver notification message
insert into notification_message
    (id                                  , notification_id                       , contract_agreement_id, edc_url     , notification_reference_id             , created_by                                                   , send_to        , target_date                           , severity              , created_by_name    , send_to_name                         , edc_notification_id                   , status    , created                              , updated                              , message_id, is_initial, error_message)
values
    (${investigationNotificationSentId4b}, ${investigationSentId4}               , 'contractAgreementId', null        , '10454df4-c04d-4e48-ab86-6aeb568fe64a', ${bpnSupplier3}                                              , ${bpnOwn}      , current_timestamp + interval '1 week' , 'MINOR'               , ${bpnSupplier3Name}, ${bpnOwnName}                        , '10454df4-c04d-4e48-ab86-6aeb568fe64a', 'ACCEPTED', current_timestamp - interval '2 days', null                                 , null      , false     , null);

---
-- join ACCEPTED notification to asset
insert into assets_as_built_notification_messages
    (notification_message_id             , asset_id)
values
    (${investigationNotificationSentId4b}, ${assetAsBuiltId23});
