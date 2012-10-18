package com.herakles.api.client.oauth2.draft25;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.herakles.api.client.oauth2.draft25.grant.TokenManager;

public class OAuthProperties {
	final static ResourceBundle PROPS = ResourceBundle.getBundle(OAuthConstants.OAuthPropertiesFileName);
	private static final Logger log = LogManager.getLogger(TokenManager.class);

	static {
		log.debug("Obtained the following propertes from "+OAuthConstants.OAuthPropertiesFileName);
		for (Enumeration<String> iterator = PROPS.getKeys(); iterator.hasMoreElements();) {
			String tmp= (String) iterator.nextElement();
			System.out.println(tmp+":"+PROPS.getString(tmp));
		}
	}

	private static String clientId = PROPS.getString("client.id");
	private static String clientSecret = PROPS.getString("client.secret");
	private static String userId = PROPS.getString("resource.owner.username");
	private static String userPassword = PROPS.getString("resource.owner.password");
	private static String authURL = PROPS.getString("authorization.url");
	private static String tokenURL = PROPS.getString("token.url");
	private static String redirectURL = PROPS.getString("redirect.url");
	private static String serverMgrURL= PROPS.getString("server.mgr.url");
	
	static public String getClientId() {
		return clientId;
	}
	static public void setClientId(String cid) {
		clientId = cid;
	}
	static public String getClientSecret() {
		return clientSecret;
	}
	static public void setClientSecret(String cs) {
		clientSecret = cs;
	}
	static public String getUserId() {
		return userId;
	}
	static public void setUserId(String uid) {
		userId = uid;
	}
	static public String getUserPassword() {
		return userPassword;
	}
	static public void setUserPassword(String pwd) {
		userPassword = pwd;
	}
	static public String getAuthURL() {
		return authURL;
	}
	static public void setAuthURL(String url) {
		authURL = url;
	}
	static public String getTokenURL() {
		return tokenURL;
	}
	static public void setTokenURL(String url) {
		tokenURL = url;
	}
	static public String getRedirectURL() {
		return redirectURL;
	}
	static public void setRedirectURL(String url) {
		redirectURL = url;
	}
	public static String getServerMgrURL() {
		return serverMgrURL;
	}
	public static void setServerMgrURL(String url) {
		serverMgrURL = url;
	}

}
