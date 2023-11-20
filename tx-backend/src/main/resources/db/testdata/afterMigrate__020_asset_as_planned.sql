-- this script is executed by flyway as part of a lifecycle hook after all migrations have been applied
-- (see https://documentation.red-gate.com/fd/callback-concept-184127466.html).
-- it is only intended for usage in local or test environments

insert into assets_as_planned
    (id                   , id_short          , manufacturer_part_id, name_at_manufacturer     , quality_type, classification, owner     , semantic_data_model, function    , manufacturer_name, van , semantic_model_id          , catenax_site_id                                , function_valid_from  , function_valid_until , validity_period_from , validity_period_to)
values
    (${assetAsPlannedId01}, 'H-LeftTailLight' , 'XT2309'            , 'Xenon Left-Taillights'  , 'OK'        , 'C-Level'     , 'OWN'     , 'PARTASPLANNED'       , 'production', 'Hella'          , '--', '5739525733343254979259249', 'urn:uuid:bd7b648a-29eb-46ba-8da2-3221463bf5e2', '2019-03-04T10:00:00', '2025-03-04T10:00:00', '2022-02-04T08:00:00', '2024-02-04T08:00:00'),
    (${assetAsPlannedId02}, 'H-RightTailLight', 'XT2310'            , 'Xenon Right-Taillights' , 'OK'        , 'C-Level'     , 'OWN'     , 'PARTASPLANNED'       , 'production', 'Hella'          , '--', '3555795495432474727732252', 'urn:uuid:12c75a3b-b207-4415-b93a-3e0d1b3b145e', '2019-03-04T16:00:00', '2025-03-04T16:00:00', '2022-02-04T08:00:00', '2024-02-04T08:00:00'),
    (${assetAsPlannedId03}, 'O-LeftTailBulb'  , 'LBT910'            , 'Osram Rear Left-ZX500'  , 'OK'        , 'C-Level'     , 'SUPPLIER', 'PARTASPLANNED'       , 'production', 'Osram'          , '--', '7557973754595993573779235', 'urn:uuid:f4280b3f-01d3-4259-a3ee-b51b8748a0e3', '2019-02-05T10:00:00', '2025-02-05T10:00:00', '2022-02-04T08:00:00', '2024-02-04T08:00:00'),
    (${assetAsPlannedId04}, 'O-RightTailBulb' , 'LBT920'            , 'Osram Rear Right-ZX500' , 'OK'        , 'C-Level'     , 'SUPPLIER', 'PARTASPLANNED'       , 'production', 'Osram'          , '--', '7724524552224773742557429', 'urn:uuid:9751d1c3-a092-4e51-9ce2-c3353be307ff', '2019-02-05T16:00:00', '2025-02-05T10:00:00', '2022-02-04T08:00:00', '2024-02-04T08:00:00'),
    (${assetAsPlannedId05}, 'P-LeftTailBulb'  , 'C4RPTL'            , 'Phillips Rear Left-C4R' , 'OK'        , 'C-Level'     , 'SUPPLIER', 'PARTASPLANNED'       , 'production', 'Phillips'       , '--', '7259534753592972592444239', 'urn:uuid:141499f8-5762-44dc-8d7f-0f598ff94208', '2019-02-08T10:00:00', '2025-02-08T10:00:00', '2022-02-04T08:00:00', '2024-02-04T08:00:00'),
    (${assetAsPlannedId06}, 'P-RightTailBulb' , 'C4RPTR'            , 'Phillips Rear Right-C4R', 'OK'        , 'C-Level'     , 'SUPPLIER', 'PARTASPLANNED'       , 'production', 'Phillips'       , '--', '4922952344449439397957339', 'urn:uuid:f8629534-ae9e-4350-8c0f-6874f88fca4e', '2019-02-08T10:00:00', '2025-02-08T10:00:00', '2022-02-04T08:00:00', '2024-02-04T08:00:00'),
    (${assetAsPlannedId07}, 'BMW-Z1'          , 'Z1ABC'             , 'Z1'                     , 'OK'        , 'C-Level'     , 'CUSTOMER', 'PARTASPLANNED'       , 'production', 'BMW AG'         , '--', '7922333444399397355743927', 'urn:uuid:7368dd7a-f442-4124-b31f-ae48b67e966a', '2019-07-30T10:00:00', '2025-07-30T10:00:00', '2022-02-04T08:00:00', '2024-02-04T08:00:00'),
    (${assetAsPlannedId08}, 'BMW-Z4'          , 'Z4ABC'             , 'Z3'                     , 'OK'        , 'C-Level'     , 'CUSTOMER', 'PARTASPLANNED'       , 'production', 'BMW AG'         , '--', '4943443393237449573773535', 'urn:uuid:47c8f2d5-472d-4a24-8772-b388603ef1b1', '2019-07-31T10:00:00', '2025-07-31T10:00:00', '2022-02-04T08:00:00', '2024-02-04T08:00:00'),
    (${assetAsPlannedId09}, 'Audi-A7'         , 'A7XXX'             , 'A7'                     , 'OK'        , 'C-Level'     , 'CUSTOMER', 'PARTASPLANNED'       , 'production', 'Audi AG'        , '--', '9923399774344455937394339', 'urn:uuid:bf23b459-d3cc-445e-8839-3fe7f49d8cc5', '2019-08-10T10:00:00', '2025-08-10T10:00:00', '2022-02-04T08:00:00', '2024-02-04T08:00:00'),
    (${assetAsPlannedId10}, 'Audi-A8'         , 'A8XXX'             , 'A8'                     , 'OK'        , 'C-Level'     , 'CUSTOMER', 'PARTASPLANNED'       , 'production', 'Audi AG'        , '--', '5422724375732744449977947', 'urn:uuid:28ddd434-fabc-4e82-bfe9-d2aab5d64251', '2019-08-10T10:00:00', '2025-08-10T10:00:00', '2022-02-04T08:00:00', '2024-02-04T08:00:00');
