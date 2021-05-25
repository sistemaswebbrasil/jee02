package br.com.siswbrasil.jee02.repository;

import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

	Page<T> datatableLazyFilter(int first, int pageSize, Map<String, SortMeta> sortBy,	Map<String, FilterMeta> filterBy); 

}
