package com.herakles.api.client.oauth2.draft25;

import java.util.HashMap;
import java.sql.Timestamp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class OAuthServerResponse extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(OAuthServerResponse.class);
	
	
	private long whenAccessTokenWasGot;

	public OAuthServerResponse() {
		super();
		setTimestamp();
	}
	
	public void setTimestamp() {
		whenAccessTokenWasGot =  java.lang.System.currentTimeMillis();
//		this.put("timestamp", String.valueOf(whenAccessTokenWasGot));
	}
	
	public boolean isAccessTokenValid() {
		if (!this.containsKey(OAuthConstants.ExpiresInKey)) return true; // Assume that key never expires if "expires_in" is not present
		
		long expiresAt = Long.valueOf(this.get(OAuthConstants.ExpiresInKey)).longValue() * 1000 + whenAccessTokenWasGot;
		log.debug("Token was obtained at:"+new Timestamp(whenAccessTokenWasGot));
		log.debug("Token will expire  at:"+new Timestamp(expiresAt));
		return (expiresAt > java.lang.System.currentTimeMillis());
	}

	public String getToken() {
		return this.get(OAuthConstants.TokenTypeKey) + " " + this.get(OAuthConstants.AccessTokenKey);
	}
}
