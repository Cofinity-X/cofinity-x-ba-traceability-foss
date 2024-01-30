import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { TableColumnSettingsComponent } from '../table-column-settings/table-column-settings.component';
import { PartTableType } from '../table/table.model';

@Component({
  selector: 'app-table-settings',
  templateUrl: './table-settings.component.html',
  styleUrls: ['./table-settings.component.scss']
})
export class TableSettingsComponent {

  @Input() tableType: PartTableType;
  @Input() defaultColumns: string[];
  @Input() defaultFilterColumns: string[];

  @Output() filtersReset = new EventEmitter<void>();

  constructor(private dialog: MatDialog) { }

  resetFilters(): void {
    this.filtersReset.emit();
  }

  openTableColumnSettingsDialog(): void {
    const config = new MatDialogConfig();
    config.autoFocus = false;
    config.data = {
      title: 'table.tableSettings.title',
      panelClass: 'custom',
      tableType: this.tableType,
      defaultColumns: this.defaultColumns,
      defaultFilterColumns: this.defaultFilterColumns
    };
    this.dialog.open(TableColumnSettingsComponent, config);
  }
}
