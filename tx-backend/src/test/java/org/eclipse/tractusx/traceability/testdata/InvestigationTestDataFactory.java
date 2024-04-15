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

package org.eclipse.tractusx.traceability.testdata;

import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.notification.domain.base.model.Notification;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationAffectedPart;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationId;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationMessage;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationSeverity;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationSide;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationStatus;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class InvestigationTestDataFactory {
    public static Notification createInvestigationTestData(NotificationStatus investigationStatus, final String bpnString) {
        NotificationId investigationId = new NotificationId(1L);
        BPN bpn = new BPN(bpnString);
        NotificationSide investigationSide = NotificationSide.SENDER;
        String description = "Test Investigation";
        Instant createdAt = Instant.now();
        List<String> assetIds = List.of("asset123", "asset456");

        NotificationMessage notification = NotificationMessage.builder()
                .id("1")
                .notificationReferenceId("notificationId")
                .createdBy("senderBPN")
                .createdByName("senderManufacturerName")
                .sendTo("recipientBPN")
                .sendToName("receiverManufacturerName")
                .contractAgreementId("agreement")
                .description(description)
                .notificationStatus(investigationStatus)
                .affectedParts(List.of(new NotificationAffectedPart("part123")))
                .targetDate(Instant.now())
                .severity(NotificationSeverity.MINOR)
                .edcNotificationId("1")
                .messageId("messageId")
                .build();
        List<NotificationMessage> notifications = List.of(notification);

        return Notification.builder()
                .notificationId(investigationId)
                .bpn(bpn)
                .notificationStatus(investigationStatus)
                .notificationSide(investigationSide)
                .description(description)
                .notificationType(NotificationType.INVESTIGATION)
                .createdAt(createdAt)
                .assetIds(assetIds)
                .notifications(notifications)
                .build();
    }


    public static Notification createInvestigationTestDataWithNotificationList(NotificationStatus investigationStatus, String bpnString, List<NotificationMessage> notifications) {
        NotificationId investigationId = new NotificationId(1L);
        BPN bpn = new BPN(bpnString);
        NotificationSide investigationSide = NotificationSide.SENDER;
        String description = "Test Investigation";
        Instant createdAt = Instant.now();
        List<String> assetIds = List.of("asset123", "asset456");

        return Notification.builder()
                .notificationId(investigationId)
                .bpn(bpn)
                .notificationStatus(investigationStatus)
                .notificationSide(investigationSide)
                .description(description)
                .createdAt(createdAt)
                .assetIds(assetIds)
                .notifications(notifications)
                .build();
    }

    public static Notification createInvestigationTestData(NotificationStatus investigationStatus, NotificationStatus notificationInvestigationStatus) {
        NotificationId investigationId = new NotificationId(1L);
        BPN bpn = new BPN("bpn123");
        NotificationSide investigationSide = NotificationSide.SENDER;
        String description = "Test Investigation";
        Instant createdAt = Instant.now();
        List<String> assetIds = List.of("asset123", "asset456");

        NotificationMessage notification = NotificationMessage.builder()
                .id("1")
                .notificationReferenceId("notificationId")
                .createdBy("senderBPN")
                .createdByName("senderManufacturerName")
                .sendTo("recipientBPN")
                .sendToName("receiverManufacturerName")
                .contractAgreementId("agreement")
                .description(description)
                .notificationStatus(notificationInvestigationStatus)
                .type(NotificationType.INVESTIGATION)
                .affectedParts(List.of(new NotificationAffectedPart("part123")))
                .severity(NotificationSeverity.MINOR)
                .edcNotificationId("123")
                .messageId("messageId")
                .build();

        NotificationMessage notification2 = NotificationMessage.builder()
                .id("1")
                .notificationReferenceId("notificationId")
                .createdBy("senderBPN")
                .createdByName("senderManufacturerName")
                .sendTo("recipientBPN")
                .sendToName("receiverManufacturerName")
                .contractAgreementId("agreement")
                .description(description)
                .notificationStatus(NotificationStatus.SENT)
                .type(NotificationType.INVESTIGATION)
                .affectedParts(List.of(new NotificationAffectedPart("part123")))
                .severity(NotificationSeverity.MINOR)
                .edcNotificationId("123")
                .messageId("messageId")
                .build();
        List<NotificationMessage> notifications = new ArrayList<>();
        notifications.add(notification);
        notifications.add(notification2);

        return Notification.builder()
                .notificationId(investigationId)
                .bpn(bpn)
                .notificationStatus(investigationStatus)
                .notificationSide(investigationSide)
                .description(description)
                .createdAt(createdAt)
                .notificationType(NotificationType.INVESTIGATION)
                .assetIds(assetIds)
                .notifications(notifications)
                .build();
    }

    public static Notification createInvestigationTestData(NotificationSide investigationSide) {
        NotificationId investigationId = new NotificationId(1L);
        BPN bpn = new BPN("bpn123");
        String description = "Test Investigation";
        Instant createdAt = Instant.now();
        List<String> assetIds = List.of("asset123", "asset456");


        NotificationMessage notification = NotificationMessage.builder()
                .id("1")
                .notificationReferenceId("notificationId")
                .createdBy("senderBPN")
                .createdByName("senderManufacturerName")
                .sendTo("recipientBPN")
                .sendToName("receiverManufacturerName")
                .contractAgreementId("agreement")
                .description(description)
                .notificationStatus(NotificationStatus.ACKNOWLEDGED)
                .affectedParts(List.of(new NotificationAffectedPart("part123")))
                .severity(NotificationSeverity.MINOR)
                .edcNotificationId("123")
                .messageId("messageId")
                .build();
        List<NotificationMessage> notifications = List.of(notification);

        return Notification.builder()
                .notificationId(investigationId)
                .bpn(bpn)
                .notificationStatus(NotificationStatus.ACKNOWLEDGED)
                .notificationSide(investigationSide)
                .description(description)
                .createdAt(createdAt)
                .assetIds(assetIds)
                .notifications(notifications)
                .build();
    }

//    public static SearchCriteria createSearchCriteria() {
//        SearchCriteriaFilter searchCriteriaFilter = SearchCriteriaFilter.builder()
//                                                    .key("sendToName")
//                                                    .strategy(SearchStrategy.EQUAL)
//                                                    .value("receiverManufacturerName")
//                                                    .build();
//        SearchCriteria searchCriteria = SearchCriteria.builder()
//                                        .searchCriteriaFilterList(List.of(searchCriteriaFilter))
//                                        .searchCriteriaOperator(SearchCriteriaOperator.AND)
//                                        .build();
//
//        return searchCriteria;
//    }

    private static InvestigationEntity[] createSenderMajorityInvestigationEntitiesTestData(String senderBpn) {
        Instant now = Instant.now();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("First Investigation on Asset1")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("Second Investigation on Asset2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Third Investigation on Asset3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                .description("Fourth Investigation on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Fifth Investigation on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();

        InvestigationEntity[] InvestigationEntities = {firstInvestigation, secondInvestigation, thirdInvestigation, fourthInvestigation, fifthInvestigation};
        return InvestigationEntities;
    }

    public static InvestigationNotificationEntity[] createSenderMajorityInvestigationNotificationEntitiesTestData(String senderBpn) {
        String targetDateInNovString1 = "12:00 PM, Sun 11/9/2025";
        String targetDateInNovString2 = "12:00 PM, Mon 11/10/2025";
        String targetDateInDecString = "12:00 PM, Tue 12/9/2025";
        String dateFormatter = "hh:mm a, EEE M/d/uuuu";
        Instant targetDateInNov1 = LocalDateTime.parse(targetDateInNovString1, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant targetDateInNov2 = LocalDateTime.parse(targetDateInNovString2, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant targetDateInDec = LocalDateTime.parse(targetDateInDecString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();

        InvestigationEntity[] investigationEntities = createSenderMajorityInvestigationEntitiesTestData(senderBpn);

        InvestigationNotificationEntity[] investigationNotificationEntities = {
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(investigationEntities[0])
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov1)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("2")
                        .investigation(investigationEntities[1])
                        .status(NotificationStatusBaseEntity.SENT)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.CRITICAL)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("3")
                        .investigation(investigationEntities[2])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov2)
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("4")
                        .investigation(investigationEntities[3])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("5")
                        .investigation(investigationEntities[4])
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov1)
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        };

        return investigationNotificationEntities;
    }

    private static InvestigationEntity[] createReceiverMajorityInvestigationEntitiesTestData(String senderBpn) {
        Instant now = Instant.now();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.RECEIVED)
                .description("First Investigation on Asset1")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.minusSeconds(10L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                .description("Second Investigation on Asset2")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(21L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Third Investigation on Asset3")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now)
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.CLOSED)
                .description("Fourth Investigation on Asset4")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(20L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Fifth Investigation on Asset5")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(40L))
                .build();

        InvestigationEntity[] InvestigationEntities = {firstInvestigation, secondInvestigation, thirdInvestigation, fourthInvestigation, fifthInvestigation};
        return InvestigationEntities;
    }

    public static InvestigationNotificationEntity[] createReceiverMajorityInvestigationNotificationEntitiesTestData(String senderBpn) {
        String targetDateInNovString1 = "12:00 PM, Sun 11/9/2025";
        String targetDateInNovString2 = "12:00 PM, Mon 11/10/2025";
        String targetDateInDecString = "12:00 PM, Tue 12/9/2025";
        String dateFormatter = "hh:mm a, EEE M/d/uuuu";
        Instant targetDateInNov1 = LocalDateTime.parse(targetDateInNovString1, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant targetDateInNov2 = LocalDateTime.parse(targetDateInNovString2, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant targetDateInDec = LocalDateTime.parse(targetDateInDecString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();

        InvestigationEntity[] investigationEntities = createReceiverMajorityInvestigationEntitiesTestData(senderBpn);

        InvestigationNotificationEntity[] investigationNotificationEntities = {
                InvestigationNotificationEntity
                        .builder()
                        .id("1")
                        .investigation(investigationEntities[0])
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov1)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("2")
                        .investigation(investigationEntities[1])
                        .status(NotificationStatusBaseEntity.RECEIVED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.CRITICAL)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("3")
                        .investigation(investigationEntities[2])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov2)
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("4")
                        .investigation(investigationEntities[3])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("5")
                        .investigation(investigationEntities[4])
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov1)
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        };

        return investigationNotificationEntities;
    }

    public static InvestigationNotificationEntity[] createExtendedReceiverInvestigationNotificationEntitiesTestData(String senderBpn) {
        String targetDateInNovString = "12:00 PM, Sun 11/9/2025";
        String targetDateInDecString = "12:00 PM, Tue 12/9/2025";
        String dateFormatter = "hh:mm a, EEE M/d/uuuu";
        Instant targetDateInNov = LocalDateTime.parse(targetDateInNovString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant targetDateInDec = LocalDateTime.parse(targetDateInDecString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();

        InvestigationEntity[] investigationEntities = createExtendedReceiverInvestigationEntitiesTestData(senderBpn);

        InvestigationNotificationEntity[] investigationNotificationEntities = {
                InvestigationNotificationEntity
                        .builder()
                        .id("6")
                        .investigation(investigationEntities[0])
                        .status(NotificationStatusBaseEntity.RECEIVED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("7")
                        .investigation(investigationEntities[1])
                        .status(NotificationStatusBaseEntity.RECEIVED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.CRITICAL)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("8")
                        .investigation(investigationEntities[2])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("9")
                        .investigation(investigationEntities[3])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("10")
                        .investigation(investigationEntities[4])
                        .status(NotificationStatusBaseEntity.CANCELED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        };

        return investigationNotificationEntities;
    }

    private static InvestigationEntity[] createExtendedReceiverInvestigationEntitiesTestData(String senderBpn) {
        Instant now = Instant.now();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.RECEIVED)
                .description("Sixth Investigation on Asset1")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.minusSeconds(100L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.RECEIVED)
                .description("Seventh Investigation on Asset2")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(210L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Eighth Investigation on Asset3")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(1L))
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Ninth Investigation on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(25L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Tenth Investigation on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(80L))
                .build();

        return new InvestigationEntity[]{firstInvestigation, secondInvestigation, thirdInvestigation, fourthInvestigation, fifthInvestigation};
    }

    public static InvestigationNotificationEntity[] createExtendedSenderInvestigationNotificationEntitiesTestData(String senderBpn) {
        String targetDateInNovString = "12:00 PM, Sun 11/9/2025";
        String targetDateInDecString = "12:00 PM, Tue 12/9/2025";
        String dateFormatter = "hh:mm a, EEE M/d/uuuu";
        Instant targetDateInNov = LocalDateTime.parse(targetDateInNovString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant targetDateInDec = LocalDateTime.parse(targetDateInDecString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();

        InvestigationEntity[] investigationEntities = createExtendedSenderInvestigationEntitiesTestData(senderBpn);

        InvestigationNotificationEntity[] investigationNotificationEntities = {
                InvestigationNotificationEntity
                        .builder()
                        .id("6")
                        .investigation(investigationEntities[0])
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("7")
                        .investigation(investigationEntities[1])
                        .status(NotificationStatusBaseEntity.SENT)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.CRITICAL)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("8")
                        .investigation(investigationEntities[2])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("9")
                        .investigation(investigationEntities[3])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                InvestigationNotificationEntity
                        .builder()
                        .id("10")
                        .investigation(investigationEntities[4])
                        .status(NotificationStatusBaseEntity.CANCELED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        };

        return investigationNotificationEntities;
    }

    private static InvestigationEntity[] createExtendedSenderInvestigationEntitiesTestData(String senderBpn) {
        Instant now = Instant.now();

        InvestigationEntity firstInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                .description("Sixth Investigation on Asset1")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.minusSeconds(100L))
                .build();
        InvestigationEntity secondInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("Seventh Investigation on Asset2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(210L))
                .build();
        InvestigationEntity thirdInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Eighth Investigation on Asset3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(1L))
                .build();
        InvestigationEntity fourthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Ninth Investigation on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(25L))
                .build();
        InvestigationEntity fifthInvestigation = InvestigationEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Tenth Investigation on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(80L))
                .build();

        return new InvestigationEntity[]{firstInvestigation, secondInvestigation, thirdInvestigation, fourthInvestigation, fifthInvestigation};
    }
}
