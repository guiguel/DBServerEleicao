package br.com.dbserver.eleicao.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DbserverEleicaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbserverEleicaoApplication.class, args);
	}

}
