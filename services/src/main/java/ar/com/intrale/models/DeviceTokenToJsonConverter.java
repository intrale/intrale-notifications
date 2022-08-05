package ar.com.intrale.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import ar.com.intrale.tools.JsonConverter;

public class DeviceTokenToJsonConverter extends JsonConverter<DeviceToken> implements DynamoDBTypeConverter<String, DeviceToken>{

}
