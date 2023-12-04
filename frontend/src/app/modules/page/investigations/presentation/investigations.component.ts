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

import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { getRoute, INVESTIGATION_BASE_ROUTE } from '@core/known-route';
import { InvestigationDetailFacade } from '@page/investigations/core/investigation-detail.facade';
import { InvestigationHelperService } from '@page/investigations/core/investigation-helper.service';
import { NotificationMenuActionsAssembler } from '@shared/assembler/notificationMenuActions.assembler';
import { NotificationCommonModalComponent } from '@shared/components/notification-common-modal/notification-common-modal.component';
import {
  MenuActionConfig,
  PartTableType,
  TableEventConfig,
  TableHeaderSort,
  TableFilter,
  FilterMethod,
  FilterInfo,
} from '@shared/components/table/table.model';
import { TableSortingUtil } from '@shared/components/table/tableSortingUtil';
import { FilterCongigOptions } from '@shared/model/filter-config';
import { NotificationTabInformation } from '@shared/model/notification-tab-information';
import { Notification, NotificationStatusGroup } from '@shared/model/notification.model';
import { TranslationContext } from '@shared/model/translation-context.model';
import { Subscription } from 'rxjs';
import { InvestigationsFacade } from '../core/investigations.facade';
import { FormGroup, FormControl } from '@angular/forms';
import { SearchHelper } from '@shared/helper/search-helper';
import { ToastService } from '@shared/index';
import { NotificationComponent } from '@shared/modules/notification/presentation/notification.component';
import { FilterOperator } from '@page/parts/model/parts.model';

@Component({
  selector: 'app-investigations',
  templateUrl: './investigations.component.html',
  styleUrls: ['./investigations.component.scss'],
})
export class InvestigationsComponent {
  @ViewChild(NotificationCommonModalComponent) notificationCommonModalComponent: NotificationCommonModalComponent;
  @ViewChild(NotificationComponent) notificationComponent: NotificationComponent;

  public searchFormGroup = new FormGroup({});
  public searchControl: FormControl;
  public readonly investigationsReceived$;
  public readonly investigationsQueuedAndRequested$;

  public menuActionsConfig: MenuActionConfig<Notification>[];

  public investigationReceivedSortList: TableHeaderSort[] = [];
  public investigationQueuedAndRequestedSortList: TableHeaderSort[] = [];

  public filterReceived: TableFilter = { filterMethod: FilterMethod.AND };
  public filterQueuedAndRequested: TableFilter = { filterMethod: FilterMethod.AND };
  public readonly filterConfigOptions = new FilterCongigOptions();
  public investigationsReceivedFilterConfiguration: any[];
  public investigationsQueuedAndRequestedFilterConfiguration: any[];
  private paramSubscription: Subscription;
  private ctrlKeyState = false;
  public DEFAULT_PAGE_SIZE = 50;

  private pagination: TableEventConfig = {
    page: 0,
    pageSize: this.DEFAULT_PAGE_SIZE,
    sorting: ['createdDate', 'desc'],
  };

  protected readonly PartTableType = PartTableType;
  public readonly searchHelper = new SearchHelper();

  constructor(
    public readonly helperService: InvestigationHelperService,
    public readonly investigationsFacade: InvestigationsFacade,
    private readonly investigationDetailFacade: InvestigationDetailFacade,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly cd: ChangeDetectorRef,
    public toastService: ToastService,
  ) {
    this.investigationsReceived$ = this.investigationsFacade.investigationsReceived$;
    this.investigationsQueuedAndRequested$ = this.investigationsFacade.investigationsQueuedAndRequested$;

    window.addEventListener('keydown', event => {
      this.ctrlKeyState = event.ctrlKey;
    });
    window.addEventListener('keyup', event => {
      this.ctrlKeyState = event.ctrlKey;
    });
  }

  public ngOnInit(): void {
    this.paramSubscription = this.route.queryParams.subscribe(params => {
      this.pagination.page = params?.pageNumber;
      this.investigationsFacade.setReceivedInvestigations(
        this.pagination.page,
        this.pagination.pageSize,
        this.investigationReceivedSortList,
        this.filterReceived,
      );
      this.investigationsFacade.setQueuedAndRequestedInvestigations(
        this.pagination.page,
        this.pagination.pageSize,
        this.investigationQueuedAndRequestedSortList,
        this.filterQueuedAndRequested,
      );
    });
    this.setupFilterConfig();
    const searchControlName = 'investigationSearch';
    this.searchFormGroup.addControl(searchControlName, new FormControl([]));
    this.searchControl = this.searchFormGroup.get(searchControlName) as unknown as FormControl;
  }

