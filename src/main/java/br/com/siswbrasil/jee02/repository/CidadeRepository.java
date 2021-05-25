package br.com.siswbrasil.jee02.repository;

import org.springframework.stereotype.Repository;

import br.com.siswbrasil.jee02.entity.Cidade;

@Repository
public interface CidadeRepository extends CustomJpaRepository<Cidade, Long>{

}
