package br.ufsc.grad.renatoback.tcc.email.service.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailServiceMqApplication {

	public static final String QUEUE_NAME = "send-email.queue";
	public static final String EXCHANGE_NAME = "customer-creation.exchange";

	public static void main(String[] args) {
		SpringApplication.run(EmailServiceMqApplication.class, args);
	}
}
