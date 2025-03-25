package com.cledess.thats_my_jam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;

@Configuration
public class ConfigurationApplication {
  
  @Value("${spring.data.mongodb.database}")
  private String database;

  @Bean
  MongoOperations mongoTemplate(MongoClient mongoClient) {
      return new MongoTemplate(mongoClient, database);
  }
}
