package br.ufsc.grad.renatoback.tcc.email.service.mq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfiguration {

	@Bean
	MessageListenerAdapter listenerAdapter(EmailService receiver) {
		return new MessageListenerAdapter(receiver, "sendEmail");
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(EmailServiceMqApplication.QUEUE_NAME);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean
	Queue queue(AmqpAdmin amqpAdmin) {
		Queue queue = new Queue(EmailServiceMqApplication.QUEUE_NAME, false);
		amqpAdmin.declareQueue(queue);
		return queue;
	}

	@Bean
	FanoutExchange exchange(AmqpAdmin amqpAdmin) {
		FanoutExchange exchange = new FanoutExchange(EmailServiceMqApplication.EXCHANGE_NAME, false, false);
		amqpAdmin.declareExchange(exchange);
		return exchange;
	}

	@Bean
	Binding binding(AmqpAdmin amqpAdmin, Queue queue, FanoutExchange exchange) {
		Binding binding = BindingBuilder.bind(queue).to(exchange);
		amqpAdmin.declareBinding(binding);
		return binding;
	}

}
