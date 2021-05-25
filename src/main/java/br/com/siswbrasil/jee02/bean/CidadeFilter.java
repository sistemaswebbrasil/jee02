package br.com.siswbrasil.jee02.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CidadeFilter {

	private String nome;

	private Long ibge;

	private Long estado;

}
