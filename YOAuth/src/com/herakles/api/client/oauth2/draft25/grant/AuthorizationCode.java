package com.herakles.api.client.oauth2.draft25.grant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.herakles.api.client.oauth2.draft25.OAuthConstants;
import com.herakles.api.client.oauth2.draft25.OAuthProperties;
import com.herakles.api.client.oauth2.draft25.OAuthServerResponse;
import com.herakles.api.client.oauth2.draft25.helper.Util;

/**
 * @author yrajaram
 *
 */
public class AuthorizationCode extends GrantType {
	private static final Logger log = LogManager.getLogger(AuthorizationCode.class);

	@Override
	public Map<String, String> refreshToken(String refreshToken) throws Exception {
		return super.refreshToken(refreshToken);
	}

	@Override
	public Map<String, String> getAccessToken() {
		if ((TokenManager.KeyStore.get()!=null) && (!TokenManager.KeyStore.get().containsKey(OAuthConstants.AccessTokenKey))) {
			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair(OAuthConstants.ClientIdKey, TokenManager.KeyStore.getClientCred().getClientId()));
			nameValuePairs.add(new BasicNameValuePair(OAuthConstants.ClientSecretKey, TokenManager.KeyStore.getClientCred().getClientSecret()));
			nameValuePairs.add(new BasicNameValuePair(OAuthConstants.GrantTypeKey, OAuthConstants.AuthorizationCodeGrantTypeValue));
			nameValuePairs.add(new BasicNameValuePair(OAuthConstants.RedirectUriKey, OAuthProperties.getRedirectURL()));
//			nameValuePairs.add(new BasicNameValuePair(OAuthConstants.RedirectUriKey, OAuthProperties.BaseCallbackURL+OAuthProperties.AuthCodeCallbackSuffix));
			nameValuePairs.add(new BasicNameValuePair(OAuthConstants.AuthorizeResponseTypeValue, TokenManager.KeyStore.get().get(OAuthConstants.AuthorizeResponseTypeValue)));
			
			OAuthServerResponse osr = new OAuthServerResponse(); // Just to make sure that timestamp is older than call invocation
			Map<String, String> ret = Util.getServerResponse(nameValuePairs);
			
			osr.putAll(ret);
			osr.putAll(TokenManager.KeyStore.get());
			TokenManager.KeyStore.remove();
			TokenManager.KeyStore.put(osr);
			log.debug("Saving in cache :"+osr);
		}
		return TokenManager.KeyStore.get();
	}

}
