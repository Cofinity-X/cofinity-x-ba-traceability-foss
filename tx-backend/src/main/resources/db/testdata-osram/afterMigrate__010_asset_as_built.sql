-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built
    (id                 , customer_part_id, id_short             , manufacturer_id , manufacturer_name   , manufacturer_part_id, manufacturing_country, name_at_customer              , name_at_manufacturer      , quality_type, van , owner     , semantic_model_id            , semantic_data_model, classification, product_type, manufacturing_date   , import_state)
values
    (${assetAsBuiltId01}, '95657763-01'   , 'H-LeftHeadLight'    , ${bpnCustomer1} , ${bpnCustomer1Name} , 'XH4711'            , 'DEU'                , 'Xenon Headlights left'       , 'Xenon Left-Headlights'   , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961301', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-04T13:48:54', 'PERSISTENT'),
    (${assetAsBuiltId02}, '95657763-02'   , 'H-RightHeadLight'   , ${bpnCustomer1} , ${bpnCustomer1Name} , 'XH4712'            , 'DEU'                , 'Xenon Headlights right'      , 'Xenon Right-Headlights'  , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961302', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-04T13:55:00', 'PERSISTENT'),
    (${assetAsBuiltId03}, '95657763-03'   , 'H-LeftTurningLight' , ${bpnCustomer1} , ${bpnCustomer1Name} , 'XH4713'            , 'DEU'                , 'Turning lights left'         , 'Left Turning Lights'     , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961303', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-01T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId04}, '95657763-04'   , 'H-RightTurningLight', ${bpnCustomer1} , ${bpnCustomer1Name} , 'XH4714'            , 'DEU'                , 'Turning lights right'        , 'Right Turning Lights'    , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961304', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-03T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId05}, '95657763-05'   , 'O-LeftHeadBulb'     , ${bpnOwn}       , ${bpnOwnName}       , 'LBH815'            , 'DEU'                , 'Head Light Bulb left'        , 'Osram Front Left-AX400'  , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961305', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-05T08:22:00', 'PERSISTENT'),
    (${assetAsBuiltId06}, '95657763-06'   , 'O-RightHeadBulb'    , ${bpnOwn}       , ${bpnOwnName}       , 'LBH816'            , 'DEU'                , 'Head Light Bulb right'       , 'Osram Front Right-AX400' , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961306', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-05T09:45:30', 'PERSISTENT'),
    (${assetAsBuiltId07}, '95657763-07'   , 'O-TurningLight'     , ${bpnOwn}       , ${bpnOwnName}       , 'OTLB1'             , 'DEU'                , 'Turning Light Bulb'          , 'Osram Turning Light Bulb', 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961307', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-01T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId08}, '95657763-08'   , 'O-BrakeLight'       , ${bpnOwn}       , ${bpnOwnName}       , 'OBLB1'             , 'DEU'                , 'Brake Light Bulb'            , 'Osram Brake Light Bulb'  , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961308', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-03T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId09}, '95657763-09'   , 'S-GlassBulb'        , ${bpnSupplier21}, ${bpnSupplier21Name}, 'SGB01'             , 'DEU'                , 'Glass bulb for turning light', 'Schott GBTL001'          , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961309', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-17T06:22:00', 'PERSISTENT'),
    (${assetAsBuiltId10}, '95657763-10'   , 'P-Packaging'        , ${bpnSupplier22}, ${bpnSupplier22Name}, 'PPCK01'            , 'DEU'                , 'Packaging for turning light' , 'Petersmeier K2367'       , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961310', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-19T13:50:25', 'PERSISTENT');
