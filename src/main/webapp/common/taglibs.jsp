<%@ page import="com.sharewin.utils.AppConstants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<c:set var="ctxAdmin" value="${pageContext.request.contextPath}/sys"/>
<c:set var="ctxfront" value="${pageContext.request.contextPath}/wx"/>
<%
    long sysInitTime = AppConstants.SYS_INIT_TIME;
    //系统启动时间
    request.setAttribute("sysInitTime",sysInitTime);
%>