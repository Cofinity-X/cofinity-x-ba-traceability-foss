-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into alert
    (id, bpn, close_reason, created, description, status, side, accept_reason, decline_reason, updated, error_message)
values
    (${alertId1}, 'BPNL00000003CML1', null, '2023-10-04T13:48:54', 'Test Alert 1', 'RECEIVED', 'RECEIVER', null, null, null, null),
    (${alertId2}, 'BPNL00000003CML1', null, '2023-10-22T17:05:22', 'Test Alert 2', 'ACKNOWLEDGED', 'RECEIVER', null, null, null, null),
    (${alertId3}, 'BPNL00000003CML1', null, '2023-10-22T17:05:22', 'Test Alert 3', 'CREATED', 'SENDER', null, null, null, null),
    (${alertId4}, 'BPNL00000003CML1', null, '2023-10-22T17:05:22', 'Test Alert 4', 'SENT', 'SENDER', null, null, null, null);


