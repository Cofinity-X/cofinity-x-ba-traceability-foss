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
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationSeverity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.investigation.model.InvestigationEntity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.investigation.model.InvestigationNotificationEntity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.model.NotificationSideBaseEntity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.model.NotificationStatusBaseEntity;
import org.hamcrest.Matchers;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.eclipse.tractusx.traceability.common.security.JwtRole.ADMIN;

class ReadCreatedInvestigationsWithSearchCriteriaControllerIT extends IntegrationTestSpecification {

    @Autowired
    BpnSupport bpnSupport;
    @Autowired
    InvestigationNotificationsSupport investigationNotificationsSupport;

    @Test
    void givenSortByCreatedDateFilterBySendToProvided_whenGetInvestigations_thenReturnCreatedInvestigationsProperlySortedFilteredBySendTo() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "sendTo,EQUAL,BPNL000000000001";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        investigationNotificationsSupport.storedNotifications(
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(firstInvestigation)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .investigation(secondInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .investigation(thirdInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .investigation(fourthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .investigation(fifthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(2))
                .body("totalItems", Matchers.is(2))
                .body("content.sendTo", Matchers.hasItems("BPNL000000000001"));
        ;
    }

    @Test
    void givenSortByCreatedDateFilterByCreatedDateProvided_whenGetInvestigations_thenReturnCreatedInvestigationsProperlySortedFilteredByCreatedDate() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "createdDate,AT_LOCAL_DATE,2023-11-09";
        String createdDateInNovString = "12:00 PM, Thu 11/9/2023";
        String createdDateInDecString = "12:00 PM, Sat 12/9/2023";
        String dateFormatter = "hh:mm a, EEE M/d/uuuu";
        Instant createdDateInNov = LocalDateTime.parse(createdDateInNovString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant createdDateInDec = LocalDateTime.parse(createdDateInDecString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        String testBpn = bpnSupport.testBpn();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(createdDateInNov)
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(createdDateInNov)
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(createdDateInNov)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(createdDateInDec)
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(createdDateInDec)
                .build();


        investigationNotificationsSupport.storedNotifications(
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(firstInvestigation)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .investigation(secondInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .investigation(thirdInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .investigation(fourthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .investigation(fifthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(3))
                .body("totalItems", Matchers.is(3));
    }

    @Test
    void givenSortByCreatedDateFilterBySendToNameProvided_whenGetInvestigations_thenReturnCreatedInvestigationsProperlySortedFilteredBySendToName() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "sendToName,EQUAL,OEM2";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        investigationNotificationsSupport.storedNotifications(
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(firstInvestigation)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .investigation(secondInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .investigation(thirdInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .sendToName("OEM2")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .investigation(fourthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .sendToName("OEM3")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .investigation(fifthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .sendToName("OEM4")
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(1))
                .body("totalItems", Matchers.is(1))
                .body("content.sendToName", Matchers.hasItems("OEM2"));
        ;
        ;
    }

    @Test
    void givenSortByCreatedDateFilterByStatusProvided_whenGetInvestigations_thenReturnCreatedInvestigationsProperlySortedFilteredByStatus() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "status,EQUAL,ACCEPTED";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        investigationNotificationsSupport.storedNotifications(
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(firstInvestigation)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .investigation(secondInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .investigation(thirdInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .sendToName("OEM2")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .investigation(fourthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .sendToName("OEM3")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .investigation(fifthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .sendToName("OEM4")
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(2))
                .body("totalItems", Matchers.is(2))
                .body("content.status", Matchers.hasItems("ACCEPTED"));
        ;
        ;
    }

    @Test
    void givenSortByCreatedDateFilterBySeverityProvided_whenGetInvestigations_thenReturnCreatedInvestigationsProperlySortedFilteredBySeverity() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "severity,EQUAL,3";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        investigationNotificationsSupport.storedNotifications(
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(firstInvestigation)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .investigation(secondInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .investigation(thirdInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .investigation(fourthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .investigation(fifthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(1))
                .body("totalItems", Matchers.is(1))
                .body("content.severity", Matchers.hasItems("LIFE-THREATENING"));
        ;
        ;
    }

    @Test
    void givenSortByCreatedDateFilterByCreatedByProvided_whenGetInvestigations_thenReturnCreatedInvestigationsProperlySortedFilteredByCreatedBy() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "createdBy,EQUAL,BPNL00000000000A";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        investigationNotificationsSupport.storedNotifications(
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(firstInvestigation)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .investigation(secondInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .investigation(thirdInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .investigation(fourthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .investigation(fifthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.createdBy", Matchers.hasItems("BPNL00000000000A"));
        ;
        ;
    }

    @Test
    void givenSortByCreatedDateFilterByDescriptionProvided_whenGetInvestigations_thenReturnCreatedInvestigationsProperlySortedFilteredByDescription() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "description,STARTS_WITH,First";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("First Investigation on Asset1")
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("Second Investigation on Asset2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Third Investigation on Asset3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Fourth Investigation on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Fifth Investigation on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        investigationNotificationsSupport.storedNotifications(
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(firstInvestigation)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .investigation(secondInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .investigation(thirdInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .investigation(fourthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .investigation(fifthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(1))
                .body("totalItems", Matchers.is(1))
                .body("content.description", Matchers.hasItems("First Investigation on Asset1"));
        ;
        ;
    }

    @Test
    void givenSortByCreatedDateFilterByDescriptionAndSendToProvided_whenGetInvestigations_thenReturnCreatedInvestigationsProperlySortedFilteredByDescriptionAndSendTo() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString1 = "description,STARTS_WITH,First";
        String filterString2 = "sendTo,EQUAL,BPNL000000000001";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("First Investigation on Asset1")
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("Second Investigation on Asset2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Third Investigation on Asset3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Fourth Investigation on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Fifth Investigation on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        investigationNotificationsSupport.storedNotifications(
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(firstInvestigation)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .investigation(secondInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .investigation(thirdInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .investigation(fourthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .investigation(fifthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created?page=0&size=10&sort=$sortString&filter=$filterString1&filter=$filterString2&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString1", filterString1)
                        .replace("$filterString2", filterString2))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(1))
                .body("totalItems", Matchers.is(1))
                .body("content.sendTo", Matchers.hasItems("BPNL000000000001"))
                .body("content.description", Matchers.hasItems("First Investigation on Asset1"));
    }

    @Test
    void givenSortByCreatedDateFilterBySendToNameOrSendToProvided_whenGetInvestigations_thenReturnCreatedInvestigationsProperlySortedFilteredBySendToNameOrSendTo() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString1 = "sendToName,EQUAL,OEM2";
        String filterString2 = "sendTo,EQUAL,BPNL000000000001";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("First Investigation on Asset1")
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("Second Investigation on Asset2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Third Investigation on Asset3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Fourth Investigation on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Fifth Investigation on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        investigationNotificationsSupport.storedNotifications(
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(firstInvestigation)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .investigation(secondInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .investigation(thirdInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .investigation(fourthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .investigation(fifthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/created?page=0&size=10&sort=$sortString&filter=$filterString1&filter=$filterString2&filterOperator=OR"
                        .replace("$sortString", sortString)
                        .replace("$filterString1", filterString1)
                        .replace("$filterString2", filterString2))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(3))
                .body("totalItems", Matchers.is(3))
                .body("content.sendTo", Matchers.hasItems("BPNL000000000001"))
                .body("content.sendToName", Matchers.hasItems("OEM2"));
    }

    @Test
    void givenSortByCreatedDateFilterBySendToProvided_whenGetInvestigations_thenReturnReceivedInvestigationsProperlySortedFilteredBySendTo() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "sendTo,EQUAL,BPNL000000000004";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        investigationNotificationsSupport.storedNotifications(
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(firstInvestigation)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .investigation(secondInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .investigation(thirdInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .investigation(fourthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .investigation(fifthInvestigation)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/investigations/received?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(1))
                .body("totalItems", Matchers.is(1))
                .body("content.sendTo", Matchers.hasItems("BPNL000000000004"));
        ;
    }
}
