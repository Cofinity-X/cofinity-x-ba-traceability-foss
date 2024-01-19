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


import { DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, QueryList, ViewChildren } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { OTHER_PARTS_BASE_ROUTE, getRoute } from '@core/known-route';
import { Pagination } from '@core/model/pagination.model';
import { PartsFacade } from '@page/parts/core/parts.facade';
import { MainAspectType } from '@page/parts/model/mainAspectType.enum';
import { AssetAsBuiltFilter, AssetAsDesignedFilter, AssetAsOrderedFilter, AssetAsPlannedFilter, AssetAsRecycledFilter, AssetAsSupportedFilter, Part } from '@page/parts/model/parts.model';
import { PartsTableComponent } from '@shared/components/parts-table/parts-table.component';
import { RequestContext } from '@shared/components/request-notification/request-notification.base';
import { RequestStepperComponent } from '@shared/components/request-notification/request-stepper/request-stepper.component';
import { PartTableType, TableEventConfig, TableHeaderSort } from '@shared/components/table/table.model';
import { toAssetFilter, toGlobalSearchAssetFilter } from '@shared/helper/filter-helper';
import { ToastService } from '@shared/index';
import { View } from '@shared/model/view.model';
import { PartDetailsFacade } from '@shared/modules/part-details/core/partDetails.facade';
import { StaticIdService } from '@shared/service/staticId.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Component({
  selector: 'app-own-parts',
  templateUrl: './own-parts.component.html',
  styleUrls: ['../parts.component.scss'],
})
export class OwnPartsComponent implements OnInit, OnDestroy {
  protected readonly MainAspectType = MainAspectType;
  protected readonly PartTableType = PartTableType;

  public readonly titleId = this.staticIdService.generateId('PartsComponent.title');

  @Output() onPartsSelected = new EventEmitter<Part[]>();
  @Output() onTotalItemsChanged = new EventEmitter<number>();
  @Output() onPartDeselected = new EventEmitter<Part>();

  public readonly partsAsBuilt$: Observable<View<Pagination<Part>>>;
  public readonly partsAsPlanned$: Observable<View<Pagination<Part>>>;
  public readonly partsAsDesigned$: Observable<View<Pagination<Part>>>;
  public readonly partsAsOrdered$: Observable<View<Pagination<Part>>>;
  public readonly partsAsSupported$: Observable<View<Pagination<Part>>>;
  public readonly partsAsRecycled$: Observable<View<Pagination<Part>>>;

  public readonly deselectPartTrigger$ = new Subject<Part[]>();
  public readonly addPartTrigger$ = new Subject<Part>();
  public readonly currentSelectedItems$ = new BehaviorSubject<Part[]>([]);

  public tableAsBuiltSortList: TableHeaderSort[];
  public tableAsPlannedSortList: TableHeaderSort[];
  public tableAsDesignedSortList: TableHeaderSort[];
  public tableAsSupportedSortList: TableHeaderSort[];
  public tableAsOrderedSortList: TableHeaderSort[];
  public tableAsRecycledSortList: TableHeaderSort[];

  public assetAsBuiltFilter: AssetAsBuiltFilter;
  public assetAsPlannedFilter: AssetAsPlannedFilter;
  public assetAsDesignedFilter: AssetAsDesignedFilter;
  public assetAsRecycledFilter: AssetAsRecycledFilter;
  public assetAsSupportedFilter: AssetAsSupportedFilter;
  public assetAsOrderedFilter: AssetAsOrderedFilter;

  public readonly searchListAsBuilt: string[];
  public readonly searchListAsPlanned: string[];

  public DEFAULT_PAGE_SIZE = 50;
  public ctrlKeyState = false;
  public globalSearchActive = false;

  @Input() public bomLifecycle: MainAspectType;
  @Input() public showActionButton = true;
  @Input() public showHeader = true;
  @Input() public compactSize = false;

  @ViewChildren(PartsTableComponent) partsTableComponents: QueryList<PartsTableComponent>;

