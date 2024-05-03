-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https: //documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

-- This creates an investigation in state DECLINED in Severity Major for asBuilt asset Würth W-HighBeam which is sent from BPNL000000000001 to BPNL000SUPPLIER3

---
insert into notification
    (id                                  , title                                 , bpn                  , close_reason, created                              , description                                             , status         , side                                  , accept_reason, decline_reason     , updated                              , type)
values
    (${investigationSentId5}             , ''                                    , ${bpnOwn}            , null        , current_timestamp - interval '5 days', 'Investigation on Würth W-HighBeam due to broken glass.', 'DECLINED'     , 'SENDER'                              , null         , 'It''s not broken' , current_timestamp - interval '1 hour', 'INVESTIGATION');

---
-- reset sequence to highest next-val
select setval('notification_id_seq'      , (select max(n.id) from notification n), true);

---
insert into notification_message
 (id                                     , notification_id                       , contract_agreement_id, edc_url     , notification_reference_id            , created_by                                              , send_to        , target_date                           , severity     , created_by_name    , send_to_name                         , edc_notification_id                   , status    , created                              , updated                              , message_id                            , is_initial, error_message)
values
    (${investigationNotificationSentId5a}, ${investigationSentId5}               , 'contractAgreementId', null        , null                                 , ${bpnOwn}                                               , ${bpnSupplier3}, current_timestamp + interval '2 weeks', 'MAJOR'      , ${bpnOwnName}      , ${bpnSupplier3Name}                  , '3ac2239a-e63f-4c19-b3b3-e6a2e5a240da', 'DECLINED', current_timestamp - interval '5 days', current_timestamp - interval '1 hour', '749b31e9-9e73-4699-9470-dbee67ebc7a7', true      , null);

---
-- join investigation to asset
insert into assets_as_built_notification_messages
    (notification_message_id             , asset_id)
values
    (${investigationNotificationSentId5a}, ${assetAsBuiltId24});

---
-- join investigation to asset
insert into assets_as_built_notifications
    (notification_id                     , asset_id)
values
    (${investigationSentId5}             , ${assetAsBuiltId24});

---
-- DECLINED by receiver notification message
insert into notification_message
    (id                                  , notification_id                       , contract_agreement_id, edc_url     , notification_reference_id            , created_by                                              , send_to        , target_date                           , severity     , created_by_name    , send_to_name                         , edc_notification_id                   , status    , created                              , updated                              , message_id                            , is_initial, error_message)
values
    (${investigationNotificationSentId5b}, ${investigationSentId5}               , 'contractAgreementId', null        , null                                 , ${bpnSupplier3}                                         , ${bpnOwn}      , current_timestamp + interval '2 weeks', 'MAJOR'      , ${bpnSupplier3Name}, ${bpnOwnName}                        , '8925f21f-09eb-4789-81fb-ec221e9e1561', 'DECLINED', current_timestamp - interval '5 days', current_timestamp - interval '1 hour', '207ba6cf-217b-401d-a5da-69cac8b154a5', false     , null);

---
-- join DECLINED notification to asset
insert into assets_as_built_notification_messages
    (notification_message_id             , asset_id)
values
    (${investigationNotificationSentId5b}, ${assetAsBuiltId24});
