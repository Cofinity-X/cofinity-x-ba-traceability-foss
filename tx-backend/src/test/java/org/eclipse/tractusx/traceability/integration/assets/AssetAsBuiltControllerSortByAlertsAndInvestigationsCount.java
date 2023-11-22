/********************************************************************************
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/
package org.eclipse.tractusx.traceability.integration.assets;

import io.restassured.http.ContentType;
import org.eclipse.tractusx.traceability.assets.infrastructure.asbuilt.model.AssetAsBuiltEntity;
import org.eclipse.tractusx.traceability.assets.infrastructure.asbuilt.repository.JpaAssetAsBuiltRepository;
import org.eclipse.tractusx.traceability.integration.IntegrationTestSpecification;
import org.eclipse.tractusx.traceability.integration.common.support.AlertsSupport;
import org.eclipse.tractusx.traceability.integration.common.support.AssetsSupport;
import org.eclipse.tractusx.traceability.integration.common.support.InvestigationsSupport;
import org.hamcrest.Matchers;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.eclipse.tractusx.traceability.common.security.JwtRole.ADMIN;
import static org.eclipse.tractusx.traceability.qualitynotification.infrastructure.model.NotificationStatusBaseEntity.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AssetAsBuiltControllerSortByAlertsAndInvestigationsCount extends IntegrationTestSpecification {

    @Autowired
    AssetsSupport assetsSupport;


    @Autowired
    JpaAssetAsBuiltRepository jpaAssetAsBuiltRepository;

    @Autowired
    AlertsSupport alertsSupport;

    @Autowired
    InvestigationsSupport investigationsSupport;


    private static Stream<Arguments> requests() {
        return Stream.of(
                arguments(Map.of("qualityType", "NOT_EXISTING_QUALITY_TYPE"), "Failed to deserialize request body."),
                arguments(Map.of("qualityType", "'CRITICAL'"), "Failed to deserialize request body."),
                arguments(Map.of("qualityType", ""), "Failed to deserialize request body."),
                arguments(Map.of("qualityType", " "), "Failed to deserialize request body.")
        );
    }

    @Test
    void givenAlertsForAsset_whenCallWithSortByQualityAlertsInStatusActiveDesc_thenReturnAssetsWithActiveAlertsCountInDesc() throws JoseException {
        // Given
        assetsSupport.defaultMultipleAssetsAsBuiltStored();
        AssetAsBuiltEntity assetAsBuilt1 = jpaAssetAsBuiltRepository.findById("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2bb").orElseThrow();
        alertsSupport.storeAlertWithStatusAndAssets(CREATED, List.of(assetAsBuilt1), null);
        alertsSupport.storeAlertWithStatusAndAssets(SENT, List.of(assetAsBuilt1), null);
        alertsSupport.storeAlertWithStatusAndAssets(RECEIVED, List.of(assetAsBuilt1), null);
        alertsSupport.storeAlertWithStatusAndAssets(CANCELED, List.of(assetAsBuilt1), null);
        alertsSupport.storeAlertWithStatusAndAssets(CLOSED, List.of(assetAsBuilt1), null);

        AssetAsBuiltEntity assetAsBuilt3 = jpaAssetAsBuiltRepository.findById("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2cc").orElseThrow();
        alertsSupport.storeAlertWithStatusAndAssets(CREATED, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(SENT, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(RECEIVED, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(ACKNOWLEDGED, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(ACCEPTED, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(DECLINED, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(CANCELED, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(CLOSED, List.of(assetAsBuilt3), null);

        // When
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "50")
                .param("filter", "owner,EQUAL,OWN")
                .param("filterOperator", "AND")
                .param("sort", "qualityAlertsInStatusActive,desc")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/assets/as-built")
                .then()
                .log().all()
                .statusCode(200)
                .body("content.qualityAlertsInStatusActive", Matchers.containsInRelativeOrder(6,3));
    }

    @Test
    void givenAlertsForAsset_whenCallWithSortByQualityAlertsInStatusActiveAsc_thenReturnAssetsWithActiveAlertsCountInAsc() throws JoseException {
        // Given
        assetsSupport.defaultMultipleAssetsAsBuiltStored();
        AssetAsBuiltEntity assetAsBuilt1 = jpaAssetAsBuiltRepository.findById("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2bb").orElseThrow();
        alertsSupport.storeAlertWithStatusAndAssets(CREATED, List.of(assetAsBuilt1), null);
        alertsSupport.storeAlertWithStatusAndAssets(SENT, List.of(assetAsBuilt1), null);
        alertsSupport.storeAlertWithStatusAndAssets(RECEIVED, List.of(assetAsBuilt1), null);
        alertsSupport.storeAlertWithStatusAndAssets(CANCELED, List.of(assetAsBuilt1), null);
        alertsSupport.storeAlertWithStatusAndAssets(CLOSED, List.of(assetAsBuilt1), null);

        AssetAsBuiltEntity assetAsBuilt3 = jpaAssetAsBuiltRepository.findById("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2cc").orElseThrow();
        alertsSupport.storeAlertWithStatusAndAssets(CREATED, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(SENT, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(RECEIVED, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(ACKNOWLEDGED, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(CANCELED, List.of(assetAsBuilt3), null);
        alertsSupport.storeAlertWithStatusAndAssets(CLOSED, List.of(assetAsBuilt3), null);

        // When
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "50")
                .param("filter", "owner,EQUAL,OWN")
                .param("filterOperator", "AND")
                .param("sort", "qualityAlertsInStatusActive,asc")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/assets/as-built")
                .then()
                .log().all()
                .statusCode(200)
                .body("content.qualityAlertsInStatusActive", Matchers.containsInRelativeOrder(3,4));
    }

    @Test
    void givenInvestigationsForAsset_whenCallWithSortByQualityInvestigationsInStatusActiveDesc_thenReturnAssetsWithActiveInvestigationsCountInDesc() throws JoseException {
        // Given
        assetsSupport.defaultMultipleAssetsAsBuiltStored();
        AssetAsBuiltEntity assetAsBuilt1 = jpaAssetAsBuiltRepository.findById("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2bb").orElseThrow();
        investigationsSupport.storeInvestigationWithStatusAndAssets(CREATED, List.of(assetAsBuilt1), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(SENT, List.of(assetAsBuilt1), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(RECEIVED, List.of(assetAsBuilt1), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(CANCELED, List.of(assetAsBuilt1), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(CLOSED, List.of(assetAsBuilt1), null);

        AssetAsBuiltEntity assetAsBuilt2 = jpaAssetAsBuiltRepository.findById("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2cc").orElseThrow();
        investigationsSupport.storeInvestigationWithStatusAndAssets(CREATED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(SENT, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(RECEIVED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(ACKNOWLEDGED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(ACCEPTED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(DECLINED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(CANCELED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(CLOSED, List.of(assetAsBuilt2), null);

        // When
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "50")
                .param("filter", "owner,EQUAL,OWN")
                .param("filterOperator", "AND")
                .param("sort", "qualityInvestigationsInStatusActive,desc")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/assets/as-built")
                .then()
                .statusCode(200)
                .body("content.qualityInvestigationsInStatusActive", Matchers.containsInRelativeOrder(6,3));
    }

    @Test
    void givenInvestigationsForAsset_whenCallWithSortByQualityInvestigationsInStatusActiveAsc_thenReturnAssetsWithActiveInvestigationsCountInAsc() throws JoseException {
        // Given
        assetsSupport.defaultMultipleAssetsAsBuiltStored();
        AssetAsBuiltEntity assetAsBuilt1 = jpaAssetAsBuiltRepository.findById("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2bb").orElseThrow();
        investigationsSupport.storeInvestigationWithStatusAndAssets(CREATED, List.of(assetAsBuilt1), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(SENT, List.of(assetAsBuilt1), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(RECEIVED, List.of(assetAsBuilt1), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(CANCELED, List.of(assetAsBuilt1), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(CLOSED, List.of(assetAsBuilt1), null);

        AssetAsBuiltEntity assetAsBuilt2 = jpaAssetAsBuiltRepository.findById("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2cc").orElseThrow();
        investigationsSupport.storeInvestigationWithStatusAndAssets(CREATED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(SENT, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(RECEIVED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(ACKNOWLEDGED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(ACCEPTED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(DECLINED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(CANCELED, List.of(assetAsBuilt2), null);
        investigationsSupport.storeInvestigationWithStatusAndAssets(CLOSED, List.of(assetAsBuilt2), null);

        // When
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "50")
                .param("filter", "owner,EQUAL,OWN")
                .param("filterOperator", "AND")
                .param("sort", "qualityInvestigationsInStatusActive,asc")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/assets/as-built")
                .then()
                .statusCode(200)
                .body("content.qualityInvestigationsInStatusActive", Matchers.containsInRelativeOrder(3,6));
    }
}
