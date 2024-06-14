/********************************************************************************
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.traceability.notification.application.notification.mapper;

import lombok.experimental.UtilityClass;
import org.eclipse.tractusx.traceability.common.model.PageResult;
import org.eclipse.tractusx.traceability.notification.domain.base.model.Notification;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationMessage;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationSeverity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import notification.response.NotificationReasonResponse;
import notification.response.NotificationResponse;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.eclipse.tractusx.traceability.notification.application.notification.mapper.NotificationMessageMapper.fromNotifications;

@UtilityClass
public class NotificationResponseMapper {

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse
                .builder()
                .id(notification.getNotificationId().value())
                .status(NotificationMessageMapper.from(notification.getNotificationStatus()))
                .description(notification.getDescription())
                .createdBy(getSenderBPN(notification.getNotifications()))
                .createdByName(getSenderName(notification.getNotifications()))
                .createdDate(notification.getCreatedAt().toString())
                .assetIds(Collections.unmodifiableList(notification.getAffectedPartIds()))
                .channel(NotificationMessageMapper.from(notification.getNotificationSide()))
                .type(NotificationMessageMapper.from(notification.getNotificationType()))
                .title(notification.getTitle())
                .reason(new NotificationReasonResponse(
                        notification.getCloseReason(),
                        notification.getAcceptReason(),
                        notification.getDeclineReason()
                ))
                .sendTo(getReceiverBPN(notification.getNotifications()))
                .sendToName(getReceiverName(notification.getNotifications()))
                // TODO severity should not be inside the notification it should be in the message
                .severity(NotificationMessageMapper.from(notification.getNotifications().stream().findFirst().map(NotificationMessage::getSeverity).orElse(NotificationSeverity.MINOR)))
                .targetDate(notification.getNotifications().stream().findFirst().map(NotificationMessage::getTargetDate).map(Instant::toString).orElse(null))
                .messages(fromNotifications(notification.getNotifications()))
                .build();
    }

    public static PageResult<NotificationResponse> fromAsPageResult(PageResult<Notification> notificationPageResult) {
        List<NotificationResponse> investigationResponses = notificationPageResult.content().stream().map(NotificationResponseMapper::from).toList();
        int pageNumber = notificationPageResult.page();
        int pageSize = notificationPageResult.pageSize();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<NotificationResponse> investigationDataPage = new PageImpl<>(investigationResponses, pageable, notificationPageResult.totalItems());
        return new PageResult<>(investigationDataPage);
    }

    private static String getSenderBPN(Collection<NotificationMessage> notifications) {
        return notifications.stream()
                .findFirst()
                .map(NotificationMessage::getCreatedBy)
                .orElse(null);
    }

    private static String getReceiverBPN(Collection<NotificationMessage> notifications) {
        return notifications.stream()
                .findFirst()
                .map(NotificationMessage::getSendTo)
                .orElse(null);
    }

    private static String getSenderName(Collection<NotificationMessage> notifications) {
        return notifications.stream()
                .findFirst()
                .map(NotificationMessage::getCreatedByName)
                .orElse(null);
    }

    private static String getReceiverName(Collection<NotificationMessage> notifications) {
        return notifications.stream()
                .findFirst()
                .map(NotificationMessage::getSendToName)
                .orElse(null);
    }
}
