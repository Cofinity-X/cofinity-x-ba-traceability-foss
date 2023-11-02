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

import { LayoutModule } from '@layout/layout.module';
import { SidenavComponent } from '@layout/sidenav/sidenav.component';
import { SidenavService } from '@layout/sidenav/sidenav.service';
import { OtherPartsModule } from '@page/other-parts/other-parts.module';
import { PartsComponent } from '@page/parts/presentation/parts.component';
import { SharedModule } from '@shared/shared.module';
import { screen, waitFor } from '@testing-library/angular';
import { renderComponent } from '@tests/test-render.utils';
import { PartsModule } from '../parts.module';
import { AssetAsBuiltFilter, AssetAsPlannedFilter } from "@page/parts/model/parts.model";
import { TableHeaderSort } from "@shared/components/table/table.model";
import { PartDetailsFacade } from "@shared/modules/part-details/core/partDetails.facade";
import { toGlobalSearchAssetFilter } from "@shared/helper/filter-helper";
import { MainAspectType } from '../model/mainAspectType.enum';

describe('Parts', () => {

    const renderParts = () => {

        return renderComponent(PartsComponent, {
            declarations: [SidenavComponent],
            imports: [PartsModule, SharedModule, LayoutModule, OtherPartsModule],
            providers: [{ provide: SidenavService }, { provide: PartDetailsFacade }],
            roles: ['admin', 'wip'],
        });
    };

    it('should render split areas', async () => {
        await renderParts();
        expect(await waitFor(() => screen.getByTestId('as-split-area-1-component--test-id'))).toBeInTheDocument();
        expect(await waitFor(() => screen.getByTestId('as-split-area-2-component--test-id'))).toBeInTheDocument();
    });


    it('should have correct sizes for split areas', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;
        expect(componentInstance.bomLifecycleSize.asPlannedSize).toBe(50);
        expect(componentInstance.bomLifecycleSize.asBuiltSize).toBe(50);
        expect(componentInstance.bomLifecycleSize.asDesignedSize).toBe(0);
        expect(componentInstance.bomLifecycleSize.asOrderedSize).toBe(0);
        expect(componentInstance.bomLifecycleSize.asSupportedSize).toBe(0);
        expect(componentInstance.bomLifecycleSize.asRecycledSize).toBe(0);
    });


    it('should call partsFacade.setPartsAsBuiltWithFilter when filter is set', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;
        // Arrange
        const assetAsBuiltFilter: AssetAsBuiltFilter = {
            id: "123"
        };
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsBuilt');

        componentInstance.filterActivated(true, assetAsBuiltFilter);


        expect(partsFacadeSpy).toHaveBeenCalledWith(0, 50, [], assetAsBuiltFilter);
    });

    it('should call partsFacade.setPartsAsPlannedWithFilter when filter is set', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;
        // Arrange
        const assetAsPlannedFilter: AssetAsPlannedFilter = {
            id: "123"
        };
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsPlanned');


        componentInstance.filterActivated(false, assetAsPlannedFilter);


        expect(partsFacadeSpy).toHaveBeenCalledWith(0, 50, [], assetAsPlannedFilter);
    });

    it('should call partsFacade.setPartsAsBuilt when filter is not set', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        const assetAsBuiltFilter: AssetAsBuiltFilter = {};
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsBuilt');

        // Act
        componentInstance.filterActivated(true, assetAsBuiltFilter);

        // Assert
        expect(partsFacadeSpy).toHaveBeenCalledWith(0, 50, [], null);
    });

    it('should call partsFacade.setPartsAsBuilt with the correct parameters', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        const page = 1; // Set the page number
        const pageSize = 10; // Set the page size
        const sorting = ['id', 'asc'] as TableHeaderSort;
        componentInstance.ctrlKeyState = true;

        // Access the private partsFacade property
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsBuilt');

        // Act
        componentInstance['onAsBuiltTableConfigChange']({ page, pageSize, sorting }); // Access private method

        // Assert
        expect(partsFacadeSpy).toHaveBeenCalledWith(page, pageSize, componentInstance['tableAsBuiltSortList']);
    });

    it('should call partsFacade.setPartsAsBuilt with the correct parameters no ctrlkey pressed', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        const page = 1; // Set the page number
        const pageSize = 10; // Set the page size
        const sorting = ['id', 'asc'] as TableHeaderSort;
        componentInstance.ctrlKeyState = false;

        // Access the private partsFacade property
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsBuilt');

        // Act
        componentInstance['onAsBuiltTableConfigChange']({ page, pageSize, sorting }); // Access private method

        // Assert
        expect(partsFacadeSpy).toHaveBeenCalledWith(page, pageSize, componentInstance['tableAsBuiltSortList']);
    });


    it('should call partsFacade.setPartsAsPlanned with the correct parameters', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        const page = 1; // Set the page number
        const pageSize = 10; // Set the page size
        const sorting = ['id', 'asc'] as TableHeaderSort;
        componentInstance.ctrlKeyState = true;

        // Access the private partsFacade property
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsPlanned');

        // Act
        componentInstance['onAsPlannedTableConfigChange']({ page, pageSize, sorting }); // Access private method

        // Assert
        expect(partsFacadeSpy).toHaveBeenCalledWith(page, pageSize, componentInstance['tableAsPlannedSortList']);
    });

    it('should call partsFacade.setPartsAsPlanned with the correct parameters  and ctrlkey not pressed', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        const page = 1; // Set the page number
        const pageSize = 10; // Set the page size
        const sorting = ['id', 'asc'] as TableHeaderSort;
        componentInstance.ctrlKeyState = false;

        // Access the private partsFacade property
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsPlanned');

        // Act
        componentInstance['onAsPlannedTableConfigChange']({ page, pageSize, sorting }); // Access private method

        // Assert
        expect(partsFacadeSpy).toHaveBeenCalledWith(page, pageSize, componentInstance['tableAsPlannedSortList']);
    });

    it('should call partsFacade.setPartsAsDesigned with the correct parameters', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        const page = 1; // Set the page number
        const pageSize = 10; // Set the page size
        const sorting = ['id', 'asc'] as TableHeaderSort;
        componentInstance.ctrlKeyState = true;

        // Access the private partsFacade property
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsDesigned');

        // Act
        componentInstance['onAsDesignedTableConfigChange']({ page, pageSize, sorting }); // Access private method

        // Assert
        expect(partsFacadeSpy).toHaveBeenCalledWith(page, pageSize, componentInstance['tableAsDesignedSortList']);
    });

    it('should call partsFacade.setPartsAsOrdered with the correct parameters', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        const page = 1; // Set the page number
        const pageSize = 10; // Set the page size
        const sorting = ['id', 'asc'] as TableHeaderSort;
        componentInstance.ctrlKeyState = true;

        // Access the private partsFacade property
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsOrdered');

        // Act
        componentInstance['onAsOrderedTableConfigChange']({ page, pageSize, sorting }); // Access private method

        // Assert
        expect(partsFacadeSpy).toHaveBeenCalledWith(page, pageSize, componentInstance['tableAsOrderedSortList']);
    });

    it('should call partsFacade.setPartsAsSupported with the correct parameters', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        const page = 1; // Set the page number
        const pageSize = 10; // Set the page size
        const sorting = ['id', 'asc'] as TableHeaderSort;
        componentInstance.ctrlKeyState = true;

        // Access the private partsFacade property
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsSupported');

        // Act
        componentInstance['onAsSupportedTableConfigChange']({ page, pageSize, sorting }); // Access private method

        // Assert
        expect(partsFacadeSpy).toHaveBeenCalledWith(page, pageSize, componentInstance['tableAsSupportedSortList']);
    });

    it('should call partsFacade.setPartsAsRecycled with the correct parameters', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        const page = 1; // Set the page number
        const pageSize = 10; // Set the page size
        const sorting = ['id', 'asc'] as TableHeaderSort;
        componentInstance.ctrlKeyState = true;

        // Access the private partsFacade property
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsRecycled');

        // Act
        componentInstance['onAsRecycledTableConfigChange']({ page, pageSize, sorting }); // Access private method

        // Assert
        expect(partsFacadeSpy).toHaveBeenCalledWith(page, pageSize, componentInstance['tableAsRecycledSortList']);
    });

    it('should set selectedPart in PartDetailsFacade correctly', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;
        const sampleEvent: Record<string, unknown> = { id: 123, name: 'Sample Part' };

        componentInstance.onSelectItem(sampleEvent);
        const partDetailsFacade = (componentInstance as any)['partDetailsFacade'];
        expect(partDetailsFacade.selectedPart).toEqual(sampleEvent);
    });

    it('should resetTableSortingList on correct values', async () => {
        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        const page = 1; // Set the page number
        const pageSize = 10; // Set the page size
        const sorting = null;

        // Access the private partsFacade property
        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsBuilt');

        // Act
        componentInstance['onAsBuiltTableConfigChange']({ page, pageSize, sorting }); // Access private method

        // Assert
        expect(partsFacadeSpy).toHaveBeenCalledWith(page, pageSize, componentInstance['tableAsBuiltSortList']);
    });

    it('should clear filters and call partsFacade methods with search value', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;
        // Arrange
        const searchValue = 'searchTerm';

        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsBuilt');
        const partsFacadeAsPlannedSpy = spyOn(partsFacade, 'setPartsAsPlanned');
        componentInstance.searchControl.setValue(searchValue);


        // Act
        componentInstance.triggerPartSearch();

        // Assert
        expect(partsFacadeAsPlannedSpy).toHaveBeenCalledWith(0, 50, [], toGlobalSearchAssetFilter(searchValue, false), true);
        expect(partsFacadeSpy).toHaveBeenCalledWith(0, 50, [], toGlobalSearchAssetFilter(searchValue, true), true);
    });

    it('should not filter if filter search is unset', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;
        // Arrange
        const searchValue = '';

        const partsFacade = (componentInstance as any)['partsFacade'];
        const partsFacadeSpy = spyOn(partsFacade, 'setPartsAsBuilt');
        const partsFacadeAsPlannedSpy = spyOn(partsFacade, 'setPartsAsPlanned');
        componentInstance.searchControl.setValue(searchValue);

        // Act
        componentInstance.triggerPartSearch();

        // Assert
        expect(partsFacadeAsPlannedSpy).toHaveBeenCalledWith();
        expect(partsFacadeSpy).toHaveBeenCalledWith();
    });

    it('should set the item count of the AsBuilt lifecycle', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        componentInstance.setPartsCount(10, MainAspectType.AS_BUILT)
        expect(componentInstance.asBuiltCount).toBe(10);
    });

    it('should set the item count of the AsPlanned lifecycle', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        componentInstance.setPartsCount(10, MainAspectType.AS_PLANNED)
        expect(componentInstance.asPlannedCount).toBe(10);
    });

    it('should set the item count of the AsDesigned lifecycle', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        componentInstance.setPartsCount(10, MainAspectType.AS_DESIGNED)
        expect(componentInstance.asDesignedCount).toBe(10);
    });

    it('should set the item count of the AsOrdered lifecycle', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        componentInstance.setPartsCount(10, MainAspectType.AS_ORDERED)
        expect(componentInstance.asOrderedCount).toBe(10);
    });

    it('should set the item count of the AsSupported lifecycle', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        componentInstance.setPartsCount(10, MainAspectType.AS_SUPPORTED)
        expect(componentInstance.asSupportedCount).toBe(10);
    });

    it('should set the item count of the AsRecycled lifecycle', async () => {

        const { fixture } = await renderParts();
        const { componentInstance } = fixture;

        componentInstance.setPartsCount(10, MainAspectType.AS_RECYCLED)
        expect(componentInstance.asRecycledCount).toBe(10);
    });
});
