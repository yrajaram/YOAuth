/*
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2012. All rights reserved.
 *
 *  Licensed under the ??? License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may not re-distribute or reverse engineer or copy or alter this file.
 * 
 *       http://www.???.com
 * 
 *  This software is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.herakles.api.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.herakles.api.client.oauth2.draft25.OAuthProperties;
import com.herakles.api.client.oauth2.draft25.OAuthServerResponse;
import com.herakles.api.client.oauth2.draft25.grant.AuthorizationCode;
import com.herakles.api.client.oauth2.draft25.grant.ClientCredentials;
import com.herakles.api.client.oauth2.draft25.grant.GrantType;
import com.herakles.api.client.oauth2.draft25.grant.Implicit;
import com.herakles.api.client.oauth2.draft25.grant.ResourceOwnerCredentials;
import com.herakles.api.client.oauth2.draft25.grant.TokenManager;
import com.herakles.api.client.oauth2.draft25.helper.CredentialHelper;
import com.herakles.api.client.oauth2.draft25.helper.Util;



/**
 * @author yrajaram
 *
 */
public class ClientHelper {
	public static final String IMPLICIT_GT = "Implicit";
	public static final String AUTH_CODE_GT = "AuthCode";
	public static final String RESORCE_OWNER_GT = "ResorceOwner";
	public static final String CLIENT_CRED_GT = "ClientCred";
	private static final ResourceBundle PROPS = ResourceBundle.getBundle("yoauth");
	private String ClientIdValue =	PROPS.getString("client.id");
	private String ClientSecretValue = PROPS.getString("client.secret");
	private String UserIdValue = PROPS.getString("resource.owner.username");
	private String UserPasswordValue = PROPS.getString("resource.owner.username");
	private static String AppURL= PROPS.getString("api.url");
	private static String Response = "";
	private String GType = CLIENT_CRED_GT;
	private boolean submtted = false;
	
	public static String AuthURL = PROPS.getString("authorization.url");
	public static String TokenURL = PROPS.getString("token.url");
	
	private static final String	AcceptHeaderKey="Accept",
			AcceptHeaderValue="application/json",
			AuthorizationKey="Authorization";
	private static final String MasheryDebugKey="X-Mashery-Debug",
			MasheryDebugValue="Debug:f73850fa0368cb25f42448031b27bde10b94ef6705c7ed83e90ef204f87330d4";
	private String RedirectURL = OAuthProperties.getRedirectURL();		
	
	public ClientHelper() {
		init();
	}
	
	// 1. Initialize the Token manager by setting all required properties in 
	// oauth2lite.properties and placing in classpath. Or do the steps below so these values can be changed as in this JSP.
	public void init() {
		// Reinitialize TokenManager (also clean the cache which is not done here in this example)
		CredentialHelper cred = new CredentialHelper(ClientIdValue, ClientSecretValue, UserIdValue, UserPasswordValue);
		TokenManager.setClientCred(cred);
		TokenManager.setResourceCred(cred);
	}
	
	// 3. Then invoke the only API exposed in Oauth2.0 Lite to get access token for the set Grant Type
	private OAuthServerResponse getAccessToken() throws Exception{
		OAuthServerResponse tokenResponse = null;
		if (TokenManager.getCurrentGrantType() != null) {
			// ------------Get Access Token based on Grant Type
			tokenResponse = TokenManager.getAccessToken();
		}
		return tokenResponse;
	}
	
	private void getAccessTokenAndInvoke() throws Exception{
		// ------------Get Access Token based on Grant Type
		OAuthServerResponse serverResponse = getAccessToken();
		
		if (serverResponse!= null) {
			// ------------Invoking Hello API
			invokeTargetService(serverResponse.getToken());
		}
	
	}

