package ar.com.intrale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import ar.com.intrale.exceptions.FunctionException;
import ar.com.intrale.functions.DeleteNotificationTokenFunction;
import ar.com.intrale.functions.SendNotificationFunction;
import ar.com.intrale.messages.DeleteNotificationContactRequest;
import ar.com.intrale.messages.DeleteNotificationTokenRequest;
import ar.com.intrale.messages.FunctionExceptionResponse;
import ar.com.intrale.messages.Response;
import ar.com.intrale.messages.SaveNotificationTokenRequest;
import ar.com.intrale.messages.SendNotificationRequest;
import ar.com.intrale.models.DeviceToken;
import ar.com.intrale.models.NotificationContact;
import io.micronaut.test.annotation.MicronautTest;


@MicronautTest(rebuildContext = true )
public class ServicesTest extends ar.com.intrale.Test {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesTest.class);

	@Override
    public void beforeEach() {
		FirebaseMessaging provider = Mockito.mock(FirebaseMessaging.class);
    	//applicationContext.registerSingleton(FirebaseMessaging.class, provider);
    	LOGGER.info("applicationContext:" + applicationContext);
    	LOGGER.info("ServicesTest beforeEach provider:" + provider);
    	LOGGER.info("FirebaseMessaging registred");
    }

	@Override
	public void afterEach() {
		/*FirebaseMessaging provider = applicationContext.getBean(FirebaseMessaging.class);
    	Mockito.reset(provider);
    	LOGGER.info("FirebaseMessaging reset");*/
	}
    
    @Test
    public void test() throws Exception {
    	FirebaseMessaging provider = applicationContext.getBean(FirebaseMessaging.class);
    	LOGGER.info("provider on test:" + provider);
    	APIGatewayProxyResponseEvent responseEvent = null;
    	Response response  = null;
    	SaveNotificationTokenRequest request = null;
    	SendNotificationRequest notificationRequest = null;
    	DeleteNotificationContactRequest deleteNotificationContactRequest = null;
    	
    	/* a)Almacenamos token para un contacto en un negocio en particular 
    	 * Guardar token
    	 * 			business: 	[businessA]
    	 * 			email:		[emailContactoA]
    	 * 			device:		[deviceA]
    	 * 			token:		nuevo
    	 */
    	
    	String emailContactoA = faker.internet().emailAddress();
    	String businessA = faker.app().name();
    	String deviceA = faker.internet().userAgentAny();
    	
    	request = new SaveNotificationTokenRequest();
    	request.setRequestId(faker.idNumber().ssnValid());
    	request.setEmail(emailContactoA);    	
    	request.setToken(faker.internet().uuid());
    	request.setDevice(deviceA);
    	
    	responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(request, FunctionConst.SAVE, businessA));
    	response  = readEncodedValue(responseEvent.getBody(), Response.class);
    	
    	assertEquals(200, response.getStatusCode());
    	

    	/* b)Almacenamos otro token de un dispositivo diferente para el mismo contacto y el mismo negocio anterior
    	 * Guardar token
    	 * 			business: 	[businessA]
    	 * 			email:		[emailContactoA]
    	 * 			device:		[deviceB]
    	 * 			token:		nuevo
    	 */
    	resetFaker();
    	
    	String deviceB = faker.internet().userAgentAny();
   
    	request = new SaveNotificationTokenRequest();
    	request.setRequestId(faker.idNumber().ssnValid());
    	request.setEmail(emailContactoA);    	
    	request.setToken(faker.internet().uuid());
    	request.setDevice(deviceB);
    	
    	responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(request, FunctionConst.SAVE, businessA));
    	response  = readEncodedValue(responseEvent.getBody(), Response.class);
    	 
    	assertEquals(200, response.getStatusCode());

    	/* c) Para el contacto anterior, en el mismo dispositivo, refrescamos el token almacenado
    	 * Guardar token
    	 * 			business: 	[businessA]
    	 * 			email:		[emailContactoA]
    	 * 			device:		[deviceB]
    	 * 			token:		nuevo
    	 */
    	resetFaker();
    	
    	request = new SaveNotificationTokenRequest();
    	request.setRequestId(faker.idNumber().ssnValid());
    	request.setEmail(emailContactoA);    	
    	request.setToken(faker.internet().uuid());
    	request.setDevice(deviceB);
    	
    	responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(request, FunctionConst.SAVE, businessA));
    	response  = readEncodedValue(responseEvent.getBody(), Response.class);
    	
    	assertEquals(200, response.getStatusCode());
    	
    	/* d) Para el mismo contacto anterior, asignamos un token de un dispositivo en un negocio nuevo
    	 * Guardar token
    	 * 			business: 	[businessB]
    	 * 			email:		[emailContactoA]
    	 * 			device:		[deviceA]
    	 * 			token:		nuevo
    	 */
    	resetFaker();
    	
    	String businessB = faker.app().name();
    	
    	request = new SaveNotificationTokenRequest();
    	request.setRequestId(faker.idNumber().ssnValid());
    	request.setEmail(emailContactoA);    	
    	request.setToken(faker.internet().uuid());
    	request.setDevice(deviceA);
    	
    	responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(request, FunctionConst.SAVE, businessB));
    	response  = readEncodedValue(responseEvent.getBody(), Response.class);
    	
    	assertEquals(200, response.getStatusCode());
    	
    	/* e) Para  un contacto nuevo, asignamos un token de un dispositivo en el negocio anterior
    	 * Guardar token
    	 * 			business: 	[businessA]
    	 * 			email:		[emailContactoE]
    	 * 			device:		[deviceE]
    	 * 			token:		nuevo
    	 */
    	resetFaker();
    	String emailContactoE = faker.internet().emailAddress();
    	String deviceE = faker.internet().userAgentAny();
   
    	request = new SaveNotificationTokenRequest();
    	request.setRequestId(faker.idNumber().ssnValid());
    	request.setEmail(emailContactoE);    	
    	request.setToken(faker.internet().uuid());
    	request.setDevice(deviceE);
    	
    	responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(request, FunctionConst.SAVE, businessA));
    	response  = readEncodedValue(responseEvent.getBody(), Response.class);
    	
    	assertEquals(200, response.getStatusCode());
    	
    	/* f) Para el contacto anterior en el mismo negocio, refrescamos el token del dispositivo anterior
    	 * Guardar token
    	 * 			business: 	[businessA]
    	 * 			email:		[emailContactoE]
    	 * 			device:		[deviceE]
    	 * 			token:		nuevo
    	 */
    	resetFaker();

    	request = new SaveNotificationTokenRequest();
    	request.setRequestId(faker.idNumber().ssnValid());
    	request.setEmail(emailContactoE);    	
    	request.setToken(faker.internet().uuid());
    	request.setDevice(deviceE);
    	
    	responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(request, FunctionConst.SAVE, businessA));
    	response  = readEncodedValue(responseEvent.getBody(), Response.class);
    	
    	assertEquals(200, response.getStatusCode());
    	
    	/* g) 
    	 * Enviar notificacion
    	 * 			business: 	[businessA]
    	 * 			email:		[emailContactoA]
    	 */
    	resetFaker();
    	
    	notificationRequest = new SendNotificationRequest();
        notificationRequest.setRequestId(faker.idNumber().ssnValid());
        notificationRequest.setEmail(emailContactoA);
        notificationRequest.setMessage(faker.book().title());
        
        responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(notificationRequest, SendNotificationFunction.SEND, businessA));
        response  = readEncodedValue(responseEvent.getBody(), Response.class);
        
        assertEquals(200, response.getStatusCode());
    	
    	/* h) 
    	 * Enviar notificacion
    	 * 			business: 	[businessB]
    	 * 			email:		[emailContactoA]
    	 */
    	resetFaker();
    	
    	notificationRequest = new SendNotificationRequest();
        notificationRequest.setRequestId(faker.idNumber().ssnValid());
        notificationRequest.setEmail(emailContactoA);
        notificationRequest.setMessage(faker.book().title());
        
        responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(notificationRequest, SendNotificationFunction.SEND, businessB));
        response  = readEncodedValue(responseEvent.getBody(), Response.class);
        
        assertEquals(200, response.getStatusCode());
        
    	/* i) 
    	 * Enviar notificacion
    	 * 			business: 	[businessA]
    	 * 			email:		[emailContactoE]
    	 */
    	resetFaker();
    	
    	notificationRequest = new SendNotificationRequest();
        notificationRequest.setRequestId(faker.idNumber().ssnValid());
        notificationRequest.setEmail(emailContactoE);
        notificationRequest.setMessage(faker.book().title());
        
        responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(notificationRequest, SendNotificationFunction.SEND, businessA));
        response  = readEncodedValue(responseEvent.getBody(), Response.class);
        
        assertEquals(200, response.getStatusCode());
        
        // Enviar notificacion a contacto inexistente
        resetFaker();

        notificationRequest = new SendNotificationRequest();
        notificationRequest.setRequestId(faker.idNumber().ssnValid());
        notificationRequest.setEmail(faker.internet().emailAddress());
        notificationRequest.setMessage(faker.book().title());

        responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(notificationRequest, SendNotificationFunction.SEND, businessA));
        FunctionExceptionResponse exceptionResponse  = readEncodedValue(responseEvent.getBody(), FunctionExceptionResponse.class);

        assertNotEquals(200, exceptionResponse.getStatusCode());
        
        
        // J) Enviar notificacion con un topico
    	// K) Enviar notificacion con un topico diferente
        
        
    	// M) Eliminar contacto emailContactoA para el negocio businessA
        deleteNotificationContactRequest = new DeleteNotificationContactRequest();
        deleteNotificationContactRequest.setRequestId(DUMMY_VALUE);
        deleteNotificationContactRequest.setEmail(emailContactoA);

        responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(deleteNotificationContactRequest, FunctionConst.DELETE, businessA));
        response  = readEncodedValue(responseEvent.getBody(), Response.class);

        assertEquals(200, response.getStatusCode());
    	
    	// N) Eliminar contacto emailContactoA para el negocio businessB
        deleteNotificationContactRequest = new DeleteNotificationContactRequest();
        deleteNotificationContactRequest.setRequestId(DUMMY_VALUE);
        deleteNotificationContactRequest.setEmail(emailContactoA);

        responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(deleteNotificationContactRequest, FunctionConst.DELETE, businessB));
        response  = readEncodedValue(responseEvent.getBody(), Response.class);

        assertEquals(200, response.getStatusCode());
        
    	// O) Eliminar contacto emailContactoE para el negocio businessA
        deleteNotificationContactRequest = new DeleteNotificationContactRequest();
        deleteNotificationContactRequest.setRequestId(DUMMY_VALUE);
        deleteNotificationContactRequest.setEmail(emailContactoE);

        responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(deleteNotificationContactRequest, FunctionConst.DELETE, businessA));
        response  = readEncodedValue(responseEvent.getBody(), Response.class);

        assertEquals(200, response.getStatusCode());        

        DeviceToken deviceToken = new DeviceToken();
        assertNotEquals(Boolean.TRUE, deviceToken.equals(null));
        assertNotEquals(Boolean.TRUE, deviceToken.equals(new NotificationContact()));
        assertEquals(Boolean.TRUE, deviceToken.equals(deviceToken));
        
        // Borrar un token que no exista
        DeleteNotificationTokenRequest deleteNotificationTokenRequest = new DeleteNotificationTokenRequest();
        deleteNotificationTokenRequest.setRequestId(DUMMY_VALUE);
        deleteNotificationTokenRequest.setDevice(deviceA);
        deleteNotificationTokenRequest.setEmail(emailContactoE);
        
        responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(deleteNotificationTokenRequest, DeleteNotificationTokenFunction.FUNCTION_NAME, businessA));
        FunctionExceptionResponse errorResponse  = readEncodedValue(responseEvent.getBody(), FunctionExceptionResponse.class);

        assertNotEquals(200, errorResponse.getStatusCode());        
        
        
    }
    
    @Test
    public void testSendException() throws IOException {
    	FirebaseMessaging provider = applicationContext.getBean(FirebaseMessaging.class);
    	LOGGER.info("provider on test:" + provider);
    	APIGatewayProxyResponseEvent responseEvent = null;
    	Response response  = null;
    	SaveNotificationTokenRequest request = null;
    	SendNotificationRequest notificationRequest = null;
    	DeleteNotificationContactRequest deleteNotificationContactRequest = null;
    	
    	/* a)Almacenamos token para un contacto en un negocio en particular 
    	 * Guardar token
    	 * 			business: 	[businessA]
    	 * 			email:		[emailContactoA]
    	 * 			device:		[deviceA]
    	 * 			token:		nuevo
    	 */
    	
    	String emailContactoA = faker.internet().emailAddress();
    	String businessA = faker.app().name();
    	String deviceA = faker.internet().userAgentAny();
    	
    	LOGGER.info("Envio con excepcion para el contacto:" + emailContactoA);
    	
    	request = new SaveNotificationTokenRequest();
    	request.setRequestId(faker.idNumber().ssnValid());
    	request.setEmail(emailContactoA);    	
    	request.setToken(faker.internet().uuid());
    	request.setDevice(deviceA);
    	
    	responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(request, FunctionConst.SAVE, businessA));
    	response  = readEncodedValue(responseEvent.getBody(), Response.class);
    	
    	assertEquals(200, response.getStatusCode());
    	
        /* Enviar notificacion a contacto y que esta arroje error
	   	 * 			business: 	[businessA]
	   	 * 			email:		[emailContactoE]
	   	 */
		try {
			when(provider.send(any())).thenThrow(mock(FirebaseMessagingException.class));
		} catch (FirebaseMessagingException e) {
			LOGGER.error(FunctionException.toString(e));
		}
	    
		notificationRequest = new SendNotificationRequest();
        notificationRequest.setRequestId(faker.idNumber().ssnValid());
        notificationRequest.setEmail(emailContactoA);
        notificationRequest.setMessage(faker.book().title());
    	
	    try {
			Message message = Message.builder()
				    .putData("message", faker.book().title())
				    .setToken("1111111")
				    .build();
	    	provider.send(message);
	    	assertTrue(Boolean.FALSE);
	    } catch (FirebaseMessagingException e) {
			assertTrue(Boolean.TRUE);
		}

	    SendNotificationFunction function = lambda.getApplicationContext().getBean(SendNotificationFunction.class);
	    function.setProvider(provider);
	    responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(notificationRequest, SendNotificationFunction.SEND, businessA));
	    response  = readEncodedValue(responseEvent.getBody(), Response.class);
	    
	    assertEquals(200, response.getStatusCode());
	    
    	// M) Eliminar contacto emailContactoA para el negocio businessA
        deleteNotificationContactRequest = new DeleteNotificationContactRequest();
        deleteNotificationContactRequest.setRequestId(DUMMY_VALUE);
        deleteNotificationContactRequest.setEmail(emailContactoA);

        responseEvent = (APIGatewayProxyResponseEvent) lambda.execute(makeRequestEvent(deleteNotificationContactRequest, FunctionConst.DELETE, businessA));
        response  = readEncodedValue(responseEvent.getBody(), Response.class);

        assertEquals(200, response.getStatusCode());
    }
    
}
