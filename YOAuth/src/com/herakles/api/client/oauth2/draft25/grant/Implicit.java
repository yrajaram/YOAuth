package com.herakles.api.client.oauth2.draft25.grant;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * @author yrajaram
 *
 */
//http://127.0.0.1/#state=Hmmm&expires_in=3599&token_type=Bearer&access_token=KCv52gdUmw4XDsv6PeQTiJ09p9Rs
public class Implicit extends GrantType {
	private static final Logger log = LogManager.getLogger(Implicit.class);

	@Override
	public Map<String, String> refreshToken(String refreshToken) throws Exception {
		return super.refreshToken(refreshToken);
	}

	@Override
	public Map<String, String> getAccessToken() {
		log.debug(TokenManager.KeyStore.get());
		return TokenManager.KeyStore.get();
	}
}
