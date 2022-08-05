package ar.com.intrale.models;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class DeviceToken {

	private String device;
	private String token;
	private Date lastActivity;
	
	public DeviceToken() {}
	
	public DeviceToken(String device) {
		this.device = device;
	}
	
	public Date getLastActivity() {
		return lastActivity;
	}
	public void setLastActivity(Date lastActivity) {
		this.lastActivity = lastActivity;
	}
	
	public void updateLastActivity() {
		this.lastActivity = new Date();
	}
	
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public int hashCode() {
		return getDevice().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj==null) || (!(obj instanceof DeviceToken))){
			return Boolean.FALSE;
		}
		if (obj==this) return Boolean.TRUE;
		DeviceToken token = (DeviceToken) obj;
		return token.getDevice().equals(getDevice());
	}
	
}
