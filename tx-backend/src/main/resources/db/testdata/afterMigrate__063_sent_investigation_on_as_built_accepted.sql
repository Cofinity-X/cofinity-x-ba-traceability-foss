-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

-- This creates an investigation in state ACCEPTED in Severity Minor for asBuilt asset Würth W-FogLight which is sent from BPNL000000000001 to BPNL000SUPPLIER3

---
insert into investigation
    (id                     , bpn               , close_reason, created                               , description                                                  , status    , side    , accept_reason          , decline_reason, updated                              , error_message)
values
    (${investigationSentId4}, 'BPNL000000000001', null        , current_timestamp - interval '4 days' , 'Investigation on Würth W-FogLight due to damaged packaging.', 'ACCEPTED', 'SENDER', 'Damn, it''s damaged!' , null          , current_timestamp - interval '1 hour', null);

---
-- reset sequence to highest next-val
select setval('investigation_id_seq', (select max(i.id) from investigation i), true);

---
insert into investigation_notification
    (id                                  , contract_agreement_id, edc_url                                                 , notification_reference_id, send_to           , created_by        , investigation_id       , target_date                           , severity, created_by_name, send_to_name   , edc_notification_id                   , status, created                               , updated                              , message_id                            , is_initial)
values
    (${investigationNotificationSentId4a}, 'contractAgreementId', 'http://localhost:8082/api/qualitynotifications/receive', null                     , 'BPNL000SUPPLIER3', 'BPNL000000000001', ${investigationSentId4}, current_timestamp + interval '1 month', 0       , 'Hella'        , 'Würth'        , '3ac2239a-e63f-4c19-b3b3-e6a2e5a240da', 4     , current_timestamp - interval '4 days' , current_timestamp - interval '1 hour', '749b31e9-9e73-4699-9470-dbee67ebc7a7', true);

---
-- join investigation to asset
insert into assets_as_built_notifications
    (notification_id                     , asset_id)
values
    (${investigationNotificationSentId4a}, ${assetAsBuiltId23});

---
-- join investigation to asset
insert into assets_as_built_investigations
    (investigation_id       , asset_id)
values
    (${investigationSentId4}, ${assetAsBuiltId23});

---
update assets_as_built
    set in_investigation = true
    where id in (${assetAsBuiltId23});

---
-- ACCEPTED by receiver notification message
insert into investigation_notification
    (id                                  , contract_agreement_id, edc_url                                                 , notification_reference_id, send_to           , created_by        , investigation_id       , target_date                          , severity, created_by_name, send_to_name, edc_notification_id                   , status, created                              , updated                              , message_id                            , is_initial)
values
    (${investigationNotificationSentId4b}, 'contractAgreementId', 'http://localhost:8082/api/qualitynotifications/receive', null                     , 'BPNL000SUPPLIER3', 'BPNL000000000001', ${investigationSentId4}, current_timestamp + interval '1 week', 0       , 'Hella'        , 'Würth'     , '8925f21f-09eb-4789-81fb-ec221e9e1561', '4'   , current_timestamp - interval '4 days', current_timestamp - interval '1 hour', '207ba6cf-217b-401d-a5da-69cac8b154a5', false);

---
-- join ACCEPTED notification to asset
insert into assets_as_built_notifications
    (notification_id                     , asset_id)
values
    (${investigationNotificationSentId4b}, ${assetAsBuiltId23});
