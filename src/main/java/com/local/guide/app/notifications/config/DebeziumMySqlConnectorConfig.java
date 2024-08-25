package com.local.guide.app.notifications.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

public class DebeziumMySqlConnectorConfig {
	@Value("${mysql.db.host}")
	private String dbHost;
	@Value("${mysql.db.port}")
	private String dbPort;
	@Value("${mysql.db.username}")
	private String userName;
	@Value("${mysql.db.password}")
	private String password;
	@Value("${mysql.schema.include.list}")
	private String schemaList;
	@Value("${mysql.table.include.list}")
	private String tableList;
	
	public io.debezium.config.Configuration mysqlConnector(){
		Map<String, String> configMap = new HashMap<>();
		configMap.put("name",  "request_mysql_connector");
		configMap.put("connector.class","io.debezium.connector.mysql.MySqlConnector");
		configMap.put("database.hostname", dbHost);
        configMap.put("database.port", dbPort);
        configMap.put("database.user", userName);
        configMap.put("database.password", password);
        configMap.put("schema.include.list", schemaList);
        configMap.put("table.include.list", tableList);
        configMap.put("database.server.name", "request-mysql-service");
        configMap.put("database.dbname", "localapp");
        configMap.put("topic.prefix", "mysql_");
        configMap.put("database.allowPublicKeyRetrieval", "true");
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
		return io.debezium.config.Configuration.from(configMap);
	}
}
