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

import { OtherPartsState } from '@page/other-parts/core/other-parts.state';
import { OtherPartsModule } from '@page/other-parts/other-parts.module';
import { PartsState } from '@page/parts/core/parts.state';
import { MainAspectType } from '@page/parts/model/mainAspectType.enum';
import { PartsAssembler } from '@shared/assembler/parts.assembler';
import { toAssetFilter, toGlobalSearchAssetFilter } from '@shared/helper/filter-helper';
import { fireEvent, screen, waitFor } from '@testing-library/angular';
import { renderComponent } from '@tests/test-render.utils';
import { OTHER_PARTS_MOCK_6 } from '../../../../../mocks/services/otherParts-mock/otherParts.test.model';

import { SupplierPartsComponent } from './supplier-parts.component';
import { TableEventConfig } from '@shared/components/table/table.model';
import { Part } from '@page/parts/model/parts.model';
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';

describe('SupplierPartsComponent', () => {
  let otherPartsState: OtherPartsState;
  beforeEach(() => (otherPartsState = new OtherPartsState()));

  const renderSupplierPartsAsBuilt = ({ roles = [] } = {}) =>
    renderComponent(SupplierPartsComponent, {
      imports: [OtherPartsModule],
      providers: [{ provide: OtherPartsState, useFactory: () => otherPartsState }, { provide: PartsState }],
      roles,
      componentInputs: {
        bomLifecycle: MainAspectType.AS_BUILT,
      },
    });

  const renderSupplierPartsAsPlanned = ({ roles = [] } = {}) =>
    renderComponent(SupplierPartsComponent, {
      imports: [OtherPartsModule],
      providers: [{ provide: OtherPartsState, useFactory: () => otherPartsState }, { provide: PartsState }],
      roles,
      componentInputs: {
        bomLifecycle: MainAspectType.AS_PLANNED,
      },
    });

  it('should render part table', async () => {
    await renderSupplierPartsAsBuilt();

    const tableElements = await waitFor(() => screen.getAllByTestId('table-component--test-id'));
    expect(tableElements.length).toEqual(1);
  });

  it('should render table and display correct amount of rows', async () => {
    await renderSupplierPartsAsBuilt();

    const tableElement = await waitFor(() => screen.getByTestId('table-component--test-id'));
    expect(tableElement).toBeInTheDocument();
    expect(tableElement.children[1].childElementCount).toEqual(4);
  });

  // it('should add item to current list and then remove', async () => {
  //   const { fixture } = await renderSupplierPartsAsBuilt({ roles: [ 'user' ] });
  //   const expectedPart = PartsAssembler.assembleOtherPart(OTHER_PARTS_MOCK_6, MainAspectType.AS_BUILT);

  //   // first click to check checkbox
  //   fireEvent.click(await getTableCheckbox(screen, 0));

  //   const selectedText_1 = await waitFor(() => screen.getByText('page.selectedParts.info'));
  //   expect(selectedText_1).toBeInTheDocument();
  //   expect(fixture.componentInstance.currentSelectedItems).toEqual([ expectedPart ]);

  //   // second click to uncheck checkbox
  //   fireEvent.click(await getTableCheckbox(screen, 0));

  //   const selectedText_2 = await waitFor(() => screen.getByText('page.selectedParts.info'));
  //   expect(selectedText_2).toBeInTheDocument();
  //   expect(fixture.componentInstance.currentSelectedItems).toEqual([]);
  // });

  it('test addItemToSelection method', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();

    const expectedPart = PartsAssembler.assembleOtherPart(OTHER_PARTS_MOCK_6, MainAspectType.AS_BUILT);

    fixture.componentInstance.addItemToSelection(expectedPart);
    expect(fixture.componentInstance.currentSelectedItems).toEqual([expectedPart]);
  });

  it('test removeItemFromSelection method', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();

    const expectedPart = PartsAssembler.assembleOtherPart(OTHER_PARTS_MOCK_6, MainAspectType.AS_BUILT);

    fixture.componentInstance.currentSelectedItems = [expectedPart];

    fixture.componentInstance.removeItemFromSelection(expectedPart);
    expect(fixture.componentInstance.currentSelectedItems).toEqual([]);
  });

  it('test clearSelected method', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();

    const expectedPart = PartsAssembler.assembleOtherPart(OTHER_PARTS_MOCK_6, MainAspectType.AS_BUILT);

    fixture.componentInstance.currentSelectedItems = [expectedPart];

    fixture.componentInstance.clearSelected();
    expect(fixture.componentInstance.currentSelectedItems).toEqual([]);
  });

  it('sort supplier parts after name column', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt({ roles: ['admin'] });
    const supplierPartsComponent = fixture.componentInstance;

    let nameHeader = await screen.findByText('table.column.name');
    fireEvent.click(nameHeader);

    expect(supplierPartsComponent['tableSupplierAsBuiltSortList']).toEqual([['name', 'asc']]);
  });

  it('should multisort after column name and semanticModelId', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt({ roles: ['admin'] });
    const supplierPartsComponent = fixture.componentInstance;

    let nameHeader = await screen.findByText('table.column.name');
    fireEvent.click(nameHeader);
    let semanticModelIdHeader = await screen.findByText('table.column.semanticModelId');

    await waitFor(() => {
      fireEvent.keyDown(semanticModelIdHeader, {
        ctrlKey: true,
        charCode: 17,
      });
    });
    expect(supplierPartsComponent['ctrlKeyState']).toBeTruthy();
    await waitFor(() => {
      fireEvent.click(semanticModelIdHeader);
    });

    await waitFor(() => {
      fireEvent.keyUp(semanticModelIdHeader, {
        ctrlKey: true,
        charCode: 17,
      });
    });

    await waitFor(() => {
      fireEvent.click(semanticModelIdHeader);
    });
    expect(supplierPartsComponent['tableSupplierAsBuiltSortList']).toEqual([
      ['name', 'asc'],
      ['semanticModelId', 'desc'],
    ]);
  });

  it('should handle updateSupplierParts null for AsBuilt', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();
    const supplierPartsComponent = fixture.componentInstance;

    spyOn(supplierPartsComponent.otherPartsFacade, 'setSupplierPartsAsBuilt');

    supplierPartsComponent.updateSupplierParts();

    expect(supplierPartsComponent.otherPartsFacade.setSupplierPartsAsBuilt).toHaveBeenCalledWith(0, 50);
  });

  it('should handle updateSupplierParts null for AsPlanned', async () => {
    const { fixture } = await renderSupplierPartsAsPlanned();
    const supplierPartsComponent = fixture.componentInstance;

    spyOn(supplierPartsComponent.otherPartsFacade, 'setSupplierPartsAsPlanned');

    supplierPartsComponent.updateSupplierParts();

    expect(supplierPartsComponent.otherPartsFacade.setSupplierPartsAsPlanned).toHaveBeenCalledWith(0, 50);
  });

  it('should handle updateSupplierParts including search for AsBuilt', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();
    const supplierPartsComponent = fixture.componentInstance;

    spyOn(supplierPartsComponent.otherPartsFacade, 'setSupplierPartsAsBuilt');

    const search = 'test';
    supplierPartsComponent.updateSupplierParts(search);

    expect(supplierPartsComponent.otherPartsFacade.setSupplierPartsAsBuilt).toHaveBeenCalledWith(
      0,
      50,
      [],
      toGlobalSearchAssetFilter(search, true, supplierPartsComponent.searchListAsBuilt, supplierPartsComponent.datePipe),
      true,
    );
  });

  it('should handle updateSupplierParts including search for AsPlanned', async () => {
    const { fixture } = await renderSupplierPartsAsPlanned();
    const supplierPartsComponent = fixture.componentInstance;

    spyOn(supplierPartsComponent.otherPartsFacade, 'setSupplierPartsAsPlanned');

    const search = 'test';
    supplierPartsComponent.updateSupplierParts(search);

    expect(supplierPartsComponent.otherPartsFacade.setSupplierPartsAsPlanned).toHaveBeenCalledWith(
      0,
      50,
      [],
      toGlobalSearchAssetFilter(search, false, supplierPartsComponent.searchListAsPlanned, supplierPartsComponent.datePipe),
      true,
    );
  });

  it('should set the default Pagination by recieving a size change event', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();
    const supplierPartsComponent = fixture.componentInstance;

    supplierPartsComponent.onDefaultPaginationSizeChange(100);
    expect(supplierPartsComponent.DEFAULT_PAGE_SIZE).toEqual(100);
  });

  it('should use the default page size if the page size in the onAsBuiltTableConfigChange is given as 0', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();
    const supplierPartsComponent = fixture.componentInstance;

    const pagination: TableEventConfig = { page: 0, pageSize: 0, sorting: ['name', 'asc'] };
    spyOn(supplierPartsComponent.otherPartsFacade, 'setSupplierPartsAsBuilt');

    supplierPartsComponent.onAsBuiltTableConfigChange(pagination);
    fixture.detectChanges();
    expect(supplierPartsComponent.otherPartsFacade.setSupplierPartsAsBuilt).toHaveBeenCalledWith(0, 50, [['name', 'asc']], toAssetFilter(null, true), false);

  });

  it('should use the given page size if the page size in the onAsBuiltTableConfigChange is given as not 0', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();
    const supplierPartsComponent = fixture.componentInstance;

    const pagination: TableEventConfig = { page: 0, pageSize: 10, sorting: ['name', 'asc'] };
    spyOn(supplierPartsComponent.otherPartsFacade, 'setSupplierPartsAsBuilt');

    supplierPartsComponent.onAsBuiltTableConfigChange(pagination);
    fixture.detectChanges();
    expect(supplierPartsComponent.otherPartsFacade.setSupplierPartsAsBuilt).toHaveBeenCalledWith(0, 10, [['name', 'asc']], toAssetFilter(null, true), false);

  });

  it('should use the default page size if the page size in the onAsPlannedTableConfigChange is given as 0', async () => {
    const { fixture } = await renderSupplierPartsAsPlanned();
    const supplierPartsComponent = fixture.componentInstance;

    const pagination: TableEventConfig = { page: 0, pageSize: 0, sorting: ['name', 'asc'] };
    spyOn(supplierPartsComponent.otherPartsFacade, 'setSupplierPartsAsPlanned');

    supplierPartsComponent.onAsPlannedTableConfigChange(pagination);
    fixture.detectChanges();
    expect(supplierPartsComponent.otherPartsFacade.setSupplierPartsAsPlanned).toHaveBeenCalledWith(0, 50, [['name', 'asc']], toAssetFilter(null, false), false);
  });

  it('should use the given page size if the page size in the onAsPlannedTableConfigChange is given as not 0', async () => {
    const { fixture } = await renderSupplierPartsAsPlanned();
    const supplierPartsComponent = fixture.componentInstance;

    const pagination: TableEventConfig = { page: 0, pageSize: 10, sorting: ['name', 'asc'] };
    spyOn(supplierPartsComponent.otherPartsFacade, 'setSupplierPartsAsPlanned');

    supplierPartsComponent.onAsPlannedTableConfigChange(pagination);
    fixture.detectChanges();
    expect(supplierPartsComponent.otherPartsFacade.setSupplierPartsAsPlanned).toHaveBeenCalledWith(0, 10, [['name', 'asc']], toAssetFilter(null, false), false);
  });

  it('should pass on the filtering to the api services for As_Built', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();
    const supplierPartsComponent = fixture.componentInstance;

    const assetFilterAsBuilt = {
      "select": [],
      "semanticDataModel": [],
      "nameAtManufacturer": "value1",
      "manufacturerName": [],
      "manufacturerPartId": [],
      "semanticModelId": [],
      "manufacturingDate": [],
      "qualityAlertsInStatusActive": [],
      "qualityInvestigationsInStatusActive": []
    };
    spyOn(supplierPartsComponent.otherPartsFacade, 'setSupplierPartsAsBuilt');


    supplierPartsComponent.filterActivated(true, assetFilterAsBuilt);
    fixture.detectChanges();
    expect(supplierPartsComponent.otherPartsFacade.setSupplierPartsAsBuilt).toHaveBeenCalledWith(0, 50, [], toAssetFilter(assetFilterAsBuilt, true), false);
  });

  it('should pass on the filtering to the api services for As_Planned', async () => {
    const { fixture } = await renderSupplierPartsAsPlanned();
    const supplierPartsComponent = fixture.componentInstance;

    const assetFilterAsPlanned = {
      "select": [],
      "semanticDataModel": [],
      "nameAtManufacturer": "value2",
      "manufacturerName": [],
      "semanticModelId": [],
      "manufacturerPartId": []
    };
    spyOn(supplierPartsComponent.otherPartsFacade, 'setSupplierPartsAsPlanned');

    supplierPartsComponent.filterActivated(false, assetFilterAsPlanned);
    fixture.detectChanges();
    expect(supplierPartsComponent.otherPartsFacade.setSupplierPartsAsPlanned).toHaveBeenCalledWith(0, 50, [], toAssetFilter(assetFilterAsPlanned, false), false);
  });

  it('should set selected part and open detail page when item is selected', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();
    const supplierPartsComponent = fixture.componentInstance;
    const router = TestBed.inject(Router);

    const part: any = { id: '1' };

    spyOn(router, 'navigate').and.stub();

    const partDetailsFacade = (supplierPartsComponent as any)['partDetailsFacade'];

    supplierPartsComponent.onSelectItem(part);

    expect(partDetailsFacade.selectedPart).toEqual(part);
    expect(router.navigate).toHaveBeenCalledWith(['/otherParts/1']);
  });

  it('should navigate to the correct detail page with the provided part', async () => {
    const { fixture } = await renderSupplierPartsAsBuilt();
    const supplierPartsComponent = fixture.componentInstance;
    const router = TestBed.inject(Router);

    const part: any = { id: 2 };

    spyOn(router, 'navigate').and.stub();

    supplierPartsComponent.openDetailPage(part);

    const expectedLink = '/otherParts/2';
    expect(router.navigate).toHaveBeenCalledWith([expectedLink]);
  });


});
