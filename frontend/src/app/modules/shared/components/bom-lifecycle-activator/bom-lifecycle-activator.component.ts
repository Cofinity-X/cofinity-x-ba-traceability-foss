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
import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { MatChipInputEvent } from '@angular/material/chips';
import {
    BomLifecycleConfig,
    BomLifecycleSize,
    BomLifecycleType
} from "@shared/components/bom-lifecycle-activator/bom-lifecycle-activator.model";
import { BomLifecycleSettingsService, UserSettingView } from "@shared/service/bom-lifecycle-settings.service";
import { MatAutocompleteModule, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { FormControl, FormsModule } from '@angular/forms';
import { MatOptionSelectionChange } from '@angular/material/core';

@Component({
    selector: 'app-bom-lifecycle-activator',
    templateUrl: './bom-lifecycle-activator.component.html',
    styleUrls: ['./bom-lifecycle-activator.component.scss'],
})
export class BomLifecycleActivatorComponent implements OnInit {

    @Input() view: UserSettingView;
    @Output() bomLifecycleConfigChanged = new EventEmitter<BomLifecycleConfig>();
    public bomLifecycleConfig: BomLifecycleConfig;
    public lifecycleTypes: string[] = Object.values(BomLifecycleType);
    public lifecycleCtrl = new FormControl('');
    public selectedLifecycles: string[] = [];

    constructor(public bomLifeCycleUserSetting: BomLifecycleSettingsService) {

    }

    ngOnInit() {
        if (this.view) {
            this.bomLifecycleConfig = this.bomLifeCycleUserSetting.getUserSettings(this.view);

            // update the selected items
            if (this.bomLifecycleConfig.asDesignedActive) {
                this.selectedLifecycles.push(BomLifecycleType.AS_DESIGNED);
            }
            if (this.bomLifecycleConfig.asBuiltActive) {
                this.selectedLifecycles.push(BomLifecycleType.AS_BUILT);
            }
            if (this.bomLifecycleConfig.asOrderedActive) {
                this.selectedLifecycles.push(BomLifecycleType.AS_ORDERED);
            }
            if (this.bomLifecycleConfig.asPlannedActive) {
                this.selectedLifecycles.push(BomLifecycleType.AS_PLANNED);
            }
            if (this.bomLifecycleConfig.asSupportedActive) {
                this.selectedLifecycles.push(BomLifecycleType.AS_SUPPORTED);
            }
            if (this.bomLifecycleConfig.asRecycledActive) {
                this.selectedLifecycles.push(BomLifecycleType.AS_RECYCLED);
            }
        } else {
            throw new DOMException("Unsupported view", "BomLifecycleActivatorComponent");
        }
    }

    @Output() buttonClickEvent = new EventEmitter<BomLifecycleSize>();

    selected(event: MatAutocompleteSelectedEvent): void {
        const value = event.option.viewValue;
        this.selectedLifecycles.push(value);

        this.updateLifecycleConfig(value, true);
    }

    isSelected(value: string): boolean {
        return this.selectedLifecycles.includes(value);
    }

    disableOption(value: string): boolean {
        return this.selectedLifecycles.includes(value) === false && this.selectedLifecycles.length === 2;
    }

    updateLifecycleConfig(value: string, state: boolean) {
        const index = Object.values(BomLifecycleType).indexOf(value as unknown as BomLifecycleType);

        switch (index) {
            case 0: {
                this.bomLifecycleConfig.asDesignedActive = state;
                break;
            }
            case 1: {
                this.bomLifecycleConfig.asPlannedActive = state;
                break;
            }
            case 2: {
                this.bomLifecycleConfig.asOrderedActive = state;
                break;
            }
            case 3: {
                this.bomLifecycleConfig.asBuiltActive = state;
                break;
            }
            case 4: {
                this.bomLifecycleConfig.asSupportedActive = state;
                break;
            }
            case 5: {
                this.bomLifecycleConfig.asRecycledActive = state;
                break;
            }
        }

        this.bomLifeCycleUserSetting.setUserSettings({
            asDesignedActive: this.bomLifecycleConfig.asDesignedActive,
            asBuiltActive: this.bomLifecycleConfig.asBuiltActive,
            asOrderedActive: this.bomLifecycleConfig.asOrderedActive,
            asPlannedActive: this.bomLifecycleConfig.asPlannedActive,
            asSupportedActive: this.bomLifecycleConfig.asSupportedActive,
            asRecycledActive: this.bomLifecycleConfig.asRecycledActive
        }, this.view);

        this.buttonClickEvent.emit(this.bomLifeCycleUserSetting.getSize(this.view));
    }

    selectionChanged(values: string[]) {
        this.disabledAllLifecycleStates();

        for (let i = 0; i < values.length; i++) {
            this.updateLifecycleConfig(values[i], true);
        }
    }

    removeLifeCycle(lifeCycle: string): void {
        const index = this.selectedLifecycles.indexOf(lifeCycle);

        if (index >= 0) {
            this.selectedLifecycles.splice(index, 1);
            this.selectedLifecycles = Array.from(this.selectedLifecycles);
        }

        this.updateLifecycleConfig(lifeCycle, false);
    }

    disabledAllLifecycleStates() {
        for (let i = 0; i < this.lifecycleTypes.length; i++) {
            this.updateLifecycleConfig(this.lifecycleTypes[i], false);
        }
    }

    emitBomLifecycleState() {
        this.bomLifeCycleUserSetting.setUserSettings({
            asDesignedActive: this.bomLifecycleConfig.asDesignedActive,
            asBuiltActive: this.bomLifecycleConfig.asBuiltActive,
            asOrderedActive: this.bomLifecycleConfig.asOrderedActive,
            asPlannedActive: this.bomLifecycleConfig.asPlannedActive,
            asSupportedActive: this.bomLifecycleConfig.asSupportedActive,
            asRecycledActive: this.bomLifecycleConfig.asRecycledActive
        }, this.view);
        this.buttonClickEvent.emit(this.bomLifeCycleUserSetting.getSize(this.view));
    }
}
