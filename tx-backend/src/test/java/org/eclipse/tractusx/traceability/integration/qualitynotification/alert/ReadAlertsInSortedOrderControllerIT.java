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
import org.eclipse.tractusx.traceability.integration.common.support.AlertsSupport;
import org.eclipse.tractusx.traceability.integration.common.support.BpnSupport;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationSeverity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.alert.model.AlertEntity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.alert.model.AlertNotificationEntity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.model.NotificationSideBaseEntity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.model.NotificationStatusBaseEntity;
import org.eclipse.tractusx.traceability.testdata.AlertTestDataFactory;
import org.hamcrest.Matchers;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.eclipse.tractusx.traceability.common.security.JwtRole.ADMIN;
import static org.eclipse.tractusx.traceability.integration.common.support.ISO8601DateTimeMatcher.isIso8601DateTime;

class ReadAlertsInSortedOrderControllerIT extends IntegrationTestSpecification {

    @Autowired
    AlertsSupport alertsSupport;

    @Autowired
    AlertNotificationsSupport alertNotificationsSupport;

    @Autowired
    BpnSupport bpnSupport;

    @Test
    void givenAlerts_whenGetAlerts_thenReturnSortedByCreationTime() throws JoseException {
        // given
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
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .alert(fifthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build()
        );

        // when/then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/created")
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4));
    }

    @Test
    void givenSortByCreatedDateProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // given
        String sortString = "createdDate,desc";
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
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .alert(fifthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/created?page=0&size=10&sort=$sortString".replace("$sortString", sortString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4));
    }

    @Test
    void givenSortByDescriptionProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // given
        String sortString = "description,desc";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
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
                .status(NotificationStatusBaseEntity.CLOSED)
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
                .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                .description("5")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(40L))
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .alert(fifthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/created?page=0&size=10&sort=$sortString".replace("$sortString", sortString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(5))
                .body("totalItems", Matchers.is(5))
                .body("content.description", Matchers.containsInRelativeOrder("5", "4", "3", "2", "1"));
    }

    @Test
    void givenSortByStatusProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // given
        String sortString = "status,asc";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.SENT)
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
                .status(NotificationStatusBaseEntity.CLOSED)
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
                .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                .description("5")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(40L))
                .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .alert(fifthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/created?page=0&size=10&sort=$sortString".replace("$sortString", sortString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(5))
                .body("totalItems", Matchers.is(5))
                .body("content.status", Matchers.containsInRelativeOrder("CREATED", "CREATED", "SENT", "ACKNOWLEDGED", "CLOSED"));
    }

    @Test
    void givenSortBySeverityProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // given
        String sortString = "severity,asc";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                                            .assets(Collections.emptyList())
                                            .bpn(testBpn)
                                            .status(NotificationStatusBaseEntity.SENT)
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
                                            .status(NotificationStatusBaseEntity.CLOSED)
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
                                            .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                                            .description("5")
                                            .side(NotificationSideBaseEntity.SENDER)
                                            .createdDate(now.plusSeconds(40L))
                                            .build();


        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .severity(QualityNotificationSeverity.CRITICAL)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .severity(QualityNotificationSeverity.MAJOR)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("2")
                        .alert(secondAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("3")
                        .alert(thirdAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .severity(QualityNotificationSeverity.MINOR)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("4")
                        .alert(fourthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .severity(QualityNotificationSeverity.CRITICAL)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .id("5")
                        .alert(fifthAlert)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build()
        );

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/created?page=0&size=10&sort=$sortString".replace("$sortString", sortString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(5))
                .body("totalItems", Matchers.is(5))
                .body("content.severity", Matchers.containsInRelativeOrder("MINOR", "MAJOR", "CRITICAL", "CRITICAL", "LIFE-THREATENING"));
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
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/created?page=0&size=10&sort=$sortString".replace("$sortString", sortString))
                .then()
                .statusCode(400)
                .body("message", Matchers.is(
                        "Invalid sort param provided sort=createdDate,failure expected format is following sort=parameter,order"
                ));
    }

    @Test
    void shouldReturnPagedCreatedAlerts() throws JoseException {
        // given
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        IntStream.range(1, 101)
                .forEach(
                        number -> {
                            alertsSupport.storedAlert(
                                    AlertEntity.builder()
                                            .assets(Collections.emptyList())
                                            .bpn(testBpn)
                                            .status(NotificationStatusBaseEntity.CREATED)
                                            .side(NotificationSideBaseEntity.SENDER)
                                            .createdDate(now)
                                            .build()
                            );
                        }
                );

        // when/then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "2")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/created")
                .then()
                .statusCode(200)
                .body("page", Matchers.is(2))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(10))
                .body("totalItems", Matchers.is(100));
    }

    @Test
    void shouldReturnProperlyPagedReceivedAlerts() throws JoseException {
        // given
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();
        String senderBPN = "BPN0001";
        String senderName = "Sender name";
        String receiverBPN = "BPN0002";
        String receiverName = "Receiver name";

        IntStream.range(101, 201)
                .forEach(number ->
                        {
                            AlertEntity alertEntity = AlertEntity.builder()
                                    .assets(Collections.emptyList())
                                    .bpn(testBpn)
                                    .status(NotificationStatusBaseEntity.CREATED)
                                    .side(NotificationSideBaseEntity.RECEIVER)
                                    .createdDate(now)
                                    .build();

                            AlertEntity alert = alertsSupport.storedAlertFullObject(alertEntity);

                            AlertNotificationEntity notificationEntity = AlertNotificationEntity
                                    .builder()
                                    .id(UUID.randomUUID().toString())
                                    .alert(alert)
                                    .createdBy(senderBPN)
                                    .status(NotificationStatusBaseEntity.CREATED)
                                    .createdByName(senderName)
                                    .sendTo(receiverBPN)
                                    .sendToName(receiverName)
                                    .messageId("messageId")
                                    .build();

                            AlertNotificationEntity persistedNotification = alertNotificationsSupport.storedAlertNotification(notificationEntity);
                            persistedNotification.setAlert(alert);
                            alertNotificationsSupport.storedAlertNotification(persistedNotification);
                        }
                );

        // when/then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "2")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/received")
                .then()
                .statusCode(200)
                .body("content.createdBy", Matchers.hasItems(senderBPN))
                .body("content.createdByName", Matchers.hasItems(senderName))
                .body("content.sendTo", Matchers.hasItems(receiverBPN))
                .body("content.sendToName", Matchers.hasItems(receiverName))
                .body("page", Matchers.is(2))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(10))
                .body("totalItems", Matchers.is(100));
    }

    @Test
    void shouldReturnReceivedAlertsSortedByCreationTime() throws JoseException {
        // given
        String sortString = "createdDate,desc";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.RECEIVED)
                .side(NotificationSideBaseEntity.RECEIVER)
                .description("1")
                .createdDate(now.minusSeconds(5L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.RECEIVED)
                .description("2")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(2L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.RECEIVED)
                .description("3")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.RECEIVED)
                .description("4")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(testBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("5")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(40L))
                .build();

        alertNotificationsSupport.storedAlertNotifications(
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(firstAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("2")
                        .alert(secondAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("3")
                        .alert(thirdAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("4")
                        .alert(fourthAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("5")
                        .alert(fifthAlert)
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .build()
        );

        // then
        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/received?sort=$sortString".replace("$sortString", sortString))
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.description", Matchers.containsInRelativeOrder("4", "2", "3", "1"))
                .body("content.createdDate", Matchers.hasItems(isIso8601DateTime()));
    }

    @Test
    void givenSortBySendToProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // given
        String sortString = "sendTo,desc";
        String testBpn = bpnSupport.testBpn();

        AlertNotificationEntity[] alertNotificationEntities = AlertTestDataFactory.createAlertNotificationEntitiesTestData(testBpn);
        alertNotificationsSupport.storedAlertNotifications(alertNotificationEntities);

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .param("sort", sortString)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/created")
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.sendTo", Matchers.containsInRelativeOrder("BPNL000000000003", "BPNL000000000002", "BPNL000000000001", "BPNL000000000001"));
    }

    @Test
    void givenSortByTargetDateProvided_whenGetAlerts_thenReturnAlertsProperlySorted() throws JoseException {
        // given
        String sortString = "targetDate,asc";
        Instant now = Instant.now();
        String testBpn = bpnSupport.testBpn();

        AlertNotificationEntity[] alertNotificationEntities = AlertTestDataFactory.createAlertNotificationEntitiesTestData(testBpn);
        alertNotificationsSupport.storedAlertNotifications(alertNotificationEntities);

        given()
                .header(oAuth2Support.jwtAuthorization(ADMIN))
                .param("page", "0")
                .param("size", "10")
                .param("sort", sortString)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/alerts/created")
                .then()
                .statusCode(200)
                .body("page", Matchers.is(0))
                .body("pageSize", Matchers.is(10))
                .body("content", Matchers.hasSize(4))
                .body("totalItems", Matchers.is(4))
                .body("content.sendToName", Matchers.containsInRelativeOrder("OEM1", "OEM2", "OEM1", "OEM3"));
    }
}
