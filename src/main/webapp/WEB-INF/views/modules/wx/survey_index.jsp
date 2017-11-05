<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>问卷调查</title>
    <%@include file="/common/wxmeta.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/app/modules/dlfm/css/default.css?v=${sysInitTime}">
    <style>
        .survey-cell{
            height: 46px;
            align-items: center;
            padding: 0 10px;
            color: #333;
        }
    </style>
</head>
<body ontouchstart>
<div class="module module-nomargin">
    <c:forEach var="survey" items="${surveys}">
        <a href="${ctxfront}/survey/${survey.id}?userid=${userid}" class="module-cell survey-cell btmline">
            <p class="module-signtext">${survey.surveyName}</p>
        </a>
    </c:forEach>
</div>

</body>
</html>
