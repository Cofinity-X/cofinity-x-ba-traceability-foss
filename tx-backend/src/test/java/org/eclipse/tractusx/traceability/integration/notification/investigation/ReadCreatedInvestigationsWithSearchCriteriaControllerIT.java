///********************************************************************************
// * Copyright (c) 2023 Contributors to the Eclipse Foundation
// *
// * See the NOTICE file(s) distributed with this work for additional
// * information regarding copyright ownership.
// *
// * This program and the accompanying materials are made available under the
// * terms of the Apache License, Version 2.0 which is available at
// * https://www.apache.org/licenses/LICENSE-2.0.
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and limitations
// * under the License.
// *
// * SPDX-License-Identifier: Apache-2.0
// ********************************************************************************/
//
//package org.eclipse.tractusx.traceability.integration.qualitynotification.investigation;
//
//import io.restassured.http.ContentType;
//import org.eclipse.tractusx.traceability.common.request.OwnPageable;
//import org.eclipse.tractusx.traceability.common.request.PageableFilterRequest;
//import org.eclipse.tractusx.traceability.common.request.SearchCriteriaRequestParam;
//import org.eclipse.tractusx.traceability.integration.IntegrationTestSpecification;
//import org.eclipse.tractusx.traceability.integration.common.support.BpnSupport;
//import org.eclipse.tractusx.traceability.integration.common.support.InvestigationNotificationsSupport;
//import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.investigation.model.InvestigationNotificationEntity;
//import org.eclipse.tractusx.traceability.testdata.InvestigationTestDataFactory;
//import org.hamcrest.Matchers;
//import org.jose4j.lang.JoseException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.text.SimpleDateFormat;
//import java.time.Instant;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Stream;
//
//import static io.restassured.RestAssured.given;
//import static org.eclipse.tractusx.traceability.common.security.JwtRole.ADMIN;
//import static org.hamcrest.Matchers.equalTo;
//
//class ReadCreatedInvestigationsWithSearchCriteriaControllerIT extends IntegrationTestSpecification {
//
//    @Autowired
//    BpnSupport bpnSupport;
//    @Autowired
//    InvestigationNotificationsSupport investigationNotificationsSupport;
//
//    @Test
//    void givenFilterBySendToProvided_whenGetInvestigations_thenReturnCreatedInvestigationsFilteredBySendTo() throws JoseException {
//       // Given
//        String filterString = "channel,EQUAL,SENDER,AND";
//        String filter = "sendTo,EQUAL,BPNL000000000001,AND";
//        String testBpn = bpnSupport.testBpn();
//
//        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
//        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);
//
//        // Then
//        given()
//                .header(oAuth2Support.jwtAuthorization(ADMIN))
//                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString, filter))))
//                .contentType(ContentType.JSON)
//                .when()
//                .post("/api/investigations/filter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("page", Matchers.is(0))
//                .body("pageSize", Matchers.is(10))
//                .body("content", Matchers.hasSize(2))
//                .body("totalItems", Matchers.is(2))
//                .body("content.sendTo", Matchers.hasItems("BPNL000000000001"));
//    }
//
//    @Test
//    void givenFilterByCreatedDateProvided_whenGetInvestigations_thenReturnCreatedInvestigationsFilteredByCreatedDate() throws JoseException {
//       // Given
//        String filterString = "channel,EQUAL,SENDER,AND";
//        Date myDate = Date.from(Instant.now());
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = formatter.format(myDate);
//        String filter = "createdDate,AT_LOCAL_DATE," + formattedDate + ",AND";
//        String testBpn = bpnSupport.testBpn();
//
//        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
//        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);
//
//        // Then
//        given()
//                .header(oAuth2Support.jwtAuthorization(ADMIN))
//                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString, filter))))
//                .contentType(ContentType.JSON)
//                .when()
//                .post("/api/investigations/filter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("page", Matchers.is(0))
//                .body("pageSize", Matchers.is(10))
//                .body("content", Matchers.hasSize(4))
//                .body("totalItems", Matchers.is(4));
//    }
//
//    @Test
//    void givenFilterBySendToNameProvided_whenGetInvestigations_thenReturnCreatedInvestigationsFilteredBySendToName() throws JoseException {
//       // Given
//        String filterString = "channel,EQUAL,SENDER,AND";
//        String filter = "sendToName,EQUAL,OEM2,AND";
//        String testBpn = bpnSupport.testBpn();
//
//        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
//        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);
//
//        // Then
//        given()
//                .header(oAuth2Support.jwtAuthorization(ADMIN))
//                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString, filter))))
//                .contentType(ContentType.JSON)
//                .when()
//                .post("/api/investigations/filter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("page", Matchers.is(0))
//                .body("pageSize", Matchers.is(10))
//                .body("content", Matchers.hasSize(1))
//                .body("totalItems", Matchers.is(1))
//                .body("content.sendToName", Matchers.hasItems("OEM2"));
//    }
//
//    @Test
//    void givenFilterByStatusProvided_whenGetInvestigations_thenReturnCreatedInvestigationsFilteredByStatus() throws JoseException {
//       // Given
//        String filterString = "channel,EQUAL,SENDER,AND";
//        String filter = "status,EQUAL,ACCEPTED,AND";
//        String testBpn = bpnSupport.testBpn();
//
//        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
//        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);
//
//        // Then
//        given()
//                .header(oAuth2Support.jwtAuthorization(ADMIN))
//                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString, filter))))
//                .contentType(ContentType.JSON)
//                .when()
//                .post("/api/investigations/filter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("page", Matchers.is(0))
//                .body("pageSize", Matchers.is(10))
//                .body("content", Matchers.hasSize(1))
//                .body("totalItems", Matchers.is(1))
//                .body("content.status", Matchers.hasItems("ACCEPTED"));
//    }
//
//    @Test
//    void givenFilterBySeverityProvided_whenGetInvestigations_thenReturnCreatedInvestigationsFilteredBySeverity() throws JoseException {
//       // Given
//        String filterString = "channel,EQUAL,SENDER,AND";
//        String filter = "severity,EQUAL,LIFE_THREATENING,AND";
//        String testBpn = bpnSupport.testBpn();
//
//        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
//        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);
//
//        // Then
//        given()
//                .header(oAuth2Support.jwtAuthorization(ADMIN))
//                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString, filter))))
//                .contentType(ContentType.JSON)
//                .when()
//                .post("/api/investigations/filter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("page", Matchers.is(0))
//                .body("pageSize", Matchers.is(10))
//                .body("content", Matchers.hasSize(1))
//                .body("totalItems", Matchers.is(1))
//                .body("content.severity", Matchers.hasItems("LIFE-THREATENING"));
//    }
//
//    @Test
//    void givenFilterByCreatedByProvided_whenGetInvestigations_thenReturnCreatedInvestigationsFilteredByCreatedBy() throws JoseException {
//       // Given
//        String filterString = "channel,EQUAL,SENDER,AND";
//        String filter = "createdBy,EQUAL,BPNL00000000000A,AND";
//        String testBpn = bpnSupport.testBpn();
//
//        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
//        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);
//
//        // Then
//        given()
//                .header(oAuth2Support.jwtAuthorization(ADMIN))
//                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString, filter))))
//                .contentType(ContentType.JSON)
//                .when()
//                .post("/api/investigations/filter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("page", Matchers.is(0))
//                .body("pageSize", Matchers.is(10))
//                .body("content", Matchers.hasSize(4))
//                .body("totalItems", Matchers.is(4))
//                .body("content.createdBy", Matchers.hasItems("BPNL00000000000A"));
//    }
//
//    @Test
//    void givenFilterByDescriptionProvided_whenGetInvestigations_thenReturnCreatedInvestigationsFilteredByDescription() throws JoseException {
//       // Given
//        String filterString = "channel,EQUAL,SENDER,AND";
//        String filter = "description,STARTS_WITH,First,AND";
//        String testBpn = bpnSupport.testBpn();
//
//        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
//        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);
//
//        // Then
//        given()
//                .header(oAuth2Support.jwtAuthorization(ADMIN))
//                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString, filter))))
//                .contentType(ContentType.JSON)
//                .when()
//                .post("/api/investigations/filter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("page", Matchers.is(0))
//                .body("pageSize", Matchers.is(10))
//                .body("content", Matchers.hasSize(1))
//                .body("totalItems", Matchers.is(1))
//                .body("content.description", Matchers.hasItems("First Investigation on Asset1"));
//    }
//
//    @Test
//    void givenFilterByDescriptionAndSendToProvided_whenGetInvestigations_thenReturnCreatedInvestigationsFilteredByDescriptionAndSendTo() throws JoseException {
//       // Given
//        String filterString = "channel,EQUAL,SENDER,AND";
//        String filterString1 = "description,STARTS_WITH,First,AND";
//        String filterString2 = "sendTo,EQUAL,BPNL000000000001,AND";
//        String testBpn = bpnSupport.testBpn();
//
//        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
//        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);
//
//        // Then
//        given()
//                .header(oAuth2Support.jwtAuthorization(ADMIN))
//                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString, filterString1, filterString2))))
//                .contentType(ContentType.JSON)
//                .when()
//                .post("/api/investigations/filter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("page", Matchers.is(0))
//                .body("pageSize", Matchers.is(10))
//                .body("content", Matchers.hasSize(1))
//                .body("totalItems", Matchers.is(1))
//                .body("content.sendTo", Matchers.hasItems("BPNL000000000001"))
//                .body("content.description", Matchers.hasItems("First Investigation on Asset1"));
//    }
//
//    @Test
//    void givenFilterBySendToNameOrSendToProvided_whenGetInvestigations_thenReturnCreatedInvestigationsFilteredBySendToNameOrSendTo() throws JoseException {
//       // Given
//        String filterString = "channel,EQUAL,SENDER,AND";
//        String filterString1 = "sendToName,EQUAL,OEM2,OR";
//        String filterString2 = "sendTo,EQUAL,BPNL000000000001,OR";
//        String testBpn = bpnSupport.testBpn();
//
//        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
//        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);
//
//        // Then
//        given()
//                .header(oAuth2Support.jwtAuthorization(ADMIN))
//                .body(new PageableFilterRequest(new OwnPageable(0, 10, List.of()), new SearchCriteriaRequestParam(List.of(filterString, filterString1, filterString2))))
//                .contentType(ContentType.JSON)
//                .when()
//                .post("/api/investigations/filter")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("page", Matchers.is(0))
//                .body("pageSize", Matchers.is(10))
//                .body("content", Matchers.hasSize(3))
//                .body("totalItems", Matchers.is(3))
//                .body("content.sendTo", Matchers.hasItems("BPNL000000000001"))
//                .body("content.sendToName", Matchers.hasItems("OEM2"));
//    }
//
//    private static Stream<Arguments> filterArguments() {
//        return Stream.of(
//                Arguments.of(
//                        "description", "first", "SENDER"
//                ),
//                Arguments.of(
//                        "description", "First", "SENDER"
//                ),
//                Arguments.of(
//                        "description", "FIRST", "SENDER"
//                )
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("filterArguments")
//    void testIfFilteringIsCaseInsensitive(String fieldName,
//                                          String startWith,
//                                          String channel) throws JoseException {
//        String testBpn = bpnSupport.testBpn();
//
//        InvestigationNotificationEntity[] investigationNotificationEntities = InvestigationTestDataFactory.createSenderMajorityInvestigationNotificationEntitiesTestData(testBpn);
//        investigationNotificationsSupport.storedNotifications(investigationNotificationEntities);
//
//        // Then
//        given()
//                .header(oAuth2Support.jwtAuthorization(ADMIN))
//                .contentType(ContentType.JSON)
//                .log().all()
//                .when()
//                .param("fieldName", fieldName)
//                .param("size", 50)
//                .param("startWith", startWith)
//                .param("channel", channel)
//                .get("/api/investigations/distinctFilterValues")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .assertThat()
//                .body(".", Matchers.containsInRelativeOrder(List.of("First Investigation on Asset1").toArray()));
//    }
//}
