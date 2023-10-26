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
    public lifeCycleTypes: string[] = Object.values(BomLifecycleType);
    public lifeCycleCtrl = new FormControl('');
    public selectedLifeCycles: string[] = [];

    constructor(public bomLifeCycleUserSetting: BomLifecycleSettingsService) {

    }

    ngOnInit() {
        if (this.view) {
            this.bomLifecycleConfig = this.bomLifeCycleUserSetting.getUserSettings(this.view);
        } else {
            throw new DOMException("Unsupported view", "BomLifecycleActivatorComponent");
        }
    }

    @Output() buttonClickEvent = new EventEmitter<BomLifecycleSize>();

    selected(event: MatAutocompleteSelectedEvent): void {
        const value = event.option.viewValue;
        this.selectedLifeCycles.push(value);

        this.updateLifecycleConfig(value, true);
    }

    disableOption(value: string): boolean {
        return this.selectedLifeCycles.includes(value) === false && this.selectedLifeCycles.length === 2;
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

    selectLifeCycle(lifeCycle: string): void {
        if (this.selectedLifeCycles.includes(lifeCycle) === true) {
            var index = this.selectedLifeCycles.indexOf(lifeCycle);
            this.selectedLifeCycles.splice(index, 1)
        } else {
            this.selectedLifeCycles.push(lifeCycle);
        }

        this.bomLifecycleConfigChanged.emit(this.bomLifecycleConfig);
    }

    removeLifeCycle(lifeCycle: string): void {
        const index = this.selectedLifeCycles.indexOf(lifeCycle);

        if (index >= 0) {
            this.selectedLifeCycles.splice(index, 1);
            this.selectedLifeCycles = Array.from(this.selectedLifeCycles);
        }

        this.updateLifecycleConfig(lifeCycle, false);
    }

    toggleLifecycleType(value: string): void {
        const index = Object.values(BomLifecycleType).indexOf(value as unknown as BomLifecycleType);

        switch (index) {
            case 0: {
                this.bomLifecycleConfig.asDesignedActive = !this.bomLifecycleConfig.asDesignedActive;
                break;
            }
            case 1: {
                this.bomLifecycleConfig.asPlannedActive = !this.bomLifecycleConfig.asPlannedActive;
                break;
            }
            case 2: {
                this.bomLifecycleConfig.asOrderedActive = !this.bomLifecycleConfig.asOrderedActive;
                break;
            }
            case 3: {
                this.bomLifecycleConfig.asBuiltActive = !this.bomLifecycleConfig.asBuiltActive;
                break;
            }
            case 4: {
                this.bomLifecycleConfig.asSupportedActive = !this.bomLifecycleConfig.asSupportedActive;
                break;
            }
            case 5: {
                this.bomLifecycleConfig.asRecycledActive = !this.bomLifecycleConfig.asRecycledActive;
                break;
            }
        }

        this.emitBomLifecycleState();
    }


    toggleAsPlanned() {
        this.bomLifecycleConfig.asPlannedActive = !this.bomLifecycleConfig.asPlannedActive;
        this.emitBomLifecycleState();
    }

    toggle() {
        // If the other button is also inactive, prevent this one from being deactivated
        this.bomLifecycleConfig.asBuiltActive = !this.bomLifecycleConfig.asBuiltActive;
        this.emitBomLifecycleState();
    }

    disabledAllLifecycleStates() {
        for (let i = 0; i < this.lifeCycleTypes.length; i++) {
            this.updateLifecycleConfig(this.lifeCycleTypes[i], false);
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
