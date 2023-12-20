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

package org.eclipse.tractusx.traceability.assets.application.rest;

import org.eclipse.tractusx.traceability.assets.domain.dashboard.model.Dashboard;
import org.eclipse.tractusx.traceability.assets.domain.dashboard.service.DashboardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    DashboardServiceImpl dashboardService;

    @Test
    void dashboard() {
        // given
        final Dashboard dashboard = Dashboard.builder()
                .myParts(9)
                .otherParts(99)
                .investigationsReceived(8)
                .alertsReceived(88)
                .build();
        Mockito.when(dashboardService.getDashboard()).thenReturn(dashboard);

        // when
        final Dashboard dashboardReceived = dashboardService.getDashboard();

        // then
        assertEquals(9, dashboardReceived.getMyParts());
        assertEquals(99, dashboardReceived.getOtherParts());
        assertEquals(8, dashboardReceived.getInvestigationsReceived());
        assertEquals(88, dashboardReceived.getAlertsReceived());
    }
}
