-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

-- truncate all tables, except for the "technical" ones (flyway_schema_history and shedlock)
truncate table
    alert,
    alert_notification,
    asset_as_built_alert_notifications,
    assets_as_built,
    assets_as_built_alerts,
    assets_as_built_childs,
    assets_as_built_investigations,
    assets_as_built_notifications,
    assets_as_built_parents,
    assets_as_planned,
    assets_as_planned_childs,
    bpn_storage,
    import_job,
    import_job_assets_as_built,
    import_job_assets_as_planned,
    investigation,
    investigation_notification,
    shedlock,
    submodel,
    submodel_payload,
    traction_battery_code_subcomponent
    cascade;

---
-- reset sequences
select setval('alert_id_seq1'        , 1, true);
select setval('investigation_id_seq1', 1, true);