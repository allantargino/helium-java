package com.microsoft.azure.helium.health;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

import com.microsoft.azure.helium.utils.IntegrationTestsUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.Health.Builder;

/**
 * CosmosDbHealthIndicatorTest
 */
@RunWith(MockitoJUnitRunner.class)
public class CosmosDbHealthIndicatorTest {

    @Spy
    CosmosDbHealthIndicator indicator = new CosmosDbHealthIndicator("database");

    @Test
    public void healthCheckIndicatorShouldReturnUp() throws Exception {
        // Arrange
        Builder builder = new Builder();
        Mockito.doReturn(getRandomHttpSuccessCode()).when(indicator).getStatusCode(anyString());

        // Act
        indicator.doHealthCheck(builder);
        Health health = builder.build();

        // Assert
        assertNotNull(health);
        assertThat(health.getStatus(), is(Status.UP));
    }

    @Test
    public void healthCheckIndicatorShouldReturnDown() throws Exception {
        // Arrange
        Builder builder = new Builder();
        Mockito.doReturn(getRandomHttpErrorCode()).when(indicator).getStatusCode(anyString());

        // Act
        indicator.doHealthCheck(builder);
        Health health = builder.build();

        // Assert
        assertNotNull(health);
        assertThat(health.getStatus(), is(Status.DOWN));
    }

    private int getRandomHttpSuccessCode(){
        return IntegrationTestsUtils.getRandomBetween(200, 204);
    }

    private int getRandomHttpErrorCode(){
        return IntegrationTestsUtils.getRandomBetween(400, 600);
    }
}