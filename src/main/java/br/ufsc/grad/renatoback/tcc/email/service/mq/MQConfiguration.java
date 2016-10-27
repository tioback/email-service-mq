package br.ufsc.grad.renatoback.tcc.email.service.mq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	AmqpAdmin admin;

	@Bean
	Queue queue() {
		Queue queue = new Queue(EmailServiceMqApplication.QUEUE_NAME, false);
		admin.declareQueue(queue);
		return queue;
	}

	@Bean
	FanoutExchange exchange() {
		FanoutExchange exchange = new FanoutExchange(EmailServiceMqApplication.EXCHANGE_NAME, false, false);
		admin.declareExchange(exchange);
		return exchange;
	}

	@Bean
	Binding binding(Queue queue, FanoutExchange exchange) {
		Binding binding = BindingBuilder.bind(queue).to(exchange);
		admin.declareBinding(binding);
		return binding;
	}

}
