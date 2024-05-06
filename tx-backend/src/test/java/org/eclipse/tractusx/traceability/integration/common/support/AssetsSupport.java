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
package org.eclipse.tractusx.traceability.integration.common.support;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.traceability.assets.infrastructure.asbuilt.model.AssetAsBuiltEntity;
import org.eclipse.tractusx.traceability.notification.infrastructure.notification.repository.JpaNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AssetsSupport {

    @Autowired
    AssetRepositoryProvider assetRepositoryProvider;

    @Autowired
    JpaNotificationRepository jpaInvestigationRepository;

    public String emptyText() {
        return "";
    }

    public void defaultAssetsStored() {
        assetRepositoryProvider.assetAsBuiltRepository()
                .saveAll(assetRepositoryProvider.testdataProvider().readAndConvertAssetsForTests());
    }

    public AssetAsBuiltEntity findById(String id) {
        return AssetAsBuiltEntity.from(assetRepositoryProvider.assetAsBuiltRepository.getAssetById(id));
    }

    public void defaultMultipleAssetsAsBuiltStored() {
        assetRepositoryProvider.assetAsBuiltRepository()
                .saveAll(assetRepositoryProvider.testdataProvider().readAndConvertMultipleAssetsAsBuiltForTests());
    }

    public void tractionBatteryCodeAssetsStored() {
        assetRepositoryProvider.assetAsBuiltRepository()
                .saveAll(assetRepositoryProvider.testdataProvider().readAndConvertTractionBatteryCodeAssetsForTests());
    }

    public void defaultAssetsAsPlannedStored() {
        assetRepositoryProvider.assetAsPlannedRepository()
                .saveAll(assetRepositoryProvider.testdataProvider().readAndConvertAssetsAsPlannedForTests());
    }

    public void assetsAsPlannedStored(final String resourceName) {
        assetRepositoryProvider.assetAsPlannedRepository()
                .saveAll(assetRepositoryProvider.testdataProvider().readAndConvertAssetsAsPlannedForTests(resourceName));
    }

    public void assertAssetAsBuiltSize(final int size) {
        final long assetCount = assetRepositoryProvider.assetAsBuiltRepository().countAssets();
        log.info("AsBuiltRepository asset count: {}, expected: {}", assetCount, size);
        assert assetCount == size;
    }

    public void assertAssetAsPlannedSize(final int size) {
        final long assetCount = assetRepositoryProvider.assetAsPlannedRepository().countAssets();
        log.info("AsPlannedRepository asset count: {}, expected: {}", assetCount, size);
        assert assetCount == size;
    }

    public void assertNoAssetsStored() {
        assertAssetAsBuiltSize(0);
    }
}
