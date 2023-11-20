/********************************************************************************
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
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
import { Pipe, PipeTransform } from '@angular/core';
import { Part, SemanticDataModelInCamelCase } from '@page/parts/model/parts.model';

@Pipe({
  name: 'formatPartlistSemanticDataModelToCamelCase',
})
export class FormatPartlistSemanticDataModelToCamelCasePipe implements PipeTransform {
  transform(partList: Part[] | any[]): Part[] | any[] {
    partList.forEach(part => {
      let semanticDataModel: SemanticDataModelInCamelCase;
      switch (part.semanticDataModel.toString().toLowerCase()) {
        case 'batch': {
          semanticDataModel = SemanticDataModelInCamelCase.BATCH;
          break;
        }
        case 'serialpart': {
          semanticDataModel = SemanticDataModelInCamelCase.SERIALPART;
          break;
        }
        case 'partasplanned': {
          semanticDataModel = SemanticDataModelInCamelCase.PARTASPLANNED;
          break;
        }
        case 'justinsequence': {
          semanticDataModel = SemanticDataModelInCamelCase.JUSTINSEQUENCE;
          break;
        }
        default: {
          semanticDataModel = SemanticDataModelInCamelCase.UNKNOWN;
          break;
        }
      }
      return {
        ...part,
        semanticDataModel: semanticDataModel,
      };
    });
    return partList;
  }
}
