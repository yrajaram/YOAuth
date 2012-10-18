package com.herakles.api.client;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.herakles.api.client.oauth2.draft25.OAuthServerResponse;
import com.herakles.api.client.oauth2.draft25.grant.ClientCredentials;
import com.herakles.api.client.oauth2.draft25.grant.GrantType;
import com.herakles.api.client.oauth2.draft25.grant.ResourceOwnerCredentials;
import com.herakles.api.client.oauth2.draft25.grant.TokenManager;

public class Main {

	public static void main(String[] args) {

		// 1. Initialize the Token manager by setting all required properties in
		// oauth2lite.properties and placing in classpath.

		
		// 2. Then make sure the required grant type is set in TokenManager
		GrantType gt = new ClientCredentials();
//		gt = new ResourceOwnerCredentials();
		TokenManager.setCurrentGrantType(gt);

		// 3. Then invoke the only API exposed in Oauth2.0 Lite to get access token for the set Grant Type
		OAuthServerResponse tokenResponse = null;
		if (TokenManager.getCurrentGrantType() != null) {
			// ------------Get Access Token based on Grant Type
			tokenResponse = TokenManager.getAccessToken();

			System.out.println("Token from the cache " + tokenResponse.getToken()	+ " isValid?  = " + tokenResponse.isAccessTokenValid() );

			//4. and then use the returned access token invoke the target service (API implementation)

			// ------------Invoke your API---
			try {
				ClientHelper.invokeTargetService(tokenResponse.getToken());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			 System.err.println("Access Token is null...Pls check the Oauth2 configuration...");
		}

		System.out.println("This is a standalone application, so TokenManager cache will become empty once application terminates.");
	}
}
