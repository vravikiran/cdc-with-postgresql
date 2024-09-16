package com.local.guide.app.notifications.listeners;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.local.guide.app.notifications.services.RequestProducerService;

import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class DBChangeEventListener {
	private final Executor excutor;
	private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
	@Autowired
	RequestProducerService requestProducerService;

	public DBChangeEventListener(Configuration postgresConnector) {
		this.excutor = Executors.newSingleThreadExecutor();
		this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
				.using(postgresConnector.asProperties()).notifying(this::handleChangeEvent).build();
	}

	@PostConstruct
	private void start() {
		this.excutor.execute(debeziumEngine);
	}

	private void handleChangeEvent(RecordChangeEvent<SourceRecord> event) {
		var sourceRecord = event.record();
		Struct sourceRecordChangeValue = (Struct) sourceRecord.value();
		Map<String, Object> payload = sourceRecordChangeValue.schema().fields().stream().map(Field::name)
				.filter(fieldName -> sourceRecordChangeValue.get(fieldName) != null)
				.map(fieldName -> Pair.of(fieldName, sourceRecordChangeValue.get(fieldName)))
				.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
		String tableName = (String) payload.get("__table");
		payload.remove("__op");
		payload.remove("__table");
		publishRecordChange(tableName, payload);
	}
	
	private void publishRecordChange(String tableName,Map<String,Object> payLoad) {
		switch(tableName.toUpperCase()) {
		case "REQUEST":
			requestProducerService.publishRequestUpdates(payLoad);
		}
	}

	@PreDestroy
	private void stop() throws IOException {
		if (this.debeziumEngine != null) {
			this.debeziumEngine.close();
		}
	}
}