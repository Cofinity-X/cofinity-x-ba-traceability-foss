package org.eclipse.tractusx.traceability.common.request;

import org.eclipse.tractusx.traceability.common.model.SearchCriteria;
import org.eclipse.tractusx.traceability.qualitynotification.domain.base.model.QualityNotificationSide;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchCriteriaRequestParamTest {

    @Test
    void testToSearchCriteria() {

        List<String> filter = List.of("status,EQUAL,RECEIVED");
        String operator = "AND";

        SearchCriteriaRequestParam searchCriteriaRequestParam = new SearchCriteriaRequestParam(filter, operator);

        SearchCriteria searchCriteria = searchCriteriaRequestParam.toSearchCriteria();

        assertEquals(searchCriteria.getSearchCriteriaOperator().toString(), "AND");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(0).getKey(), "status");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(0).getValue(), "RECEIVED");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(0).getStrategy().toString(), "EQUAL");
    }

    @Test
    void testToSearchCriteriaWithSide() {

        List<String> filter = List.of("status,EQUAL,RECEIVED");
        String operator = "AND";

        SearchCriteriaRequestParam searchCriteriaRequestParam = new SearchCriteriaRequestParam(filter, operator);

        SearchCriteria searchCriteria = searchCriteriaRequestParam.toSearchCriteria(QualityNotificationSide.RECEIVER);

        assertEquals(searchCriteria.getSearchCriteriaOperator().toString(), operator);
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(0).getKey(), "status");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(0).getValue(), "RECEIVED");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(0).getStrategy().toString(), "EQUAL");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(1).getKey(), "side");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(1).getValue(), "RECEIVER");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(1).getStrategy().toString(), "EQUAL");
    }

    @Test
    void testToSearchCriteriaWithSideOnly() {

        SearchCriteriaRequestParam searchCriteriaRequestParam = new SearchCriteriaRequestParam(null, null);

        SearchCriteria searchCriteria = searchCriteriaRequestParam.toSearchCriteria(QualityNotificationSide.SENDER);

        assertEquals(searchCriteria.getSearchCriteriaOperator().toString(), "AND");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(0).getKey(), "side");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(0).getValue(), "SENDER");
        assertEquals(searchCriteria.getSearchCriteriaFilterList().get(0).getStrategy().toString(), "EQUAL");
    }
}
