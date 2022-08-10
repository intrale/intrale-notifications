package ar.com.intrale.functions;

import java.util.Date;
import java.util.function.Consumer;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import ar.com.intrale.BaseFunction;
import ar.com.intrale.FunctionResponseToBase64HttpResponseBuilder;
import ar.com.intrale.builders.StringToSendNotificationRequestBuilder;
import ar.com.intrale.exceptions.FunctionException;
import ar.com.intrale.exceptions.NotFoundException;
import ar.com.intrale.messages.DeleteNotificationTokenRequest;
import ar.com.intrale.messages.DeviceTokenMessage;
import ar.com.intrale.messages.Error;
import ar.com.intrale.messages.GetNotificationContactRequest;
import ar.com.intrale.messages.GetNotificationContactResponse;
import ar.com.intrale.messages.Response;
import ar.com.intrale.messages.SaveNotificationTokenRequest;
import ar.com.intrale.messages.SendNotificationRequest;
import io.micronaut.http.HttpStatus;

@Singleton
@Named(SendNotificationFunction.SEND)
public class SendNotificationFunction extends
		BaseFunction<SendNotificationRequest, Response, FirebaseMessaging, StringToSendNotificationRequestBuilder, FunctionResponseToBase64HttpResponseBuilder> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendNotificationFunction.class);

	public static final String SEND = "send";

	@Override
	public Response execute(SendNotificationRequest request) throws FunctionException {
		LOGGER.info("Inicializando SendNotification");

		Response response = (Response) callFunction(
				GetNotificationContactFunction.class, new GetNotificationContactRequest(request.getEmail()), request);

		LOGGER.info("response:" + response);
		LOGGER.info("response.getStatusCode():" + response.getStatusCode());
		if (response.getStatusCode() == HttpStatus.OK.getCode()) {
			GetNotificationContactResponse getNotificationContactResponse = (GetNotificationContactResponse) response;
			
			getNotificationContactResponse.getTokens().stream().forEach(new Consumer<DeviceTokenMessage>() {
	
				@Override
				public void accept(DeviceTokenMessage deviceToken) {
					LOGGER.info("deviceToken.getLastActivity():" + deviceToken.getLastActivity().toLocaleString());
					Long aging = (new Date().getTime() - deviceToken.getLastActivity().getTime()) / 1000 / 60 / 60 / 24;
					LOGGER.info("Para el contacto:" + request.getEmail() + ", aging:" + aging + ", dif:"
							+ (new Date().getTime() - deviceToken.getLastActivity().getTime()) + ", time:"
							+ deviceToken.getLastActivity().getTime() + ", actual:" + new Date().getTime());
					if (aging < 60) {
						sendPushNotification(request, deviceToken);
					} else {
						callFunction(DeleteNotificationTokenFunction.class, new DeleteNotificationTokenRequest(request.getEmail(), deviceToken.getDevice()), request);
					}
				}
			});
			LOGGER.info("Finalizando SendNotification");
			return new Response();
		}
		LOGGER.info("NotFoundException");
		throw new NotFoundException(new Error(), mapper);
	}

	private void sendPushNotification(SendNotificationRequest request, DeviceTokenMessage deviceToken) {
		// See documentation on defining a message payload.
		LOGGER.info("Sending message to token:" + deviceToken.getToken() + ", message:" + request.getMessage());
		Message message = Message.builder()
				.putData("title", request.getMessage())
				.putData("body", request.getMessage())
				.setToken(deviceToken.getToken())
				.build();

		LOGGER.info("Provider usado:" + provider);
		try {
			// Send a message to the device corresponding to the provided
			// registration token.
			LOGGER.info("sendPushNotification message:" + message);
			String messageId = provider.send(message);
			deviceToken.updateLastActivity();

			LOGGER.info("Message sended to token:" + deviceToken.getToken() + " , contacto:" + request.getEmail()
			+ " , messageId:" + messageId);
			
			// Si el mensaje se envio con exito, entonces se actualiza la fecha de ultima actividad del token
			SaveNotificationTokenRequest saveNotificationTokenRequest = new SaveNotificationTokenRequest();
			saveNotificationTokenRequest.setDevice(deviceToken.getDevice());
			saveNotificationTokenRequest.setEmail(request.getEmail());
			saveNotificationTokenRequest.setToken(deviceToken.getToken());
			
			callFunction(SaveNotificationTokenFunction.class, saveNotificationTokenRequest, request);
			
		} catch (FirebaseMessagingException e) {
			LOGGER.info("Ocurrio un error al intentar enviar notificacion para contacto:" + request.getEmail()
					+ ", device:" + deviceToken.getDevice());
			LOGGER.error(FunctionException.toString(e));
		}

	}

}
