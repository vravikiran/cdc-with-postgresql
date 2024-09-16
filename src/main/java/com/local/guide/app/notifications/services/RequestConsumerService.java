package com.local.guide.app.notifications.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
@KafkaListener(topics = { "request" })
public class RequestConsumerService {
	
	@KafkaListener(topics = { "request" }, containerFactory = "kafkaListenerStringFactory", concurrency = "4")
	public void consumeMessages(@Payload String message) throws JsonMappingException, JsonProcessingException {
		System.out.println("received message" + message);
	}
}
