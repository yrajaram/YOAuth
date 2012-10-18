<%@page import="com.herakles.api.client.oauth2.draft25.grant.TokenManager"%>
<%@page import="com.herakles.api.client.oauth2.draft25.grant.TokenManagerHelper"%>
<%@page import="com.herakles.api.client.oauth2.draft25.OAuthProperties"%>
<%@page import="java.util.*,java.sql.*,com.herakles.api.client.ClientHelper"%>
<%@page import="org.apache.log4j.LogManager,org.apache.log4j.Logger" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<jsp:useBean id="form" class="com.herakles.api.client.ClientHelper" scope="request">
<jsp:setProperty name="form" property="*" /> 
</jsp:useBean>
<%!
private static final Logger log = LogManager.getLogger("invokeTarget_jsp.class");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Invoke Target</title>
<script type="text/javascript">
	function fnGTChange() {
		var selectedVal = document.invokeForm.grantType.options[document.invokeForm.grantType.selectedIndex].value;
		if( selectedVal != 'ResorceOwner' ) {
			document.invokeForm.username.disabled = true;
			document.invokeForm.password.disabled = true;
		} else {
			document.invokeForm.username.disabled = false;
			document.invokeForm.password.disabled = false;
		}
		if( selectedVal == 'AuthCode' ) {
			document.invokeForm.authURL.disabled = false;
		} else {
			document.invokeForm.authURL.disabled = true;
		}
	}
</script>
<link rel="stylesheet" type="text/css" href="light-blue.css" /> 
</head>
<body onload="fnGTChange()">
<h1>Invoke Target Application</h1>
<h3>Using OAuth2</h3>
<%
	log.debug("............................................................About to start new request<<<====================");
%>
<!-- Replace the form with your logic to invoke web service -->
<form name="invokeForm" method="POST" action=""  enctype="application/x-www-form-urlencoded" >
<table border="0" cellpadding="0">
<tr>
<th colspan="2">Input</th>
</tr>
<tr>
	<td>OAuth2 Protected API URL</td>
	<td width="50"><input type="text" name="appURL" size="50" value="${form.appURL}" /></td>
</tr>
<tr>
	<td>Grant Type</td>
	<td>
	<select name="grantType" onchange="fnGTChange()">
		<option value="<%=ClientHelper.AUTH_CODE_GT%>" <%=(form.getGrantType()==ClientHelper.AUTH_CODE_GT)?"selected=\"selected\"":""%>>Auth Code</option>
		<option value="<%=ClientHelper.IMPLICIT_GT%>" <%=(form.getGrantType()==ClientHelper.IMPLICIT_GT)?"selected=\"selected\"":""%>>Implicit</option>
		<option value="<%=ClientHelper.CLIENT_CRED_GT%>" <%=(form.getGrantType()==ClientHelper.CLIENT_CRED_GT)?"selected=\"selected\"":""%>>Client Cred</option>
		<option value="<%=ClientHelper.RESORCE_OWNER_GT%>" <%=(form.getGrantType()==ClientHelper.RESORCE_OWNER_GT)?"selected=\"selected\"":""%>>Resource Owner</option>
		</select>
	</td>
</tr>
<tr>
	<td>Redirect URL</td>
	<td width="50"><input type="text" name="redirectURL" size="50" value="${form.redirectURL}" /></td>
</tr>
<tr>
	<td width="25%">Client ID</td>
	<td><input type="text" name="clientId" size="50" value="${form.clientId}" /></td>
</tr>
<tr>
	<td>Client Secret</td>
	<td><input type="text" name="clientSecret" size="50" value="${form.clientSecret}" /></td>
</tr>
<tr>
	<td>Resource Owner ID</td>
	<td><input type="text" name="username" size="50" value="${form.username}" /></td>
</tr>
<tr>
	<td>Resource Owner Password</td>
	<td><input type="password" name="password" size="50" value="${form.password}" /></td>
</tr>
<tr>
	<td>Authorization URL</td>
	<td width="50"><input type="text" name="authURL" size="50" value="${form.authURL}" /></td>
</tr>
<tr>
	<td>Token URL</td>
	<td width="50"><input type="text" name="tokenURL" size="50" value="${form.tokenURL}" /></td>
</tr>
<tr>
		<td>
			<input type="submit" value="Get Token" onclick="{document.invokeForm.submtted.value='false';}"/>
			<input type="submit" value="Execute API" /> <input type="hidden" name="submtted" value="true"/>
		</td>
		<td><input type="reset" /></td>
</tr>
</table>

<table border="0" cellpadding="0">
<tr>
<th colspan="2">Output</th>
</tr>
<tr>
<%
// Do not remove the following from this JSP
log.debug("Value of Grant Type in form (form.getGrantType()):"+form.getGrantType()+" and TokenManager.KeyStore.get():"+TokenManagerHelper.get());

	if (ClientHelper.AUTH_CODE_GT.equalsIgnoreCase(form.getGrantType())
			&& (TokenManagerHelper.get() == null)) {
		String newLoc = OAuthProperties.getAuthURL()+"?response_type=code&client_id="+form.getClientId()+"&state=authCode&redirect_uri="+OAuthProperties.getRedirectURL()+"&scope=Read%20Write";
		response.setHeader("Refresh", "0;url="+newLoc);

	} else if (ClientHelper.IMPLICIT_GT.equalsIgnoreCase(form.getGrantType())
			&& (TokenManagerHelper.get() == null)) {
		String newLoc = OAuthProperties.getAuthURL()+"?response_type=token&client_id="+form.getClientId()+"&state=accessCode&redirect_uri="+OAuthProperties.getRedirectURL()+"&scope=Read%20Write";
		response.setHeader("Refresh", "0;url="+newLoc);
	}

	java.util.Date date = new java.util.Date();
%>
	<td width="25%">Response at <%=new Timestamp(date.getTime())%></td>
	<td width="50">
	<textarea rows="10" cols="50" name="response">${form.response}</textarea>
	</td>
</tr>
<tr>
	<td>Token Used</td>
	<td>
	<%=TokenManagerHelper.get()%>
	</td>
</tr>
</table>
</form>
<div id="response">
</div>
</body>
</html>