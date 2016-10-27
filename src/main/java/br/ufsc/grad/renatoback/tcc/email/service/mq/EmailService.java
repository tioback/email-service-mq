package br.ufsc.grad.renatoback.tcc.email.service.mq;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private Log logger = LogFactory.getLog(EmailService.class);

	AtomicInteger counter = new AtomicInteger();

	public void sendEmail(String time) {
		logger.info(String.format("OK Email #%d after %d ns", counter.incrementAndGet(),
				System.nanoTime() - Long.parseLong(time)));
	}

}
