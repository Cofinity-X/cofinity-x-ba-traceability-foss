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

import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationSeverity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.alert.model.AlertEntity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.alert.model.AlertNotificationEntity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.model.NotificationSideBaseEntity;
import org.eclipse.tractusx.traceability.qualitynotification.infrastructure.model.NotificationStatusBaseEntity;

import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class AlertTestDataFactory {

    private static AlertEntity[] createSenderMajorityAlertEntitiesTestData(String senderBpn) {
        Instant now = Instant.now();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.CREATED)
                .description("First Alert on Asset1")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.minusSeconds(10L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("Second Alert on Asset2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(21L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Third Alert on Asset3")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Fourth Alert on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Fifth Alert on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(40L))
                .build();

        AlertEntity[] alertEntities = {firstAlert, secondAlert, thirdAlert, fourthAlert, fifthAlert};
        return alertEntities;
    }

    public static AlertNotificationEntity[] createSenderMajorityAlertNotificationEntitiesTestData(String senderBpn) {
        String targetDateInNovString = "12:00 PM, Sun 11/9/2025";
        String targetDateInDecString = "12:00 PM, Tue 12/9/2025";
        String dateFormatter = "hh:mm a, EEE M/d/uuuu";
        Instant targetDateInNov = LocalDateTime.parse(targetDateInNovString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant targetDateInDec = LocalDateTime.parse(targetDateInDecString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();

        AlertEntity[] alertEntities = createSenderMajorityAlertEntitiesTestData(senderBpn);

        AlertNotificationEntity[] alertNotificationEntities = {
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(alertEntities[0])
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("2")
                        .alert(alertEntities[1])
                        .status(NotificationStatusBaseEntity.SENT)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.CRITICAL)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("3")
                        .alert(alertEntities[2])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("4")
                        .alert(alertEntities[3])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("5")
                        .alert(alertEntities[4])
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        };

        return alertNotificationEntities;
    }

    private static AlertEntity[] createReceiverMajorityAlertEntitiesTestData(String senderBpn) {
        Instant now = Instant.now();

        AlertEntity firstAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                .description("First Alert on Asset1")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.minusSeconds(10L))
                .build();
        AlertEntity secondAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.RECEIVED)
                .description("Second Alert on Asset2")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(21L))
                .build();
        AlertEntity thirdAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Third Alert on Asset3")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now)
                .build();
        AlertEntity fourthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Fourth Alert on Asset4")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(20L))
                .build();
        AlertEntity fifthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(senderBpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Fifth Alert on Asset5")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(40L))
                .build();

        AlertEntity[] alertEntities = {firstAlert, secondAlert, thirdAlert, fourthAlert, fifthAlert};
        return alertEntities;
    }

    public static AlertNotificationEntity[] createReceiverMajorityAlertNotificationEntitiesTestData(String senderBpn) {
        String targetDateInNovString = "12:00 PM, Sun 11/9/2025";
        String targetDateInDecString = "12:00 PM, Tue 12/9/2025";
        String dateFormatter = "hh:mm a, EEE M/d/uuuu";
        Instant targetDateInNov = LocalDateTime.parse(targetDateInNovString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant targetDateInDec = LocalDateTime.parse(targetDateInDecString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();

        AlertEntity[] alertEntities = createReceiverMajorityAlertEntitiesTestData(senderBpn);

        AlertNotificationEntity[] alertNotificationEntities = {
                AlertNotificationEntity
                        .builder()
                        .id("1")
                        .alert(alertEntities[0])
                        .status(NotificationStatusBaseEntity.CREATED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("2")
                        .alert(alertEntities[1])
                        .status(NotificationStatusBaseEntity.SENT)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.CRITICAL)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("3")
                        .alert(alertEntities[2])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("4")
                        .alert(alertEntities[3])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("5")
                        .alert(alertEntities[4])
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        };

        return alertNotificationEntities;
    }

    public static AlertNotificationEntity[] createExtendedReceiverAlertNotificationEntitiesTestData(String bpn) {
        String targetDateInNovString = "12:00 PM, Sun 11/9/2025";
        String targetDateInDecString = "12:00 PM, Tue 12/9/2025";
        String dateFormatter = "hh:mm a, EEE M/d/uuuu";
        Instant targetDateInNov = LocalDateTime.parse(targetDateInNovString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant targetDateInDec = LocalDateTime.parse(targetDateInDecString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();

        AlertEntity[] alertEntities = createExtendedReceiverAlertEntitiesTestData(bpn);

        AlertNotificationEntity[] alertNotificationEntities = createReceiverMajorityAlertNotificationEntitiesTestData(bpn);

        AlertNotificationEntity[] newAlertNotificationEntities = {
                AlertNotificationEntity
                        .builder()
                        .id("6")
                        .alert(alertEntities[5])
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("7")
                        .alert(alertEntities[6])
                        .status(NotificationStatusBaseEntity.RECEIVED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.CRITICAL)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("8")
                        .alert(alertEntities[7])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("9")
                        .alert(alertEntities[8])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("10")
                        .alert(alertEntities[9])
                        .status(NotificationStatusBaseEntity.CANCELED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        };

        return concatWithStream(alertNotificationEntities, newAlertNotificationEntities);
    }

    private static AlertEntity[] createExtendedReceiverAlertEntitiesTestData(String bpn) {
        AlertEntity[] alertEntities = createReceiverMajorityAlertEntitiesTestData(bpn);

        Instant now = Instant.now();

        AlertEntity sixthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(bpn)
                .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                .description("Sixth Alert on Asset1")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.minusSeconds(100L))
                .build();
        AlertEntity seventhAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(bpn)
                .status(NotificationStatusBaseEntity.RECEIVED)
                .description("Seventh Alert on Asset2")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(210L))
                .build();
        AlertEntity eighthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(bpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Eighth Alert on Asset3")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(1L))
                .build();
        AlertEntity ninthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(bpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Ninth Alert on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(25L))
                .build();
        AlertEntity tenthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(bpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Tenth Alert on Asset5")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(80L))
                .build();


        AlertEntity[] newAlerts = {sixthAlert, seventhAlert, eighthAlert, ninthAlert, tenthAlert};
        return concatWithStream(alertEntities, newAlerts);
    }

    public static AlertNotificationEntity[] createExtendedSenderAlertNotificationEntitiesTestData(String bpn) {
        String targetDateInNovString = "12:00 PM, Sun 11/9/2025";
        String targetDateInDecString = "12:00 PM, Tue 12/9/2025";
        String dateFormatter = "hh:mm a, EEE M/d/uuuu";
        Instant targetDateInNov = LocalDateTime.parse(targetDateInNovString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();
        Instant targetDateInDec = LocalDateTime.parse(targetDateInDecString, DateTimeFormatter.ofPattern(dateFormatter, Locale.US))
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();

        AlertEntity[] alertEntities = createExtendedSenderAlertEntitiesTestData(bpn);

        AlertNotificationEntity[] alertNotificationEntities = createSenderMajorityAlertNotificationEntitiesTestData(bpn);

        AlertNotificationEntity[] newAlertNotificationEntities = {
                AlertNotificationEntity
                        .builder()
                        .id("6")
                        .alert(alertEntities[5])
                        .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.MAJOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("7")
                        .alert(alertEntities[6])
                        .status(NotificationStatusBaseEntity.SENT)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000001")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM1")
                        .severity(QualityNotificationSeverity.CRITICAL)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("8")
                        .alert(alertEntities[7])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000002")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM2")
                        .severity(QualityNotificationSeverity.LIFE_THREATENING)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("9")
                        .alert(alertEntities[8])
                        .status(NotificationStatusBaseEntity.ACCEPTED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000003")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInDec)
                        .sendToName("OEM3")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build(),
                AlertNotificationEntity
                        .builder()
                        .id("10")
                        .alert(alertEntities[9])
                        .status(NotificationStatusBaseEntity.CANCELED)
                        .edcNotificationId("cda2d956-fa91-4a75-bb4a-8e5ba39b268a")
                        .sendTo("BPNL000000000004")
                        .createdBy("BPNL00000000000A")
                        .targetDate(targetDateInNov)
                        .sendToName("OEM4")
                        .severity(QualityNotificationSeverity.MINOR)
                        .build()
        };

        return concatWithStream(alertNotificationEntities, newAlertNotificationEntities);
    }

    private static AlertEntity[] createExtendedSenderAlertEntitiesTestData(String bpn) {
        AlertEntity[] alertEntities = createSenderMajorityAlertEntitiesTestData(bpn);

        Instant now = Instant.now();

        AlertEntity sixthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(bpn)
                .status(NotificationStatusBaseEntity.ACKNOWLEDGED)
                .description("Sixth Alert on Asset1")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.minusSeconds(100L))
                .build();
        AlertEntity seventhAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(bpn)
                .status(NotificationStatusBaseEntity.SENT)
                .description("Seventh Alert on Asset2")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(210L))
                .build();
        AlertEntity eighthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(bpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Eighth Alert on Asset3")
                .side(NotificationSideBaseEntity.RECEIVER)
                .createdDate(now.plusSeconds(1L))
                .build();
        AlertEntity ninthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(bpn)
                .status(NotificationStatusBaseEntity.ACCEPTED)
                .description("Ninth Alert on Asset4")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(25L))
                .build();
        AlertEntity tenthAlert = AlertEntity.builder()
                .assets(Collections.emptyList())
                .bpn(bpn)
                .status(NotificationStatusBaseEntity.CANCELED)
                .description("Tenth Alert on Asset5")
                .side(NotificationSideBaseEntity.SENDER)
                .createdDate(now.plusSeconds(80L))
                .build();


        AlertEntity[] newAlerts = {sixthAlert, seventhAlert, eighthAlert, ninthAlert, tenthAlert};
        return concatWithStream(alertEntities, newAlerts);
    }
    @SuppressWarnings("unchecked")
    private static <T> T[] concatWithStream(T[] array1, T[] array2) {
        return Stream.concat(Arrays.stream(array1), Arrays.stream(array2))
                .toArray(size -> (T[]) Array.newInstance(array1.getClass().getComponentType(), size));
    }
}