  constructor(
    private readonly partsFacade: PartsFacade,
    private readonly partDetailsFacade: PartDetailsFacade,
    private readonly staticIdService: StaticIdService,
    public dialog: MatDialog,
    private readonly router: Router,
    public toastService: ToastService,
    public datePipe: DatePipe,
  ) {
    this.partsAsBuilt$ = this.partsFacade.partsAsBuilt$;
    this.partsAsPlanned$ = this.partsFacade.partsAsPlanned$;
    this.partsAsDesigned$ = this.partsFacade.partsAsDesigned$;
    this.partsAsOrdered$ = this.partsFacade.partsAsOrdered$;
    this.partsAsSupported$ = this.partsFacade.partsAsSupported$;
    this.partsAsRecycled$ = this.partsFacade.partsAsRecycled$;

    this.tableAsBuiltSortList = [];
    this.tableAsPlannedSortList = [];
    this.tableAsDesignedSortList = [];
    this.tableAsOrderedSortList = [];
    this.tableAsSupportedSortList = [];
    this.tableAsRecycledSortList = [];
    this.searchListAsBuilt = [
      'id',
      'idShort',
      'nameAtManufacturer',
      'manufacturerName',
      'manufacturerPartId',
      'customerPartId',
      'classification',
      'nameAtCustomer',
      'semanticDataModel',
      'semanticModelId',
      'manufacturingDate',
      'manufacturingCountry',
    ];
    this.searchListAsPlanned = [
      'id',
      'idShort',
      'nameAtManufacturer',
      'manufacturerName',
      'manufacturerPartId',
      'classification',
      'semanticDataModel',
      'semanticModelId',
      'validityPeriodFrom',
      'validityPeriodTo',
      'function',
      'catenaXSiteId',
      'functionValidFrom',
      'functionValidUntil',
    ];

    this.assetAsBuiltFilter = {};
    this.assetAsDesignedFilter = {};
    this.assetAsOrderedFilter = {};
    this.assetAsPlannedFilter = {};
    this.assetAsRecycledFilter = {};
    this.assetAsSupportedFilter = {};

    window.addEventListener('keydown', event => {
      this.ctrlKeyState = event.ctrlKey;
    });
    window.addEventListener('keyup', event => {
      this.ctrlKeyState = event.ctrlKey;
    });
  }

  public ngOnInit(): void {
    if (this.bomLifecycle === MainAspectType.AS_BUILT) {
      this.partsFacade.setPartsAsBuilt();
    } else if (this.bomLifecycle === MainAspectType.AS_PLANNED) {
      this.partsFacade.setPartsAsPlanned();
    }
  }

  public updateOwnParts(searchValue?: string) {
    if (searchValue && searchValue !== '') {
      this.globalSearchActive = true;
      this.assetAsBuiltFilter = toGlobalSearchAssetFilter(searchValue, true, this.searchListAsBuilt, this.datePipe);
      this.assetAsPlannedFilter = toGlobalSearchAssetFilter(searchValue, false, this.searchListAsPlanned, this.datePipe);
      this.partsFacade.setPartsAsBuilt(
        0,
        this.DEFAULT_PAGE_SIZE,
        this.tableAsPlannedSortList,
        this.assetAsBuiltFilter,
        this.globalSearchActive,
      );
      this.partsFacade.setPartsAsPlanned(
        0,
        this.DEFAULT_PAGE_SIZE,
        this.tableAsBuiltSortList,
        this.assetAsPlannedFilter,
        this.globalSearchActive,
      );
    } else {
      this.globalSearchActive = false;
      this.assetAsBuiltFilter = {};
      this.assetAsPlannedFilter = {};
      this.partsFacade.setPartsAsBuilt(0, this.DEFAULT_PAGE_SIZE);
      this.partsFacade.setPartsAsPlanned(0, this.DEFAULT_PAGE_SIZE);
    }
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(RequestStepperComponent, {
      autoFocus: false,
      data: {
        selectedItems: this.currentSelectedItems$.value,
        context: RequestContext.REQUEST_ALERT,
        tabIndex: 1,
        fromExternal: true,
      },
    });

    const callback = (part: Part) => {
      this.deselectPartTrigger$.next([part]);
    };

    dialogRef?.componentInstance.deselectPart.subscribe(callback);
    if (dialogRef?.afterClosed) {
      dialogRef.afterClosed().subscribe((_part: Part) => {
        dialogRef.componentInstance.deselectPart.unsubscribe();
      });
    }

  }

