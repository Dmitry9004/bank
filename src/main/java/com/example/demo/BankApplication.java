package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import account.core.EStatus;
import card.core.CardService;
import card.core.ECard;
import card.model.Card;
import user.core.UserService;
import user.model.ERole;
import user.model.User;

@ComponentScan({
	"interfaces",
	"configuration",
	"security",
	"account",
	"card",
	"currency",
	"transfer",
	"user"
})
@EnableJpaRepositories({
	"account.repository",
	"card.repository",
	"user.repository",
})
@EntityScan({
	"account.model",
	"card.model",
	"user.model"
})
@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class,args);
	}
}
