package ar.com.intrale.client;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;

@Singleton
public class NotificationsClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsClient.class);
	
	@Client("https://mgnr0htbvd.execute-api.us-east-2.amazonaws.com")
	@Inject
	private HttpClient httpClient;
	
   	@Inject
   	protected ObjectMapper mapper;
	
	/*public GetNotificationsResponse get(String businessname, GetNotificationsRequest getRequest) throws ClientResponseException, JsonMappingException, JsonProcessingException {
		
		HttpRequest<GetNotificationsRequest> request = HttpRequest.POST("/dev/products", getRequest)
				.header(ACCEPT, "application/json")
				.header(USER_AGENT, "Micronaut HTTP Client")
				.header(FunctionBuilder.HEADER_FUNCTION, FunctionConst.READ)
				.header(FunctionBuilder.HEADER_BUSINESS_NAME, businessname);

		try {
			HttpResponse<String> response = httpClient.toBlocking().exchange(request, String.class);
			GetNotificationsResponse getResponse = mapper.readValue(response.body(), GetNotificationsResponse.class);
			return getResponse;
		} catch (HttpClientResponseException e) {
			LOGGER.error("statusCode:" + e.getStatus().getCode());
			LOGGER.error("reason:" + e.getStatus().getReason());
			LOGGER.error("message:" + e.getMessage());
			LOGGER.error("response:" + e.getResponse());
			LOGGER.error("body:" + e.getResponse().getBody());
			throw new ClientResponseException(e.getResponse().getBody(String.class).get());
		} finally {
			httpClient.refresh();
		}
	}*/
	
	
}
