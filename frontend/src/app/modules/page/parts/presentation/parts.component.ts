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

import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit, QueryList, ViewChildren } from '@angular/core';
import { Pagination } from '@core/model/pagination.model';
import { PartsFacade } from '@page/parts/core/parts.facade';
import { MainAspectType } from '@page/parts/model/mainAspectType.enum';
import { AssetAsBuiltFilter, AssetAsPlannedFilter, Part } from '@page/parts/model/parts.model';
import { PartTableType, TableEventConfig, TableHeaderSort } from '@shared/components/table/table.model';
import { View } from '@shared/model/view.model';
import { PartDetailsFacade } from '@shared/modules/part-details/core/partDetails.facade';
import { StaticIdService } from '@shared/service/staticId.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { BomLifecycleSize } from "@shared/components/bom-lifecycle-activator/bom-lifecycle-activator.model";
import { BomLifecycleSettingsService, UserSettingView } from "@shared/service/bom-lifecycle-settings.service";
import { toAssetFilter, toGlobalSearchAssetFilter } from "@shared/helper/filter-helper";
import { FormControl, FormGroup } from "@angular/forms";
import { ToastService } from "@shared/components/toasts/toast.service";
import { PartsTableComponent } from "@shared/components/parts-table/parts-table.component";
import { resetMultiSelectionAutoCompleteComponent } from "@page/parts/core/parts.helper";

@Component({
  selector: 'app-parts',
  templateUrl: './parts.component.html',
  styleUrls: ['./parts.component.scss'],
})
export class PartsComponent implements OnInit, OnDestroy, AfterViewInit {
  public readonly titleId = this.staticIdService.generateId('PartsComponent.title');

  public readonly partsAsBuilt$: Observable<View<Pagination<Part>>>;
  public readonly partsAsPlanned$: Observable<View<Pagination<Part>>>;
  public readonly partsAsDesigned$: Observable<View<Pagination<Part>>>;
  public readonly partsAsOrdered$: Observable<View<Pagination<Part>>>;
  public readonly partsAsSupported$: Observable<View<Pagination<Part>>>;
  public readonly partsAsRecycled$: Observable<View<Pagination<Part>>>;
  public readonly isAlertOpen$ = new BehaviorSubject<boolean>(false);

  public readonly deselectPartTrigger$ = new Subject<Part[]>();
  public readonly addPartTrigger$ = new Subject<Part>();
  public readonly currentSelectedItems$ = new BehaviorSubject<Part[]>([]);

  public asBuiltCount: number;
  public asPlannedCount: number;
  public asDesignedCount: number;
  public asOrderedCount: number;
  public asSupportedCount: number;
  public asRecycledCount: number;

  public tableAsBuiltSortList: TableHeaderSort[];
  public tableAsPlannedSortList: TableHeaderSort[];
  public tableAsDesignedSortList: TableHeaderSort[];
  public tableAsSupportedSortList: TableHeaderSort[];
  public tableAsOrderedSortList: TableHeaderSort[];
  public tableAsRecycledSortList: TableHeaderSort[];

  public DEFAULT_PAGE_SIZE = 50;
  public ctrlKeyState = false;

  protected readonly UserSettingView = UserSettingView;
  protected readonly PartTableType = PartTableType;
  protected readonly MainAspectType = MainAspectType;

  @ViewChildren(PartsTableComponent) partsTableComponents: QueryList<PartsTableComponent>;

  constructor(
    private changeDetectorRef: ChangeDetectorRef,
    private readonly partsFacade: PartsFacade,
    private readonly partDetailsFacade: PartDetailsFacade,
    private readonly staticIdService: StaticIdService,
    private readonly userSettingService: BomLifecycleSettingsService,
    public toastService: ToastService,
  ) {
    this.partsAsBuilt$ = this.partsFacade.partsAsBuilt$;
    this.partsAsPlanned$ = this.partsFacade.partsAsPlanned$;
    this.tableAsBuiltSortList = [];
    this.tableAsPlannedSortList = [];

    window.addEventListener('keydown', event => {
      this.ctrlKeyState = event.ctrlKey;
    });
    window.addEventListener('keyup', event => {
      this.ctrlKeyState = event.ctrlKey;
    });
  }

  public bomLifecycleSize: BomLifecycleSize = this.userSettingService.getSize(UserSettingView.PARTS);

