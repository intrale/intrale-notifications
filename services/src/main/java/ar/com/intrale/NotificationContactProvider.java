package ar.com.intrale;

import javax.inject.Singleton;

import ar.com.intrale.models.NotificationContact;
import ar.com.intrale.persistence.AmazonDynamoDBProvider;
import io.micronaut.context.annotation.Requires;

@Singleton
@Requires(property = IntraleFactory.PROVIDER, value = IntraleFactory.TRUE, defaultValue = IntraleFactory.TRUE)
public class NotificationContactProvider extends AmazonDynamoDBProvider<NotificationContact> {

	@Override
	public String getTableName() {
		return NotificationContact.TABLE_NAME;
	}

}
