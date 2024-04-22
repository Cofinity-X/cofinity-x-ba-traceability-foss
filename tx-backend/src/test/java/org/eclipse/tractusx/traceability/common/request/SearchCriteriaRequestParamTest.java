package org.eclipse.tractusx.traceability.common.request;

import org.eclipse.tractusx.traceability.common.model.SearchCriteria;
import org.eclipse.tractusx.traceability.notification.domain.base.model.NotificationSide;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchCriteriaRequestParamTest {
//
//    @Test
//    void testToSearchCriteria() {
//
//        List<String> filter = List.of("status,EQUAL,RECEIVED,AND");
//
//        SearchCriteriaRequestParam searchCriteriaRequestParam = new SearchCriteriaRequestParam(filter);
//
//        SearchCriteria searchCriteria = searchCriteriaRequestParam.toSearchCriteria(null);
//
//        assertEquals("AND", searchCriteria.getSearchCriteriaFilterList().get(0).getOperator().toString());
//        assertEquals("status", searchCriteria.getSearchCriteriaFilterList().get(0).getKey());
//        assertEquals("EQUAL", searchCriteria.getSearchCriteriaFilterList().get(0).getStrategy().toString());
//        assertEquals("RECEIVED", searchCriteria.getSearchCriteriaFilterList().get(0).getValue());
//    }
//
//    @Test
//    void testToSearchCriteriaWithSide() {
//
//        List<String> filter = List.of("status,EQUAL,RECEIVED,AND");
//
//        SearchCriteriaRequestParam searchCriteriaRequestParam = new SearchCriteriaRequestParam(filter);
//
//        SearchCriteria searchCriteria = searchCriteriaRequestParam.toSearchCriteria(NotificationSide.RECEIVER);
//
//        assertEquals("AND", searchCriteria.getSearchCriteriaOperator().toString());
//        assertEquals("status", searchCriteria.getSearchCriteriaFilterList().get(0).getKey());
//        assertEquals("EQUAL", searchCriteria.getSearchCriteriaFilterList().get(0).getStrategy().toString());
//        assertEquals("RECEIVED", searchCriteria.getSearchCriteriaFilterList().get(0).getValue());
//        assertEquals("side", searchCriteria.getSearchCriteriaFilterList().get(1).getKey());
//        assertEquals("EQUAL", searchCriteria.getSearchCriteriaFilterList().get(1).getStrategy().toString());
//        assertEquals("RECEIVER", searchCriteria.getSearchCriteriaFilterList().get(1).getValue());
//    }
//
//    @Test
//    void testToSearchCriteriaWithSideOnly() {
//
//        SearchCriteriaRequestParam searchCriteriaRequestParam = new SearchCriteriaRequestParam(null, null);
//
//        SearchCriteria searchCriteria = searchCriteriaRequestParam.toSearchCriteria(NotificationSide.SENDER);
//
//        assertEquals("AND", searchCriteria.getSearchCriteriaOperator().toString());
//        assertEquals("side", searchCriteria.getSearchCriteriaFilterList().get(0).getKey());
//        assertEquals("EQUAL", searchCriteria.getSearchCriteriaFilterList().get(0).getStrategy().toString());
//        assertEquals("SENDER", searchCriteria.getSearchCriteriaFilterList().get(0).getValue());
//    }
}
