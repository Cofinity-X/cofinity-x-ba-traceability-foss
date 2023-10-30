-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built_alerts
    (alert_id, asset_id)
values
    (${alertId1}, ${assetAsBuiltId1}),
    (${alertId3}, ${assetAsBuiltId3});



