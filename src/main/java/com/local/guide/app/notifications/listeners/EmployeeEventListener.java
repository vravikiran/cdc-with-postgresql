package com.local.guide.app.notifications.listeners;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class EmployeeEventListener {
	private final Executor excutor;
	private final DebeziumEngine<ChangeEvent<String,String>> debeziumEngine;

	public EmployeeEventListener(Configuration postgresConnector) {
		this.excutor = Executors.newSingleThreadExecutor();
		this.debeziumEngine = DebeziumEngine.create(Json.class)
				.using(postgresConnector.asProperties())
				.notifying(this::handleChangeEvent)
				.build();
	}
	
	@PostConstruct
	private void start() {
		this.excutor.execute(debeziumEngine);
	}
	
	private void handleChangeEvent(ChangeEvent<String,String> event) {
		System.out.println("Event Key:: "+event.key());
		System.out.println("Event Value:: "+event.value());
	}
	
	@PreDestroy
	private void stop() throws IOException {
		if(this.debeziumEngine != null) {
			this.debeziumEngine.close();
		}
	}
}
