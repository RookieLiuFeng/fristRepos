<%@ page import="com.sharewin.utils.AppConstants" %>
<%
    String ctx = request.getContextPath();
    String adminPath = AppConstants.getAdminPath();
    response.sendRedirect(ctx + adminPath);
%>
