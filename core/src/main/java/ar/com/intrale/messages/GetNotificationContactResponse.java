package ar.com.intrale.messages;

import java.util.HashSet;
import java.util.Set;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class GetNotificationContactResponse extends Response{
	
	private String email;
	
	private Set<DeviceTokenMessage> tokens = new HashSet<DeviceTokenMessage>();
	
	public Set<DeviceTokenMessage> getTokens() {
		return tokens;
	}
	public void setTokens(Set<DeviceTokenMessage> tokens) {
		this.tokens = tokens;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
