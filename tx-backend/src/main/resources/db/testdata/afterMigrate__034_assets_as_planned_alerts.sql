-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_planned_alerts
    (alert_id   , asset_id)
values
    (${alertId2}, ${assetAsPlannedId1}), --
    (${alertId4}, ${assetAsPlannedId3}); --
