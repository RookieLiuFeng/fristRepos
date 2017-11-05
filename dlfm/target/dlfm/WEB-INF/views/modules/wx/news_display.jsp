<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${news.newsName}</title>
    <%@include file="/common/wxmeta.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/app/modules/dlfm/css/default.css?v=${sysInitTime}">
    <style>
        body{
            padding: 10px;
        }
        img{
            width: 100%;
            height: auto;
        }
    </style>
</head>
<body ontouchstart>
     ${news.newsContent}
</body>
</html>
