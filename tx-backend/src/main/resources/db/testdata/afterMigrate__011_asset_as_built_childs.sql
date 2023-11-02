-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built_childs
    (asset_as_built_id  , id                 , id_short)
values
    (${assetAsBuiltId01}, ${assetAsBuiltId07}, '--'), -- Xenon Left-Headlights isChildOf BMW Z1
    (${assetAsBuiltId01}, ${assetAsBuiltId08}, '--'), -- Xenon Left-Headlights isChildOf BMW Z3
    (${assetAsBuiltId02}, ${assetAsBuiltId09}, '--'), -- Xenon Right-Headlights isChildOf Audi A7
    (${assetAsBuiltId02}, ${assetAsBuiltId10}, '--'); -- Xenon Right-Headlights isChildOf Audi A8
