package br.com.siswbrasil.jee02.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.siswbrasil.jee02.entity.Estado;

@Repository
public interface EstadoRepository extends CustomJpaRepository<Estado, Long> {

	List<Estado> findAllByOrderBySigla();

}
