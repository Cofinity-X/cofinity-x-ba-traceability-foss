-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_planned_childs
    (asset_as_planned_id  , id                   , id_short)
values
    -- owner has child supplier
    (${assetAsPlannedId03}, ${assetAsPlannedId01}, 'H-LeftTailLight'),  -- Xenon Left-Taillight has child Osram Rear Left-ZX500
    (${assetAsPlannedId05}, ${assetAsPlannedId01}, 'H-LeftTailLight'),  -- Xenon Left-Taillight has child Philips Rear Left-C4R
    (${assetAsPlannedId04}, ${assetAsPlannedId02}, 'H-RightTailLight'), -- Xenon Right-Taillight has child Osram Rear Right-ZX500
    (${assetAsPlannedId06}, ${assetAsPlannedId02}, 'H-RightTailLight'), -- Xenon Right-Taillight has child Philips Rear Right-C4R

    -- customer has child owner
    (${assetAsPlannedId01}, ${assetAsPlannedId09}, 'Audi-A7'), -- A7 has child Xenon Left-Taillight
    (${assetAsPlannedId02}, ${assetAsPlannedId09}, 'Audi-A7'); -- A7 has child Xenon Right-Taillight
