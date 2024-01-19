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

import { AfterViewInit, Component, OnDestroy, OnInit, QueryList, ViewChildren } from '@angular/core';
import { PartsFacade } from '@page/parts/core/parts.facade';
import { MainAspectType } from '@page/parts/model/mainAspectType.enum';
import {
  Part,
} from '@page/parts/model/parts.model';
import { PartTableType } from '@shared/components/table/table.model';
import { PartDetailsFacade } from '@shared/modules/part-details/core/partDetails.facade';
import { StaticIdService } from '@shared/service/staticId.service';
import { BehaviorSubject, Subject } from 'rxjs';
import { BomLifecycleSize } from '@shared/components/bom-lifecycle-activator/bom-lifecycle-activator.model';
import { BomLifecycleSettingsService, UserSettingView } from '@shared/service/bom-lifecycle-settings.service';
import { FormControl, FormGroup } from '@angular/forms';
import { ToastService } from '@shared/components/toasts/toast.service';
import { SearchHelper } from '@shared/helper/search-helper';
import { OwnPartsComponent } from './own-parts/own-parts.component';

@Component({
  selector: 'app-parts',
  templateUrl: './parts.component.html',
  styleUrls: ['./parts.component.scss'],
})
export class PartsComponent implements OnInit, OnDestroy, AfterViewInit {
  public readonly titleId = this.staticIdService.generateId('PartsComponent.title');

  public readonly deselectPartTrigger$ = new Subject<Part[]>();
  public readonly addPartTrigger$ = new Subject<Part>();
  public readonly currentSelectedItems$ = new BehaviorSubject<Part[]>([]);

  public readonly searchListAsBuilt: string[];
  public readonly searchListAsPlanned: string[];

  public DEFAULT_PAGE_SIZE = 50;
  public ctrlKeyState = false;
  public globalSearchActive = false;

  protected readonly UserSettingView = UserSettingView;
  protected readonly PartTableType = PartTableType;
  protected readonly MainAspectType = MainAspectType;
  public readonly searchHelper = new SearchHelper();

  @ViewChildren(OwnPartsComponent) ownPartsComponents: QueryList<OwnPartsComponent>;

  constructor(
    private readonly partsFacade: PartsFacade,
    private readonly partDetailsFacade: PartDetailsFacade,
    private readonly staticIdService: StaticIdService,
    public userSettings: BomLifecycleSettingsService,
    public toastService: ToastService,
  ) { }

  public bomLifecycleSize: BomLifecycleSize = this.userSettings.getSize(UserSettingView.PARTS);

  public searchFormGroup = new FormGroup({});
  public searchControl: FormControl;

  public ngOnInit(): void {
    this.searchFormGroup.addControl('partSearch', new FormControl([]));
    this.searchControl = this.searchFormGroup.get('partSearch') as unknown as FormControl;
  }

  public ngOnDestroy(): void {
    this.partsFacade.unsubscribeParts();
  }

  triggerPartSearch() {
    this.resetFilterAndShowToast();

    const searchValue = this.searchFormGroup.get('partSearch').value;

    for (const ownPartsComponent of this.ownPartsComponents) {
      ownPartsComponent.updateOwnParts(searchValue);
    }
  }

  private resetFilterAndShowToast() {
    const resetComponents = (components: QueryList<OwnPartsComponent>) => {
      for (const component of components) {
        const filterIsSet = this.searchHelper.resetFilterForAssetComponents(component.partsTableComponents, false);
        if (filterIsSet) {
          this.toastService.info('parts.input.global-search.toastInfo');
        }
      }
    };
    resetComponents(this.ownPartsComponents);
  }

  public ngAfterViewInit(): void {
    this.handleTableActivationEvent(this.bomLifecycleSize);
  }

  public handleTableActivationEvent(bomLifecycleSize: BomLifecycleSize) {
    this.bomLifecycleSize = bomLifecycleSize;
  }
}
