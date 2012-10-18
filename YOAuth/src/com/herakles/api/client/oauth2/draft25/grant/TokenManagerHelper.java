package com.herakles.api.client.oauth2.draft25.grant;

import java.util.Map;

import com.herakles.api.client.oauth2.draft25.OAuthServerResponse;

public final class TokenManagerHelper {
	public static void put(OAuthServerResponse value) {
		TokenManager.KeyStore.put(value);
	}
	public static OAuthServerResponse get() {
		return TokenManager.KeyStore.get();
	}
	public static OAuthServerResponse remove(String key){
		return TokenManager.KeyStore.remove(key);
	}
	public static Map<String, OAuthServerResponse> getAllTokens() {
		return TokenManager.KeyStore.getAllTokens();
	}
}
