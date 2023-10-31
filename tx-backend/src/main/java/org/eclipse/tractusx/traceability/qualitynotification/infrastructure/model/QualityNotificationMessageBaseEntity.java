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
package org.eclipse.tractusx.traceability.qualitynotification.infrastructure.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationSeverity;
import org.hibernate.annotations.Formula;

import java.time.Instant;
import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@Data
@MappedSuperclass
public class QualityNotificationMessageBaseEntity {
    @Id
    private String id;
    private String createdBy;
    private String createdByName;
    private String sendTo;
    private String sendToName;
    private String edcUrl;
    private String contractAgreementId;
    private String notificationReferenceId;
    private Instant targetDate;
    private QualityNotificationSeverity severity;
    private String edcNotificationId;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String messageId;
    private Boolean isInitial;

    @Formula("case status "
            + "when 'CREATED' then 0 "
            + "when 'SENT' then 1 "
            + "when 'RECEIVED' then 2 "
            + "when 'ACKNOWLEDGED' then 3 "
            + "when 'CANCELED' then 4 "
            + "when 'ACCEPTED' then 5 "
            + "when 'DECLINED' then 6 "
            + "when 'CLOSED' then 7 "
            + "else -1 "
            + "end")
    private Integer statusrank;

    private NotificationStatusBaseEntity status;

    @PreUpdate
    public void preUpdate() {
        this.updated = LocalDateTime.now();
    }
}
