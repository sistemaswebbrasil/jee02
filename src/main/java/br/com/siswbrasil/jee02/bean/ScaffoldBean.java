package br.com.siswbrasil.jee02.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import org.primefaces.component.tabview.TabView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import br.com.siswbrasil.jee02.util.MessageUtil;
import br.com.siswbrasil.jee02.util.Messages;
import br.com.siswbrasil.scaffoldjee.OutputGenereate;
import br.com.siswbrasil.scaffoldjee.ScaffoldBuilder;
import br.com.siswbrasil.scaffoldjee.SourceProperty;
import br.com.siswbrasil.scaffoldjee.exception.ScaffoldBuilderException;
import br.com.siswbrasil.scaffoldjee.exception.ScaffoldBuilderNotFoudException;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ScaffoldBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Value("${scaffoldjee.source.entity}")
	private String baseEntitypath;

	@Value("${scaffoldjee.destination.labels}")
	private String labelsPath;

	@Inject
	private Messages messages;

	@Autowired
	private ScaffoldBuilder scaffoldBuilder;

	@Getter
	private List<SourceProperty> properties;

	@Getter
	@Setter
	private SourceProperty selected = new SourceProperty();

	@Getter
	@Setter
	private OutputGenereate outputGenereate = new OutputGenereate();

	@Getter
	@Setter
	private TabView tabView = new TabView();


	private void setOutputTabView() {
		tabView.setActiveIndex(1);
	}

	@PostConstruct
	public void init() {
		tabView.setActiveIndex(0);
		listProperties();
	}

	public void listProperties() {
		try {
			properties = scaffoldBuilder.scanSourceFiles(baseEntitypath, "entity");
		} catch (Exception e) {
			MessageUtil.addErrorMessage(messages.get("error"), e.getMessage());
		}
	}

	public void readProperties() {
		try {
			selected = scaffoldBuilder.readProperties(selected);
		} catch (ScaffoldBuilderNotFoudException e) {
			MessageUtil.addErrorMessage(messages.get("error"), e.getMessage());
		}
	}

	public void loadSelected() {
		readProperties();
	}

	public void generateLabels() throws ScaffoldBuilderException {
		try {
			System.out.println("###################################");
			outputGenereate = scaffoldBuilder.generateLabels(selected, labelsPath);
			setOutputTabView();
		} catch (ScaffoldBuilderNotFoudException e) {
			MessageUtil.addErrorMessage(messages.get("error"), e.getMessage());
		}
		tabView.setActiveIndex(1);
	}
	
	public void writeInFile() {
		try {
			scaffoldBuilder.writeInFile(outputGenereate);
		} catch (ScaffoldBuilderException e) {
			MessageUtil.addErrorMessage(messages.get("error"), e.getMessage());
		}
	}

}
