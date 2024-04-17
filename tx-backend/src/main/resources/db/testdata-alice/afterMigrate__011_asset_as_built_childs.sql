-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built_childs
    (asset_as_built_id  , id                 , id_short)
values
    -- owner has child supplier
    (${assetAsBuiltId01}, ${assetAsBuiltId03}, 'O-LeftHeadBulb'),   -- Xenon Left-Headlights has child Osram Front Left-AX400
    (${assetAsBuiltId02}, ${assetAsBuiltId04}, 'O-RightHeadBulb');  -- Xenon Right-Headlights has child Osram Front Right-AX400
