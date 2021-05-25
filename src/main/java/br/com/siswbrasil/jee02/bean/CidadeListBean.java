package br.com.siswbrasil.jee02.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;

import br.com.siswbrasil.jee02.entity.Cidade;
import br.com.siswbrasil.jee02.entity.Estado;
import br.com.siswbrasil.jee02.repository.CidadeRepository;
import br.com.siswbrasil.jee02.repository.EstadoRepository;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class CidadeListBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CidadeRepository cidadeRepository;

	@Inject
	private EstadoRepository estadoRepository;

	@Setter
	@Getter
	private List<Cidade> cidadeList;

	@Getter
	private List<Estado> estadoList;

	@Setter
	@Getter
	private LazyDataModel<Cidade> lazyModel;

	@Setter
	@Getter
	private CidadeFilter filter = new CidadeFilter();

	@Setter
	@Getter
	private List<FilterMeta> filterBy;

	@PostConstruct
	public void init() {
		estadoList = estadoRepository.findAllByOrderBySigla();
		load();
	}

	public void load() {
		System.out.println("Filtro manual " + filter);
		this.setLazyModel(new LazyDataModel<Cidade>() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Cidade> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				filterBy.clear();
				if (!ObjectUtils.isEmpty(filter.getNome())) {
					filterBy.put("", FilterMeta.builder().field("nome").filterValue(filter.getNome())
							.matchMode(MatchMode.STARTS_WITH).build());
				}

				if (!ObjectUtils.isEmpty(filter.getIbge())) {
					filterBy.put("ibge", FilterMeta.builder().field("ibge").filterValue(filter.getIbge())
							.matchMode(MatchMode.STARTS_WITH).build());
				}
				
				if (!ObjectUtils.isEmpty(filter.getEstado())) {
					filterBy.put("estado.id", FilterMeta.builder().field("estado").filterValue(filter.getEstado())
							.matchMode(MatchMode.EXACT).build());
				}
				Page<Cidade> page = cidadeRepository.datatableLazyFilter(first, pageSize, sortBy, filterBy);
				setRowCount((int) page.getTotalElements());
				return page.getContent();
			}
		});
	}

	public void search() {
		load();
	}
	
	public void clear() {		
		filter = new CidadeFilter();		
	}

}
