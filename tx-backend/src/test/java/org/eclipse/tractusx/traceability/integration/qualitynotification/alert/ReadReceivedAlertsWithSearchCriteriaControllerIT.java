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

package org.eclipse.tractusx.traceability.integration.qualitynotification.alert;

import io.restassured.http.ContentType;
import org.eclipse.tractusx.traceability.integration.IntegrationTestSpecification;
import org.eclipse.tractusx.traceability.integration.common.support.AlertNotificationsSupport;
import org.eclipse.tractusx.traceability.integration.common.support.BpnSupport;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationSeverity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.alert.model.AlertEntity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.alert.model.AlertNotificationEntity;
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

class ReadReceivedAlertsWithSearchCriteriaControllerIT extends IntegrationTestSpecification {

    @Autowired
    AlertNotificationsSupport alertNotificationsSupport;

    @Autowired
    BpnSupport bpnSupport;

    @Test
    void givenSortByCreatedDateFilterBySendToProvided_whenGetAlerts_thenReturnReceivedAlertsProperlySortedFilteredBySendTo() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "sendTo,EQUAL,BPNL000000000004";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .alert(fifthAlert)
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
                .get("/api/alerts/received?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
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

    @Test
    void givenSortByCreatedDateFilterByCreatedDateProvided_whenGetAlerts_thenReturnReceivedAlertsProperlySortedFilteredByCreatedDate() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "createdDate,AT_LOCAL_DATE,2023-12-09";
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

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(createdDateInNov)
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(createdDateInNov)
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(createdDateInNov)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("4")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(createdDateInDec)
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(createdDateInDec)
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .alert(fifthAlert)
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
                .get("/api/alerts/received?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(2))
                .body("totalItems", Matchers.is(2));
    }

    @Test
    void givenSortByCreatedDateFilterBySendToNameProvided_whenGetAlerts_thenReturnReceivedAlertsProperlySortedFilteredBySendToName() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "sendToName,EQUAL,OEM3";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("4")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .sendToName("OEM2")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .sendToName("OEM3")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .alert(fifthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .sendToName("OEM3")
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/received?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(2))
                .body("totalItems", Matchers.is(2))
                .body("content.sendToName", Matchers.hasItems("OEM3"));
        ;
        ;
    }

    @Test
    void givenSortByCreatedDateFilterByStatusProvided_whenGetAlerts_thenReturnReceivedAlertsProperlySortedFilteredByStatus() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "status,EQUAL,CANCELED";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .sendToName("OEM2")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .sendToName("OEM3")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CANCELED)
                        .id("5")
                        .alert(fifthAlert)
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
                .get("/api/alerts/received?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(1))
                .body("totalItems", Matchers.is(1))
                .body("content.status", Matchers.hasItems("CANCELED"));
        ;
        ;
    }

    @Test
    void givenSortByCreatedDateFilterBySeverityProvided_whenGetAlerts_thenReturnReceivedAlertsProperlySortedFilteredBySeverity() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "severity,EQUAL,0";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("4")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .alert(fifthAlert)
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
                .get("/api/alerts/received?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(2))
                .body("totalItems", Matchers.is(2))
                .body("content.severity", Matchers.hasItems("MINOR"));
        ;
        ;
    }

    @Test
    void givenSortByCreatedDateFilterByCreatedByProvided_whenGetAlerts_thenReturnReceivedAlertsProperlySortedFilteredByCreatedBy() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "createdBy,EQUAL,BPNL00000000000A";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("1")
                .createdDate(now.minusSeconds(10L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .alert(fifthAlert)
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
                .get("/api/alerts/received?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(1))
                .body("totalItems", Matchers.is(1))
                .body("content.createdBy", Matchers.hasItems("BPNL00000000000A"));
        ;
        ;
    }

    @Test
    void givenSortByCreatedDateFilterByDescriptionProvided_whenGetAlerts_thenReturnReceivedAlertsProperlySortedFilteredByDescription() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString = "description,STARTS_WITH,Fifth";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("First Alert on Asset1")
                .createdDate(now.minusSeconds(10L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("Second Alert on Asset2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Third Alert on Asset3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Fourth Alert on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Fifth Alert on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .alert(fifthAlert)
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
                .get("/api/alerts/received?page=0&size=10&sort=$sortString&filter=$filterString&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString", filterString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(1))
                .body("totalItems", Matchers.is(1))
                .body("content.description", Matchers.hasItems("Fifth Alert on Asset5"));
        ;
        ;
    }

    @Test
    void givenSortByCreatedDateFilterByDescriptionAndSendToProvided_whenGetAlerts_thenReturnReceivedAlertsProperlySortedFilteredByDescriptionAndSendTo() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString1 = "description,STARTS_WITH,Fifth";
        String filterString2 = "sendTo,EQUAL,BPNL000000000004";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("First Alert on Asset1")
                .createdDate(now.minusSeconds(10L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("Second Alert on Asset2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Third Alert on Asset3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Fourth Alert on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Fifth Alert on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .alert(fifthAlert)
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
                .get("/api/alerts/received?page=0&size=10&sort=$sortString&filter=$filterString1&filter=$filterString2&filterOperator=AND"
                        .replace("$sortString", sortString)
                        .replace("$filterString1", filterString1)
                        .replace("$filterString2", filterString2))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(1))
                .body("totalItems", Matchers.is(1))
                .body("content.sendTo", Matchers.hasItems("BPNL000000000004"))
                .body("content.description", Matchers.hasItems("Fifth Alert on Asset5"));
    }

    @Test
    void givenSortByCreatedDateFilterBySendToNameOrSendToProvided_whenGetAlerts_thenReturnReceivedAlertsProperlySortedFilteredBySendToNameOrSendTo() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        String filterString1 = "sendToName,EQUAL,OEM3";
        String filterString2 = "sendTo,EQUAL,BPNL000000000004";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .side(NotificationSideBaseEntity.SENDER)
                .description("First Alert on Asset1")
                .createdDate(now.minusSeconds(10L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("Second Alert on Asset2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Third Alert on Asset3")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Fourth Alert on Asset4")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Fifth Alert on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.SENT)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .id("5")
                        .alert(fifthAlert)
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
                .get("/api/alerts/received?page=0&size=10&sort=$sortString&filter=$filterString1&filter=$filterString2&filterOperator=OR"
                        .replace("$sortString", sortString)
                        .replace("$filterString1", filterString1)
                        .replace("$filterString2", filterString2))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(2))
                .body("totalItems", Matchers.is(2))
                .body("content.sendTo", Matchers.hasItems("BPNL000000000004"))
                .body("content.sendToName", Matchers.hasItems("OEM3"));
    }
}
