package ar.com.intrale.models;

import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import ar.com.intrale.persistence.Entity;

@DynamoDBTable(tableName=NotificationContact.TABLE_NAME)
public class NotificationContact implements Entity{
	
	public static final String TABLE_NAME = "notificationContact";
	@DynamoDBHashKey
	private String email;
	@DynamoDBHashKey
	private String businessName;
	
	@DynamoDBTypeConverted(converter = DeviceTokenToJsonConverter.class)
	private Set<DeviceToken> tokens = new HashSet<DeviceToken>();
	
	public Set<DeviceToken> getTokens() {
		return tokens;
	}
	public void setTokens(Set<DeviceToken> tokens) {
		this.tokens = tokens;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	/*@Override
	public boolean equals(Object obj) {
		if ((obj==null) || (!(obj instanceof Token))){
			return Boolean.FALSE;
		}
		if (obj==this) return Boolean.TRUE;
		Token token = (Token) obj;
		return token.getDevice().equals(getDevice());
	}*/

}
