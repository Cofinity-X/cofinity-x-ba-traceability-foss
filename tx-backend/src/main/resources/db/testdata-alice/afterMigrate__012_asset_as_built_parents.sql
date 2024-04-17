-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built_parents
    (asset_as_built_id  , id                 , id_short)
values
    -- owner has parent customer

    -- supplier has parent owner
    (${assetAsBuiltId03}, ${assetAsBuiltId01}, 'H-LeftHeadLight'),  -- Osram Front Left-AX400 has parent Xenon Left-Headlights
    (${assetAsBuiltId04}, ${assetAsBuiltId02}, 'H-RightHeadLight'); -- Osram Front Right-AX400 has parent Xenon Right-Headlights
