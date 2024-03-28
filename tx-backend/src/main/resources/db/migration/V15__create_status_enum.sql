DROP VIEW IF EXISTS assets_as_built_view;

CREATE TYPE status AS ENUM ('CREATED', 'SENT', 'RECEIVED','ACKNOWLEDGED', 'ACCEPTED', 'DECLINED','CANCELED', 'CLOSED');

ALTER TABLE alert_notification ALTER COLUMN "status" TYPE status USING ("status"::status);
ALTER TABLE alert ALTER COLUMN "status" TYPE status USING ("status"::status);

ALTER TABLE investigation_notification ALTER COLUMN "status" TYPE status USING ("status"::status);
ALTER TABLE investigation ALTER COLUMN "status" TYPE status USING ("status"::status);


CREATE OR REPLACE VIEW assets_as_built_view AS
    SELECT asset.id,
        asset.customer_part_id,
        asset.id_short,
        asset.manufacturer_id,
        asset.manufacturer_name,
        asset.manufacturer_part_id,
        asset.manufacturing_country,
        asset.name_at_customer,
        asset.name_at_manufacturer,
        asset.quality_type,
        asset.van,
        asset.owner,
        asset.semantic_model_id,
        asset.semantic_data_model,
        asset.classification,
        asset.product_type,
        asset.traction_battery_code,
        asset.manufacturing_date,
        asset.import_state,
        asset.import_note,
        asset.policy_id,
        asset.tombstone,
    (
        SELECT count(alert.id) AS count
        FROM alert alert
        JOIN assets_as_built_alerts alert_assets ON alert.id = alert_assets.alert_id
        WHERE alert.status IN ('CREATED', 'SENT', 'RECEIVED', 'ACKNOWLEDGED', 'ACCEPTED', 'DECLINED') -- Use string literals
        AND alert.side = 'RECEIVER' -- Use string literal
        AND alert_assets.asset_id = asset.id
    ) AS received_active_alerts,
    (
        SELECT count(alert.id) AS count
        FROM alert alert
        JOIN assets_as_built_alerts alert_assets ON alert.id = alert_assets.alert_id
        WHERE alert.status IN ('CREATED', 'SENT', 'RECEIVED', 'ACKNOWLEDGED', 'ACCEPTED', 'DECLINED') -- Use string literals
        AND alert.side = 'SENDER' -- Use string literal
        AND alert_assets.asset_id = asset.id
    ) AS sent_active_alerts,
    (
        SELECT count(investigation.id) AS count
        FROM investigation investigation
        JOIN assets_as_built_investigations investigation_assets ON investigation.id = investigation_assets.investigation_id
        WHERE investigation.status IN ('CREATED', 'SENT', 'RECEIVED', 'ACKNOWLEDGED', 'ACCEPTED', 'DECLINED') -- Use string literals
        AND investigation.side = 'RECEIVER' -- Use string literal
        AND investigation_assets.asset_id = asset.id
    ) AS received_active_investigations,
    (
        SELECT count(investigation.id) AS count
        FROM investigation investigation
        JOIN assets_as_built_investigations investigation_assets ON investigation.id = investigation_assets.investigation_id
        WHERE investigation.status IN ('CREATED', 'SENT', 'RECEIVED', 'ACKNOWLEDGED', 'ACCEPTED', 'DECLINED') -- Use string literals
        AND investigation.side = 'SENDER' -- Use string literal
        AND investigation_assets.asset_id = asset.id
    ) AS sent_active_investigations
FROM
    assets_as_built asset;




