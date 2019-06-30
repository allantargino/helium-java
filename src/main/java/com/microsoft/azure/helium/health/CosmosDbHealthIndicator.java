package com.microsoft.azure.helium.health;

import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.RequestOptions;
import com.microsoft.azure.documentdb.ResourceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * CosmosDbHealthIndicator
 */
@Component
public class CosmosDbHealthIndicator extends AbstractHealthIndicator {

	@Autowired
	private DocumentClient documentClient;

	private String dbName;

	public CosmosDbHealthIndicator(@Value("${azure.cosmosdb.database}") String dbName) {
		super();
		this.dbName = dbName;
	}

	@Override
	protected void doHealthCheck(Builder builder) throws DocumentClientException {
		try {
			int statusCode = getStatusCode(dbName);
			if (HttpStatus.valueOf(statusCode).is2xxSuccessful()) {
				builder.up().build();
			} else {
				builder.down().withDetail("Error Code", statusCode).build();
			}
		} catch (Exception ex) {
			builder.down().withDetail("Error", ex.getMessage()).build();
		}
	}

	protected int getStatusCode(String dbName) throws DocumentClientException {
		ResourceResponse<Database> response = this.documentClient.readDatabase("dbs/" + dbName, new RequestOptions());
		return response.getStatusCode();
	}

}