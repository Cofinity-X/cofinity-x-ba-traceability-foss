-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https: //documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

-- This creates an investigation in state CLOSED in Severity Minor for asBuilt asset H-LeftFogLight which is sent from BPNL000CUSTOMER3 to BPNL000000000001

---
insert into investigation
    (id                         , bpn            , close_reason                                 , created                              , description                                          , status  , side      , accept_reason, decline_reason, updated                               )
values
    (${investigationReceivedId5}, ${bpnCustomer3}, 'We confirm that the problem has been fixed.', current_timestamp - interval '5 days', 'Investigation on H-LeftFogLight due to malfunction.', 'CLOSED', 'RECEIVER', null         , null          , current_timestamp - interval '1 hours');

---
-- reset sequence to highest next-val
select setval('investigation_id_seq1', (select max(i.id) from investigation i), true);

---
insert into investigation_notification
    (id                                      , contract_agreement_id, notification_reference_id             , created_by     , send_to  , investigation_id           , target_date                           , severity, created_by_name    , send_to_name , edc_notification_id                   , status  , created                              , updated                               , message_id                            , error_message)
values
    (${investigationNotificationReceivedId5a}, null                 , '8925f21f-09eb-4789-81fb-ec221e9e1561', ${bpnCustomer3}, ${bpnOwn}, ${investigationReceivedId5}, current_timestamp + interval '1 month', 'MINOR' , ${bpnCustomer3Name}, ${bpnOwnName}, '8925f21f-09eb-4789-81fb-ec221e9e1561', 'CLOSED', current_timestamp - interval '5 days', current_timestamp - interval '2 hours', 'e04f75e8-d37b-42e4-8cf7-6127f35f3ed5', null);

---
-- join investigation to asset
insert into assets_as_built_notifications
    (notification_id                         , asset_id)
values
    (${investigationNotificationReceivedId5a}, ${assetAsBuiltId15});

---
-- join investigation to asset
insert into assets_as_built_investigations
    (investigation_id           , asset_id)
values
    (${investigationReceivedId5}, ${assetAsBuiltId15});

---
---
-- CLOSED by sender notification message
insert into investigation_notification
    (id                                      , contract_agreement_id, notification_reference_id, created_by     , send_to  , investigation_id           , target_date                           , severity, created_by_name    , send_to_name , edc_notification_id                   , status  , created                               , updated                              , message_id                            , error_message)
values
    (${investigationNotificationReceivedId5b}, 'contractAgreementId', null                     , ${bpnCustomer3}, ${bpnOwn}, ${investigationReceivedId5}, current_timestamp + interval '1 month', 'MINOR' , ${bpnCustomer3Name}, ${bpnOwnName}, '8925f21f-09eb-4789-81fb-ec221e9e1561', 'CLOSED', current_timestamp - interval '5 days ', current_timestamp - interval '1 hour', '207ba6cf-217b-401d-a5da-69cac8b154a5', null);

---
-- join CLOSED notification to asset
insert into assets_as_built_notifications
    (notification_id                         , asset_id)
values
    (${investigationNotificationReceivedId5b}, ${assetAsBuiltId15});
