package br.com.siswbrasil.jee02.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.Metamodel;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {

	private EntityManager manager;

	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);

		this.manager = entityManager;
	}

	@Override
	public Page<T> datatableLazyFilter(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		ArrayList<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		countQuery.select(builder.count(countQuery.from(getDomainClass())));
		CriteriaQuery<T> listQuery = builder.createQuery(getDomainClass());
		Root<T> root = listQuery.from(getDomainClass());
		addFiltersParams(filterBy, builder, predicates, root);
		addOrderFromRequest(sortBy, builder, predicates, listQuery, root);
		countQuery.where(predicates.toArray(new Predicate[0]));		
		TypedQuery<T> query = manager.createQuery(listQuery);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		Long count = manager.createQuery(countQuery).getSingleResult();		
		Pageable pageable = PageRequest.of(first, pageSize);	
		Page<T> result = new PageImpl<T>(
			query.getResultList(), pageable , count
		);				
		System.out.println(result.getNumberOfElements());
		System.out.println(result.getNumber() );
		System.out.println(result.getSize() );
		System.out.println(result.getTotalPages() );
		System.out.println(result.getTotalElements() );
		
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addFiltersParams(Map<String, FilterMeta> filterBy, CriteriaBuilder builder,
			ArrayList<Predicate> predicates, Root<T> root) {

		for (FilterMeta meta : filterBy.values()) {
			String filterField = meta.getField();
			Object filterValue = meta.getFilterValue();
			MatchMode filterMatchMode = meta.getMatchMode();

			if (filterValue != null) {
				if (filterField.contains(".time")) {
					filterField = filterField.split(".time")[0];
				}
				switch (filterMatchMode) {
				case CONTAINS:					
					predicates.add(builder.like(builder.upper(root.get(filterField)),"%" +filterValue.toString().toUpperCase() + "%"));
					break;
				case ENDS_WITH:
					predicates.add(builder.like(builder.upper(root.get(filterField)),"%" +filterValue.toString().toUpperCase() ));
					break;
				case EQUALS:
					predicates.add(builder.equal(builder.upper(root.get(filterField)),filterValue.toString().toUpperCase() ));
					break;
				case EXACT:
					predicates.add(builder.equal(root.<String>get(filterField), filterValue));
					break;
				case GREATER_THAN:
					predicates.add(builder.greaterThan(root.<String>get(filterField), filterValue.toString()));
					break;
				case GREATER_THAN_EQUALS:
					predicates.add(builder.greaterThanOrEqualTo(root.<String>get(filterField), filterValue.toString()));
					break;
				case IN:
					predicates.add(builder.in(root.<String>get(filterField).in(filterValue)));
					break;
				case LESS_THAN:
					predicates.add(builder.lessThan(root.<String>get(filterField), filterValue.toString()));
					break;
				case LESS_THAN_EQUALS:
					predicates.add(builder.lessThanOrEqualTo(root.get(filterField), (Comparable) filterValue));
					break;
				case STARTS_WITH:	
					//Primefaces pattern filter is START_WITH and does not work properly with like
					if(isNumeric(manager, getDomainClass(), filterField)) {						
						predicates.add(builder.greaterThanOrEqualTo(root.get(filterField), (Comparable) filterValue));
						break;
					}
					predicates.add(builder.like(builder.upper(root.get(filterField)),filterValue.toString().toUpperCase() + "%"));				
					break;
				default:
					predicates.add(builder.equal(root.<String>get(filterField), filterValue.toString()));
					break;
				}
			}
		}
	}	

	private void addOrderFromRequest(Map<String, SortMeta> sortBy, CriteriaBuilder builder,
			ArrayList<Predicate> predicates, CriteriaQuery<T> listQuery, Root<T> root) {		
		listQuery.where(predicates.toArray(new Predicate[0]));		
		List<Order> orders = new ArrayList<Order>();
		if (sortBy != null && !sortBy.isEmpty()) {
			for (SortMeta meta : sortBy.values()) {
				String sortField = meta.getField() ;
				SortOrder sortOrder = meta.getOrder();
				if (sortField.contains(".time")) {
					sortField = sortField.split(".time")[0];
				}
				switch (sortOrder) {
				case DESCENDING:
					orders.add(builder.desc(root.get(sortField)));
					break;
				default:
					orders.add(builder.asc(root.get(sortField)));
					break;
				}
			}
			listQuery.orderBy(orders);
		}			
	}	
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private Class getTypeFromEntity(EntityManager em, Class<T> clazz, String field) {
		Metamodel m = em.getMetamodel();
		IdentifiableType<T> of = (IdentifiableType<T>) m.managedType(clazz);
		Class type = of.getAttribute(field).getJavaType();
		return type;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private boolean isNumeric(EntityManager em, Class<T> clazz, String field) {		
		Class type = getTypeFromEntity( em, clazz,field);
		if(type.getSuperclass().getSimpleName().equalsIgnoreCase("number")) {
			return true;
		}
		return false;
	}
	
}


