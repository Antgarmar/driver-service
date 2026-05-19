package com.driver.adapter.out.producer;

import com.driver.DriverProducer;
import com.driver.model.DriverLocationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverProducerImpl implements DriverProducer {

	private static final String TOPIC = "driver-location-topic";

	private final KafkaTemplate<String, DriverLocationEvent> kafkaTemplate;

	public void sendDriverLocation(DriverLocationEvent event) {
		this.kafkaTemplate.send(TOPIC, event.getDriverId(), event);
	}
}