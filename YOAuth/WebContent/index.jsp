<%@page import="com.herakles.api.client.oauth2.draft25.helper.ClientCred"%>
<%@page import="com.herakles.api.client.oauth2.draft25.grant.AuthorizationCode"%>
<%@page import="com.herakles.api.client.oauth2.draft25.grant.Implicit"%>
<%@page import="com.herakles.api.client.oauth2.draft25.grant.TokenManager"%>
<%@page import="com.herakles.api.client.oauth2.draft25.grant.TokenManagerHelper"%>
<%@page import="com.herakles.api.client.oauth2.draft25.OAuthConstants"%>
<%@page import="com.herakles.api.client.oauth2.draft25.OAuthServerResponse"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>OAuth2.0 Web Client</title>
<script type="text/javascript">
	function load() {
		var qsval = location.hash;
		var qs = qsval.substring(1);

		if (qs != null && qs != "") {
			var qsSplit = qs.split("&");
			var numSplit = qsSplit.length;
			var x = 0;
			document.write('<h2>Client: Implicit Grant Type</h2><br> <h4>Response:</h4> ');

			while (x < numSplit) {
				document.write(qsSplit[x] + '<br>');
				x = x + 1;
			}
			document.write('<br> <br><h4>Note:</h4>With Implicit Grant Type, Access Token is requested directly without the Authorization Code by the client (Javascript) through user agent(browser).<br>');
			setTimeout(redirect(qs), 5000);
		}
	}

	function redirect(qs) {
		//newURL = window.location.protocol + "//" + window.location.host + "/" + window.location.pathname +  "?" + qs;
		var newURL = window.location.protocol + "//" + window.location.host;
		var pathval = window.location.pathname;
		if ((null != pathval) && ("" != pathval) && ("/" != pathval)) {
			newURL += "/" + pathval;
		}
		newURL += "?" + qs;
		window.location = newURL;
	}
</script>
<link rel="stylesheet" type="text/css" href="light-blue.css" /> 
</head>
<body onload="setTimeout('load()', 1);"> 
<h1>Welcome</h1>
<h3>Here's where the redirect URI terminates</h3>
<%!
String goToLoc="invokeTarget.jsp";
%>
<%
if (request.getParameter("state")==null || "".equals(request.getParameter("state"))) {
	out.println("New Request... try <a href='invokeTarget.jsp'>this</a>");
} else if (request.getParameter("state").equalsIgnoreCase("authCode")) {
	out.println("Auth Code Callback:"+request.getParameter("code"));
	
	TokenManager.setCurrentGrantType(new AuthorizationCode());
	
	OAuthServerResponse resp = new OAuthServerResponse();
	resp.put(OAuthConstants.AuthorizeResponseTypeValue, request.getParameter("code"));
	TokenManagerHelper.put(resp);
	response.setHeader("Refresh", "0;url="+goToLoc);
} else if (request.getParameter("state").equalsIgnoreCase("accessCode")) {
	out.println("Access Token Callback");
	TokenManager.setCurrentGrantType(new Implicit());
	
	OAuthServerResponse resp = new OAuthServerResponse();
	resp.put(OAuthConstants.ExpiresInKey, request.getParameter(OAuthConstants.ExpiresInKey));
	resp.put(OAuthConstants.TokenTypeKey, request.getParameter(OAuthConstants.TokenTypeKey));
	resp.put(OAuthConstants.AccessTokenKey, request.getParameter(OAuthConstants.AccessTokenKey));

	TokenManagerHelper.put(resp);
	response.setHeader("Refresh", "0;url="+goToLoc);
} else {
	//Unknown state
	out.println("<h4>Oops!</h4> Something went wrong.");
}
%>
<div id="debug"></div>
</body>
</html>