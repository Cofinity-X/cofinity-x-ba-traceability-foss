import { Component, Input } from '@angular/core';
import { RequestContext } from '../request-notification.base';
import { Observable } from 'rxjs';
import { Pagination } from '@core/model/pagination.model';
import { AssetAsBuiltFilter, Part } from '@page/parts/model/parts.model';
import { View } from '@shared/model/view.model';
import { OtherPartsFacade } from '@page/other-parts/core/other-parts.facade';
import { TableHeaderSort } from '@shared/components/table/table.model';

@Component({
  selector: 'app-request-part-selection',
  templateUrl: './request-part-selection.component.html',
  styleUrls: ['./request-part-selection.component.scss']
})
export class RequestPartSelectionComponent {
  // @Input() public context: RequestContext;

  // protected readonly RequestContext = RequestContext;

  // public supplierPartsAsBuilt$: Observable<View<Pagination<Part>>>;
  // public tableSupplierAsBuiltSortList: TableHeaderSort[];
  // public assetAsBuiltFilter: AssetAsBuiltFilter;


  // public constructor(public readonly otherPartsFacade: OtherPartsFacade) {

  // }

  // public ngOnInit(): void {
  //   if (this.context === RequestContext.REQUEST_INVESTIGATION) {
  //     this.supplierPartsAsBuilt$ = this.otherPartsFacade.supplierPartsAsBuilt$;
  //     this.tableSupplierAsBuiltSortList = [];
  //     this.assetAsBuiltFilter = {};
  //     this.otherPartsFacade.setSupplierPartsAsBuilt();
  //   }
  // }

}
