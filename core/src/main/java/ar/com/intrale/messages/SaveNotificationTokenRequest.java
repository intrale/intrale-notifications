package ar.com.intrale.messages;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class SaveNotificationTokenRequest extends RequestRoot {

    @NonNull
    @NotBlank
    @Email
	private String email;
    
    @NonNull
    @NotBlank
    private String token;

    @NonNull
    @NotBlank
    private String device;

    
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
