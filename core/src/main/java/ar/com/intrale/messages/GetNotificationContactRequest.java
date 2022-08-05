package ar.com.intrale.messages;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class GetNotificationContactRequest extends RequestRoot {

    @NonNull
    @NotBlank
    @Email
	private String email;
    
    public GetNotificationContactRequest() {}
    
    public GetNotificationContactRequest(String email) {
    	this.email = email;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
}
