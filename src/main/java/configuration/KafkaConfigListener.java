package configuration;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class KafkaConfigListener {
	
	
	@Value(value = "${spring.kafka.bootstrap-servers}")
	public String bootstrapServer;

	@Value(value = "${spring.kafka.consumer.group-id}")
	public String groupId;

	@Bean
	public ConsumerFactory<String, String> consumerFactory(){
		HashMap<String, Object> cfg = new HashMap<>();
		cfg.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		cfg.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		cfg.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		cfg.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		
		return new DefaultKafkaConsumerFactory<>(cfg);
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> 
		concurrenctKafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, String> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		
		return factory;
	}
}
