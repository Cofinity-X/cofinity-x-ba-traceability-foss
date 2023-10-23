INSERT INTO public.assets_as_built(
	id, customer_part_id, id_short, manufacturer_id, manufacturer_name, manufacturer_part_id, manufacturing_country, manufacturing_date, name_at_customer, name_at_manufacturer, quality_type, van, owner, in_investigation, active_alert, semantic_model_id, semantic_data_model, classification, product_type, traction_battery_code)
	VALUES ('urn:uuid:6ec3f1db-2798-454b-a73f-0d21a8966c78', '95657762-59', '--', 'BPNL00000003CML1', 'BMW AG', 'ManuPartID', 'DEU', '2022-02-04T13:48:54', 'Door Key', 'MyAsBuiltPartName', 'OK', '--', 'SUPPLIER', False, False, 'NO-613963493493659233961306', 'SERIALPART', 'component', 'AS_BUILT', '--');


INSERT INTO public.investigation(
	id, bpn, close_reason, created, description, status, updated, side, accept_reason, decline_reason, error_message)
	VALUES (12358, 'BPNL00000003CML1', '', '2023-10-17T13:48:54', 'Investigation No 002', 'CREATED', '2023-10-17T13:48:54', 'SENDER', '', '', '');

INSERT INTO public.assets_as_built_investigations(
	investigation_id, asset_id)
	VALUES (12358, 'urn:uuid:6ec3f1db-2798-454b-a73f-0d21a8966c78');

