package com.local.guide.app.notifications.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.local.guide.app.notifications.entities.User;
import com.local.guide.app.notifications.services.RequestNotificationService;

import io.debezium.config.Configuration;

@RestController
@RequestMapping("/guides")
public class GuideController {
	@Autowired
	RequestNotificationService notificationService;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	Configuration customerConnector;
	
	@GetMapping("/locations")
	public ResponseEntity<List<User>> getGuidesByLocation(@RequestParam("city_id") int city_id,
			@RequestParam("state_id") int state_id, @RequestParam("country_id") int country_id) {
		Map<String, Integer> requestParams = new HashMap<>();
		requestParams.put("city_id", city_id);
		requestParams.put("country_id", country_id);
		requestParams.put("state_id", state_id);
		System.out.println(customerConnector);
		/*ResponseEntity<List<User>> result = restTemplate.exchange(
				"http://localhost:8090/user/location?city_id={city_id}&country_id={country_id}&state_id={state_id}",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
				}, requestParams);
		List<User> users = result.getBody();
		notificationService.notifyLocalGuides(users);*/
		return ResponseEntity.ok(null);
	}
}
