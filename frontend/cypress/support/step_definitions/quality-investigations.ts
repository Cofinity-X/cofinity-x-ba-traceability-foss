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
//package org.eclipse.tractusx.traceability.test;

//import static from org.eclipse.tractusx.traceability.test.validator.TestUtils.wrapStringWithTimestamp;
import { When, Then, Given } from '@badeball/cypress-cucumber-preprocessor';
import { QualityInvestigationsPage } from '../../integration/pages/QualityInvestigationsPage';

let notificationDescription = null;

Then("select {string} other part", (partAmount) => {
//since IDs of desired asset are not shown in FE the selection has to be done by other number
//   cy.wait(500);
  //cy.get('span').contains('NO-989134870198932317923938').parent('.row').as('part');
  cy.get('span').contains('NO-989134870198932317923938').parent('.row').get('#mat-mds-checkbox-21').click();
  //cy.get('@part').get('#mat-mds-checkbox-21').click();
  //cy.get('span').contains('As Planned').click(); // see comment above. This has to be done to avoid asPlanned selection
  //cy.get('#mat-mdc-checkbox-38').click(); //---TBD--- this is only a method to make it run, has to be changed to selected part as above!
});

Then("start investigation creation with description {string}", function (description) {
  //notificationDescription = wrapStringWithTimestamp(input.get("description"));
  //notificationDescription = "Test 123123 description";
  cy.get('div').contains('Start investigation').click();
  cy.get('mat-label').contains('Description').click().type(description);
});

When("severity {string}", function (severity) {
  cy.get('#mat-select-56').click(); // First the dropdown has to be opened.
  cy.get('p').contains(severity).click();
  //cy.get('#mat-select-56-panel').select(severity);  // Dropdown menu has own id.
});

When("{string} deadline", function (deadline) {
//---TBD---
      if (deadline == 'no') {
        // do nothing
      } else {
       // ---TBD---
      }
});

When("request the investigation", () => {
  cy.get('span').contains('ADD TO QUEUE').click();
});

Then("selected parts are marked as investigated", () => {
  //cy.get('class').contains('highlighted');
  //---TBD--- to check the desired assets, have to be adjusted with desired asset selection
});

When("popup with information about queued investigation is shown", () => {
  cy.contains(/You queued an investigation for 1 part/i).should('be.visible');
});

When("user navigate to {string} with button in popup", (popupClick) => {
  cy.get('a').contains('Go to Queue').click();
});

When("open details of created investigation", () => {
  cy.get('p').should('contain.text', notificationDescription).parent('.row').get('[class="mat-mdc-button-touch-target"]').click()
  cy.get('span').contains('View details').click();
});

//When user cancel selected investigation with entering "correct" id
// #include: check popup (id, description, status, created, createdby, texts, buttons (cancel, approve), then click on Delete
// #check: Deletion is only possible after entering the expected id
// #check: popup on the right sight is shown
When("user cancel selected investigation with entering {string} id", (input) => {
  let investigationId = '';
  switch (input) {
    case 'no': {
      cy.get('span').contains('Confirm cancellation').click();
      break;
    }
    case 'wrong': {
      cy.get('#mat-mdc-form-field-label-4').click().focus().type('000');
      cy.get('span').contains('Confirm cancellation').click();
      break;
    }
    case 'correct': {
      //get correct id ---TBD---
      cy.get('#mat-mdc-form-field-label-4').click().focus().type(investigated);
      cy.get('span').contains('Confirm cancellation').click();
      break;
    }
  }
});

Then("cancelation is not possible due to {string} id", (id) => {
  switch (id) {
    case 'no': {
      cy.contains(/This field is required!/i).should('be.visible');
      break;
    }
    case 'wrong': {
      cy.contains(/Please enter data that matches this pattern:/i).should('be.visible');
      break;
    }
  }
});

Then("informations for selected investigation are displayed as expected", () => {
// ---TBD--- include: overview, supplier parts, STATUS
});

Then("selected {string} has been {string} as expected", (notificationType, expectedStatus) => {
matched = false;
    switch (expectedStatus) {
      case 'canceled': {
        matched = true;
        cy.get('[title="Cancelled"]').should('be.visible');
        break;
      }
      case 'requested': {
      // same as "approved"
      matched = true;
        cy.get('[title="Requested"]').should('be.visible');
        break;
      }
      case 'accepted': {
      matched = true;
        cy.get('[title="Accepted"]').should('be.visible');
        break;
      }
      case 'declined': {
      matched = true;
        cy.get('[title="Declined"]').should('be.visible');
        break;
      }
      case 'acknowledged': {
      matched = true;
        cy.get('[title="Acknowledged"]').should('be.visible');
        break;
      }
      case 'closed': {
      matched = true;
        cy.get('[title="Closed"]').should('be.visible');
        break;
      }
    }
    if (!matched) {
      throw new Error("Set expected status '" + expectedStatus + "' is not one of valid status [canceled, requested, accepted, declined, acknowledged, closed].");
    }
});

When("selected {string} is not allowed to be {string}", (notificationType, status) => {
matched = false;
    switch (status) {
      case 'canceled': {
        matched = true;
        //TBD selection once the environment is running up again
        cy.get('[title="Cancelled"]').should('not.be.visible');
        break;
      }
      case 'requested': {
      // same as "approved"
      matched = true;
      //TBD selection once the environment is running up again
        cy.get('[title="Requested"]').should('not.be.visible');
        break;
      }
      case 'accepted': {
      matched = true;
      //TBD selection once the environment is running up again
        cy.get('[title="Accepted"]').should('not.be.visible');
        break;
      }
      case 'declined': {
      matched = true;
      //TBD selection once the environment is running up again
        cy.get('[title="Declined"]').should('not.be.visible');
        break;
      }
      case 'acknowledged': {
      matched = true;
      //TBD selection once the environment is running up again
        cy.get('[title="Acknowledged"]').should('not.be.visible');
        break;
      }
      case 'closed': {
      matched = true;
      //TBD selection once the environment is running up again
        cy.get('[title="Closed"]').should('not.be.visible');
        break;
      }
    }
    if (!matched) {
      throw new Error("Set status '" + status + "' is not one of valid status [canceled, requested, accepted, declined, acknowledged, closed].");
    }
});