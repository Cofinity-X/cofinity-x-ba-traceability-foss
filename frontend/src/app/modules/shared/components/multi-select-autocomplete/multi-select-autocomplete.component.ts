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
} from '@angular/core';
import { FormControl } from '@angular/forms';
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
  theSearchDate: FormControl<Date> = new FormControl();

  @Output() triggerFilter = new EventEmitter<void>();

  selectionChange: EventEmitter<any> = new EventEmitter();

  @ViewChild('selectElem', { static: true }) selectElem: MatSelect;
  public filterName = 'filterLabel';
  public filteredOptions: Array<any> = [];
  public selectedValue: Array<any> = [];
  public selectAllChecked = false;
  public displayString = '';
  public selectedCheckboxOptions: Array<any> = [];
  public filterActive = '';
  public searched = false;
  private inputTimer;
  public runningTimer = false;
  public optionClasses = {};
  public isSeverity = false;
  public severityIcon = {};
  public severityIconName = {};
  public searchDate = new FormControl<string>('');

  constructor(
    public datePipe: DatePipe,
    public _adapter: DateAdapter<any>,
    @Inject(MAT_DATE_LOCALE) public _locale: string,
    @Inject(LOCALE_ID) private locale: string,
  ) {
    let localeTime = 'en-GB';
    if (locale === 'de') {
      localeTime = 'de-DE';
    }
    registerLocaleData(localeDe, 'de', localeDeExtra);
    this._adapter.setLocale(localeTime);
    this._adapter.getFirstDayOfWeek = () => { return 1; };
  }

  ngOnInit(): void {
    if (this.textSearch) {
      this.formControl.valueChanges.pipe(startWith(0), pairwise()).subscribe(([_prev, next]: [any, any]) => {
        this.theSearchElement = next;
        this.searched = true;
        this.triggerFilteringTimeout(true);
      });
    } else if (this.isDate) {
      this.filterName = 'filterLabelDate';
      this.searchDate.valueChanges.pipe(startWith(0), pairwise()).subscribe(([_prev, next]: [any, any]) => {
        clearTimeout(this.inputTimer);
        this.inputTimer = setTimeout(() => {
          this.dateManuelSelectionEvent(next);
        }, 750);
        this.runningTimer = true;
      });
    } else if (this.multiple) {
      this.filterName = 'filterLabelSelect';
      this.setColumnClass();
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

  private triggerFilteringTimeout(isTextSearch: boolean): void {
    clearTimeout(this.inputTimer);
    this.inputTimer = setTimeout(() => {
      this.triggerFiltering(isTextSearch);
    }, 500);
    this.runningTimer = true;
  }

  private triggerFiltering(isTextSearch: boolean): void {
    this.runningTimer = false;
    if (isTextSearch) {
      this.setFilterActive();
    }
    this.triggerFilter.emit();
  }

  public toggleSelect = function (val: any, isSelectAll: boolean): void {
    if (isSelectAll) {
      if (val.checked) {
        this.options.forEach(option => {
          option.checked = true;
        });
      } else {
        this.options.forEach(option => (option.checked = false));
      }
    } else if (!isSelectAll && !val.checked && this.selectAllChecked) {
      this.selectAllChecked = false;
    } else if (!isSelectAll && val.checked && !this.selectAllChecked && this.options.filter(option => !option.checked).length === 0) {
      this.selectAllChecked = true;
    }
    this.triggerFilteringTimeout(false);
  };


  public someSelected(): boolean {
    return this.options.filter(option => option.checked).length > 0 && !this.selectAllChecked;
  }

  public setFilterActive(): void {
    this.filterActive = this.theSearchElement;
  }

  private dateValidation(dateString: string): Date {
    this.runningTimer = false;
    const regexOne = /[a-zA-Z]/;
    const regexTwo = /[./-]/;
    if (!regexOne.test(dateString)) {
      const dateArray = dateString.split(regexTwo);
      if (dateArray.length === 3 && !isNaN(+dateArray[0]) && !isNaN(+dateArray[1]) && !isNaN(+dateArray[2]) && +dateArray[0] <= 31 && +dateArray[1] <= 12 && (dateArray[2].length === 2 || dateArray[2].length === 4)) {
        if (dateArray[2].length === 2) {
          dateArray[2] = '20' + dateArray[2];
        }
        const date = new Date(+dateArray[2], +dateArray[1] - 1, +dateArray[0]);
        if (date <= this.maxDate) {
          return date;
        }
      }
    }
    return null;
  }

  public dateManuelSelectionEvent(dateString: string) {
    if (dateString === '') {
      this.theSearchDate.patchValue(null);
      this.formControl.patchValue(null);
      this.theSearchElement = null;
      return;
    }
    this.theSearchDate.patchValue(this.dateValidation(dateString));
    if (this.theSearchDate.value !== null) {
      const value = this.datePipe.transform(this.theSearchDate.value, 'yyyy-MM-dd');
      this.formControl.patchValue(value);
      this.theSearchElement = this.theSearchDate.value.toLocaleDateString('en-GB');
    } else {
      this.formControl.patchValue(null);
      this.theSearchElement = null;
    }
    this.triggerFilter.emit();
  }

  public dateSelectionEvent(event: Date) {
    if (this.runningTimer) {
      clearTimeout(this.inputTimer);
      this.runningTimer = false;
    }
    this.theSearchDate.patchValue(event);
    const value = this.datePipe.transform(event, 'yyyy-MM-dd');
    this.formControl.patchValue(value);
    this.theSearchElement = this.theSearchDate.value.toLocaleDateString('en-GB');
    this.triggerFilter.emit();
  }

  public clickClear(extern = false): boolean {
    let wasSet = false;
    if (this.searchInput) {
      this.searchInput.value = '';
    }
    this.theSearchElement = '';
    this.selectedValue = [];
    wasSet = this.formControl.value !== null && ((Array.isArray(this.formControl.value) && this.formControl.value.length > 0) || (!Array.isArray(this.formControl.value) && this.formControl.value !== ''));
    this.formControl.patchValue('');
    this.formControl.reset();
    if (extern) {
      clearTimeout(this.inputTimer);
      this.runningTimer = false;
      this.filterActive = '';
      this.searched = false;
    }
    return wasSet;
  }

  public getColumnName(): string {
    const columnName = this.options[0]['display'].split('.');
    return columnName[columnName.length - 2].toUpperCase();
  }

  public setColumnClass(): void {
    for (const option of this.options) {
      const optionClass = { 'body-large': true };
      const column = option['display'].split('.');
      if (column[column.length - 2].toUpperCase() === 'STATUS') {
        optionClass['notification-display-status'] = true;
        const statusClass = 'notification-display-status--' + column[column.length - 1].toUpperCase();
        optionClass[statusClass] = true;
      } else if (column[column.length - 2].toUpperCase() === 'SEVERITY') {
        optionClass['notification-display-severity'] = true;
        this.isSeverity = true;
        this.setSeverityIcon(column);
      }
      this.optionClasses[option['display']] = optionClass;
    }
  }
  private setSeverityIcon(column): void {
    if (column[column.length - 1].toUpperCase() === 'MINOR') {
      this.severityIcon[column.join('.')] = './assets/images/icons/info.svg';
      this.severityIconName[column.join('.')] = 'MINOR';
    } else if (column[column.length - 1].toUpperCase() === 'MAJOR') {
      this.severityIcon[column.join('.')] = './assets/images/icons/warning.svg';
      this.severityIconName[column.join('.')] = 'MAJOR';
    } else if (column[column.length - 1].toUpperCase() === 'CRITICAL') {
      this.severityIcon[column.join('.')] = './assets/images/icons/error_outline.svg';
      this.severityIconName[column.join('.')] = 'CRITICAL';
    } else {
      this.severityIcon[column.join('.')] = './assets/images/icons/error.svg';
      this.severityIconName[column.join('.')] = 'LIFE-THREATENING';
    }
  }

  public onDeselect(inputfield: string): void {
    if (this.runningTimer) {
      this.runningTimer = false;
      if (inputfield === 'textFilter') {
        clearTimeout(this.inputTimer);
        this.triggerFiltering(true);
      } else if (inputfield === 'dateFilter') {
        clearTimeout(this.inputTimer);
        this.dateManuelSelectionEvent(this.searchDate.value);
      } else if (inputfield === 'selectionFilter') {
        clearTimeout(this.inputTimer);
        this.triggerFiltering(false);
      }
    }
  }
}
