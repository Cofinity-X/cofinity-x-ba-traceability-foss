-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_built
    (id                 , customer_part_id, id_short                    , manufacturer_id, manufacturer_name  , manufacturer_part_id, manufacturing_country, name_at_customer           , name_at_manufacturer       , quality_type, van , owner     , semantic_model_id            , semantic_data_model, classification, product_type, traction_battery_code, manufacturing_date                    , import_state)
values
    (${assetAsBuiltId01}, '95657764-01'   , 'SO-XenonLeftHeadLight'     , ${bpnOwn}      , ${bpnOwnName}      , 'SO4711'            , 'DEU'                , 'Xenon Headlight left'     , 'Xenon Left-Headlight'     , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961401', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId02}, '95657764-02'   , 'SO-XenonRightHeadLight'    , ${bpnOwn}      , ${bpnOwnName}      , 'SO4712'            , 'DEU'                , 'Xenon Headlight right'    , 'Xenon Right-Headlight'    , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233961402', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId03}, '95657764-03'   , 'LT-LeftHeadbulb'           , ${bpnSupplier1}, ${bpnSupplier1Name}, 'LT4713'            , 'DEU'                , 'Left Headbulb'            , 'Left Headbulb'            , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961403', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId04}, '95657764-04'   , 'LT-RightHeadbulb'          , ${bpnSupplier1}, ${bpnSupplier1Name}, 'LT4714'            , 'DEU'                , 'Right Headbulb'           , 'Right Headbulb'           , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961404', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId05}, '95657764-05'   , 'BS-LeftXenonGland'         , ${bpnSupplier2}, ${bpnSupplier2Name}, 'BS4715'            , 'DEU'                , 'Left Xenon Gland'         , 'Left Xenon Gland'         , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961405', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId06}, '95657764-06'   , 'BS-RightXenonGland'        , ${bpnSupplier2}, ${bpnSupplier2Name}, 'BS4716'            , 'DEU'                , 'Right Xenon Gland'        , 'Right Xenon Gland'        , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961406', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId07}, '95657764-07'   , 'CC-SmallElectricFuse'      , ${bpnSupplier3}, ${bpnSupplier3Name}, 'CC4717'            , 'DEU'                , 'Electric Fuse small'      , 'Electric Fuse small'      , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961407', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId08}, '95657764-08'   , 'CC-BigElectricFuse'        , ${bpnSupplier3}, ${bpnSupplier3Name}, 'CC4718'            , 'DEU'                , 'Electric Fuse big'        , 'Electric Fuse big'        , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961408', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId09}, '95657764-09'   , 'LT-SmallElectricController', ${bpnSupplier4}, ${bpnSupplier4Name}, 'LI4719'            , 'DEU'                , 'Electric Controller small', 'Electric Controller small', 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961409', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId10}, '95657764-10'   , 'LT-BigElectricController'  , ${bpnSupplier4}, ${bpnSupplier4Name}, 'LI4720'            , 'DEU'                , 'Electric Controller big'  , 'Electric Controller big'  , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961410', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId11}, '95657764-11'   , 'LL-RedBreakLED'            , ${bpnSupplier5}, ${bpnSupplier5Name}, 'LL4721'            , 'DEU'                , 'LED Red Break'            , 'LED Red Break'            , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961411', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId12}, '95657764-12'   , 'LL-OrangeTurnLED'          , ${bpnSupplier5}, ${bpnSupplier5Name}, 'LL4722'            , 'DEU'                , 'LED Orange Turn'          , 'LED Orange Turn'          , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961412', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId13}, '95657764-13'   , 'CF-TurnCaseOrange'         , ${bpnSupplier6}, ${bpnSupplier6Name}, 'CF4723'            , 'DEU'                , 'Case Orange Turn'         , 'Case Orange Turn'         , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961413', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId14}, '95657764-14'   , 'CF-BreakCaseRed'           , ${bpnSupplier6}, ${bpnSupplier6Name}, 'CF4724'            , 'DEU'                , 'Case Red Break'           , 'Case Red Break'           , 'OK'        , '--', 'SUPPLIER', 'NO-613963493493659233961414', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId15}, '95657764-15'   , 'SO-ControlUnitSmall'       , ${bpnOwn}      , ${bpnOwnName}      , 'SO4725'            , 'DEU'                , 'Control Unit small'       , 'Control Unit small'       , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233960415', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId16}, '95657764-16'   , 'SO-ControlUnitBig'         , ${bpnOwn}      , ${bpnOwnName}      , 'SO4726'            , 'DEU'                , 'Control Unit big'         , 'Control Unit big'         , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233960416', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId17}, '95657764-17'   , 'SO-TurnLight'              , ${bpnOwn}      , ${bpnOwnName}      , 'SO4727'            , 'DEU'                , 'Turn Light'               , 'Turn Light'               , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233960417', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId18}, '95657764-18'   , 'SO-BreakLight'             , ${bpnOwn}      , ${bpnOwnName}      , 'SO4728'            , 'DEU'                , 'Break Light'              , 'Break Light'              , 'OK'        , '--', 'OWN'     , 'NO-613963493493659233960418', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId19}, '95657764-19'   , 'MA-C4'                     , ${bpnCustomer1}, ${bpnCustomer1Name}, 'MA4729'            , 'DEU'                , 'C4'                       , 'C4'                       , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233961419', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId20}, '95657764-20'   , 'ME-S1'                     , ${bpnCustomer2}, ${bpnCustomer2Name}, 'ME4730'            , 'DEU'                , 'S1'                       , 'S1'                       , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233960420', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId21}, '95657764-21'   , 'ME-S2'                     , ${bpnCustomer2}, ${bpnCustomer2Name}, 'ME4731'            , 'DEU'                , 'S2'                       , 'S2'                       , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233960421', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT'),
    (${assetAsBuiltId22}, '95657764-22'   , 'MA-C5'                     , ${bpnCustomer2}, ${bpnCustomer2Name}, 'MA4732'            , 'DEU'                , 'C5'                       , 'C5'                       , 'OK'        , '--', 'CUSTOMER', 'NO-613963493493659233960422', 'SERIALPART'       , 'component'   , 'AS_BUILT'  , '--'                 , current_timestamp + interval '3 month', 'PERSISTENT');