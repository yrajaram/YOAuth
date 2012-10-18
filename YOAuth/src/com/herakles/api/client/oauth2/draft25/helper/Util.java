package com.herakles.api.client.oauth2.draft25.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.herakles.api.client.oauth2.draft25.OAuthConstants;
import com.herakles.api.client.oauth2.draft25.OAuthProperties;


public class Util {
	private static final Logger log = LogManager.getLogger(Util.class);

	public static DefaultHttpClient getTestHttpClient() {
		try {
			SSLSocketFactory sf = new SSLSocketFactory(new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain,
						String authType) {
					return true;
				}
			});
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("https", 443, sf));
			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					registry);
			return new DefaultHttpClient(ccm);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
	
	public static Map<String, String> getServerResponse(List<BasicNameValuePair> nameValuePairs) {
		DefaultHttpClient httpclient = Util.getTestHttpClient();
		Map<String, String> authServerResponse = new HashMap<String, String>();
		try {
			/*
			 * Currently Mashery is available to all
			 * 
		    httpclient.getCredentialsProvider().setCredentials(
		            new AuthScope("localhost", 443),
		            new UsernamePasswordCredentials("username", "password"));
			 */
//			HttpPost httpPost = new HttpPost(OAuthConstants.TokenURL);
			HttpPost httpPost = new HttpPost(OAuthProperties.getTokenURL());
			
			httpPost.addHeader(OAuthConstants.ContentTypeKey, OAuthConstants.ContentTypeFormUrlEncodedValue);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		    log.debug("Calling server to generate access token " + httpPost.getRequestLine());
		    HttpResponse response = httpclient.execute(httpPost);
		    HttpEntity entity = response.getEntity();

		    log.debug("----------------------------------------");
		    log.debug(response.getStatusLine());
		    if (entity != null) {
		        log.debug("Response content length: " + entity.getContentLength());
		        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Charset.forName("UTF-8")));

				String line = "";
				while ((line = rd.readLine()) != null) {
					log.debug(line);
					String[] pairs = line.split(",");
					for (String eachLine : pairs) {
						String[] nameVal = eachLine.split(":");
						
						String key = nameVal[0].replaceAll("\"","");
						String val = nameVal[1].replaceAll("\"","");
						 val = val.replaceAll("}","");
						 key = key.contains("{")?key.substring(1):key;

						authServerResponse.put(key, val);
					}
				}
		    }
		    EntityUtils.consume(entity);
		    
//	        log.debug("Here is what I received:"+authServerResponse);

		} catch (Exception e) {
			log.error("Unexpected error", e);
		} finally {
		    // When HttpClient instance is no longer needed,
		    // shut down the connection manager to ensure
		    // immediate deallocation of all system resources
		    httpclient.getConnectionManager().shutdown();
		}
		return authServerResponse;
	}

}
