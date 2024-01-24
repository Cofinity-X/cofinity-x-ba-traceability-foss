import { ChangeDetectorRef, Component, EventEmitter, Inject, Output, QueryList, ViewChildren } from '@angular/core';
import { RequestContext } from '../request-notification.base';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { MainAspectType } from '@page/parts/model/mainAspectType.enum';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { Part } from '@page/parts/model/parts.model';
import { ModalComponent } from '@shared/modules/modal/component/modal.component';
import { FormGroup, FormControl } from '@angular/forms';
import { SupplierPartsComponent } from '@page/other-parts/presentation/supplier-parts/supplier-parts.component';
import { OwnPartsComponent } from '@page/parts/presentation/own-parts/own-parts.component';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-request-stepper',
  templateUrl: './request-stepper.component.html',
  styleUrls: ['./request-stepper.component.scss']
})
export class RequestStepperComponent {

  public totalItems: number;
  public context: RequestContext;
  public selectedParts: Part[] = [];
  public tabIndex = 0;
  public readonly isLoading$ = new BehaviorSubject(false);
  public itemCount: number[] = [];

  public searchFormGroup = new FormGroup({});
  public searchControl: FormControl;

  public readonly deselectPartTrigger$ = new Subject<Part[]>();
  public readonly addPartTrigger$ = new Subject<Part>();

  @ViewChildren(SupplierPartsComponent) supplierPartsComponents: QueryList<SupplierPartsComponent>;
  @ViewChildren(OwnPartsComponent) ownPartsComponents: QueryList<OwnPartsComponent>;
  @Output() deselectPart = new EventEmitter<Part>();

  protected readonly MainAspectType = MainAspectType;
  protected readonly RequestContext = RequestContext;

  private fromExternal = false;

  constructor(public dialog: MatDialog, @Inject(MAT_DIALOG_DATA) data,
    private readonly changeDetector: ChangeDetectorRef,
  ) {
    this.context = data.context;
    this.tabIndex = data.tabIndex ?? 0;
    this.selectedParts = data.selectedItems ?? [];
    this.fromExternal = data.fromExternal ?? false;
  }

  ngOnInit(): void {
    this.searchFormGroup.addControl('partSearch', new FormControl([]));
    this.searchControl = this.searchFormGroup.get('partSearch') as unknown as FormControl;
  }

  public onBackClicked(): void {
    if (this.fromExternal) {
      this.dialog.closeAll();
      return;
    }
    this.tabIndex = 0;
    this.changeDetector.detectChanges();
  }

  public onTabChange(index: number): void {
    this.tabIndex = index;
    this.changeDetector.detectChanges();
  }

  public closeAction(): void {
    if (this.fromExternal) {
      this.dialog.closeAll();
      return;
    }

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

  public onPartDeselected(part: Part): void {
    this.deselectPart.emit(part);

    if (this.fromExternal === false) {
      this.deselectPartTrigger$.next([part]);
    }
  }

  public triggerPartSearch() {
    const searchValue = this.searchFormGroup.get('partSearch').value;

    for (const supplierPartsComponent of this.supplierPartsComponents) {
      supplierPartsComponent.updateSupplierParts(searchValue);
    }

    for (const ownPartsComponent of this.ownPartsComponents) {
      ownPartsComponent.updateOwnParts(searchValue);
    }
  }

  public onTotalItemsChanged(totalItems: number): void {
    this.totalItems = totalItems;
    this.changeDetector.detectChanges();
  }

}
