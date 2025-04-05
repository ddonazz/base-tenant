package it.andrea.start.searchcriteria.audit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import it.andrea.start.models.audit.AuditTrace;
import it.andrea.start.models.support.AuditActivity;
import it.andrea.start.models.support.AuditTypeOperation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class AuditTraceSearchSpecification implements Specification<AuditTrace> {

    private static final long serialVersionUID = -6128605859804781607L;

    private AuditTraceSearchCriteria criterias;

    public AuditTraceSearchSpecification(AuditTraceSearchCriteria criterias) {
	this.criterias = criterias;
    }

    @Override
    public Predicate toPredicate(Root<AuditTrace> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	List<Predicate> predicatesAnd = new ArrayList<>();

	Long id = criterias.getId();
	String sessionId = criterias.getSessionId();
	AuditActivity activity = criterias.getActivity();
	Long userId = criterias.getUserId();
	String userName = criterias.getUserName();
	AuditTypeOperation auditType = criterias.getAuditType();
	String textSearch = criterias.getTextSearch();
	LocalDateTime dateFrom = criterias.getDateFrom();
	LocalDateTime dateTo = criterias.getDateTo();

	if (id != null) {
	    predicatesAnd.add(criteriaBuilder.equal(root.get("id"), id));
	}
	if (StringUtils.isNotBlank(sessionId)) {
	    predicatesAnd.add(criteriaBuilder.equal(root.get("sessionId"), sessionId));
	}
	if (activity != null) {
	    predicatesAnd.add(criteriaBuilder.equal(root.get("activity"), activity.toString()));
	}
	if (userId != null) {
	    predicatesAnd.add(criteriaBuilder.equal(root.get("userId"), userId));
	}
	if (StringUtils.isNotBlank(userName)) {
	    predicatesAnd.add(criteriaBuilder.equal(root.get("userName"), userName));
	}
	if (auditType != null) {
	    predicatesAnd.add(criteriaBuilder.equal(root.get("auditType"), auditType.toString()));
	}
	if (StringUtils.isNotBlank(textSearch)) {
	    List<Predicate> predicatesOr = new ArrayList<>();

	    String value = "%" + textSearch + "%";
	    predicatesOr.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("controllerMethod")), value.toUpperCase()));
	    predicatesOr.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("entityName")), value.toUpperCase()));
	    predicatesOr.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("entityKeyValue")), value.toUpperCase()));
	    predicatesOr.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("entityOldValue")), value.toUpperCase()));
	    predicatesOr.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("entityNewValue")), value.toUpperCase()));
	    predicatesOr.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("httpContextRequest")), value.toUpperCase()));
	    predicatesOr.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("httpContextResponse")), value.toUpperCase()));
	    predicatesOr.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("exceptionTrace")), value.toUpperCase()));

	    Predicate orPredicate = criteriaBuilder.or(predicatesOr.toArray(new Predicate[0]));
	    predicatesAnd.add(orPredicate);
	}
	if (dateFrom != null) {
	    predicatesAnd.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateEvent"), dateFrom));
	}
	if (dateTo != null) {
	    predicatesAnd.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateEvent"), dateTo));
	}

	return criteriaBuilder.and(predicatesAnd.toArray(new Predicate[0]));
    }

}
