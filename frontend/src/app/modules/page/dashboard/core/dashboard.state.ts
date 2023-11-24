/********************************************************************************
 * Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 * Copyright (c) 2022, 2023 ZF Friedrichshafen AG
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

import {Injectable} from '@angular/core';

import {Notifications} from '@shared/model/notification.model';
import {State} from '@shared/model/state';
import {Observable} from 'rxjs';
import {View} from 'src/app/modules/shared/model/view.model';
import {DashboardStats} from "@page/dashboard/model/dashboard.model";

@Injectable()
export class DashboardState {

  // part counts
  private readonly _numberOfAsBuiltOwnParts$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfAsPlannedOwnParts$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfAsBuiltSupplierParts$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfAsPlannedSupplierParts$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfAsBuiltCustomerParts$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfAsPlannedCustomerParts$: State<View<number>> = new State<View<number>>({ loader: true });

  // calculated part counts
  private readonly _numberOfTotalMyParts$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfTotalOtherParts$: State<View<number>> = new State<View<number>>({ loader: true });

  // notification counts
  private readonly _numberOfMyPartsWithOpenAlerts$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfMyPartsWithOpenInvestigations$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfOtherPartsWithOpenAlerts$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfOtherPartsWithOpenInvestigations$: State<View<number>> = new State<View<number>>({ loader: true });

  // calculated notification counts
  private readonly _numberOfOwnOpenInvestigationsReceived$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfOwnOpenInvestigationsCreated$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfOwnOpenAlertsReceived$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _numberOfOwnOpenAlertsCreated$: State<View<number>> = new State<View<number>>({ loader: true });
  private readonly _dashboardStats$: State<View<DashboardStats>> = new State<View<DashboardStats>>({ loader: true });

  // recent notifications
  private readonly _recentReceivedInvestigations$: State<View<Notifications>> = new State<View<Notifications>>({ loader: true });
  private readonly _recentCreatedInvestigations$: State<View<Notifications>> = new State<View<Notifications>>({ loader: true });

  private readonly _recentReceivedAlerts$: State<View<Notifications>> = new State<View<Notifications>>({ loader: true });
  private readonly _recentCreatedAlerts$: State<View<Notifications>> = new State<View<Notifications>>({ loader: true });

  /**
   * part counts getter/setter
   */

  public get numberOfAsBuiltOwnParts$(): Observable<View<number>> {
    return this._numberOfAsBuiltOwnParts$.observable;
  }

  public setNumberOfAsBuiltOwnParts(count: View<number>): void {
    this._numberOfAsBuiltOwnParts$.update(count);
  }

  public get numberOfAsPlannedOwnParts$(): Observable<View<number>> {
    return this._numberOfAsPlannedOwnParts$.observable;
  }

  public setNumberOfAsPlannedOwnParts(count: View<number>): void {
    this._numberOfAsPlannedOwnParts$.update(count);
  }

  public get numberOfAsBuiltSupplierParts$(): Observable<View<number>> {
    return this._numberOfAsBuiltSupplierParts$.observable;
  }

  public setNumberOfAsBuiltSupplierParts(count: View<number>): void {
    this._numberOfAsBuiltSupplierParts$.update(count);
  }

  public get numberOfAsPlannedSupplierParts$(): Observable<View<number>> {
    return this._numberOfAsPlannedSupplierParts$.observable;
  }

  public setNumberOfAsPlannedSupplierParts(count: View<number>): void {
    this._numberOfAsPlannedSupplierParts$.update(count);
  }

  public get numberOfAsBuiltCustomerParts$(): Observable<View<number>> {
    return this._numberOfAsBuiltCustomerParts$.observable;
  }

  public setNumberOfAsBuiltCustomerParts(count: View<number>): void {
    this._numberOfAsBuiltCustomerParts$.update(count);
  }

  public get numberOfAsPlannedCustomerParts$(): Observable<View<number>> {
    return this._numberOfAsPlannedCustomerParts$.observable;
  }

  public setNumberOfAsPlannedCustomerParts(count: View<number>): void {
    this._numberOfAsPlannedCustomerParts$.update(count);
  }

  public get numberOfTotalMyParts$(): Observable<View<number>> {
    return this._numberOfTotalMyParts$.observable;
  }

  public setNumberOfTotalMyParts(count: View<number>): void {
    this._numberOfTotalMyParts$.update(count);
  }

  public get numberOfTotalOtherParts$(): Observable<View<number>> {
    return this._numberOfTotalOtherParts$.observable;
  }

  public setNumberOfTotalOtherParts(count: View<number>): void {
    this._numberOfTotalOtherParts$.update(count);
  }


  /**
   * part notifications getter/setter
   */


  public get numberOfMyPartsWithOpenAlerts$(): Observable<View<number>> {
    return this._numberOfMyPartsWithOpenAlerts$.observable;
  }

  public setNumberOfMyPartsWithOpenAlerts(count: View<number>): void {
    this._numberOfMyPartsWithOpenAlerts$.update(count);
  }

  public get numberOfMyPartsWithOpenInvestigations$(): Observable<View<number>> {
    return this._numberOfMyPartsWithOpenInvestigations$.observable;
  }

  public setNumberOfMyPartsWithOpenInvestigations(count: View<number>): void {
    this._numberOfMyPartsWithOpenInvestigations$.update(count);
  }

  public get numberOfOtherPartsWithOpenAlerts$(): Observable<View<number>> {
    return this._numberOfOtherPartsWithOpenAlerts$.observable;
  }

  public setNumberOfOtherPartsWithOpenAlerts(count: View<number>): void {
    this._numberOfOtherPartsWithOpenAlerts$.update(count);
  }

  public get numberOfOtherPartsWithOpenInvestigations$(): Observable<View<number>> {
    return this._numberOfOtherPartsWithOpenInvestigations$.observable;
  }

  public setNumberOfOtherPartsWithOpenInvestigations(count: View<number>): void {
    this._numberOfOtherPartsWithOpenInvestigations$.update(count);
  }

  /**
   * calculated notifications getter/setter
   */

  public get numberOfOwnOpenInvestigationsReceived$(): Observable<View<number>> {
    return this._numberOfOwnOpenInvestigationsReceived$.observable;
  }

  public setNumberOfOwnOpenInvestigationsReceived(count: View<number>): void {
    this._numberOfOwnOpenInvestigationsReceived$.update(count);
  }

  public get numberOfOwnOpenInvestigationsCreated$(): Observable<View<number>> {
    return this._numberOfOwnOpenInvestigationsCreated$.observable;
  }

  public setNumberOfOwnOpenInvestigationsCreated(count: View<number>): void {
    this._numberOfOwnOpenInvestigationsCreated$.update(count);
  }

  public get numberOfOwnOpenAlertsReceived$(): Observable<View<number>> {
    return this._numberOfOwnOpenAlertsReceived$.observable;
  }

  public setNumberOfOwnOpenAlertsReceived(count: View<number>): void {
    this._numberOfOwnOpenAlertsReceived$.update(count);
  }

  public get numberOfOwnOpenAlertsCreated$(): Observable<View<number>> {
    return this._numberOfOwnOpenAlertsCreated$.observable;
  }

  public setNumberOfOwnOpenAlertsCreated(count: View<number>): void {
    this._numberOfOwnOpenAlertsCreated$.update(count);
  }

  public setDashboardStats(dashboardStatus: View<DashboardStats>): void {
    this._dashboardStats$.update(dashboardStatus);
  }

  public get dashboardStats$(): Observable<View<DashboardStats>> {
    return this._dashboardStats$.observable;
  }

  /**
   * recent notifications getter/setter
   */
  public get recentReceivedInvestigations$(): Observable<View<Notifications>> {
    return this._recentReceivedInvestigations$.observable;
  }

  public setRecentReceivedInvestigations(investigations: View<Notifications>): void {
    this._recentReceivedInvestigations$.update(investigations);
  }

  public get recentCreatedInvestigations$(): Observable<View<Notifications>> {
    return this._recentCreatedInvestigations$.observable;
  }

  public setRecentCreatedInvestigations(investigations: View<Notifications>): void {
    this._recentCreatedInvestigations$.update(investigations);
  }

  public get recentReceivedAlerts$(): Observable<View<Notifications>> {
    return this._recentReceivedAlerts$.observable;
  }

  public setRecentReceivedAlerts(alerts: View<Notifications>): void {
    this._recentReceivedAlerts$.update(alerts);
  }

  public get recentCreatedAlerts$(): Observable<View<Notifications>> {
    return this._recentCreatedAlerts$.observable;
  }

  public setRecentCreatedAlerts(alerts: View<Notifications>): void {
    this._recentCreatedAlerts$.update(alerts);
  }

}
