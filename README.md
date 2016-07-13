#YOAuth

The best and simplest OAuth2.0 client library

This particular implementation is targetted for Java/JSP based projects. Token fetching has been abstracted to be totally transparent for developers. All grant types are supported. 

##How to use this?
* The entire implementation is available under YOAuth folder under GIT home; so copy and use that folder as a starter JSP based project and deploy it on your Application Container, and then access the index.html page. That would fetch the token using the particular grant type you have set in index.html.
* If however you want to use this in a Java Application, just take the Main.java (under src/com/herakles/api/client folder) and run it - modify it to use the grant type you want. 

##APIs available in OYAuth
Invoking the following will always fetch a valid access token based on the appropriate Grant Type:
```
TokenManager.KeyStore.getAccessToken();
```
After invoking this API the calling application can use the access token returned to invoke the target service (your API implementation).  

###This section shows how a calling application can invoke the target service using YOAuth.
* Initialize the Token manager by setting all required URLs and properties in oauth2lite.properties and place it in classpath.  (See next section for info on the contents of `oauth2lite.properties`)
* Then make sure the required grant type is set by using values defined for GrantTypes:
```
TokenManager.KeyStore.setGrantType(grantType);
```
* Then invoke the only API exposed in OAuth to get access token and then use the returned access token invoke the target service (API implementation)
```
// ------------Get Access Token based on Grant Type
String accessToken = TokenManager.KeyStore.getAccessToken();
if (accessToken!= null) {
  // ------------Invoke your API
            yourImpementationToInvokeTargetService(accessToken);
}
```
##How to set the authorization and token URLs:
To initialize the TokenManager place a properties file in your classpath. It should be named oauth2lite.properties  and should have the following properties initialized in it - the values provided below are for illustration onl, you need to configure with correct values that would work for your needs.
```
// provide only one grant type here or set programatically
grant.type=Implicit/AuthCode/ResourceOwner/ClientCred
client.id=your-client-id
client.secret=your-client-secret
resource.owner.username=your-ro-username
resource.owner.password=your-ro-password
authorization.url=https://localhost:7777/authorization.oauth2
token.url=https://localhost:7778/token.oauth2
redirect.url= http://127.0.0.1
```

##Handling the Redirect URI:
The YOAoth framework comes bundled with a JSP page (`callback.jsp`) to handle the redirect URI.  Please make sure to configure this to match with your client’s redirect URI. 
For example if for your client if your redirect uri is http://localhost:8080/OAuth/callback.jsp then make sure that callback.jsp is placed under the Webcontents of your application (which should be OAuth to match with the redirect uri in this case).


Try it out and let me know if you need any improvements. 
