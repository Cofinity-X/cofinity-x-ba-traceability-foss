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

import {
  Component,
  EventEmitter,
  Inject,
  Input,
  LOCALE_ID,
  OnChanges,
  Output,
  ViewChild,
  ViewEncapsulation,
} from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { DatePipe, registerLocaleData } from '@angular/common';
import { DateAdapter, MAT_DATE_LOCALE } from '@angular/material/core';
import localeDe from '@angular/common/locales/de';
import localeDeExtra from '@angular/common/locales/extra/de';
import { MatSelect } from '@angular/material/select';
import { pairwise, startWith } from 'rxjs';

@Component({
  selector: 'app-multiselect',
  templateUrl: 'multi-select-autocomplete.component.html',
  styleUrls: ['multi-select-autocomplete.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class MultiSelectAutocompleteComponent implements OnChanges {
  @Input()
  placeholder: string;
  @Input()
  options: any;
  @Input()
  disabled = false;
  @Input()
  display = 'display';
  @Input()
  value = 'value';
  @Input()
  formControl = new FormControl();
  @Input()
  panelWidth = 'auto';
  @Input()
  maxDate = new Date();

  @Input()
  multiple = false;
  @Input()
  textSearch = true;
  @Input()
  isDate = false;

  // New Options
  @Input()
  labelCount = 1;
  @Input()
  appearance = 'standard';

  public readonly minDate = new Date();

  @ViewChild('searchInput', { static: true }) searchInput: any;

  theSearchElement = '';

  @Output() triggerFilter = new EventEmitter<void>();
  @Output()
  selectionChange: EventEmitter<any> = new EventEmitter();

  @ViewChild('selectElem', { static: true }) selectElem: MatSelect;
  filterName = 'filterLabel';
  filteredOptions: Array<any> = [];
  selectedValue: Array<any> = [];
  selectAllChecked = false;
  displayString = '';
  selectedCheckboxOptions: Array<any> = [];
  filterActive = '';
  searched = false;

  constructor(
    public datePipe: DatePipe,
    public _adapter: DateAdapter<any>,
    @Inject(MAT_DATE_LOCALE) public _locale: string,
    @Inject(LOCALE_ID) private locale: string,
  ) {
    registerLocaleData(localeDe, 'de', localeDeExtra);
    this._adapter.setLocale(locale);
  }
  ngOnInit(): void {
    if (this.textSearch) {
      this.formControl.valueChanges.pipe(startWith(0), pairwise()).subscribe(([_prev, next]: [any, any]) => {
        this.theSearchElement = next;
        this.searched = true;
      });
    }
    if (this.isDate) {
      this.filterName = 'filterLabelDate';
    } else if (this.multiple) {
      this.filterName = 'filterLabelSelect';
    }
  }

  ngOnChanges(): void {
    this.theSearchElement = this.formControl.value;
    this.filteredOptions = this.options;
    if (this.formControl?.value) {
      this.selectedValue = this.formControl.value;
      this.formControl.patchValue(this.selectedValue);
    }
  }

  toggleSelectAll = function (val: any): void {
    if (val.checked) {
      this.options.forEach(option => {
        option.checked = true;
      });
    } else {
      this.options.forEach(option => (option.checked = false));
    }
  };

  toggleSelectOne(val: any): void {
    if (!val.checked && this.selectAllChecked) {
      this.selectAllChecked = false;
    } else if (val.checked && !this.selectAllChecked && this.options.filter(option => !option.checked).length === 0) {
      this.selectAllChecked = true;
    }
  }

  someSelected(): boolean {
    return this.options.filter(option => option.checked).length > 0 && !this.selectAllChecked;
  }

  setFilterActive(): void {
    this.filterActive = this.theSearchElement;
  }

  dateSelectionEvent(event: MatDatepickerInputEvent<Date>) {
    const value = this.datePipe.transform(event.value, 'yyyy-MM-dd');
    this.formControl.patchValue(value);
    this.selectedValue = value as unknown as [];
    this.theSearchElement = value;
  }

  clickClear(): void {
    this.formControl.patchValue('');
    this.formControl.reset();
    if (this.searchInput) {
      this.searchInput.value = '';
    }
    this.theSearchElement = null;
    this.selectedValue = [];
  }
}
