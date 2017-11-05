<%@ page import="com.sharewin.common.web.utils.WebUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
    <title>帐号绑定</title>
    <%@include file="/common/wxmeta.jsp" %>
    <style type="text/css">
        body {
            background-color: #fff;
        }

        .weui_label {
            display: block;
            width: 72px;
            word-wrap: break-word;
            word-break: break-all;
        }

        .weui_input {
            width: 100%;
            border: 0;
            outline: 0;
            -webkit-appearance: none;
            background-color: transparent;
            font-size: inherit;
            color: inherit;
            height: 1.41176471em;
            line-height: 1.41176471
        }

        .weui-cells_form input {
            -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        }

        .weui_cell_primary {
            -webkit-box-flex: 1;
            -webkit-flex: 1;
            -ms-flex: 1;
            flex: 1;
        }

        .content-padded {
            padding: 25px 15px;
        }

        .weui_msg {
            padding-top: 36px;
            text-align: center;
        }

        .weui_extra_area {
            position: fixed;
            left: 0;
            bottom: 0;
            width: 100%;
            text-align: center;
        }

        .weui_msg .weui_extra_area {
            margin-bottom: 15px;
            font-size: 14px;
            color: #888;
        }

        .weui_msg .weui_extra_area a {
            color: #617498;
        }
    </style>
    <%
        Cookie cookie = WebUtils.getCookie(request, "loginName");
        if (cookie != null) {
            String loginName = WebUtils.getCookie(request, "loginName").getValue();
            request.setAttribute("loginName", loginName);
        }
    %>
</head>
<body ontouchstart>
<header style="padding: 45px 15%; text-align: center;">
    <img src="${ctxStatic}/images/wx_login_logo.png" width="100%"/>
</header>
<div class="weui-content">
    <form id="loginform" name="loginform" method="post">
        <div>${openid}</div>
        <input type="hidden" name="openid" value="${openid}">
        <div class="weui-cells weui-cells_form">
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label class="weui_label">帐号:</label>
                </div>
                <div class="weui-cell__hd weui_cell_primary">
                    <input id="loginName" name="account" class="weui_input" value="${cookie.loginName.value}"
                           type="text" placeholder="请输入登录帐号"/>
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label class="weui_label">密码:</label>
                </div>
                <div class="weui-cell__hd weui_cell_primary">
                    <input id="password" name="password" class="weui_input" type="password" placeholder="请输入登录密码"/>
                </div>
            </div>
        </div>
        <div class="content-padded">
            <a href="javascript:submitlogin();" class="weui-btn weui-btn_primary">
                帐号绑定
            </a>
        </div>
    </form>
    <div class="weui_msg">
        <div class="weui_extra_area">
            <a href="javascript:void(0);">指导单位:上海市董李凤美康健学校</a>
            <br>
            <a href="javascript:void(0);">技术支持:上海协赢信息科技有限公司</a>
        </div>
    </div>
</div>
<script type="text/javascript" src="https://cdn.bootcss.com/jquery.form/4.2.1/jquery.form.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/layer/layer.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">

    function submitlogin() {
        var form = $("form[name=loginform]");
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
        $.showLoading("正在绑定...");
        var options = {
            url: ctxFront + '/login/binduser.json',
            type: 'post',
            dataType: 'json',
            data: form.serialize(),
            success: function (data) {
                $.hideLoading();
                if (data.code == 1) {
                    window.location.href = ctxFront + "/mycenter?userid=" + data.obj;
                } else {
                    setTimeout(function () {
                        $.alert(data.msg);
                    },200)
                }
            },
            error: function (XmlHttpRequest, textStatus, errorThrown) {
                $.hideLoading();
                setTimeout(function () {
                    $.alert("未知错误,请重试!");
                },200)

            }
        };
        form.ajaxSubmit(options);
    }
</script>
</body>
</html>
