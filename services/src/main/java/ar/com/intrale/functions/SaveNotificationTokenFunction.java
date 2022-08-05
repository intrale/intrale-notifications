package ar.com.intrale.functions;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.intrale.BaseFunction;
import ar.com.intrale.FunctionConst;
import ar.com.intrale.FunctionResponseToBase64HttpResponseBuilder;
import ar.com.intrale.NotificationContactProvider;
import ar.com.intrale.builders.StringToSaveNotificationTokenRequestBuilder;
import ar.com.intrale.exceptions.FunctionException;
import ar.com.intrale.messages.Response;
import ar.com.intrale.messages.SaveNotificationTokenRequest;
import ar.com.intrale.models.DeviceToken;
import ar.com.intrale.models.NotificationContact;
import ar.com.intrale.persistence.PersistenceFilters;

@Singleton
@Named(FunctionConst.SAVE)
public class SaveNotificationTokenFunction extends BaseFunction<SaveNotificationTokenRequest, Response, NotificationContactProvider, StringToSaveNotificationTokenRequestBuilder, FunctionResponseToBase64HttpResponseBuilder> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SaveNotificationTokenFunction.class);

	@Override
	public Response execute(SaveNotificationTokenRequest request) throws FunctionException {
		LOGGER.info("Inicializando SaveNotificationToken");
		
		Response response = new Response();
		
		NotificationContact contact = getContact(request);
		DeviceToken deviceToken = new DeviceToken();
		deviceToken.setDevice(request.getDevice());
		deviceToken.setToken(request.getToken());
		deviceToken.updateLastActivity();
		contact.getTokens().add(deviceToken);
		
		LOGGER.info("Almacenando contacto:" + contact.getEmail());
		
		provider.save(contact);

	   LOGGER.info("Finalizando SaveNotificationToken");
	   
       return response;
	}

	private NotificationContact getContact(SaveNotificationTokenRequest request) {
		LOGGER.info("Buscando contacto:" + request.getEmail() + ", businessName:" + request.getBusinessName());
		
		NotificationContact notificationContact = provider.get(new PersistenceFilters("businessName", request.getBusinessName()).addFilter("email", request.getEmail()));

		if (notificationContact!=null) {
			return notificationContact;
		}

		NotificationContact contact = new NotificationContact();
		contact.setEmail(request.getEmail());
		contact.setBusinessName(request.getBusinessName());
		return contact;
	}
	
}
