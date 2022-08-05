package ar.com.intrale;

import javax.inject.Singleton;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.firebase.messaging.FirebaseMessaging;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;

@Factory
@Requires(property = IntraleFactory.FIREBASE_MESSAGING, value = IntraleFactory.FALSE, defaultValue = IntraleFactory.TRUE)
public class FirebaseMessagingMockFactory extends IntraleFactory<FirebaseMessaging> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseMessagingFactory.class);

	@Singleton
	@Override
	public FirebaseMessaging provider() {
		return Mockito.mock(FirebaseMessaging.class);
	}

}
