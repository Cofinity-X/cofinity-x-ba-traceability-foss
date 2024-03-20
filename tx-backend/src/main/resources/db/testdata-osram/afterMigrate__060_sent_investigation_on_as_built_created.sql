-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https:                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   //documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

-- This creates an investigation in state CREATED in Severity Critical for asBuilt asset Petersmeier Packaging for turning light which is sent from BPNL000SUPPLIER1 to BPNL00SUPPLIER22

---
insert into investigation
    (id                                 , bpn                                    , close_reason             , created                             , description                                               , status                 , side                                 , accept_reason, decline_reason , updated)
values
    (${investigationSentId1}            , ${bpnOwn}                              , null                     , current_timestamp - interval '1 day', 'Investigation on Petersmeier Packaging for turning light', 'CREATED'              , 'SENDER'                             , null         , null           , null   );

---
-- reset sequence to highest next-val
select setval('investigation_id_seq1'   , (select max(i.id) from investigation i), true);

---
insert into investigation_notification
    (id                                 , contract_agreement_id                  , notification_reference_id, created_by                          , send_to                                                   , investigation_id       , target_date                          , severity     , created_by_name, send_to_name        , edc_notification_id                   , status   , created                             , updated                                  , message_id                            , error_message)
values
    (${investigationNotificationSentId1}, null                                   , null                     , ${bpnOwn}                           , ${bpnSupplier22}                                          , ${investigationSentId1}, current_timestamp + interval '1 week', 'CRITICAL'   , ${bpnOwnName}  , ${bpnSupplier22Name}, 'c01353db-640a-44c4-9a87-28fa3a950a95', 'CREATED', current_timestamp - interval '1 day', current_timestamp - interval '3- minutes', 'edd50c79-ded1-4259-bc55-19f38d4e3291', null);

---
-- join investigation to asset
insert into assets_as_built_notifications
    (notification_id                    , asset_id)
values
    (${investigationNotificationSentId1}, ${assetAsBuiltId10});

---
-- join investigation to asset
insert into assets_as_built_investigations
    (investigation_id                   , asset_id)
values
    (${investigationSentId1}            , ${assetAsBuiltId10});
