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

package org.eclipse.tractusx.traceability.integration.qualitynotification.investigation;

import io.restassured.http.ContentType;
import org.eclipse.tractusx.traceability.integration.IntegrationTestSpecification;
import org.eclipse.tractusx.traceability.integration.common.support.BpnSupport;
import org.eclipse.tractusx.traceability.integration.common.support.InvestigationNotificationsSupport;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.investigation.model.InvestigationNotificationEntity;
import org.eclipse.tractusx.traceability.testdata.InvestigationTestDataFactory;
import org.hamcrest.Matchers;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.eclipse.tractusx.traceability.common.security.JwtRole.ADMIN;

class ReadCreatedInvestigationsInSortedOrderControllerIT extends IntegrationTestSpecification {

    @Autowired
    BpnSupport bpnSupport;
    @Autowired
    InvestigationNotificationsSupport investigationNotificationsSupport;

    @Test
    void givenSortByCreatedDateProvided_whenGetInvestigations_thenReturnInvestigationsProperlySorted() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String testBpn = bpnSupport.testBpn();

        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);

        // when/then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .param("sort", sortString)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created")
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.description",
                        Matchers.containsInRelativeOrder("Second Investigation on Asset2", "Fourth Investigation on Asset4",
                                "Third Investigation on Asset3", "First Investigation on Asset1"));
    }


    @Test
    void givenSortByDescriptionProvided_whenGetInvestigations_thenReturnInvestigationsProperlySorted() throws JoseException {
        // given
        String sortString = "description,desc";
        String testBpn = bpnSupport.testBpn();

        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .param("sort", sortString)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created")
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.description",
                        Matchers.containsInRelativeOrder("Third Investigation on Asset3", "Second Investigation on Asset2",
                                "Fourth Investigation on Asset4", "First Investigation on Asset1"));
    }

    @Test
    void givenSortByStatusProvided_whenGetInvestigations_thenReturnInvestigationsProperlySorted() throws JoseException {
        // given
        String sortString = "status,asc";
        String testBpn = bpnSupport.testBpn();

        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .param("sort", sortString)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created")
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.status", Matchers.containsInRelativeOrder( "CREATED", "SENT", "ACKNOWLEDGED", "ACCEPTED"));
    }

    @Test
    void givenSortBySeverityProvided_whenGetInvestigations_thenReturnInvestigationsProperlySorted() throws JoseException {
        // given
        String sortString = "severity,asc";
        String testBpn = bpnSupport.testBpn();

        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .param("sort", sortString)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created")
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.severity", Matchers.containsInRelativeOrder("MINOR", "MAJOR", "CRITICAL", "LIFE-THREATENING"));
    }

    @Test
    void givenInvalidSort_whenGetCreated_thenBadRequest() throws JoseException {
        // given
        String sortString = "createdDate,failure";

        // when/then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .param("sort", sortString)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created")
                .then()
                .statusCode(400)
                .body("message", Matchers.is(
                        "Invalid sort param provided sort=createdDate,failure expected format is following sort=parameter,order"
                ));
    }

    @Test
    void givenSortBySendToProvided_whenGetInvestigations_thenReturnInvestigationsProperlySorted() throws JoseException {
        // given
        String sortString = "sendTo,desc";
        String testBpn = bpnSupport.testBpn();

        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .param("sort", sortString)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created")
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.sendTo", Matchers.containsInRelativeOrder("BPNL000000000003", "BPNL000000000002", "BPNL000000000001", "BPNL000000000001"));
    }

    @Test
    void givenSortByTargetDateProvided_whenGetInvestigations_thenReturnInvestigationsProperlySorted() throws JoseException {
        // given
        String sortString = "targetDate,asc";
        String testBpn = bpnSupport.testBpn();

        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .param("sort", sortString)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created")
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.sendToName", Matchers.containsInRelativeOrder("OEM1", "OEM2", "OEM1", "OEM3"));
    }
}