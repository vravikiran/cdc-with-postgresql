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
public class RequestEventListener {
	private final Executor excutor;
	private final DebeziumEngine<ChangeEvent<String, String>> debeziumEngine;

	public RequestEventListener(Configuration mysqlConnector) {
		this.excutor = Executors.newSingleThreadExecutor();
		this.debeziumEngine = DebeziumEngine.create(Json.class).using(mysqlConnector.asProperties())
				.notifying(this::handleChangeEvent).build();
	}

	private void handleChangeEvent(ChangeEvent<String, String> event) {
		System.out.println("Event Key:: " + event.key());
		System.out.println("Event Value:: " + event.value());
	}

	@PostConstruct
	private void start() {
		this.excutor.execute(debeziumEngine);
	}

	@PreDestroy
	private void stop() throws IOException {
		if (this.debeziumEngine != null) {
			this.debeziumEngine.close();
		}
	}

}
