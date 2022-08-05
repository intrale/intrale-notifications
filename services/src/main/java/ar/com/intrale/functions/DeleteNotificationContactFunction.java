package ar.com.intrale.functions;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.intrale.BaseFunction;
import ar.com.intrale.FunctionConst;
import ar.com.intrale.FunctionResponseToBase64HttpResponseBuilder;
import ar.com.intrale.NotificationContactProvider;
import ar.com.intrale.builders.StringToDeleteNotificationContactRequestBuilder;
import ar.com.intrale.exceptions.FunctionException;
import ar.com.intrale.messages.DeleteNotificationContactRequest;
import ar.com.intrale.messages.Response;
import ar.com.intrale.persistence.PersistenceFilters;

@Singleton
@Named(FunctionConst.DELETE)
public class DeleteNotificationContactFunction
		extends BaseFunction<DeleteNotificationContactRequest, Response, NotificationContactProvider, StringToDeleteNotificationContactRequestBuilder, FunctionResponseToBase64HttpResponseBuilder> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteNotificationContactFunction.class);
	
	@Override
	public Response execute(DeleteNotificationContactRequest request) throws FunctionException {
		LOGGER.info("Inicializando DeleteNotificationContactFunction");
		
		Response response = new Response();
		
		provider.delete(new PersistenceFilters("businessName", request.getBusinessName()).addFilter("email", request.getEmail()));
		
		return response;
	}

}
