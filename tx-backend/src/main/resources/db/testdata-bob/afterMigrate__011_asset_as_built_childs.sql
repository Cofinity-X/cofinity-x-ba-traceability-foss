-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built_childs
    (asset_as_built_id  , id                 , id_short)
values
    -- owner has child of supplier

    -- customer has child owner
    (${assetAsBuiltId01}, ${assetAsBuiltId05}, 'Left Head Bulb'),      -- Xenon Left-Headlights has child Left Head Bulb
    (${assetAsBuiltId03}, ${assetAsBuiltId07}, 'Turning Light Bulb');  -- Left Turning Lights has child Turning Light Bulb
