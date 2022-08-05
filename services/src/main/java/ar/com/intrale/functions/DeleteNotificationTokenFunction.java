package ar.com.intrale.functions;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.intrale.BaseFunction;
import ar.com.intrale.FunctionConst;
import ar.com.intrale.FunctionResponseToBase64HttpResponseBuilder;
import ar.com.intrale.NotificationContactProvider;
import ar.com.intrale.builders.StringToDeleteNotificationTokenRequestBuilder;
import ar.com.intrale.exceptions.FunctionException;
import ar.com.intrale.exceptions.NotFoundException;
import ar.com.intrale.messages.DeleteNotificationTokenRequest;
import ar.com.intrale.messages.Error;
import ar.com.intrale.messages.Response;
import ar.com.intrale.models.DeviceToken;
import ar.com.intrale.models.NotificationContact;
import ar.com.intrale.persistence.PersistenceFilters;

@Singleton
@Named(DeleteNotificationTokenFunction.FUNCTION_NAME)
public class DeleteNotificationTokenFunction
		extends BaseFunction<DeleteNotificationTokenRequest, Response, NotificationContactProvider, StringToDeleteNotificationTokenRequestBuilder, FunctionResponseToBase64HttpResponseBuilder> {

	public static final String FUNCTION_NAME = "deleteToken";
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteNotificationTokenFunction.class);
	
	@Override
	public Response execute(DeleteNotificationTokenRequest request) throws FunctionException {
		LOGGER.info("Ejecutando GetNotificationContactFunction");
		
		NotificationContact notificationContact = provider
				.get(new PersistenceFilters(FunctionConst.BUSINESS_NAME, request.getBusinessName()).addFilter("email",
						request.getEmail()));
		LOGGER.info("Finalizo busqueda en provider");
		if (notificationContact != null) {
			notificationContact.getTokens().remove(new DeviceToken(request.getDevice()));
			provider.save(notificationContact);
			return new Response();
		}
		LOGGER.info("NotFoundException");
		throw new NotFoundException(new Error(), mapper);
	}

}
