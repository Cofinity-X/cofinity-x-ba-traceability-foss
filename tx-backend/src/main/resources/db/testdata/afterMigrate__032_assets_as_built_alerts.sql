-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built_alerts
    (alert_id   , asset_id)
values
    (${alertId1}, ${assetAsBuiltId01}), -- sup1 send to owner
    (${alertId3}, ${assetAsBuiltId03}); -- owner created to cust1

update assets_as_built
    set active_alert = true
    where id in (${assetAsBuiltId01}, ${assetAsBuiltId03}); -- incoming and outgoing
