package br.ufsc.grad.renatoback.tcc.email.service.mq;

import static java.lang.System.getenv;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class EmailServiceMqApplication {

	public static final String QUEUE_NAME = "send-email.queue";
	public static final String EXCHANGE_NAME = "customer-creation.exchange";

	public static void main(String[] args) {
		SpringApplication.run(EmailServiceMqApplication.class, args);
	}

	@Profile("heroku")
	@Bean(name = "connectionFactory")
	public ConnectionFactory connectionFactory() {
		URI ampqUrl;
		try {
			// ampqUrl = new URI(getEnvOrThrow("CLOUDAMQP_URL"));
			ampqUrl = new URI("amqp://ftaollxx:33X65Vbm3dBU-sKLYczjFc7hUxlYSsk4@buck.rmq.cloudamqp.com/ftaollxx");
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		final CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setUsername(ampqUrl.getUserInfo().split(":")[0]);
		factory.setPassword(ampqUrl.getUserInfo().split(":")[1]);
		factory.setHost(ampqUrl.getHost());
		factory.setPort(ampqUrl.getPort());
		factory.setVirtualHost(ampqUrl.getPath().substring(1));

		return factory;
	}

	private static String getEnvOrThrow(String name) {
		final String env = getenv(name);
		if (env == null) {
			throw new IllegalStateException("Environment variable [" + name + "] is not set.");
		}
		return env;
	}
}
