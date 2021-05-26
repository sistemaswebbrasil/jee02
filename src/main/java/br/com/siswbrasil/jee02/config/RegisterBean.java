package br.com.siswbrasil.jee02.config;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.siswbrasil.scaffoldjee.ScaffoldBuilder;

@Configuration
public class RegisterBean {

	@Bean
	public ScaffoldBuilder scaffoldBuilder() {
		return new ScaffoldBuilder();
	}

}
