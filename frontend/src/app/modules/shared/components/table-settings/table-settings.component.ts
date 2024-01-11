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

import { Component, EventEmitter, Inject, Output, } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TableViewSettings } from '@core/user/table-settings.model';
import { TableSettingsService } from '@core/user/table-settings.service';
import { PartTableType } from '@shared/components/table/table.model';
import {
  CdkDragDrop,
  moveItemInArray,
  transferArrayItem,
} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-table-settings',
  templateUrl: 'table-settings.component.html',
  styleUrls: ['table-settings.component.scss'],
})
export class TableSettingsComponent {

  @Output() changeSettingsEvent = new EventEmitter<void>();
  title: string;
  panelClass: string;

  tableType: PartTableType;
  defaultColumns: string[];
  defaultFilterColumns: string[];

  columnOptions: Map<string, boolean>;
  dialogColumns: string[];
  tableColumns: string[];
  filterColumns: string[];

  selectAllSelected: boolean;
  selectedColumn: string = null;

  isCustomerTable: boolean;

  dragActive = false;

  constructor(public dialogRef: MatDialogRef<TableSettingsComponent>, @Inject(MAT_DIALOG_DATA) public data: any, public readonly tableSettingsService: TableSettingsService) {
    // Layout
    this.title = data.title;
    this.panelClass = data.panelClass;
    this.isCustomerTable = data.tableType === PartTableType.AS_BUILT_CUSTOMER || data.tableType === PartTableType.AS_PLANNED_CUSTOMER;
    // Passed Data
    this.tableType = data.tableType;
    this.defaultColumns = data.defaultColumns;
    this.defaultFilterColumns = data.defaultFilterColumns;

    // Storage Data
    this.columnOptions = tableSettingsService.getStoredTableSettings()[this.tableType].columnSettingsOptions;
    this.dialogColumns = tableSettingsService.getStoredTableSettings()[this.tableType].columnsForDialog;
    this.tableColumns = tableSettingsService.getStoredTableSettings()[this.tableType].columnsForTable;
    this.filterColumns = tableSettingsService.getStoredTableSettings()[this.tableType].filterColumnsForTable;

    this.selectAllSelected = this.dialogColumns.length === this.tableColumns.length;

    if (dialogRef?.afterClosed)
      dialogRef.afterClosed().subscribe(() => {
        this.save();
      });
  }

  save() {
    // build new tableColumns how they should be displayed
    const newTableColumns: string[] = [];
    const newTableFilterColumns: string[] = [];
    // iterate over dialogColumns
    for (const column of this.dialogColumns) {
      // if item in dialogColumns is true in columnOptions --> add to new tableColumns
      if (this.columnOptions.get(column)) {
        newTableColumns.push(column);
        // ignore select column in customertable
        if ((column === 'select') && !this.isCustomerTable) {
          newTableFilterColumns.push('Filter');
        } else {
          newTableFilterColumns.push('filter' + column.charAt(0).toUpperCase() + column.slice(1));
        }
      }
    }

    // get Settingslist
    const tableSettingsList = this.tableSettingsService.getStoredTableSettings();

    // set this tableType Settings from SettingsList to the new one
    tableSettingsList[this.tableType] = {
      columnSettingsOptions: this.columnOptions,
      columnsForDialog: this.dialogColumns,
      columnsForTable: newTableColumns,
      filterColumnsForTable: newTableFilterColumns
    } as TableViewSettings;

    // save all values back to localstorage
    this.tableSettingsService.storeTableSettings(this.tableType, tableSettingsList);

    // trigger action that table will refresh
    this.tableSettingsService.emitChangeEvent();
  }

  handleCheckBoxChange(item: string, isChecked: boolean) {
    this.columnOptions.set(item, isChecked);
  }

  handleListItemClick(event: MouseEvent, item: string) {
    const element = event.target as HTMLElement;

    if (element.tagName !== 'INPUT') {
      this.selectedColumn = item;
      element.classList.toggle('selected-item');
    }
  }

  dragging(state: boolean) {
    this.dragActive = state;
  }

  selectAll(isChecked: boolean) {
    for (const column of this.dialogColumns) {
      if (column === 'select' || column === 'menu' || column === 'settings') {
        continue;
      }
      this.columnOptions.set(column, isChecked);
    }
    this.selectAllSelected = true;
  }

  resetColumns() {
    this.dialogColumns = [...this.defaultColumns];
    this.selectAll(true);
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex + 1, event.currentIndex + 1);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,
      );
    }
  }
}