  filterActivated(type: MainAspectType, assetFilter: any): void {
    this.globalSearchActive = false;
    switch (type) {
      case MainAspectType.AS_BUILT: {
        this.assetAsBuiltFilter = assetFilter;
        this.partsFacade.setPartsAsBuilt(
          0,
          this.DEFAULT_PAGE_SIZE,
          this.tableAsBuiltSortList,
          toAssetFilter(this.assetAsBuiltFilter, true),
          this.globalSearchActive,
        );
        break;
      }
      case MainAspectType.AS_PLANNED: {
        this.assetAsPlannedFilter = assetFilter;
        this.partsFacade.setPartsAsPlanned(
          0,
          this.DEFAULT_PAGE_SIZE,
          this.tableAsPlannedSortList,
          toAssetFilter(this.assetAsPlannedFilter, false),
          this.globalSearchActive,
        );
        break;
      }
      case MainAspectType.AS_DESIGNED: {
        this.assetAsDesignedFilter = assetFilter;
        this.partsFacade.setPartsAsDesigned(
          0,
          this.DEFAULT_PAGE_SIZE,
          this.tableAsDesignedSortList,
          toAssetFilter(this.assetAsDesignedFilter, true),
          this.globalSearchActive,
        );
        break;
      }
      case MainAspectType.AS_ORDERED: {
        this.assetAsOrderedFilter = assetFilter;
        this.partsFacade.setPartsAsOrdered(
          0,
          this.DEFAULT_PAGE_SIZE,
          this.tableAsOrderedSortList,
          toAssetFilter(this.assetAsOrderedFilter, true),
          this.globalSearchActive,
        );
        break;
      }
      case MainAspectType.AS_SUPPORTED: {
        this.assetAsSupportedFilter = assetFilter;
        this.partsFacade.setPartsAsSupported(
          0,
          this.DEFAULT_PAGE_SIZE,
          this.tableAsSupportedSortList,
          toAssetFilter(this.assetAsSupportedFilter, true),
          this.globalSearchActive,
        );
        break;
      }
      case MainAspectType.AS_RECYCLED: {
        this.assetAsRecycledFilter = assetFilter;
        this.partsFacade.setPartsAsRecycled(
          0,
          this.DEFAULT_PAGE_SIZE,
          this.tableAsRecycledSortList,
          toAssetFilter(this.assetAsRecycledFilter, true),
          this.globalSearchActive,
        );
        break;
      }
    }
  }

  public ngOnDestroy(): void {
    this.partsFacade.unsubscribeParts();
  }

  public onSelectItem($event: Record<string, unknown>): void {
    const selectedPart = $event as unknown as Part;
    this.partDetailsFacade.mainAspectType = selectedPart.mainAspectType;
    this.partDetailsFacade.selectedPart = selectedPart;
    this.openDetailPage(selectedPart);
  }

  public openDetailPage(part: Part): void {
    const { link } = getRoute(OTHER_PARTS_BASE_ROUTE);
    this.router.navigate([`/${link}/${part.id}`], { queryParams: { type: part.mainAspectType } })?.then(_ => window.location.reload());
  }

  public onAsBuiltTableConfigChange({ page, pageSize, sorting }: TableEventConfig): void {
    let pageSizeValue = this.DEFAULT_PAGE_SIZE;
    if (pageSize !== 0) {
      pageSizeValue = pageSize;
    }
    this.setTableSortingList(sorting, MainAspectType.AS_BUILT);
    this.partsFacade.setPartsAsBuilt(page, pageSizeValue, this.tableAsBuiltSortList, toAssetFilter(this.assetAsBuiltFilter, true), this.globalSearchActive);
  }

  public onAsPlannedTableConfigChange({ page, pageSize, sorting }: TableEventConfig): void {
    let pageSizeValue = this.DEFAULT_PAGE_SIZE;
    if (pageSize !== 0) {
      pageSizeValue = pageSize;
    }
    this.setTableSortingList(sorting, MainAspectType.AS_PLANNED);
    this.partsFacade.setPartsAsPlanned(page, pageSizeValue, this.tableAsPlannedSortList, toAssetFilter(this.assetAsPlannedFilter, false), this.globalSearchActive);
  }

  public onMultiSelect(event: Part[]): void {
    this.currentSelectedItems$.next(event);
  }

  public onDefaultPaginationSizeChange(pageSize: number) {
    this.DEFAULT_PAGE_SIZE = pageSize;
  }

  public removeItemFromSelection(part: Part): void {
    this.deselectPartTrigger$.next([part]);
    this.onPartDeselected.emit(part);
  }

  public clearSelected(): void {
    this.deselectPartTrigger$.next(this.currentSelectedItems$.value);
  }

