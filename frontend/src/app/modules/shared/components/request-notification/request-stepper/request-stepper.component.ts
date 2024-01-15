import { Component, Inject } from '@angular/core';
import { RequestContext } from '../request-notification.base';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { MainAspectType } from '@page/parts/model/mainAspectType.enum';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { Part } from '@page/parts/model/parts.model';
import { ModalComponent } from '@shared/modules/modal/component/modal.component';

@Component({
  selector: 'app-request-stepper',
  templateUrl: './request-stepper.component.html',
  styleUrls: ['./request-stepper.component.scss']
})
export class RequestStepperComponent {
  public context: RequestContext;
  public selectedParts: Part[] = [];
  public tabIndex = 0;
  public readonly isLoading$ = new BehaviorSubject(false);
  public itemCount: number[] = [];

  protected readonly MainAspectType = MainAspectType;
  protected readonly RequestContext = RequestContext;

  constructor(public dialog: MatDialog, @Inject(MAT_DIALOG_DATA) data) {
    this.context = data.context;
  }

  public onTabChange(index: number): void {
    this.tabIndex = index;
  }

  public closeAction(): void {
    this.dialog.open(ModalComponent, {
      autoFocus: false,
      data: {
        title: 'commonInvestigation.discardHeader',
        message: 'commonInvestigation.discardMessage',
        buttonLeft: 'parts.confirmationDialog.resume',
        buttonRight: 'parts.confirmationDialog.discard',
        primaryButtonColour: 'primary',
        onConfirm: () => {
          this.dialog.closeAll();
        }
      }
    });
  }

  public onPartsSelected(parts: Part[]): void {
    this.selectedParts = parts;
  }
}
