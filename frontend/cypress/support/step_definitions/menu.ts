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

import { Given, Then, When } from '@badeball/cypress-cucumber-preprocessor';

// When("user navigate to {string}", function(desiredPage) {
// // ---TBD--- add more selections: "Parts", "Quality Alerts"...
//     cy.get('[href="/otherParts"]').click();
//   });


When("user navigate to {string}", function(desiredMenu) {
matched = false;
    switch (desiredMenu) {
      case 'Other parts': {
        matched = true;
        cy.get('[href="/otherParts"]').click();
        break;
      }
      case 'Parts': {
      matched = true;
        cy.get('[href="/parts"]').click();
        break;
      }
      case 'Dashboard': {
      matched = true;
        cy.get('[href="/dashboard"]').click();
        break;
      }
      case 'Quality investigations': {
      matched = true;
        cy.get('[href="/investigations"]').click();
        break;
      }
      case 'Quality alerts': {
      matched = true;
        cy.get('[href="/alerts"]').click();
        break;
      }
      case 'About': {
      matched = true;
        cy.get('[href="/about"]').click();
        break;
      }
    }
    if (!matched) {
      throw new Error("Set header menu '" + desiredMenu + "' is not one of valid status [Dashboard, Parts, Other parts, Quality investigations, Quality alerts, About].");
    }
});
