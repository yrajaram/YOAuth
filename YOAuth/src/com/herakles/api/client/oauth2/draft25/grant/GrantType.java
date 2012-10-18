package com.herakles.api.client.oauth2.draft25.grant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.herakles.api.client.oauth2.draft25.OAuthConstants;
import com.herakles.api.client.oauth2.draft25.helper.Util;

/**
 * @author yrajaram
 *
 */
public abstract class GrantType {
	private static final Logger log = LogManager.getLogger(GrantType.class);

	public final String whoami() {
		String[] tmp = this.getClass().getName().split("\\.");
		log.debug("whoami to return "+tmp[tmp.length-1]);
		return tmp[tmp.length-1];
	}
	
    public abstract  Map<String, String> getAccessToken ();
    
    public  Map<String, String> refreshToken (String refreshToken) throws Exception {		
    	List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(1);
    	nameValuePairs.add(new BasicNameValuePair(OAuthConstants.ClientIdKey, TokenManager.KeyStore.getClientCred().getClientId()));
    	nameValuePairs.add(new BasicNameValuePair(OAuthConstants.ClientSecretKey, TokenManager.KeyStore.getClientCred().getClientSecret()));
    	nameValuePairs.add(new BasicNameValuePair(OAuthConstants.GrantTypeKey, OAuthConstants.RefreshTokenGrantTypeValue));
    	nameValuePairs.add(new BasicNameValuePair(OAuthConstants.RefreshTokenGrantTypeValue, refreshToken));
    	return Util.getServerResponse(nameValuePairs);
    }
}
