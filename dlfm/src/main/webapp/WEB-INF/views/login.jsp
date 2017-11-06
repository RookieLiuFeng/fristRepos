<%--
  Created by IntelliJ IDEA.
  User: yomg
  Date: 2017/5/2 0002
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>董李凤美微信管理平台</title>
    <%@include file="/common/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/app/modules/dlfm/css/login.css?v=${sysInitTime}">
</head>
<body>
<div class="login_layout">
    <div class="login_title">
        <img src="${ctxStatic}/images/top_title.png" />
    </div>
    <div class="login_body">
        <form id="loginForm" class="login_form">
            <div class="login_account">
                <label>用户名</label>
                <input id="loginName" name="loginName" class="login_input" type="text" placeholder="请输入登录名" />
            </div>
            <div class="login_password">
                <label>密码</label>
                <input id="password" name="password" class="login_input" type="password" placeholder="请输入登密码" />
            </div>
            <div class="login_buttons">
                <div id="loginBtn" class="ztn_button" onclick="submitlogin()">登  录</div>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/layer/layer.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
    function submitlogin() {
        var form = $("#loginForm");
        var name = $.trim($("#loginName").val());
        var password = $.trim($("#password").val());
        if (!IsNullEmpty(name)) {
            layer.tips('帐号不能为空！', '#loginName', {
                tips: 3
            });
            return;
        }
        if (!IsNullEmpty(password)) {
            layer.tips('密码不能为空！', '#password', {
                tips: 3
            });
            return;
        }
        $.ajax({
            type: 'POST',
            url: ctxAdmin + '/login/login',
            data: {loginName: name,password:password},
            dataType: 'json',
            async: true,
            success: function (data) {
                if (data.code == 1) {
                    location.href = data.obj;
                } else {
                    console.log(data);
                    layer.alert('帐号或者密码错误!');
                }
            },
            error: function (xhr, type) {
                layer.closeAll('loading');
            },
            beforeSend: function (xhr, settings) {
                layer.load();
            },
            complete: function (xhr, status) {
                layer.closeAll('loading');
            }
        });
    }
</script>
</body>
</html>
