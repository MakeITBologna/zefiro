<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page buffer="8kb" %>
<%@ page import="it.makeit.jbrick.Log"%>
<%@ page import="java.util.Locale"%>
<%@ page import="it.makeit.jbrick.web.LocaleUtil"%>
<%@ page import="it.makeit.jbrick.JBrickConfigManager"%>
<%@ page import="it.makeit.jbrick.http.SessionUtil"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
if (!SessionUtil.isSessionComplete(request)) {
	SessionUtil.completeSession(request);
}
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", -1);
%>

<c:choose>
  <c:when test="${(locale.language eq 'it') or (locale.language eq 'en')}">
	  <c:set var="language" value="${locale.language}"/>
  </c:when>
  <c:otherwise>
    <c:set var="language" value="en"/>
  </c:otherwise>
</c:choose>

<c:if test="${initParam.applicationMode == 'production'}">
<c:set var="version" value="${initParam.applicationVersion}" scope="request"/>
</c:if>
<c:if test="${initParam.applicationMode != 'production'}">
<c:set var="version" value="<%=new java.util.Date().getTime()%>" scope="request"/>
</c:if>
