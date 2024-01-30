package configuration;

import java.util.HashMap;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.internals.Topic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@Configuration
public class KafkaConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;
	
	@Bean
	public KafkaAdmin kafkaAdmin() {
		HashMap<String, Object> cfg = new HashMap<>();
		cfg.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		
		return new KafkaAdmin(cfg);
	}
	
	@Bean 
	public NewTopic topicAccount() {
		return TopicBuilder.name("account").build();
	}
	
	@Bean 
	public NewTopic topicCard() {
		return TopicBuilder.name("card").build();
	}
	
	@Bean
	public ProducerFactory<String, String> producerFactory(){
		HashMap<String, Object> cfg = new HashMap<>();
		cfg.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		cfg.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		cfg.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		return new DefaultKafkaProducerFactory<>(cfg);
	}
	
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