  public addItemToSelection(part: Part): void {
    this.addPartTrigger$.next(part);
  }

  private setTableSortingList(sorting: TableHeaderSort, partTable: MainAspectType): void {
    // if a sorting Columnlist exists but a column gets resetted:
    if (
      !sorting &&
      (this.tableAsBuiltSortList ||
        this.tableAsPlannedSortList ||
        this.tableAsDesignedSortList ||
        this.tableAsOrderedSortList ||
        this.tableAsSupportedSortList ||
        this.tableAsRecycledSortList)
    ) {
      this.resetTableSortingList(partTable);
      return;
    }

    // if CTRL is pressed at to sortList
    if (this.ctrlKeyState) {
      const [columnName] = sorting;
      let tableSortList: TableHeaderSort[] = [];
      switch (partTable) {
        case MainAspectType.AS_BUILT: {
          tableSortList = this.tableAsBuiltSortList;
          break;
        }
        case MainAspectType.AS_PLANNED: {
          tableSortList = this.tableAsPlannedSortList;
          break;
        }
        case MainAspectType.AS_DESIGNED: {
          tableSortList = this.tableAsDesignedSortList;
          break;
        }
        case MainAspectType.AS_ORDERED: {
          tableSortList = this.tableAsOrderedSortList;
          break;
        }
        case MainAspectType.AS_SUPPORTED: {
          tableSortList = this.tableAsSupportedSortList;
          break;
        }
        case MainAspectType.AS_RECYCLED: {
          tableSortList = this.tableAsRecycledSortList;
          break;
        }
      }

      // Find the index of the existing entry with the same first item
      const index = tableSortList.findIndex(([itemColumnName]) => itemColumnName === columnName);

      if (index !== -1) {
        // Replace the existing entry
        tableSortList[index] = sorting;
      } else {
        // Add the new entry if it doesn't exist
        tableSortList.push(sorting);
      }

      switch (partTable) {
        case MainAspectType.AS_BUILT: {
          this.tableAsBuiltSortList = tableSortList;
          break;
        }
        case MainAspectType.AS_PLANNED: {
          this.tableAsPlannedSortList = tableSortList;
          break;
        }
        case MainAspectType.AS_DESIGNED: {
          this.tableAsDesignedSortList = tableSortList;
          break;
        }
        case MainAspectType.AS_ORDERED: {
          this.tableAsOrderedSortList = tableSortList;
          break;
        }
        case MainAspectType.AS_SUPPORTED: {
          this.tableAsSupportedSortList = tableSortList;
          break;
        }
        case MainAspectType.AS_RECYCLED: {
          this.tableAsRecycledSortList = tableSortList;
          break;
        }
      }
    }
    // If CTRL is not pressed just add a list with one entry
    else {
      switch (partTable) {
        case MainAspectType.AS_BUILT: {
          this.tableAsBuiltSortList = [sorting];
          break;
        }
        case MainAspectType.AS_PLANNED: {
          this.tableAsPlannedSortList = [sorting];
          break;
        }
        case MainAspectType.AS_DESIGNED: {
          this.tableAsDesignedSortList = [sorting];
          break;
        }
        case MainAspectType.AS_ORDERED: {
          this.tableAsOrderedSortList = [sorting];
          break;
        }
        case MainAspectType.AS_SUPPORTED: {
          this.tableAsSupportedSortList = [sorting];
          break;
        }
        case MainAspectType.AS_RECYCLED: {
          this.tableAsRecycledSortList = [sorting];
          break;
        }
      }
    }
  }

  private resetTableSortingList(partTable: MainAspectType): void {
    switch (partTable) {
      case MainAspectType.AS_BUILT: {
        this.tableAsBuiltSortList = [];
        break;
      }
      case MainAspectType.AS_PLANNED: {
        this.tableAsPlannedSortList = [];
        break;
      }
      case MainAspectType.AS_DESIGNED: {
        this.tableAsDesignedSortList = [];
        break;
      }
      case MainAspectType.AS_ORDERED: {
        this.tableAsOrderedSortList = [];
        break;
      }
      case MainAspectType.AS_SUPPORTED: {
        this.tableAsSupportedSortList = [];
        break;
      }
      case MainAspectType.AS_RECYCLED: {
        this.tableAsRecycledSortList = [];
        break;
      }
    }
  }
}
