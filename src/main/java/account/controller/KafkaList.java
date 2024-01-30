package account.controller;

import java.time.LocalDate;
import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.internals.Sender;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import jakarta.xml.bind.Marshaller.Listener;

@EnableKafka
public class KafkaList {

	private final Logger logger = LoggerFactory.getLogger(KafkaList.class);
	
	@KafkaListener(topics = "account", groupId = "${spring.kafka.consumer.group-id}")
	public void accountListener(String msg) {
		//Logic send mail or phone or another service to user
		logger.info("KafkaListener: accountListener, msg " + msg);
	}
	
	@KafkaListener(topics = "card", groupId = "${spring.kafka.consumer.group-id}")
	public void cardListener(String msg) {
		//Logic send mail or phone or another service to user
		logger.info("cardListener: cardListener, msg " + msg);
	}
}
