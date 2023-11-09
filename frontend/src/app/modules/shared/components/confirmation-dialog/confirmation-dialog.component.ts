import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import i18next from 'i18next';
import { ConfirmationDialogComponentData } from './confirmation-dialog.component.model';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent {
  public title: string;
  public message: string;
  public confirmButtonLabel: string;
  public cancelButtonLabel: string = i18next.t('confirmationDialog.cancel');

  public confirmAction: () => void;

  public constructor(@Inject(MAT_DIALOG_DATA) data: ConfirmationDialogComponentData) {
    this.title = data.title;
    this.message = data.message;
    this.confirmButtonLabel = data.confirmButtonLabel;
    this.cancelButtonLabel = data.cancelButtonLabel ?? this.cancelButtonLabel;
    this.confirmAction = data.confirmAction;
  }
}
