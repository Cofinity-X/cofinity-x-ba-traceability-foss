-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built
    (id                 , customer_part_id, id_short          , manufacturer_id   , manufacturer_name, manufacturer_part_id, manufacturing_country, name_at_customer        , name_at_manufacturer    , quality_type, van , owner     , in_investigation, active_alert, semantic_model_id            , semantic_data_model, classification, product_type, traction_battery_code, manufacturing_date)
values
    (${assetAsBuiltId01}, '95657762-59'   , 'H-LeftHeadLight' , 'BPNL000000000001', 'Hella'          , 'XH4711'            , 'DEU'                , 'Xenon Headlights left' , 'Xenon Left-Headlights' , 'OK'        , '--', 'OWN'     , False           , False       , 'NO-613963493493659233961306', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , '2022-02-04T13:48:54'),
    (${assetAsBuiltId02}, '95657762-60'   , 'H-RightHeadLight', 'BPNL000000000001', 'Hella'          , 'XH4712'            , 'DEU'                , 'Xenon Headlights right', 'Xenon Right-Headlights', 'OK'        , '--', 'OWN'     , False           , False       , 'NO-613963493493659233961307', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , '2022-02-04T13:48:54'),
    (${assetAsBuiltId03}, '95657762-61'   , 'O-LeftBulb'      , 'BPNL000SUPPLIER1', 'Osram'          , 'LB0815'            , 'DEU'                , 'Light Bulb left'       , 'Osram Left-AX400'      , 'OK'        , '--', 'SUPPLIER', False           , False       , 'NO-613963493493659233961308', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , '2022-02-04T13:48:54'),
    (${assetAsBuiltId04}, '95657762-62'   , 'O-RightBulb'     , 'BPNL000SUPPLIER1', 'Osram'          , 'LB0816'            , 'DEU'                , 'Light Bulb right'      , 'Osram Right-AX400'     , 'OK'        , '--', 'SUPPLIER', False           , False       , 'NO-613963493493659233961309', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , '2022-02-04T13:48:54'),
    (${assetAsBuiltId05}, '95657762-61'   , 'P-LeftBulb'      , 'BPNL000SUPPLIER2', 'Phillips'       , 'D3RPHL'            , 'DEU'                , 'Light Bulb left'       , 'Phillips Left-D3R'     , 'OK'        , '--', 'SUPPLIER', False           , False       , 'NO-613963493493659233961318', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , '2022-02-04T13:48:54'),
    (${assetAsBuiltId06}, '95657762-62'   , 'P-RightBuld'     , 'BPNL000SUPPLIER2', 'Phillips'       , 'D3RPHR'            , 'DEU'                , 'Light Bulb right'      , 'Phillips Right-D3R'    , 'OK'        , '--', 'SUPPLIER', False           , False       , 'NO-613963493493659233961319', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , '2022-02-04T13:48:54'),
    (${assetAsBuiltId07}, '95657762-63'   , 'BMW-Z1'          , 'BPNL000CUSTOMER1', 'BMW AG'         , 'Z1ABC'             , 'DEU'                , 'BMW Z1'                , 'Z1'                    , 'OK'        , '--', 'CUSTOMER', False           , False       , 'NO-613963493493659233961300', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , '2022-02-04T13:48:54'),
    (${assetAsBuiltId08}, '95657762-64'   , 'BMW-Z4'          , 'BPNL000CUSTOMER1', 'BMW AG'         , 'Z4ABC'             , 'DEU'                , 'BMW Z3'                , 'Z3'                    , 'OK'        , '--', 'CUSTOMER', False           , False       , 'NO-613963493493659233961301', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , '2022-02-04T13:48:54'),
    (${assetAsBuiltId09}, '95657762-65'   , 'Audi-A7'         , 'BPNL000CUSTOMER2', 'Audi AG'        , 'A7XXX'             , 'DEU'                , 'Audi A7'               , 'A7'                    , 'OK'        , '--', 'CUSTOMER', False           , False       , 'NO-613963493493659233961302', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , '2022-02-04T13:48:54'),
    (${assetAsBuiltId10}, '95657762-66'   , 'Audi-A8'         , 'BPNL000CUSTOMER2', 'Audi AG'        , 'A8XXX'             , 'DEU'                , 'Audi A8'               , 'A8'                    , 'OK'        , '--', 'CUSTOMER', False           , False       , 'NO-613963493493659233961303', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , '2022-02-04T13:48:54');