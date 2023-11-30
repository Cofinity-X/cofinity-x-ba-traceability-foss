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

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Severity } from '@shared/model/severity.model';
import { SharedModule } from '@shared/shared.module';
import { screen } from '@testing-library/angular';
import { renderComponent } from '@tests/test-render.utils';

import { SeverityComponent } from './severity.component';

describe('SeverityComponent', () => {
  const renderSeverity = (severity: Severity) => {
    return renderComponent(`<app-severity [severity]='severity'>Test</app-severity>`, {
      imports: [SharedModule],
      componentProperties: { severity },
    });
  };

  const renderSeverityComponent = (severity: Severity) => {
    return renderComponent(SeverityComponent, {
      imports: [SharedModule],
      componentProperties: { severity },
    });
  };

  it('should render correct Minor icon', async () => {
    const { fixture } = await renderSeverity(Severity.MINOR);
    const img: any = fixture.nativeElement.querySelector('img');
    expect(img.src).toContain('/assets/images/icons/info.svg');
  });

  it('should render correct Major icon', async () => {
    const { fixture } = await renderSeverity(Severity.MAJOR);
    const img: any = fixture.nativeElement.querySelector('img');
    expect(img.src).toContain('/assets/images/icons/warning.svg');
  });

  it('should render correct Critical icon', async () => {
    const { fixture } = await renderSeverity(Severity.CRITICAL);
    const img: any = fixture.nativeElement.querySelector('img');
    expect(img.src).toContain('/assets/images/icons/error_outline.svg');
  });

  it('should render correct LifeThreatening icon', async () => {
    const { fixture } = await renderSeverity(Severity.LIFE_THREATENING);
    const img: any = fixture.nativeElement.querySelector('img');
    expect(img.src).toContain('/assets/images/icons/error.svg');
  });

  it('should get the correct icon', async () => {
    const { fixture } = await renderSeverityComponent(Severity.LIFE_THREATENING);
    const { componentInstance } = fixture;
    expect(componentInstance.getIconBySeverity(Severity.LIFE_THREATENING)).toContain('/assets/images/icons/error.svg');
  });
});
