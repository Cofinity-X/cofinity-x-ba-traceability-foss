-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built
    (id, customer_part_id, id_short, manufacturer_id, manufacturer_name, manufacturer_part_id, manufacturing_country, name_at_customer, name_at_manufacturer, quality_type, van, owner, in_investigation, active_alert, semantic_model_id, semantic_data_model, classification, product_type, traction_battery_code, manufacturing_date)
values
    (${assetAsBuiltId1}, '95657762-59', '--', 'BPNL0000000ABCDE', 'Hella', 'XH4711', 'DEU', 'Xenon Headlights', 'Xenon Headlights', 'OK', '--', 'OWN', False, False, 'NO-613963493493659233961306', 'SERIALPART', 'component', 'AS_BUILT', '--', '2022-02-04T13:48:54'),
    (${assetAsBuiltId2}, '95657762-60', '--', 'BPNL0000000ABCDE', 'Hella', 'XH4711', 'DEU', 'Xenon Headlights', 'Xenon Headlights', 'OK', '--', 'OWN', False, False, 'NO-613963493493659233961306', 'SERIALPART', 'component', 'AS_BUILT', '--', '2022-02-04T13:48:54'),
    (${assetAsBuiltId3}, '95657762-61', '--', 'BPNL0000000VWXYZ', 'Osram', 'LB0815', 'DEU', 'Light Bulb', 'Osram AX400', 'OK', '--', 'SUPPLIER', False, False, 'NO-613963493493659233961306', 'SERIALPART', 'component', 'AS_BUILT', '--', '2022-02-04T13:48:54'),
    (${assetAsBuiltId4}, '95657762-62', '--', 'BPNL0000000VWXYZ', 'Osram', 'LB0815', 'DEU', 'Light Bulb', 'Osram AX400', 'OK', '--', 'SUPPLIER', False, False, 'NO-613963493493659233961306', 'SERIALPART', 'component', 'AS_BUILT', '--', '2022-02-04T13:48:54'),
    (${assetAsBuiltId5}, '95657762-63', '--', 'BPNL00000003CML1', 'BMW AG', 'Z3ABC', 'DEU', 'BMW Z3', 'Z3', 'OK', '--', 'CUSTOMER', False, False, 'NO-613963493493659233961306', 'SERIALPART', 'component', 'AS_BUILT', '--', '2022-02-04T13:48:54'),
    (${assetAsBuiltId6}, '95657762-64', '--', 'BPNL00000003CML1', 'BMW AG', 'Z3ABC', 'DEU', 'BMW Z3', 'Z3', 'OK', '--', 'CUSTOMER', False, False, 'NO-613963493493659233961306', 'SERIALPART', 'component', 'AS_BUILT', '--', '2022-02-04T13:48:54');