  public searchFormGroup = new FormGroup({});
  public searchControl: FormControl;

  assetFilter: AssetAsBuiltFilter | AssetAsPlannedFilter;

  public ngOnInit(): void {
    this.partsFacade.setPartsAsBuilt();
    this.partsFacade.setPartsAsPlanned();
    this.searchFormGroup.addControl('partSearch', new FormControl([]));
    this.searchControl = this.searchFormGroup.get('partSearch') as unknown as FormControl;
  }

  filterActivated(isAsBuilt: boolean, assetFilter: any): void {
    this.assetFilter = assetFilter;
    if (isAsBuilt) {
      this.partsFacade.setPartsAsBuilt(
        0,
        this.DEFAULT_PAGE_SIZE,
        this.tableAsBuiltSortList,
        toAssetFilter(this.assetFilter, true),
      );
    } else {
      this.partsFacade.setPartsAsPlanned(
        0,
        this.DEFAULT_PAGE_SIZE,
        this.tableAsPlannedSortList,
        toAssetFilter(this.assetFilter, false),
      );
    }
  }

  triggerPartSearch() {
    this.resetFilterAndShowToast();
    const searchValue = this.searchFormGroup.get('partSearch').value;

    if (searchValue && searchValue !== '') {
      console.log(searchValue);
      this.partsFacade.setPartsAsPlanned(
        0,
        this.DEFAULT_PAGE_SIZE,
        this.tableAsBuiltSortList,
        toGlobalSearchAssetFilter(searchValue, false),
        true,
      );
      this.partsFacade.setPartsAsBuilt(
        0,
        this.DEFAULT_PAGE_SIZE,
        this.tableAsPlannedSortList,
        toGlobalSearchAssetFilter(searchValue, true),
        true,
      );
    } else {
      this.partsFacade.setPartsAsBuilt();
      this.partsFacade.setPartsAsPlanned();
    }
  }

  private resetFilterAndShowToast() {
    let filterIsSet = resetMultiSelectionAutoCompleteComponent(this.partsTableComponents, false);
    if (filterIsSet) {
      console.log('Filter is set');
      this.toastService.info('parts.input.global-search.toastInfo');
    }
  }

  public ngAfterViewInit(): void {
    this.handleTableActivationEvent(this.bomLifecycleSize);
  }

  public ngOnDestroy(): void {
    this.partsFacade.unsubscribeParts();
  }

  public onSelectItem($event: Record<string, unknown>): void {
    this.partDetailsFacade.selectedPart = $event as unknown as Part;
  }

  public onAsBuiltTableConfigChange({ page, pageSize, sorting }: TableEventConfig): void {
    this.setTableSortingList(sorting, MainAspectType.AS_BUILT);

    let pageSizeValue = this.DEFAULT_PAGE_SIZE;
    if (pageSize !== 0) {
      pageSizeValue = pageSize;
    }

    if (this.assetFilter) {
      this.partsFacade.setPartsAsBuilt(
        0,
        pageSizeValue,
        this.tableAsBuiltSortList,
        toAssetFilter(this.assetFilter, true),
      );
    } else {
      this.partsFacade.setPartsAsBuilt(page, pageSizeValue, this.tableAsBuiltSortList);
    }
  }

  public onAsPlannedTableConfigChange({ page, pageSize, sorting }: TableEventConfig): void {
    this.setTableSortingList(sorting, MainAspectType.AS_PLANNED);
    let pageSizeValue = this.DEFAULT_PAGE_SIZE;
    if (pageSize !== 0) {
      pageSizeValue = pageSize;
    }

    if (this.assetFilter) {
      this.partsFacade.setPartsAsPlanned(
        0,
        pageSizeValue,
        this.tableAsPlannedSortList,
        toAssetFilter(this.assetFilter, true),
      );
    } else {
      this.partsFacade.setPartsAsPlanned(page, pageSizeValue, this.tableAsPlannedSortList);
    }
  }

