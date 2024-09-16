package com.local.guide.app.notifications.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RequestProducerService {
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	public void publishRequestUpdates(Map<String, Object> payload) {
		try {
			kafkaTemplate.send("request",objectMapper.writeValueAsString(payload));
			System.out.println("successfully published the message");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
