package br.com.siswbrasil.jee02.lazy;

import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import br.com.siswbrasil.jee02.bean.CidadeFilter;
import br.com.siswbrasil.jee02.entity.Cidade;
import br.com.siswbrasil.jee02.repository.CidadeRepository;
import lombok.Getter;
import lombok.Setter;

@ViewScoped
public class LazyCidadeDataModel extends LazyDataModel<Cidade> {

	private static final long serialVersionUID = 1L;

//	@Inject
	private CidadeRepository cidadeRepository;

	@Getter
	@Setter
	private CidadeFilter filter;

	public LazyCidadeDataModel(CidadeRepository cidadeRepository, CidadeFilter filter) {
		this.filter = filter;
		this.cidadeRepository = cidadeRepository;  

	}

	@Override
	public List<Cidade> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		System.out.println("-------------------");
		System.out.println("first " + first);
		System.out.println("pageSize " + pageSize);
		System.out.println("sortBy " + sortBy);
		System.out.println("filterBy " + filterBy);
		System.out.println("filter " + filter);
		System.out.println("-------------------");

		//Pageable pageable = PageRequest.of(first, pageSize);
		List<Cidade> page = cidadeRepository.findAll();
		// Page<Cidade> page = cidadeRepository.
		// cidadeRepository.findAll(pageable);

		setRowCount(10);

		// TODO Auto-generated method stub
		return page;
	}

}
