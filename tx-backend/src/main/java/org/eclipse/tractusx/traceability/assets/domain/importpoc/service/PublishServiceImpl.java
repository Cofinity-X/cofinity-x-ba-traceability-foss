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
package org.eclipse.tractusx.traceability.assets.domain.importpoc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.traceability.assets.application.importpoc.PublishService;
import org.eclipse.tractusx.traceability.assets.domain.asbuilt.repository.AssetAsBuiltRepository;
import org.eclipse.tractusx.traceability.assets.domain.asplanned.repository.AssetAsPlannedRepository;
import org.eclipse.tractusx.traceability.assets.domain.base.AssetRepository;
import org.eclipse.tractusx.traceability.assets.domain.base.model.AssetBase;
import org.eclipse.tractusx.traceability.assets.domain.base.model.ImportState;
import org.eclipse.tractusx.traceability.assets.domain.importpoc.exception.PublishAssetException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PublishServiceImpl implements PublishService {

    private final AssetAsPlannedRepository assetAsPlannedRepository;
    private final AssetAsBuiltRepository assetAsBuiltRepository;
    private static void validTransientState(AssetBase assetBase) {
        if (!ImportState.TRANSIENT.equals(assetBase.getImportState())) {
            throw new PublishAssetException("Asset with ID " + assetBase.getId() + " is not in TRANSIENT state.");
        }
    }

    @Override
    public void publishAssets(String policyId, List<String> assetIds) {

        if (checkNoAssetsFound(assetIds, assetAsPlannedRepository) && checkNoAssetsFound(assetIds, assetAsBuiltRepository)) {
            throw new PublishAssetException("No assets found with the provided IDs." + assetIds);
        }
        saveAssetsInRepository(policyId, assetIds, assetAsPlannedRepository);
        saveAssetsInRepository(policyId, assetIds, assetAsBuiltRepository);
    }

    private void saveAssetsInRepository(String policyId, List<String> assetIds, AssetRepository repository) {
        List<AssetBase> assetList = repository.getAssetsById(assetIds).stream()
                .map(asset -> {
                    validTransientState(asset);
                    asset.setImportState(ImportState.IN_SYNCHRONIZATION);
                    asset.setPolicyId(policyId);
                    return asset;
                })
                .toList();
        repository.saveAll(assetList);
    }

    private boolean checkNoAssetsFound(List<String> assetIds, AssetRepository repository) {
        return repository.getAssetsById(assetIds).stream().noneMatch(Objects::nonNull);
    }
}
