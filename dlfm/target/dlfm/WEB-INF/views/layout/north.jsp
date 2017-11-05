<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp" %>
<script type="text/javascript" src="${ctxStatic}/app/layout/north.js?v=${sysInitTime}" charset="utf-8"></script>
<div class="topleft">
    <img class="logo" src="${ctxStatic}/images/top_logo.png"/>
    <img class="title" src="${ctxStatic}/images/top_title.png"/>
</div>
<div class="topright">
    <!--头部信息放置处-->
    <i class="fa fa-calendar fa-fw"></i><span id="spaDate">${sysdate}</span>
    <i class="fa fa-user fa-fw"></i><span id="spaUser"><a href="javascript:void(0);" title="修改密码"
                                                          onclick="editLoginUserPassword();">${user.realname}</a></span>
    <i class="fa fa-power-off fa-fw"></i><span><a href="javascript:void(0);"
                                                  onclick="loginout()">退出</a></span>
</div>

