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

package org.eclipse.tractusx.traceability.assets.infrastructure.repository.rest.irs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.irs.edc.client.policy.*;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.AssetCallbackRepository;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.IrsClient;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.IrsService;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.request.BomLifecycle;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.*;
import org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response.relationship.Aspect;
import org.eclipse.tractusx.traceability.bpn.domain.service.BpnRepository;
import org.eclipse.tractusx.traceability.common.model.BPN;
import org.eclipse.tractusx.traceability.common.properties.TraceabilityProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IrsServiceTest {
    public static final String EXAMPLE_TIMESTAMP_PAST_FIRST = "2023-07-03T16:01:05.309Z";
    public static final String POLICY_MOCK_ID = "test";
    public static final String EXAMPLE_TIMESTAMP_PAST_SECOND = "2023-07-04T16:01:05.309Z";
    private IrsService irsService;

    @Mock
    private IrsClient irsClient;

    @Mock
    TraceabilityProperties traceabilityProperties;

    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    AssetCallbackRepository assetAsBuiltCallbackRepository;

    @Mock
    AssetCallbackRepository assetAsPlannedCallbackRepository;

    @Mock
    private BpnRepository bpnRepository;

    @BeforeEach
    void setUp() {
        irsService = new IrsService(irsClient, bpnRepository, traceabilityProperties, objectMapper, assetAsBuiltCallbackRepository, assetAsPlannedCallbackRepository);

    }


    @Test
    void givenNoPolicyExist_whenCreateIrsPolicyIfMissing_thenCreateIt() {
      // Given
        when(irsClient.getPolicies()).thenReturn(List.of());
        when(traceabilityProperties.getRightOperand()).thenReturn(POLICY_MOCK_ID);

        // When
        irsService.createIrsPolicyIfMissing();

       // Then
        verify(irsClient, times(1))
                .registerPolicy();
    }

    @Test
    void givenPolicyExist_whenCreateIrsPolicyIfMissing_thenDoNotCreateIt() {
      // Given
        Policy policyMock = Policy.builder()
                .policyId(POLICY_MOCK_ID)
                .createdOn(OffsetDateTime.parse(EXAMPLE_TIMESTAMP_PAST_FIRST))
                .validUntil(OffsetDateTime.now())
                .permissions(prepareMockPermissions())
                .build();
        Payload policyResponsePayload = new Payload(Context.getDefault(), POLICY_MOCK_ID, policyMock);
        final PolicyResponse existingPolicy = new PolicyResponse( OffsetDateTime.now(), policyResponsePayload);

        when(irsClient.getPolicies()).thenReturn(List.of(existingPolicy));
        when(traceabilityProperties.getRightOperand()).thenReturn(POLICY_MOCK_ID);
        when(traceabilityProperties.getValidUntil()).thenReturn(OffsetDateTime.parse(EXAMPLE_TIMESTAMP_PAST_FIRST));

        // When
        irsService.createIrsPolicyIfMissing();

       // Then
        verifyNoMoreInteractions(irsClient);
    }

    @Test
    void givenOutdatedPolicyExist_whenCreateIrsPolicyIfMissing_thenUpdateIt() {
      // Given
        Policy policyMock = Policy.builder()
                .policyId(POLICY_MOCK_ID)
                .createdOn(OffsetDateTime.parse(EXAMPLE_TIMESTAMP_PAST_FIRST))
                .validUntil(OffsetDateTime.now())
                .permissions(prepareMockPermissions())
                .build();
        Payload policyResponsePayload = new Payload(Context.getDefault(), POLICY_MOCK_ID, policyMock);
        final PolicyResponse existingPolicy = new PolicyResponse(OffsetDateTime.parse(EXAMPLE_TIMESTAMP_PAST_FIRST), policyResponsePayload);
        when(irsClient.getPolicies()).thenReturn(List.of(existingPolicy));
        when(traceabilityProperties.getRightOperand()).thenReturn(POLICY_MOCK_ID);
        when(traceabilityProperties.getValidUntil()).thenReturn(OffsetDateTime.parse(EXAMPLE_TIMESTAMP_PAST_SECOND));

        // When
        irsService.createIrsPolicyIfMissing();

       // Then
        verify(irsClient, times(1)).deletePolicy();
        verify(irsClient, times(1)).registerPolicy();
    }

    @ParameterizedTest
    @MethodSource("provideDirections")
    void testFindAssets_completedJob_returnsConvertedAssets(Direction direction) {
      // Given
        when(traceabilityProperties.getBpn()).thenReturn(BPN.of("test"));

        // When
        irsService.createJobToResolveAssets("1", direction, Aspect.downwardAspectsForAssetsAsBuilt(), BomLifecycle.AS_BUILT);

       // Then
        verify(irsClient, times(1)).registerJob(any());
    }

    private static Stream<Arguments> provideDirections() {
        return Stream.of(
                Arguments.of(Direction.DOWNWARD),
                Arguments.of(Direction.UPWARD)
        );
    }

    @Test
    void test_getPolicyConstraints() {
       // Given
        Operator eqOperator = new Operator(OperatorType.EQ);
        List<Constraint> andConstraints = List.of(new Constraint("leftOperand", eqOperator, "rightOperand"));
        List<Constraint> orConstraints = List.of(new Constraint("leftOperand", eqOperator, "rightOperand"));
        Constraints constraints = new Constraints(andConstraints, orConstraints);

        Permission permission = new Permission(PolicyType.USE, constraints);
        String policyIdMock = "policyId1";
        Policy policyMock = Policy.builder()
                .policyId(policyIdMock)
                .createdOn(OffsetDateTime.now())
                .validUntil(OffsetDateTime.now())
                .permissions(List.of(permission))
                .build();
        Payload policyResponsePayload = new Payload(Context.getDefault(), policyIdMock, policyMock);
        PolicyResponse policyResponseMock = new PolicyResponse(OffsetDateTime.now(), policyResponsePayload);

        when(irsClient.getPolicies()).thenReturn(List.of(policyResponseMock));

       // When
        List<PolicyResponse> policyResponse = irsService.getPolicies();

       // Then
        assertThat(1).isEqualTo(policyResponse.size());
    }

    private static List<Permission> prepareMockPermissions() {
        List<Constraint> constraintList = List.of(new Constraint("", new Operator(OperatorType.EQ), POLICY_MOCK_ID));
        Permission mockPermission = Permission.builder()
                .constraint(Constraints.builder()
                        .or(constraintList)
                        .and(constraintList)
                        .build())
                .action(PolicyType.USE).build();
        return List.of(mockPermission);
    }
}
