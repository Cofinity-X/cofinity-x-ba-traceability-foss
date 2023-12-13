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
import { MatCalendar } from '@angular/material/datepicker';
import { toDateInputMask } from '@shared/helper/filter-helper';


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


  @ViewChild('searchInput', { static: true }) searchInput: any;
  @ViewChild('calendar', { static: false }) calendar: MatCalendar<Date>;

  @ViewChild('selectElem', { static: true }) selectElem: MatSelect;

  @Output() triggerFilter = new EventEmitter<void>();

  selectionChange: EventEmitter<any> = new EventEmitter();

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
  public theSearchElement = '';
  public theSearchDate: FormControl<Date> = new FormControl();
  public searchDateInputControll = new FormControl<string>('');
  private previnput = '';

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
      this.searchDateInputControll.valueChanges.pipe(startWith(''), pairwise()).subscribe(([prev, next]: [any, any]) => {
        if (((next.length - prev.length) === 0 && next.length === 10) || (next.length - prev.length) > 0 || !prev) {
          this.setDateInputValue(next, false);
        } else {
          this.setDateInputValue(next, true);
        }
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

  private setDateInputValue(dateString: string, removed: boolean): void {
    let newDateString = dateString;
    if (!removed) {
      newDateString = toDateInputMask(dateString);
      if (newDateString.length === 10) {
        const newDate = new Date(+newDateString.substring(6, 10), +newDateString.substring(3, 5) - 1, +newDateString.substring(0, 2));
        if (this.maxDate !== null && this.maxDate < newDate) {
          newDateString = new Date().toLocaleDateString('en-GB');
        }
      }
      this.previnput = newDateString;
      this.searchDateInputControll.setValue(newDateString, { emitEvent: false, onlySelf: true });
    }
    clearTimeout(this.inputTimer);
    this.inputTimer = setTimeout(() => {
      if (newDateString.length !== 10 && newDateString.length !== 0) {
        newDateString = toDateInputMask(newDateString, true);
        this.searchDateInputControll.patchValue(newDateString, { emitEvent: false, onlySelf: true });
      }
      this.dateManualSelectionEvent(newDateString);
    }, 750);
    this.runningTimer = true;
  }

  public triggerFilteringTimeout(isTextSearch: boolean): void {
    this.runningTimer = true;
    let timeoutTime = 200;
    if (isTextSearch) {
      timeoutTime = 500;
    }
    clearTimeout(this.inputTimer);
    this.inputTimer = setTimeout(() => {
      this.triggerFiltering(isTextSearch);
    }, timeoutTime);
  }

  public triggerFiltering(isTextSearch: boolean): void {
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

  public dateManualSelectionEvent(dateString: string) {
    this.runningTimer = false;
    if (dateString === '') {
      this.theSearchDate.patchValue(null);
      this.formControl.patchValue(null);
      this.calendar._goToDateInView(new Date(), 'month');
    } else {
      const date = new Date(+dateString.substring(6, 10), +dateString.substring(3, 5) - 1, +dateString.substring(0, 2));
      const value = this.datePipe.transform(date, 'yyyy-MM-dd');
      this.theSearchDate.patchValue(date);
      this.formControl.patchValue(value);
      this.calendar._goToDateInView(this.theSearchDate.value, 'month');
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
    this.searchDateInputControll.patchValue(this.theSearchDate.value.toLocaleDateString('en-GB'), { emitEvent: false });
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
    this.searchDateInputControll.patchValue(null, { emitEvent: false });
    this.theSearchDate.patchValue(null);
    this.formControl.reset();
    if (extern) {
      clearTimeout(this.inputTimer);
      this.runningTimer = false;
      this.filterActive = '';
      this.searched = false;
    }
    return wasSet;
  }

  private setColumnClass(): void {
    for (const option of this.options) {
      const optionClass = { 'body-large': true };
      if (option['display'].includes('status')) {
        optionClass['notification-display-status'] = true;
        optionClass[option['displayClass']] = true;
      } else if (option['display'].includes('severity')) {
        optionClass['notification-display-severity'] = true;
        this.isSeverity = true;
      }
      this.optionClasses[option['display']] = optionClass;
    }
  }

  public onBlur(inputfield: string): void {
    if (this.runningTimer) {
      clearTimeout(this.inputTimer);
      if (inputfield === 'textFilter') {
        this.triggerFiltering(true);
      } else if (inputfield === 'dateFilter') {
        this.dateManualSelectionEvent(this.searchDateInputControll.value);
      } else if (inputfield === 'selectionFilter') {
        this.triggerFiltering(false);
      }
    }
  }
}
