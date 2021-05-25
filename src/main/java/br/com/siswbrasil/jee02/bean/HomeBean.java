package br.com.siswbrasil.jee02.bean;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;

@Named
@ViewScoped
public class HomeBean {

	@Getter
	private String teste = "Olá mundo";
}
