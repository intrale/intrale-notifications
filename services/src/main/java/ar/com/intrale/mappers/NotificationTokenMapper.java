package ar.com.intrale.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ar.com.intrale.messages.GetNotificationContactResponse;
import ar.com.intrale.messages.SaveNotificationTokenRequest;
import ar.com.intrale.models.NotificationContact;

@Mapper
public interface NotificationTokenMapper {
	
	NotificationTokenMapper INSTANCE = Mappers.getMapper(NotificationTokenMapper.class);
	
	NotificationContact saveNotificationTokenRequestToNotificationToken(SaveNotificationTokenRequest request);
	
	GetNotificationContactResponse notificationContactToGetResponse(NotificationContact notificationContact);

}
