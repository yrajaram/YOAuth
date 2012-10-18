<%@page import="com.herakles.api.client.oauth2.draft25.OAuthServerResponse"%>
<%@page import="com.herakles.api.client.oauth2.draft25.OAuthProperties"%>
<%@page import="com.herakles.api.client.oauth2.draft25.grant.TokenManagerHelper,java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Token Cache</title>
<link rel="stylesheet" type="text/css" href="light-blue.css" /> 
<script type="text/javascript">
var key;
</script>
</head>
<body>
<h1>Manage Currently Cached Tokens</h1>
<form id="notNeeded" action="">

<%
	//-----delete all selected keys
	String[] keys = request.getParameterValues("keys");
	if (keys != null) {
		for (int i = 0; i < keys.length; i++) {
			TokenManagerHelper.remove(keys[i]);
		}
	}
	//-----proceed with displaying cache
	Map<String, OAuthServerResponse> cache = TokenManagerHelper.getAllTokens();

	if (cache == null || cache.isEmpty())
		out.println("Token cache is empty.");
%>
<table>
<thead>
<tr>
<th>Client(Grant Type)</th>
<th>Value</th>
<th>Delete?</th>
</tr>
</thead>
<%
	for (Map.Entry<String, OAuthServerResponse> entry : cache.entrySet()) {
		out.println("<tr><td>" + entry.getKey() + "</td><td>" + entry.getValue()
				+ "</td><td><input type='checkbox' name='keys' value='"	+ entry.getKey() + "' /></td></tr>");

	}
	cache = null;
%>
</table>
<input type="submit" value="Delete"/>
</form>
<a href="<%=OAuthProperties.getServerMgrURL()%>">Manage at Auth Server (PingFederate)</a>
</body>
</html>