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

package org.eclipse.tractusx.traceability.common.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.tractusx.traceability.common.model.SearchCriteria;
import org.eclipse.tractusx.traceability.common.model.SearchCriteriaFilter;
import org.eclipse.tractusx.traceability.common.model.SearchCriteriaOperator;
import org.eclipse.tractusx.traceability.common.model.SearchStrategy;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@AllArgsConstructor
public class SearchCriteriaRequestParam {

    @ArraySchema(arraySchema = @Schema(description = "Filter Criteria", additionalProperties = Schema.AdditionalPropertiesValue.FALSE, example = "owner,EQUAL,OWN"), maxItems = Integer.MAX_VALUE)
    private List<String> filter;

    @Schema(description = "The filter logical operator", example = "AND", allowableValues = {"AND", "OR"})
    private String filterOperator;

    public SearchCriteria toSearchCriteria() {

        if (!isNull(this.filter) && isNull(this.filterOperator)) {
            throw new InvalidFilterException(
                    "No filter operator found. Please add param filterOperator=AND or filterOperator=OR.");
        }
        if (isNull(this.filter) && isNull(this.filterOperator)) {
            return SearchCriteria.builder().build();
        }

        final List<String> inputFilters = (filter != null) ? filter : Collections.emptyList();

        final List<SearchCriteriaFilter> filters = inputFilters
                .stream()
                .map(inputFilter -> {
                    try {
                        final String[] filterParams = inputFilter.split(",");
                        return SearchCriteriaFilter.builder()
                                .key(handleFilterParameter(filterParams[0]))
                                .strategy(SearchStrategy.valueOf(filterParams[1]))
                                .value(filterParams[2])
                                .build();
            } catch (final Exception exception) {
                throw new InvalidFilterException(
                        "Invalid filter param provided filter={provided} expected format is following filter=parameter,operation,value"
                                .replace("{provided}", inputFilter)
                );
            }

        })
                .toList();

        final SearchCriteriaOperator operator;
        try {
            operator = SearchCriteriaOperator.valueOf(filterOperator);
        } catch (final Exception exception) {
            throw new InvalidFilterException(
                    "Invalid filter operator provided filterOperator={provided} expected format is following filterOperator=value. Where value is one of AND, OR"
                            .replace("{provided}", filterOperator)
            );
        }

        return SearchCriteria.builder().searchCriteriaOperator(operator).searchCriteriaFilterList(filters).build();
    }

    private static String handleFilterParameter(final String filterParameter) {
        // As long as no clear spelling is defined, be lax with it. https://github.com/eclipse-tractusx/sldt-semantic-models/issues/470
        return filterParameter.equalsIgnoreCase("catenaxsiteid") ? "catenaXSiteId" : filterParameter;
    }
}
