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

@Component({
    selector: 'app-bom-lifecycle-activator',
    templateUrl: './bom-lifecycle-activator.component.html',
    styleUrls: ['./bom-lifecycle-activator.component.scss'],
})
export class BomLifecycleActivatorComponent implements OnInit {

    @Input() view: UserSettingView;
    public bomLifecycleConfig: BomLifecycleConfig;
    public lifeCycleTypes: string[] = Object.keys(BomLifecycleType);
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

    toggleLifecycleType(type: string) {

    }

    selected(event: MatAutocompleteSelectedEvent): void {
        this.selectedLifeCycles.push(event.option.viewValue);
    }

    selectLifeCycle(lifeCycle: string): void {
        if (this.selectedLifeCycles.includes(lifeCycle) === true) {
            var index = this.selectedLifeCycles.indexOf(lifeCycle);
            this.selectedLifeCycles.splice(index, 1)
        } else {
            this.selectedLifeCycles.push(lifeCycle);
        }
    }

    removeLifeCycle(lifeCycle: string): void {
        const index = this.selectedLifeCycles.indexOf(lifeCycle);

        if (index >= 0) {
            this.selectedLifeCycles.splice(index, 1);
            this.selectedLifeCycles = Array.from(this.selectedLifeCycles);
        }
    }


    toggleAsPlanned() {
        this.bomLifecycleConfig.asPlannedActive = !this.bomLifecycleConfig.asPlannedActive;
        this.emitBomLifecycleState();
    }

    togglel() {
        // If the other button is also inactive, prevent this one from being deactivated
        this.bomLifecycleConfig.asBuiltActive = !this.bomLifecycleConfig.asBuiltActive;
        this.emitBomLifecycleState();
    }

    emitBomLifecycleState() {
        this.bomLifeCycleUserSetting.setUserSettings({
            asBuiltActive: this.bomLifecycleConfig.asBuiltActive,
            asPlannedActive: this.bomLifecycleConfig.asPlannedActive
        }, this.view);
        this.buttonClickEvent.emit(this.bomLifeCycleUserSetting.getSize(this.view));
    }
}
