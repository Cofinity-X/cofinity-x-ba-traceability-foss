-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built_parents
    (asset_as_built_id  , id                 , id_short)
values
    (${assetAsBuiltId01}, ${assetAsBuiltId03}, '--'), -- Xenon Left-Headlights isParentOf Osram Left-AX400
    (${assetAsBuiltId01}, ${assetAsBuiltId05}, '--'), -- Xenon Left-Headlights isParentOf Xenon Vision Left-D3R
    (${assetAsBuiltId02}, ${assetAsBuiltId04}, '--'), -- Xenon Right-Headlights isParentOf Osram Right-AX400
    (${assetAsBuiltId02}, ${assetAsBuiltId06}, '--'); -- Xenon Right-Headlights isParentOf Xenon Vision Right-D3R
