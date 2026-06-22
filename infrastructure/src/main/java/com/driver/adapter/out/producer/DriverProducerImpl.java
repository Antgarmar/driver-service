package com.driver.adapter.out.producer;

import com.driver.port.out.DriverProducer;
import com.driver.model.DriverLocationEvent;
import com.driver.model.DriverStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverProducerImpl implements DriverProducer {

	private static final String LOCATION_TOPIC = "driver-location-topic";
	private static final String STATUS_TOPIC = "driver-status-changed-topic";

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public void sendDriverLocation(DriverLocationEvent event) {
		log.info("Sending driver location event to topic: {}", LOCATION_TOPIC);
		this.kafkaTemplate.send(LOCATION_TOPIC, event.getDriverId(), event);
	}

	@Override
	public void sendDriverStatusChanged(DriverStatusChangedEvent event) {
		log.info("Sending driver status changed event to topic: {} for driver: {}", 
				 STATUS_TOPIC, event.getDriverId());
		this.kafkaTemplate.send(STATUS_TOPIC, event.getDriverId(), event);
	}
}