-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built_parents
    (asset_as_built_id  , id                 , id_short)
values
    -- owner has child customer
    (${assetAsBuiltId01}, ${assetAsBuiltId07}, 'H-LeftHeadLight'),  -- Xenon Left-Headlights has child Z1
    (${assetAsBuiltId02}, ${assetAsBuiltId07}, 'H-RightHeadLight'), -- Xenon Right-Headlights has child Z1

    -- supplier has child owner
    (${assetAsBuiltId03}, ${assetAsBuiltId01}, 'O-LeftHeadBulb'),   -- Osram Front Left-AX400 has child Xenon Left-Headlights
    (${assetAsBuiltId05}, ${assetAsBuiltId01}, 'P-LeftHeadBulb'),   -- Philips Front Left-D3H has child Xenon Left-Headlights
    (${assetAsBuiltId04}, ${assetAsBuiltId02}, 'O-RightHeadBulb'),  -- Osram Front Right-AX400 has child Xenon Right-Headlights
    (${assetAsBuiltId06}, ${assetAsBuiltId02}, 'P-RightHeadBulb');  -- Philips Front Right-D3H has child Xenon Right-Headlights