  private setTableSortingList(sorting: TableHeaderSort, partTable: MainAspectType): void {
    // if a sorting Columnlist exists but a column gets resetted:
    if (!sorting && (this.tableAsBuiltSortList || this.tableAsPlannedSortList)) {
      this.resetTableSortingList(partTable);
      return;
    }

    // if CTRL is pressed at to sortList
    if (this.ctrlKeyState) {
      const [columnName] = sorting;
      const tableSortList =
        partTable === MainAspectType.AS_BUILT ? this.tableAsBuiltSortList : this.tableAsPlannedSortList;

      // Find the index of the existing entry with the same first item
      const index = tableSortList.findIndex(([itemColumnName]) => itemColumnName === columnName);

      if (index !== -1) {
        // Replace the existing entry
        tableSortList[index] = sorting;
      } else {
        // Add the new entry if it doesn't exist
        tableSortList.push(sorting);
      }
      if (partTable === MainAspectType.AS_BUILT) {
        this.tableAsBuiltSortList = tableSortList;
      } else {
        this.tableAsPlannedSortList = tableSortList;
      }
    }
    // If CTRL is not pressed just add a list with one entry
    else if (partTable === MainAspectType.AS_BUILT) {
      this.tableAsBuiltSortList = [sorting];
    } else {
      this.tableAsPlannedSortList = [sorting];
    }
  }

  private resetTableSortingList(partTable: MainAspectType): void {
    if (partTable === MainAspectType.AS_BUILT) {
      this.tableAsBuiltSortList = [];
    } else {
      this.tableAsPlannedSortList = [];
    }
  }

  public onAsDesignedTableConfigChange({ page, pageSize, sorting }: TableEventConfig): void {
    let pageSizeValue = this.DEFAULT_PAGE_SIZE;
    if (pageSize !== 0) {
      pageSizeValue = pageSize;
    }

    if (this.assetFilter) {
      this.partsFacade.setPartsAsDesignged(0, pageSizeValue, this.tableAsDesignedSortList, toAssetFilter(this.assetFilter, true));
    } else {
      this.partsFacade.setPartsAsDesignged(page, pageSizeValue, this.tableAsDesignedSortList);
    }
  }

  public onAsOrderedTableConfigChange({ page, pageSize, sorting }: TableEventConfig): void {
    let pageSizeValue = this.DEFAULT_PAGE_SIZE;
    if (pageSize !== 0) {
      pageSizeValue = pageSize;
    }

    if (this.assetFilter) {
      this.partsFacade.setPartsAsOrdered(0, pageSizeValue, this.tableAsOrderedSortList, toAssetFilter(this.assetFilter, true));
    } else {
      this.partsFacade.setPartsAsOrdered(page, pageSizeValue, this.tableAsOrderedSortList);
    }
  }

  public onAsSupportedTableConfigChange({ page, pageSize, sorting }: TableEventConfig): void {
    let pageSizeValue = this.DEFAULT_PAGE_SIZE;
    if (pageSize !== 0) {
      pageSizeValue = pageSize;
    }

    if (this.assetFilter) {
      this.partsFacade.setPartsAsSupported(0, pageSizeValue, this.tableAsSupportedSortList, toAssetFilter(this.assetFilter, true));
    } else {
      this.partsFacade.setPartsAsSupported(page, pageSizeValue, this.tableAsSupportedSortList);
    }
  }

  public onAsRecycledTableConfigChange({ page, pageSize, sorting }: TableEventConfig): void {
    let pageSizeValue = this.DEFAULT_PAGE_SIZE;
    if (pageSize !== 0) {
      pageSizeValue = pageSize;
    }

    if (this.assetFilter) {
      this.partsFacade.setPartsAsRecycled(0, pageSizeValue, this.tableAsRecycledSortList, toAssetFilter(this.assetFilter, true));
    } else {
      this.partsFacade.setPartsAsRecycled(page, pageSizeValue, this.tableAsRecycledSortList);
    }
  }

  public handleTableActivationEvent(bomLifecycleSize: BomLifecycleSize) {
    this.bomLifecycleSize = bomLifecycleSize;
  }

  // Set parts count for each table
  public setPartsCount(count: number, partTable: MainAspectType): void {
    switch (partTable) {
      case MainAspectType.AS_BUILT:
        this.asBuiltCount = count;
        break;

      case MainAspectType.AS_PLANNED:
        this.asPlannedCount = count;
        break;

      case MainAspectType.AS_DESIGNED:
        this.asDesignedCount = count;
        break;

      case MainAspectType.AS_ORDERED:
        this.asOrderedCount = count;
        break;

      case MainAspectType.AS_SUPPORTED:
        this.asSupportedCount = count;
        break;

      case MainAspectType.AS_RECYCLED:
        this.asRecycledCount = count;
        break;
    }

    this.changeDetectorRef.detectChanges();
  }
}
