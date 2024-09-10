package com.local.guide.app.notifications.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;

@Configuration
public class DebeziumPostgresConnectorConfig {
	@Value("${db.host}")
	private String dbHost;
	@Value("${db.port}")
	private String dbPort;
	@Value("${db.username}")
	private String postgresUsername;
	@Value("${db.password}")
	private String postgresPassword;
	@Value("${schema.include.list}")
	private String schemaIncludeList;
	@Value("${table.include.list}")
	private String tableIncludeList;

	@Bean
	public io.debezium.config.Configuration postgresConnector() {
		Map<String, String> configMap = new HashMap<>();
		configMap.put("name",  "customer_postgres_connector");
		configMap.put("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
		configMap.put("database.hostname", dbHost);
        configMap.put("database.port", dbPort);
        configMap.put("database.user", postgresUsername);
        configMap.put("database.password", postgresPassword);
        configMap.put("plugin.name", "pgoutput");
        configMap.put("schema.include.list", schemaIncludeList);
        configMap.put("table.include.list", tableIncludeList);
        configMap.put("database.server.name", "request-service");
        configMap.put("database.dbname", "postgres");
        configMap.put("topic.prefix", "ps_");
        configMap.put("plugin.path", "pgoutput");
        File offsetStorageTempFile = new File("offsets_.dat");
        configMap.put("offset.storage",  "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        configMap.put("offset.storage.file.filename", offsetStorageTempFile.getAbsolutePath());
        configMap.put("offset.flush.interval.ms", "60000");
        configMap.put("value.converter", "org.apache.kafka.connect.json.JsonConverter");
        configMap.put("value.converter.schemas.enable", "false");
        configMap.put("key.converter", "org.apache.kafka.connect.json.JsonConverter");
        configMap.put("key.converter.schemas.enable", "false");
        configMap.put("transforms", "unwrap");
        configMap.put("transforms.unwrap.type", "io.debezium.transforms.ExtractNewRecordState");
        configMap.put("transforms.unwrap.add.fields","table,op");
        configMap.put("time.precision.mode", "connect");
		return io.debezium.config.Configuration.from(configMap);
	}
}