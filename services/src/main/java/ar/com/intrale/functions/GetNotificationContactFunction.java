package ar.com.intrale.functions;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.intrale.BaseFunction;
import ar.com.intrale.FunctionConst;
import ar.com.intrale.FunctionResponseToBase64HttpResponseBuilder;
import ar.com.intrale.NotificationContactProvider;
import ar.com.intrale.builders.StringToGetNotificationContactRequestBuilder;
import ar.com.intrale.exceptions.FunctionException;
import ar.com.intrale.exceptions.NotFoundException;
import ar.com.intrale.mappers.NotificationTokenMapper;
import ar.com.intrale.messages.Error;
import ar.com.intrale.messages.GetNotificationContactRequest;
import ar.com.intrale.messages.GetNotificationContactResponse;
import ar.com.intrale.models.NotificationContact;
import ar.com.intrale.persistence.PersistenceFilters;

@Singleton
@Named(FunctionConst.READ)
public class GetNotificationContactFunction
		extends BaseFunction<GetNotificationContactRequest, GetNotificationContactResponse, NotificationContactProvider, StringToGetNotificationContactRequestBuilder, FunctionResponseToBase64HttpResponseBuilder> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetNotificationContactFunction.class);
	
	@Override
	public GetNotificationContactResponse execute(GetNotificationContactRequest request) throws FunctionException {
		LOGGER.info("Ejecutando GetNotificationContactFunction");
		
		NotificationContact notificationContact = provider
				.get(new PersistenceFilters(FunctionConst.BUSINESS_NAME, request.getBusinessName()).addFilter("email",
						request.getEmail()));
		LOGGER.info("Finalizo busqueda en provider");
		if (notificationContact != null) {
			GetNotificationContactResponse response = NotificationTokenMapper.INSTANCE.notificationContactToGetResponse(notificationContact);
			LOGGER.info("notificationContact:" + notificationContact);
			LOGGER.info("GetNotificationContactResponse:" + response);
			return response;
		}
		LOGGER.info("NotFoundException");
			throw new NotFoundException(new Error(), mapper);
	}

}
