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
package com.herakles.api.client.oauth2.draft25.grant;


import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.herakles.api.client.oauth2.draft25.OAuthConstants;
import com.herakles.api.client.oauth2.draft25.OAuthProperties;
import com.herakles.api.client.oauth2.draft25.OAuthServerResponse;
import com.herakles.api.client.oauth2.draft25.adapter.AccessCodeCallbackHandler;
import com.herakles.api.client.oauth2.draft25.adapter.AuthCodeCallbackHandler;
import com.herakles.api.client.oauth2.draft25.helper.ClientCred;
import com.herakles.api.client.oauth2.draft25.helper.CredentialHelper;
import com.herakles.api.client.oauth2.draft25.helper.ResourceOwnerCred;

/**
 * @author yrajaram
 *
 */
public final class TokenManager {
	static final TokenManager KeyStore = new TokenManager(); 
	private static final Map<String, OAuthServerResponse> TokenCache = new ConcurrentHashMap<String, OAuthServerResponse>();
	
	private static final Logger log = LogManager.getLogger(TokenManager.class);


	static {
		CredentialHelper cred = new CredentialHelper(OAuthProperties.getClientId(), OAuthProperties.getClientSecret(), OAuthProperties.getUserId(), OAuthProperties.getUserPassword());
		TokenManager.setClientCred(cred);
		TokenManager.setResourceCred(cred);
	}
	
	// NOTE: Private constructor
	private TokenManager() {
		super();
	}
	
	private ClientCred			clientCred;
	private ResourceOwnerCred	resourceCred;
	private AuthCodeCallbackHandler authCodeAdapter;
	private AccessCodeCallbackHandler accessCodeAdapter;
	private GrantType	grantType;
	
	private String getTokenUniqueId() {
		return clientCred.getClientId()+"("+grantType.whoami()+")";
	}
	public void put(OAuthServerResponse value) {
		TokenCache.put(getTokenUniqueId(), value);
	}
	public OAuthServerResponse get() {
		if (grantType == null) return null;
		return TokenCache.get(getTokenUniqueId());
	}
	public void remove() {
		if (grantType!=null) TokenCache.remove(getTokenUniqueId());
	}
	public OAuthServerResponse remove(String key) {
		return TokenCache.remove(key);
	}
	
	public Map<String, OAuthServerResponse> getAllTokens() {
		return Collections.unmodifiableMap(TokenCache);
	}
	
	public ClientCred getClientCred() {
		return clientCred;
	}
	public void setClientCred0(ClientCred clientCred) {
		this.clientCred = clientCred;
	}
	// To expose as API
	public static void setClientCred(ClientCred clientCred) {
		KeyStore.setClientCred0(clientCred);
	}
	public ResourceOwnerCred getResourceCred() {
		return resourceCred;
	}
	public void setResourceCred0(ResourceOwnerCred resourceCred) {
		this.resourceCred = resourceCred;
	}
	// To expose as API
	public static void setResourceCred(ResourceOwnerCred resourceCred) {
		KeyStore.setResourceCred0(resourceCred);
	}
	public void setAuthCodeHandler(AuthCodeCallbackHandler adapter) {
		this.authCodeAdapter = adapter;
	}
	public void setAccessCodeHandler(AccessCodeCallbackHandler adapter) {
		this.accessCodeAdapter = adapter;
	}
	public AuthCodeCallbackHandler getAuthCodeAdapter() {
		return authCodeAdapter;
	}
	public AccessCodeCallbackHandler getAccessCodeAdapter() {
		return accessCodeAdapter;
	}
	public GrantType getGrantType() {
		return grantType;
	}
	public void setGrantType(GrantType gt) {
		this.grantType = gt;
	}
	
	//To help expose as API
	public static GrantType getCurrentGrantType() {
		return KeyStore.getGrantType();
	}
	//To help expose as API
	public static void setCurrentGrantType(GrantType gt) {
		KeyStore.setGrantType(gt);
	}
	//To help expose as API
	public static OAuthServerResponse getAccessToken() {
		return KeyStore.getAccessToken0();
	}

	private OAuthServerResponse getAccessToken0() {
		//FIXME: have to handle not authorized condition
		OAuthServerResponse ret = get();
		if (ret!=null && !ret.isAccessTokenValid()) {
			log.debug("Token is in cache but is expired so refresh the token");
			// token is in cache but is expired so refresh the token
			try {
				OAuthServerResponse tmp = new OAuthServerResponse();
				Map<String, String> serverResponse = getGrantType().refreshToken(ret.get(OAuthConstants.RefreshTokenGrantTypeValue));
				remove();
				tmp.putAll(serverResponse);
				ret = tmp;
				put(ret);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				remove();
				ret=null; // force creation of new token
			}
		} else if ((ret!=null) && (ret.get(OAuthConstants.AuthorizeResponseTypeValue) != null)) {
			//Only auth code is in cache so use it to get token
			getGrantType().getAccessToken(); //FXME: hmmm type mismatch fix me later
			ret=get();
		}
		if (ret==null || ret.isEmpty()) {
			log.debug("Creating token");
			ret = new OAuthServerResponse();
			Map<String, String> serverResponse = getGrantType().getAccessToken();
			if (serverResponse!=null) {
				ret.putAll(serverResponse);
				put(ret);
			}
		}
		log.debug("Returning token:"+ret);
		return ret;
	}	
}
