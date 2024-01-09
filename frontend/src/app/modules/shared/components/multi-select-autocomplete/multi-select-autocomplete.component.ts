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

import { DatePipe, registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de';
import localeDeExtra from '@angular/common/locales/extra/de';
import { Component, EventEmitter, Inject, Input, LOCALE_ID, OnChanges, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { DateAdapter, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { MatSelectChange } from '@angular/material/select';
import { Owner } from '@page/parts/model/owner.enum';
import { TableContentType } from '@shared/components/multi-select-autocomplete/multi-select-autocomplete.model';
import { PartTableType } from '@shared/components/table/table.model';
import { NotificationType } from '@shared/model/notification.model';
import { FormatPartSemanticDataModelToCamelCasePipe } from '@shared/pipes/format-part-semantic-data-model-to-camelcase.pipe';
import { AlertsService } from '@shared/service/alerts.service';
import { InvestigationsService } from '@shared/service/investigations.service';
import { PartsService } from '@shared/service/parts.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-multiselect',
  templateUrl: 'multi-select-autocomplete.component.html',
  styleUrls: [ 'multi-select-autocomplete.component.scss' ],
})

export class MultiSelectAutocompleteComponent implements OnChanges {

  @Input()
  placeholder: string;
  @Input()
  options: any;
  allOptions: any;
  searchedOptions: any;
  optionsSelected: any;
  @Input()
  disabled = false;
  @Input()
  display = 'display';
  @Input()
  value = 'value';
  @Input()
  formControl = new FormControl();
  @Input()
  placeholderMultiple;
  @Input()
  singleSearch = false;

  @Input()
  selectedOptions;

  @Input()
  isDate = false;

  // New Options
  @Input()
  labelCount = 1;
  @Input()
  appearance = 'standard';

  @Input()
  filterColumn = null;

  @Input()
  notificationType: NotificationType = null;

  @Input()
  partTableType = PartTableType.AS_BUILT_OWN;

  @Input()
  isAsBuilt: boolean = true;

  public readonly minDate = new Date();

  @ViewChild('searchInput', { static: true }) searchInput: any;

  searchElement: string = '';

  searchElementChange: EventEmitter<any> = new EventEmitter();

  @ViewChild('selectElem', { static: true }) selectElem: any;

  filteredOptions: Array<any> = [];
  selectedValue: Array<any> = [];
  selectAllChecked = false;

  startDate: Date;
  endDate: Date;

  delayTimeoutId: any;

  suggestionError: boolean = false;

  isLoadingSuggestions: boolean;

  constructor(public datePipe: DatePipe, public _adapter: DateAdapter<any>,
              @Inject(MAT_DATE_LOCALE) public _locale: string, @Inject(LOCALE_ID) private locale: string, public partsService: PartsService,
              public investigationsService: InvestigationsService, public alertsService: AlertsService,
              private readonly formatPartSemanticDataModelToCamelCasePipe: FormatPartSemanticDataModelToCamelCasePipe) {
    registerLocaleData(localeDe, 'de', localeDeExtra);
    this._adapter.setLocale(locale);

    console.log(this.isDate);
  }

  ngOnInit(): void {
    this.searchElementChange.subscribe((value) => {
      if (this.delayTimeoutId) {
        clearTimeout(this.delayTimeoutId);
        this.delayTimeoutId = null;
        this.filterItem(value);
      }
    });

  }

  ngOnChanges(): void {
    this.selectedValue = this.formControl.value;
    this.formControl.patchValue(this.selectedValue);

  }

  toggleSelectAll = function(selectCheckbox: any): void {

    if (selectCheckbox.checked) {
      // if there are no suggestion but the selectAll checkbox was checked
      if (!this.filteredOptions.length) {
        this.formControl.patchValue(this.searchElement);
        this.selectedValue = this.searchElement as unknown as [];
      } else {
        this.filteredOptions.forEach(option => {
          if (!this.selectedValue.includes(option[this.value])) {
            this.selectedValue = this.selectedValue.concat([ option[this.value] ]);
          }
        });
      }

    } else {
      this.selectedValue = [];
    }
    this.formControl.patchValue(this.selectedValue);
  };

  changeSearchTextOptionSingleSearch() {
    this.formControl.patchValue(this.selectedValue);
  }

  shouldHideTextSearchOptionFieldSingleSearch() {
    return this.searchElement === null || this.searchElement === undefined || this.searchElement === '';
  }

  displayValue(): string[] {
    let suffix = '';
    let displayValue;
    // add +X others label if multiple
    if (this.selectedValue?.length > 1) {
      suffix = (' + ' + (this.selectedValue?.length - 1)) + ' ' + this.placeholderMultiple;
    }

    // apply CamelCase to semanticDataModel labels
    if (this.filterColumn === 'semanticDataModel') {
      displayValue = [this.formatPartSemanticDataModelToCamelCasePipe.transformModel(this.selectedValue[0]), suffix];
    }

    else {
      displayValue = [this.selectedValue[0], suffix];
    }

    // if no value selected, return empty string
    if (!this.selectedValue.length) {
      displayValue = [''];
    }

    return displayValue;
  }

  filterItem(value: any): void {

    if (!this.searchElement.length) {
      return;
    }

    if (!value) {
      this.filteredOptions = [];
      return;
    }

    if (this.singleSearch) {
      return;
    }

    // emit an event that the searchElement changed
    // if there is a timeout currently, abort the changes.
    if (this.delayTimeoutId) {
      this.searchElementChange.emit(value);
      return;
    }

    // if there is no timeout currently, start the delay
    const timeoutCallback = async (): Promise<void> => {
      this.isLoadingSuggestions = true;
      let newSuggestions = null;

      const tableOwner = this.getOwnerOfTable(this.partTableType);


      if (this.getContentTypeOfTable(this.partTableType) === TableContentType.NOTIFICATION) {
        console.log("channel")
        const notificationChannel = this.getChannelOfTable(this.partTableType);
        if (this.notificationType === NotificationType.INVESTIGATION) {
          newSuggestions = this.investigationsService.getDistinctFilterValues( notificationChannel, this.filterColumn, this.searchElement);
        } else {
          newSuggestions = this.alertsService.getDistinctFilterValues(notificationChannel, this.filterColumn, this.searchElement);
        }
      } else {
        newSuggestions = this.partsService.getDistinctFilterValues(
          this.isAsBuilt,
          tableOwner,
          this.filterColumn,
          this.searchElement,
        );
      }


      try {
        firstValueFrom(newSuggestions).then((res) => {
          if (this.filterColumn === 'semanticDataModel') {
            // @ts-ignore
            this.searchedOptions = res.filter(option => !this.selectedValue.includes(option))
              .map(option => ({
                display: this.formatPartSemanticDataModelToCamelCasePipe.transformModel(option),
                value: option,
              }));
            this.options = this.searchedOptions;
            // @ts-ignore
            this.allOptions = res.map(option => ({
              display: this.formatPartSemanticDataModelToCamelCasePipe.transformModel(option),
              value: option,
            }));

          } else {
            // add filter for not selected
            // @ts-ignore
            this.searchedOptions = res.filter(option => !this.selectedValue.includes(option))
              .map(option => ({ display: option, value: option }));
            this.options = this.searchedOptions;
            // @ts-ignore
            this.allOptions = res.map(option => ({ display: option, value: option }));
          }

          this.filteredOptions = this.searchedOptions;
          this.suggestionError = !this.filteredOptions?.length;
        }).catch((error) => {
          console.error('Error fetching data: ', error);
          this.suggestionError = !this.filteredOptions.length;
        }).finally(() => {
          this.delayTimeoutId = null;
          this.isLoadingSuggestions = false;
        });
      } catch (error) {
        console.error('Error in timeoutCallback: ', error);
      }
    };

    // Start the delay with the callback
    this.delayTimeoutId = setTimeout(() => Promise.resolve(timeoutCallback()), 500);
  }

  // DO NOT REMOVE: Used by parent component
  isUnsupportedAutoCompleteField(fieldName: string) {
    return fieldName === 'activeAlerts' || fieldName === 'activeInvestigations';
  }


  // Returns plain strings array of filtered values
  getFilteredOptionsValues(): string[] {
    const filteredValues = [];
    this.filteredOptions.forEach(option => {
      if (option.length) {
        filteredValues.push(option.value);
      }
    });
    return filteredValues;
  }

  startDateSelected(event: MatDatepickerInputEvent<Date>) {
    this.startDate = event.value;
    this.searchElement = this.datePipe.transform(this.startDate, 'yyyy-MM-dd');
  }

  endDateSelected(event: MatDatepickerInputEvent<Date>) {
    this.endDate = event.value;
    if (!this.endDate) {
      return;
    }
    this.searchElement += ',' + this.datePipe.transform(this.endDate, 'yyyy-MM-dd');
  }

  clickClear(): void {
    this.formControl.patchValue('');
    this.formControl.reset();
    this.searchElement = '';

    this.startDate = null;
    this.endDate = null;
    this.filteredOptions = [];
    this.updateOptionsAndSelections();
    this.selectedValue = [];
  }

  private updateOptionsAndSelections() {
    if (this.singleSearch) {
      return;
    }
    if (!this.allOptions) {
      this.allOptions = [];
    }
    this.options = this.allOptions.filter(option => !this.selectedValue.includes(option.value));
    if (!this.selectedValue) {
      this.options = this.allOptions;
    }

    if (!this.searchedOptions) {
      this.searchedOptions = [];
    }
    const filter = this.searchedOptions.filter(val => this.selectedValue.includes(val));
    for (const selected of this.selectedValue) {
      filter.push({ display: selected, value: selected });
    }
    this.optionsSelected = filter;
  }

  dateFilter() {
    this.formControl.patchValue(this.searchElement);
  }


  onSelectionChange(matSelectChange: MatSelectChange) {

    const filteredValues = this.getFilteredOptionsValues();
    const selectedCount = this.selectedValue.filter(item => filteredValues.includes(item)).length;
    this.selectAllChecked = selectedCount === this.filteredOptions.length;

    this.selectedValue = matSelectChange.value;
    this.formControl.patchValue(matSelectChange.value);
    this.updateOptionsAndSelections();
  }

  // Owner / channel by parttable type
  // TODO 2. make tableType dependend on channel of notifications switch case with notifications (PartTableType extend with notification table types)
  getOwnerOfTable(partTableType: PartTableType): Owner {
    if (partTableType === PartTableType.AS_BUILT_OWN || partTableType === PartTableType.AS_PLANNED_OWN) {
      return Owner.OWN;
    } else if (partTableType === PartTableType.AS_BUILT_CUSTOMER || partTableType === PartTableType.AS_PLANNED_CUSTOMER) {
      return Owner.CUSTOMER;
    } else if (partTableType === PartTableType.AS_BUILT_SUPPLIER || partTableType === PartTableType.AS_PLANNED_SUPPLIER) {
      return Owner.SUPPLIER;
    } else {
      return Owner.UNKNOWN;
    }
  }

  getChannelOfTable(partTableType: PartTableType): string {
    console.log(partTableType);
    if (this.getContentTypeOfTable(partTableType) === TableContentType.PART) {
      return;
    }

    if (partTableType === PartTableType.CREATED_ALERT || partTableType === PartTableType.CREATED_INVESTIGATION) {
      return 'SENDER';
    } else {
      return 'RECEIVER';
    }

  }

  getContentTypeOfTable(partTableType: PartTableType): TableContentType {
    switch (partTableType) {
      case PartTableType.RECEIVED_INVESTIGATION:
      case PartTableType.CREATED_INVESTIGATION:
      case PartTableType.RECEIVED_ALERT:
      case PartTableType.CREATED_ALERT:
        return TableContentType.NOTIFICATION;
        break;
      default:
        return TableContentType.PART;
    }
  }

  filterKeyCommands(event: any) {
    if (event.key === 'Enter' || (event.ctrlKey && event.key === 'a' || event.key === ' ')) {
      event.stopPropagation();
    }
  }
}
