package com.herakles.api.client.oauth2.draft25;

import java.util.concurrent.Exchanger;

public interface OAuthConstants {

	String ContentTypeKey="Content-Type";
	String ContentTypeFormUrlEncodedValue="application/x-www-form-urlencoded";
	
	String ClientIdKey = "client_id";
	String ClientSecretKey = "client_secret";
	String ResourceOwnerIdKey = "username";
	String ResourceOwnerSecretKey = "password";
	
	String GrantTypeKey = "grant_type";
	String ClientCredGrantTypeValue = "client_credentials";
	String ResourceOwnerGrantTypeValue = "password";
	String RefreshTokenGrantTypeValue = "refresh_token";
	String AuthorizeResponseTypeValue = "code";
	String AuthorizationCodeGrantTypeValue = "authorization_code";
	String ImplicitGrantTypeValue = "token";
	
	String AuthCodeCallbackState = "authCodeCallback";
	String AccessTokenCallbackState = "accessTokenCallback";
	
	String ScopeKey = "scope";
	String ScopeValue = "Read Write";
	
	String ExpiresInKey = "expires_in";
	String RedirectUriKey = "redirect_uri";
	String AccessTokenKey = "access_token";
	String TokenTypeKey ="token_type";
	
	Exchanger<String> StringDataExchanger = new Exchanger<String>();
	String OAuthPropertiesFileName = "yoauth";
}
