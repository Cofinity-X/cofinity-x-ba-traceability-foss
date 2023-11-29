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
import { toAssetFilter, toGlobalSearchAssetFilter } from '@shared/helper/filter-helper';
import { fireEvent, screen, waitFor } from '@testing-library/angular';
import { renderComponent } from '@tests/test-render.utils';

import { CustomerPartsComponent } from './customer-parts.component';
import { FilterMethod, TableEventConfig } from '@shared/components/table/table.model';
import { FilterOperator } from '@page/parts/model/parts.model';

describe('CustomerPartsComponent', () => {
  let otherPartsState: OtherPartsState;
  beforeEach(() => (otherPartsState = new OtherPartsState()));

  const renderCustomerPartsAsBuilt = () =>
    renderComponent(CustomerPartsComponent, {
      imports: [OtherPartsModule],
      providers: [{ provide: OtherPartsState, useFactory: () => otherPartsState }, { provide: PartsState }],
      roles: ['admin', 'wip'],
      componentInputs: {
        bomLifecycle: MainAspectType.AS_BUILT,
      },
    });
  const renderCustomerPartsAsPlanned = () =>
    renderComponent(CustomerPartsComponent, {
      imports: [OtherPartsModule],
      providers: [{ provide: OtherPartsState, useFactory: () => otherPartsState }, { provide: PartsState }],
      roles: ['admin', 'wip'],
      componentInputs: {
        bomLifecycle: MainAspectType.AS_PLANNED,
      },
    });

  it('should render part table', async () => {
    await renderCustomerPartsAsBuilt();
    const tableElements = await waitFor(() => screen.getAllByTestId('table-component--test-id'));
    expect(tableElements.length).toEqual(1);
  });

  it('should render table and display correct amount of rows', async () => {
    await renderCustomerPartsAsBuilt();
    const tableElement = await waitFor(() => screen.getByTestId('table-component--test-id'));
    expect(tableElement).toBeInTheDocument();
    expect(tableElement.children[1].childElementCount).toEqual(5);
  });

  it('sort customer parts after name column', async () => {
    const { fixture } = await renderCustomerPartsAsBuilt();
    const customerPartsComponent = fixture.componentInstance;

    let nameHeader = await screen.findByText('table.column.name');
    fireEvent.click(nameHeader);

    expect(customerPartsComponent['tableCustomerAsBuiltSortList']).toEqual([['name', 'asc']]);
  });

  it('should multisort after column name and semanticModelId', async () => {
    const { fixture } = await renderCustomerPartsAsBuilt();
    const customerPartsComponent = fixture.componentInstance;

    let nameHeader = await screen.findByText('table.column.name');
    fireEvent.click(nameHeader);
    let semanticModelIdHeader = await screen.findByText('table.column.semanticModelId');

    await waitFor(() => {
      fireEvent.keyDown(semanticModelIdHeader, {
        ctrlKey: true,
        charCode: 17,
      });
    });
    expect(customerPartsComponent['ctrlKeyState']).toBeTruthy();
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
    expect(customerPartsComponent['tableCustomerAsBuiltSortList']).toEqual([
      ['name', 'asc'],
      ['semanticModelId', 'desc'],
    ]);
  });

  it('should handle updateCustomerParts null', async () => {
    const { fixture } = await renderCustomerPartsAsBuilt();
    const customerPartsComponent = fixture.componentInstance;

    const otherPartsFacade = (customerPartsComponent as any)['otherPartsFacade'];
    const updateCustomerPartAsBuiltSpy = spyOn(otherPartsFacade, 'setCustomerPartsAsBuilt');
    const updateCustomerPartAsPlannedSpy = spyOn(otherPartsFacade, 'setCustomerPartsAsPlanned');

    customerPartsComponent.updateCustomerParts();

    expect(updateCustomerPartAsBuiltSpy).toHaveBeenCalledWith();
    expect(updateCustomerPartAsPlannedSpy).toHaveBeenCalledWith();
  });

  it('should handle updateCustomerParts including search', async () => {
    const { fixture } = await renderCustomerPartsAsBuilt();
    const customerPartsComponent = fixture.componentInstance;

    const otherPartsFacade = (customerPartsComponent as any)['otherPartsFacade'];
    const updateCustomerPartAsBuiltSpy = spyOn(otherPartsFacade, 'setCustomerPartsAsBuilt');
    const updateCustomerPartAsPlannedSpy = spyOn(otherPartsFacade, 'setCustomerPartsAsPlanned');

    const search = 'test';
    customerPartsComponent.updateCustomerParts(search);

    expect(updateCustomerPartAsBuiltSpy).toHaveBeenCalledWith(0, 50, [], toGlobalSearchAssetFilter(search, true), true);
    expect(updateCustomerPartAsPlannedSpy).toHaveBeenCalledWith(
      0,
      50,
      [],
      toGlobalSearchAssetFilter(search, false),
      true,
    );
  });


  it('should set the default Pagination by recieving a size change event', async () => {
    const { fixture } = await renderCustomerPartsAsBuilt();
    const customerPartsComponent = fixture.componentInstance;

    customerPartsComponent.onDefaultPaginationSizeChange(100);
    expect(customerPartsComponent.DEFAULT_PAGE_SIZE).toEqual(100);
  });

  it('should use the default page size if the page size in the onAsBuiltTableConfigChange is given as 0', async () => {
    const { fixture } = await renderCustomerPartsAsBuilt();
    const customerPartsComponent = fixture.componentInstance;

    const pagination: TableEventConfig = { page: 0, pageSize: 0, sorting: ['name', 'asc'] };
    spyOn(customerPartsComponent.otherPartsFacade, 'setCustomerPartsAsBuilt');

    customerPartsComponent.onAsBuiltTableConfigChange(pagination);
    fixture.detectChanges();
    expect(customerPartsComponent.otherPartsFacade.setCustomerPartsAsBuilt).toHaveBeenCalledWith(0, 50, [['name', 'asc']], toAssetFilter(null, true));

  });

  it('should use the default page size if the page size in the onAsPlannedTableConfigChange is given as 0', async () => {
    const { fixture } = await renderCustomerPartsAsPlanned();
    const customerPartsComponent = fixture.componentInstance;

    const pagination: TableEventConfig = { page: 0, pageSize: 0, sorting: ['name', 'asc'] };
    spyOn(customerPartsComponent.otherPartsFacade, 'setCustomerPartsAsPlanned');

    customerPartsComponent.onAsPlannedTableConfigChange(pagination);
    fixture.detectChanges();
    expect(customerPartsComponent.otherPartsFacade.setCustomerPartsAsPlanned).toHaveBeenCalledWith(0, 50, [['name', 'asc']], toAssetFilter(null, false));
  });

  it('should pass on the filtering to the api services for As_Built', async () => {
    const { fixture } = await renderCustomerPartsAsBuilt();
    const customerPartsComponent = fixture.componentInstance;

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
    spyOn(customerPartsComponent.otherPartsFacade, 'setCustomerPartsAsBuilt');


    customerPartsComponent.filterActivated(true, assetFilterAsBuilt);
    fixture.detectChanges();
    expect(customerPartsComponent.otherPartsFacade.setCustomerPartsAsBuilt).toHaveBeenCalledWith(0, 50, [], toAssetFilter(assetFilterAsBuilt, true));
  });

  it('should pass on the filtering to the api services for As_Planned', async () => {
    const { fixture } = await renderCustomerPartsAsPlanned();
    const customerPartsComponent = fixture.componentInstance;

    const assetFilterAsPlanned = {
      "select": [],
      "semanticDataModel": [],
      "nameAtManufacturer": "value2",
      "manufacturerName": [],
      "semanticModelId": [],
      "manufacturerPartId": []
    };
    spyOn(customerPartsComponent.otherPartsFacade, 'setCustomerPartsAsPlanned');

    customerPartsComponent.filterActivated(false, assetFilterAsPlanned);
    fixture.detectChanges();
    expect(customerPartsComponent.otherPartsFacade.setCustomerPartsAsPlanned).toHaveBeenCalledWith(0, 50, [], toAssetFilter(assetFilterAsPlanned, false));
  });

});
