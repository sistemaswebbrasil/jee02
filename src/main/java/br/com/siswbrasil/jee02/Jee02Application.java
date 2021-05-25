package br.com.siswbrasil.jee02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import br.com.siswbrasil.jee02.repository.CustomJpaRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class Jee02Application {

	public static void main(String[] args) {
		SpringApplication.run(Jee02Application.class, args);
	}

}
