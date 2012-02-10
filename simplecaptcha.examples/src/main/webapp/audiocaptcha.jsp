<%@page import="javax.portlet.PortletURL"%>
<%@page import="com.khoinguyen.simplecaptcha.example.PortletConstant"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false" import="java.util.*,java.text.*"%>
<%
   PortletURL actionURL = (PortletURL) request.getAttribute(PortletConstant.VALIDATE_AUDIO);
   PortletURL changeModeURL = (PortletURL) request.getAttribute(PortletConstant.CHANGE_IMAGE);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
.success {
	color: blue;
}

.failure {
	color: red;
}
</style>
</head>
<body>
	<h2>Simple Audio Captcha Example</h2>
	<h2>
		<c:if test="${status == true}">
			<span class="success">You are true</span>
		</c:if>
		<c:if test="${status == false}">
			<span class="failure">You are false</span>
		</c:if>
	</h2>
	<div>
	  <audio controls autoplay id="audioCaptcha" src="${resourceURL}"></audio>
	</div>
	<form action="<%=actionURL%>" method="post">
		<input type="text" name="answer" /> <input type="submit"/>
	</form>
	<br />
	<a href="<%=changeModeURL%>">Change to text mode</a>  |  <a href="#test">Change to audio mode</a>
</body>
</html>