	//4. and then use the returned access token invoke the target service (API implementation)
	public static void invokeTargetService(String authCode) throws IOException, ClientProtocolException {
		DefaultHttpClient httpclient = Util.getTestHttpClient();
		try {
			HttpGet httpGet = new HttpGet(AppURL);
			httpGet.addHeader(AcceptHeaderKey, AcceptHeaderValue);
			httpGet.addHeader(AuthorizationKey, authCode);
			httpGet.addHeader(MasheryDebugKey, MasheryDebugValue);
			System.out.println("Calling Hello API with token " + httpGet.getRequestLine());
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();

			System.out.println("----------------------------------------"+authCode);
			System.out.println(response.getStatusLine());
			if (entity != null) {
				System.out.println("Response content length: " + entity.getContentLength());
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				String line = "";
				while ((line = rd.readLine()) != null) {
					System.out.println(line);	  
					Response += line;
				}
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
       	System.err.println("Unexpected error");
       	e.printStackTrace(System.err);
       } finally {
           // When HttpClient instance is no longer needed,
           // shut down the connection manager to ensure
           // immediate deallocation of all system resources
           httpclient.getConnectionManager().shutdown();
       }
	}
	//------------------------------getters & setters
	public String getClientId() {
		return this.ClientIdValue;
	}
	public void setClientId(String clientIdValue) {
		ClientIdValue = clientIdValue;
		init();
	}
	public String getClientSecret() {
		return ClientSecretValue;
	}
	public void setClientSecret(String clientSecretValue) {
		ClientSecretValue = clientSecretValue;
		init();
	}
	public String getUsername() {
		return UserIdValue;
	}
	public void setUsername(String userIdValue) {
		UserIdValue = userIdValue;
		init();
	}
	public String getPassword() {
		return UserPasswordValue;
	}
	public void setPassword(String userPasswordValue) {
		UserPasswordValue = userPasswordValue;
		init();
	}
	public String getAppURL() {
		return AppURL;
	}
	public  void setAppURL(String appURl) {
		AppURL = appURl;
	}
	public String getResponse() { //TODO: can be simplified
		if (isSubmtted()){
			Response="";
			try {
				getAccessTokenAndInvoke();
			} catch (Exception e) {
				e.printStackTrace();
				Response = e.getMessage();
			}
			setSubmtted(false);
		} else {
			try {
				getAccessToken();
			} catch (Exception e) {
				e.printStackTrace();
				Response += e.getMessage();
			}
		}
		return Response;
	}
	public void setResponse(String response) {
		Response = response;
	}
	public String getGrantType() {
		return GType;
	}
	
	// 2. Then make sure the required grant type is set in TokenManager
	public void setGrantType(String gType) {
		System.out.println("Setting GT:"+gType);
		GType = gType;
		GrantType gt = null;
		if (CLIENT_CRED_GT.equalsIgnoreCase(GType)) {
			gt  = new ClientCredentials();
		} else if (RESORCE_OWNER_GT.equalsIgnoreCase(GType)) {
			gt = new ResourceOwnerCredentials();
		} else if (AUTH_CODE_GT.equalsIgnoreCase(GType)) {
			gt = new AuthorizationCode();
		} else if (IMPLICIT_GT.equalsIgnoreCase(GType)) {
			gt = new Implicit();
		}
		TokenManager.setCurrentGrantType(gt);
	}

	public boolean isSubmtted() {
		System.out.println("Returning submtted:"+submtted);
		return submtted;
	}

	public void setSubmtted(boolean val) {
		System.out.println("Setting submtted to:"+val);
		this.submtted = val;
	}

	public String getAuthURL() {
		return ClientHelper.AuthURL;
	}

	public void setAuthURL(String authURL) {
		ClientHelper.AuthURL = authURL;
	}

	public String getTokenURL() {
		return ClientHelper.TokenURL;
	}

	public void setTokenURL(String tokenURL) {
		ClientHelper.TokenURL = tokenURL;
	}
	public String getRedirectURL() {
		return RedirectURL;
	}

	public void setRedirectURL(String url) {
		RedirectURL = url;
	}

}
