/********************************************************************************
 * Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 * Copyright (c) 2022, 2023 ZF Friedrichshafen AG
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.tractusx.traceability.infrastructure.edc.blackbox.model;

import org.eclipse.tractusx.traceability.investigations.domain.model.AffectedPart;
import org.eclipse.tractusx.traceability.investigations.domain.model.InvestigationStatus;

import java.util.List;
import java.util.stream.Collectors;

public record EDCNotification(EDCNotificationHeader header, EDCNotificationContent content) {

	public String getRecipientBPN() {
		return header.recipientBPN();
	}

	public String getNotificationId() {
		return header.notificationId();
	}

	public String getSenderBPN() {
		return header.senderBPN();
	}

	public String getSenderAddress() {
		return header.senderAddress();
	}

	public String getInformation() {
		return content.information();
	}

	public List<AffectedPart> getListOfAffectedItems() {
		return content.listOfAffectedItems().stream()
			.map(AffectedPart::new)
			.collect(Collectors.toList());
	}

	public NotificationType convertNotificationType() {
		String classification = header().classification();

		return NotificationType.fromValue(classification)
			.orElseThrow(() -> new IllegalArgumentException("%s not supported notification type".formatted(classification)));
	}

	public InvestigationStatus convertInvestigationStatus() {
		String investigationStatus = header().status();

		return InvestigationStatus.fromValue(investigationStatus)
			.orElseThrow(() -> new IllegalArgumentException("%s not supported investigation status".formatted(investigationStatus)));
	}
}

