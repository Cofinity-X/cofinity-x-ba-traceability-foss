-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https:                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           //documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

-- This creates an investigation in state ACCEPTED in Severity Critical for asBuilt asset H-LeftTurningLight which is sent from BPNL000CUSTOMER2 to BPNL000000000001

---
insert into investigation
    (id                                      , bpn                                    , close_reason                          , created                              , description                                              , status                     , side                                 , accept_reason           , decline_reason     , updated                               )
values
    (${investigationReceivedId3}             , ${bpnCustomer2}                        , null                                  , current_timestamp - interval '3 days', 'Investigation on H-LeftTurningLight due to malfunction.', 'ACCEPTED'                 , 'RECEIVER'                           , 'True, it doesn''t work', null               , current_timestamp - interval '1 hours');

---
-- reset sequence to highest next-val
select setval('investigation_id_seq1'        , (select max(i.id) from investigation i), true);

---
insert into investigation_notification
    (id                                      , contract_agreement_id                  , notification_reference_id             , created_by                           , send_to                                                  , investigation_id           , target_date                          , severity                , created_by_name    , send_to_name                            , edc_notification_id                   , status    , created                              , updated                               , message_id                            , error_message)
values
    (${investigationNotificationReceivedId3a}, null                                   , '8925f21f-09eb-4789-81fb-ec221e9e1561', ${bpnCustomer2}                      , ${bpnOwn}                                                , ${investigationReceivedId3}, current_timestamp + interval '1 week', 'CRITICAL'              , ${bpnCustomer2Name}, ${bpnOwnName}                           , '8925f21f-09eb-4789-81fb-ec221e9e1561', 'ACCEPTED', current_timestamp - interval '3 days', current_timestamp - interval '2 hours', 'e04f75e8-d37b-42e4-8cf7-6127f35f3ed5', null);

---
-- join investigation to asset
insert into assets_as_built_notifications
    (notification_id                         , asset_id)
values
    (${investigationNotificationReceivedId3a}, ${assetAsBuiltId11});

---
-- join investigation to asset
insert into assets_as_built_investigations
    (investigation_id                        , asset_id)
values
    (${investigationReceivedId3}             , ${assetAsBuiltId11});

---
---
-- ACCEPTED by receiver notification message
insert into investigation_notification
    (id                                      , contract_agreement_id                  , notification_reference_id             , created_by                           , send_to                                                  , investigation_id           , target_date                          , severity                , created_by_name    , send_to_name                            , edc_notification_id                   , status    , created                              , updated                               , message_id                            , error_message)
values
    (${investigationNotificationReceivedId3b}, 'contractAgreementId'                  , null                                  , ${bpnOwn}                            , ${bpnCustomer2}                                          , ${investigationReceivedId3}, current_timestamp + interval '1 week', 'CRITICAL'              , ${bpnOwnName}      , ${bpnCustomer2Name}                     , '8925f21f-09eb-4789-81fb-ec221e9e1561', 'ACCEPTED', current_timestamp - interval '3 days', current_timestamp - interval '1 hour' , '207ba6cf-217b-401d-a5da-69cac8b154a5', null);

---
-- join ACCEPTED notification to asset
insert into assets_as_built_notifications
    (notification_id                         , asset_id)
values
    (${investigationNotificationReceivedId3b}, ${assetAsBuiltId11});