  public ngAfterViewInit(): void {
    this.menuActionsConfig = NotificationMenuActionsAssembler.getMenuActions(
      this.helperService,
      this.notificationCommonModalComponent,
    );
    this.cd.detectChanges();
  }

  public ngOnDestroy(): void {
    this.investigationsFacade.stopInvestigations();
    this.paramSubscription?.unsubscribe();
  }

  private setupFilterConfig() {
    const { createdDate, description, status, targetDate, severity, createdBy, sendTo } =
      this.filterConfigOptions.filterKeyOptionsNotifications;
    this.investigationsReceivedFilterConfiguration = [
      createdDate,
      description,
      status(TranslationContext.COMMONINVESTIGATION, true),
      targetDate,
      severity,
      createdBy,
    ];
    this.investigationsQueuedAndRequestedFilterConfiguration = [
      createdDate,
      description,
      status(TranslationContext.COMMONINVESTIGATION, false),
      targetDate,
      severity,
      sendTo,
    ];
  }

  public onReceivedTableConfigChanged(pagination: TableEventConfig) {
    this.pagination = pagination;
    if (this.pagination.pageSize === 0) {
      this.pagination.pageSize = this.DEFAULT_PAGE_SIZE;
    }
    this.setTableSortingList(pagination.sorting, NotificationStatusGroup.RECEIVED);
    if (pagination.filtering && Object.keys(pagination.filtering).length > 1) {
      this.filterReceived = pagination.filtering;
    }
    this.investigationsFacade.setReceivedInvestigations(
      this.pagination.page,
      this.pagination.pageSize,
      this.investigationReceivedSortList,
      this.filterReceived,
    );
  }

  public onQueuedAndRequestedTableConfigChanged(pagination: TableEventConfig) {
    this.pagination = pagination;
    if (this.pagination.pageSize === 0) {
      this.pagination.pageSize = this.DEFAULT_PAGE_SIZE;
    }
    this.setTableSortingList(pagination.sorting, NotificationStatusGroup.QUEUED_AND_REQUESTED);
    if (pagination.filtering && Object.keys(pagination.filtering).length > 1) {
      this.filterQueuedAndRequested = pagination.filtering;
    }
    this.investigationsFacade.setQueuedAndRequestedInvestigations(
      this.pagination.page,
      this.pagination.pageSize,
      this.investigationQueuedAndRequestedSortList,
      this.filterQueuedAndRequested,
    );
  }

  public onDefaultPaginationSizeChange(pageSize: number) {
    this.DEFAULT_PAGE_SIZE = pageSize;
  }

  public openDetailPage(notification: Notification): void {
    this.investigationDetailFacade.selected = { data: notification };
    const { link } = getRoute(INVESTIGATION_BASE_ROUTE);
    const tabIndex = this.route.snapshot.queryParamMap.get('tabIndex');
    const tabInformation: NotificationTabInformation = { tabIndex: tabIndex, pageNumber: this.pagination.page };
    this.router.navigate([`/${link}/${notification.id}`], { queryParams: tabInformation });
  }

  public handleConfirmActionCompletedEvent() {
    this.ngOnInit();
  }

  public triggerSearch(): void {
    this.searchHelper.resetFilterAndShowToast(false, this.notificationComponent, this.toastService);
    const searchValue = this.searchControl.value;
    const filterInfo: FilterInfo = { filterValue: searchValue, filterOperator: FilterOperator.STARTS_WITH };
    this.filterReceived = { filterMethod: FilterMethod.OR, description: filterInfo, createdBy: filterInfo };
    this.filterQueuedAndRequested = { filterMethod: FilterMethod.OR, description: filterInfo, sendTo: filterInfo };

    this.investigationsFacade.setReceivedInvestigations(this.pagination.page, this.pagination.pageSize, this.investigationReceivedSortList, this.filterReceived);
    this.investigationsFacade.setQueuedAndRequestedInvestigations(this.pagination.page, this.pagination.pageSize, this.investigationQueuedAndRequestedSortList, this.filterQueuedAndRequested);
  }

  private setTableSortingList(sorting: TableHeaderSort, notificationTable: NotificationStatusGroup): void {
    const tableSortList =
      notificationTable === NotificationStatusGroup.RECEIVED
        ? this.investigationReceivedSortList
        : this.investigationQueuedAndRequestedSortList;
    TableSortingUtil.setTableSortingList(sorting, tableSortList, this.ctrlKeyState);
  }

  protected readonly TranslationContext = TranslationContext;
}
