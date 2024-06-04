-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https: //documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built
    (id                 , customer_part_id, id_short             , manufacturer_id, manufacturer_name  , manufacturer_part_id, manufacturing_country, name_at_customer        , name_at_manufacturer        , quality_type, van , owner     , semantic_model_id            , semantic_data_model, classification, product_type, manufacturing_date   , import_state)
values
    (${assetAsBuiltId01}, '95657761-01'   , 'H-LeftHeadLight'    , ${bpnOwn}      , ${bpnOwnName}      , 'XH4711'            , 'DEU'                , 'Xenon Headlights left' , 'Xenon Left-Headlights'     , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961201', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-04T13:48:54', 'PERSISTENT'),
    (${assetAsBuiltId02}, '95657761-02'   , 'H-RightHeadLight'   , ${bpnOwn}      , ${bpnOwnName}      , 'XH4712'            , 'DEU'                , 'Xenon Headlights right', 'Xenon Right-Headlights'    , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961202', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-04T13:55:00', 'PERSISTENT'),
    (${assetAsBuiltId03}, '95657761-03'   , 'O-LeftHeadBulb'     , ${bpnSupplier1}, ${bpnSupplier1Name}, 'LBH815'            , 'DEU'                , 'Head Light Bulb left'  , 'Osram Front Left-AX400'    , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961203', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-05T08:22:00', 'PERSISTENT'),
    (${assetAsBuiltId04}, '95657761-04'   , 'O-RightHeadBulb'    , ${bpnSupplier1}, ${bpnSupplier1Name}, 'LBH816'            , 'DEU'                , 'Head Light Bulb right' , 'Osram Front Right-AX400'   , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961204', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-05T09:45:30', 'PERSISTENT'),
    (${assetAsBuiltId05}, '95657761-05'   , 'P-LeftHeadBulb'     , ${bpnSupplier2}, ${bpnSupplier2Name}, 'D3HPHL'            , 'DEU'                , 'Head Light Bulb left'  , 'Philips Front Left-D3H'    , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961205', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-17T06:22:00', 'PERSISTENT'),
    (${assetAsBuiltId06}, '95657761-06'   , 'P-RightHeadBulb'    , ${bpnSupplier2}, ${bpnSupplier2Name}, 'D3HPHR'            , 'DEU'                , 'Head Light Bulb right' , 'Philips Front Right-D3H'   , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961206', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-19T13:50:25', 'PERSISTENT'),
    (${assetAsBuiltId07}, '95657761-07'   , 'BMW-Z1'             , ${bpnCustomer1}, ${bpnCustomer1Name}, 'Z1ABC'             , 'DEU'                , 'BMW Z1'                , 'Z1'                        , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961207', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-07-30T15:00:00', 'PERSISTENT'),
    (${assetAsBuiltId08}, '95657761-08'   , 'BMW-Z4'             , ${bpnCustomer1}, ${bpnCustomer1Name}, 'Z4ABC'             , 'DEU'                , 'BMW Z3'                , 'Z3'                        , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961208', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-07-31T00:00:00', 'PERSISTENT'),
    (${assetAsBuiltId09}, '95657761-09'   , 'Audi-A7'            , ${bpnCustomer2}, ${bpnCustomer2Name}, 'A7XXX'             , 'DEU'                , 'Audi A7'               , 'A7'                        , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961209', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-08-10T10:15:00', 'PERSISTENT'),
    (${assetAsBuiltId10}, '95657761-10'   , 'Audi-A8'            , ${bpnCustomer2}, ${bpnCustomer2Name}, 'A8XXX'             , 'DEU'                , 'Audi A8'               , 'A8'                        , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961210', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-08-10T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId11}, '95657761-11'   , 'H-LeftTurningLight' , ${bpnOwn}      , ${bpnOwnName}      , 'XH4713'            , 'DEU'                , 'Turning lights left'   , 'Left Turning Lights'       , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961211', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-01T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId12}, '95657761-12'   , 'H-RightTurningLight', ${bpnOwn}      , ${bpnOwnName}      , 'XH4714'            , 'DEU'                , 'Turning lights right'  , 'Right Turning Lights'      , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961212', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-03T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId13}, '95657761-13'   , 'H-LeftBrakeLight'   , ${bpnOwn}      , ${bpnOwnName}      , 'XH4715'            , 'DEU'                , 'Brake lights left'     , 'Left Brake Lights'         , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961213', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-05T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId14}, '95657761-14'   , 'H-RightBrakeLight'  , ${bpnOwn}      , ${bpnOwnName}      , 'XH4716'            , 'DEU'                , 'Brake lights right'    , 'Right Brake Lights'        , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961214', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-07T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId15}, '95657761-15'   , 'H-LeftFogLight'     , ${bpnOwn}      , ${bpnOwnName}      , 'XH4717'            , 'DEU'                , 'Fog lights left'       , 'Left Fog Lights'           , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961215', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-02T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId16}, '95657761-16'   , 'H-RightFogLight'    , ${bpnOwn}      , ${bpnOwnName}      , 'XH4718'            , 'DEU'                , 'Fog lights right'      , 'Right Fog Lights'          , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961216', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-04T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId17}, '95657761-17'   , 'H-LeftHighBeam'     , ${bpnOwn}      , ${bpnOwnName}      , 'XH4719'            , 'DEU'                , 'High beam left'        , 'Left High Beam'            , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961217', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-06T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId18}, '95657761-18'   , 'H-RightHighBeam'    , ${bpnOwn}      , ${bpnOwnName}      , 'XH4720'            , 'DEU'                , 'High beam right'       , 'Right High Beam'           , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961218', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-03-08T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId19}, '95657761-19'   , 'O-TurningLight'     , ${bpnSupplier1}, ${bpnSupplier1Name}, 'OTLB1'             , 'DEU'                , 'Turning Light Bulb'    , 'Osram Turning Light Bulb'  , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961219', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-01T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId20}, '95657761-20'   , 'O-BrakeLight'       , ${bpnSupplier1}, ${bpnSupplier1Name}, 'OBLB1'             , 'DEU'                , 'Brake Light Bulb'      , 'Osram Brake Light Bulb'    , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961220', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-03T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId21}, '95657761-21'   , 'P-TurningLight'     , ${bpnSupplier2}, ${bpnSupplier2Name}, 'PTLB1'             , 'DEU'                , 'Turning Light Bulb'    , 'Philips Turning Light Bulb', 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961221', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-07T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId22}, '95657761-22'   , 'P-BrakeLight'       , ${bpnSupplier2}, ${bpnSupplier2Name}, 'PBLB1'             , 'DEU'                , 'Brake Light Bulb'      , 'Philips Brake Light Bulb'  , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961222', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-02T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId23}, '95657761-23'   , 'W-FogLight'         , ${bpnSupplier3}, ${bpnSupplier3Name}, 'WFLB1'             , 'DEU'                , 'Fog Light Bulb'        , 'Würth Fog Light Bulb'      , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961223', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-04T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId24}, '95657761-24'   , 'W-HighBeam'         , ${bpnSupplier3}, ${bpnSupplier3Name}, 'WHBB1'             , 'DEU'                , 'High Beam Bulb'        , 'Würth High Beam Bulb'      , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961224', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-02-06T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId25}, '95657761-25'   , 'BMW-X1'             , ${bpnCustomer1}, ${bpnCustomer1Name}, 'X1ABC'             , 'DEU'                , 'BMW X1'                , 'X1'                        , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961225', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-08-02T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId26}, '95657761-26'   , 'BMW-X3'             , ${bpnCustomer1}, ${bpnCustomer1Name}, 'X3ABC'             , 'DEU'                , 'BMW X3'                , 'X3'                        , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961226', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-08-03T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId27}, '95657761-27'   , 'Audi-Q7'            , ${bpnCustomer2}, ${bpnCustomer2Name}, 'Q7XXX'             , 'DEU'                , 'Audi Q7'               , 'Q7'                        , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961227', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-08-04T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId28}, '95657761-28'   , 'Audi-Q8'            , ${bpnCustomer2}, ${bpnCustomer2Name}, 'Q8XXX'             , 'DEU'                , 'Audi Q8'               , 'Q8'                        , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961228', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-08-05T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId29}, '95657761-29'   , 'VW-ID.4'            , ${bpnCustomer3}, ${bpnCustomer3Name}, 'ID4YZ'             , 'DEU'                , 'VW ID.4'               , 'ID.4'                      , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961229', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-08-06T05:30:00', 'PERSISTENT'),
    (${assetAsBuiltId30}, '95657761-30'   , 'VW-ID.5'            , ${bpnCustomer3}, ${bpnCustomer3Name}, 'ID5YZ'             , 'DEU'                , 'VW ID.5'               , 'ID.5'                      , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961230', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '2022-08-07T05:30:00', 'PERSISTENT');
