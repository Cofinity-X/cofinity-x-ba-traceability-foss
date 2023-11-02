-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into alert
    (id                     , bpn                            , close_reason, created              , description                  , status        , side      , accept_reason, decline_reason, updated                     , error_message)
values
    (${alertId1}            , 'BPNL000SUPPLIER1'             , null        , '2023-10-04T13:48:54', 'Alert about Left Headlights', 'RECEIVED'    , 'RECEIVER', null         , null          , null                        , null),
    (${alertId2}            , 'BPNL000SUPPLIER2'             , null        , '2023-10-22T17:05:22', 'Test Alert 2'               , 'ACKNOWLEDGED', 'RECEIVER', null         , null          , null                        , null),
    (${alertId3}            , 'BPNL000000000001'             , null        , '2023-10-22T17:05:22', 'Alert about Left Bulb'      , 'CREATED'     , 'SENDER'  , null         , null          , null                        , null),
    (${alertId4}            , 'BPNL000000000001'             , null        , '2023-10-22T17:05:22', 'Test Alert 4'               , 'SENT'        , 'SENDER'  , null         , null          , '2023-10-31 15:59:12.802881', null);

select setval('alert_id_seq', (select max(a.id) from alert a), true);
