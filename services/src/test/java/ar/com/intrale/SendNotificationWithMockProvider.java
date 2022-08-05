package ar.com.intrale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import ar.com.intrale.functions.SendNotificationFunction;
import ar.com.intrale.messages.FunctionExceptionResponse;
import ar.com.intrale.messages.Response;
import ar.com.intrale.messages.SendNotificationRequest;
import ar.com.intrale.models.DeviceToken;
import ar.com.intrale.models.NotificationContact;
import io.micronaut.context.annotation.Property;
import io.micronaut.test.annotation.MicronautTest;



@MicronautTest(rebuildContext = true )
@Property(name = IntraleFactory.PROVIDER, value = IntraleFactory.FALSE)
public class SendNotificationWithMockProvider extends ar.com.intrale.Test {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SendNotificationWithMockProvider.class);

	@Override
    public void beforeEach() {
		NotificationContactProvider provider = mock(NotificationContactProvider.class);
    	
    	NotificationContact notificationContact = mock(NotificationContact.class);
    	Set<NotificationContact> contacts = new HashSet<>();
    	contacts.add(notificationContact);
    	DeviceToken deviceToken = mock(DeviceToken.class);
    	Date expired;
		try {
			expired = new SimpleDateFormat("dd/MM/yyyy HH:mm")
			        .parse("29/06/1986 12:00");
			LOGGER.info("Expired date:" + expired.toLocaleString());
			when(deviceToken.getLastActivity()).thenReturn(expired);
			when(deviceToken.getDevice()).thenReturn(faker.internet().userAgentAny());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		when(provider.get(any())).thenReturn(notificationContact);
		Set<DeviceToken> tokens = new HashSet<>();
		tokens.add(deviceToken);
		when(notificationContact.getTokens()).thenReturn(tokens);
		
		applicationContext.registerSingleton(NotificationContactProvider.class, provider);
		LOGGER.info("In test, applicationContext:" + applicationContext + ", provider:" + applicationContext.getBean(NotificationContactProvider.class));
    }

	@Override
	public void afterEach() {}
    
    @Test
    public void testExpiration() throws IOException, ParseException {
    	LOGGER.info("initiating testExpiration:" + config.getInstantiate().getProvider());
	    
		SendNotificationRequest notificationRequest = new SendNotificationRequest();
        notificationRequest.setRequestId(faker.idNumber().ssnValid());
        notificationRequest.setEmail(faker.internet().emailAddress());
        notificationRequest.setMessage(faker.book().title());

        APIGatewayProxyResponseEvent responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(notificationRequest, SendNotificationFunction.SEND, faker.app().name()));
        Response response  = readEncodedValue(responseEvent.getBody(), Response.class);
	    
	    assertEquals(200, response.getStatusCode());

		NotificationContactProvider provider = mock(NotificationContactProvider.class);
		when(provider.get(any())).thenReturn(null);

		responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(notificationRequest, SendNotificationFunction.SEND, faker.app().name()));
		FunctionExceptionResponse functionExceptionResponse  = readEncodedValue(responseEvent.getBody(), FunctionExceptionResponse.class);
	    
	    assertEquals(200, functionExceptionResponse.getStatusCode());
	    
	    
	    
    }
}
