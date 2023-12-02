import { QueryList } from "@angular/core";
import { NotificationComponent } from "@shared/modules/notification/presentation/notification.component";
import { PartsTableComponent } from "@shared/components/parts-table/parts-table.component";
import { MultiSelectAutocompleteComponent } from "@shared/components/multi-select-autocomplete/multi-select-autocomplete.component";
import { ToastService } from "..";

function resetFilterSelector(multiSelectAutocompleteComponents: QueryList<MultiSelectAutocompleteComponent>, oneFilterSet): boolean {
  for (const multiSelectAutocompleteComponent of multiSelectAutocompleteComponents) {
    let wasSet = false;
    wasSet = multiSelectAutocompleteComponent.clickClear(true);
    oneFilterSet = wasSet || oneFilterSet;
  }
  return oneFilterSet;
}

export function resetFilterForAssetComponents(partsTableComponents: QueryList<PartsTableComponent>, oneFilterSet: boolean): boolean {
  for (const partsTableComponent of partsTableComponents) {
    oneFilterSet = resetFilterSelector(partsTableComponent.multiSelectAutocompleteComponents, oneFilterSet);
    partsTableComponent.resetfilterActive();
  }
  return oneFilterSet;
}

export function resetFilterForNotificationComponents(notificationComponent: NotificationComponent, oneFilterSet: boolean): boolean {
  for (const notifcationTabComponent of notificationComponent.notifcationTabComponents) {
    notifcationTabComponent.onFilterChange();
    notifcationTabComponent.tableComponent.resetFilter();
    oneFilterSet = resetFilterSelector(notifcationTabComponent.tableComponent.multiSelectAutocompleteComponents, oneFilterSet);
  }
  return oneFilterSet;
}

export function resetFilterAndShowToast(isAsset: boolean, component, toastService: ToastService) {
  const filterIsSet = isAsset ? resetFilterForAssetComponents(component, false) : resetFilterForNotificationComponents(component, false);
  if (filterIsSet) {
    toastService.info('parts.input.global-search.toastInfo');
  }
}