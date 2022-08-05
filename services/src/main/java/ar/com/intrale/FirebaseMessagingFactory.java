package ar.com.intrale;

import java.io.InputStream;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import ar.com.intrale.exceptions.FunctionException;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;

@Factory
@Requires(property = IntraleFactory.FIREBASE_MESSAGING, value = IntraleFactory.TRUE, defaultValue = IntraleFactory.TRUE)
public class FirebaseMessagingFactory extends 
			IntraleFactory<FirebaseMessaging>
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseMessagingFactory.class);
	
	@Singleton
	@Override
	public FirebaseMessaging provider() {
		try {
			LOGGER.info("Instanciando FirebaseMessaging desde FirebaseMessagingFactory:" + config.getInstantiate().getProvider());
			if (FirebaseApp.getApps().isEmpty()) {
				InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-credentials.json");
				FirebaseOptions options = new FirebaseOptions.Builder()
				  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
				  .build();
				
				FirebaseApp.initializeApp(options);
			}
			
	        return FirebaseMessaging.getInstance();
		} catch (Exception e) {
			LOGGER.error(FunctionException.toString(e));
		}
		return null;
	}
}
