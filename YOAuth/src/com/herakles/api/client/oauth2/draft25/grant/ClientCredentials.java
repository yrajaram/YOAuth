package com.herakles.api.client.oauth2.draft25.grant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.herakles.api.client.oauth2.draft25.OAuthConstants;
import com.herakles.api.client.oauth2.draft25.helper.Util;

/**
 * @author yrajaram
 *
 */
public class ClientCredentials extends GrantType {
	@Override
	public Map<String, String> refreshToken(String refreshToken) throws Exception {
		throw new Exception("Unsupported Operation 'refresh token' with Client Credentials Grant Type");
	}

	@Override
	public Map<String, String> getAccessToken() {
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair(OAuthConstants.ClientIdKey, TokenManager.KeyStore.getClientCred().getClientId()));
		nameValuePairs.add(new BasicNameValuePair(OAuthConstants.ClientSecretKey, TokenManager.KeyStore.getClientCred().getClientSecret()));
		nameValuePairs.add(new BasicNameValuePair(OAuthConstants.GrantTypeKey, OAuthConstants.ClientCredGrantTypeValue));
		
		return Util.getServerResponse(nameValuePairs);
	}
}
