package com.herakles.api.client.oauth2.draft25.helper;

public class CredentialHelper implements ClientCred, ResourceOwnerCred {
	private String	clientId;
	private String	clientSecret;
	private String	userName;
	private String	password;
	
	public CredentialHelper() {
		super();
	}
	
	public CredentialHelper(String clientId, String clientSecret) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public CredentialHelper(String clientId, String clientSecret, String userName, String password) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.userName = userName;
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see com.herakles.api.client.oauth2.helper.ClientCredentials#getClientId()
	 */
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/* (non-Javadoc)
	 * @see com.herakles.api.client.oauth2.helper.ClientCredentials#getClientSecret()
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	/* (non-Javadoc)
	 * @see com.herakles.api.client.oauth2.helper.ResourceOwnerCredentials#getUserName()
	 */
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/* (non-Javadoc)
	 * @see com.herakles.api.client.oauth2.helper.ResourceOwnerCredentials#getPassword()
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
