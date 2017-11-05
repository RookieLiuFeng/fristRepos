<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<div>
    <form id="login_password_form" class="dialog-form" method="post" >
        <input type="hidden" id="login_password_form_id" name="id" value="${sessionInfo.userId}"/>
        <div>
            <label>原密码:</label>
            <input type="password" id="password"
                   name="password" class="easyui-validatebox textbox eu-input" maxLength="36"
                   data-options="required:true,missingMessage:'请输入原始密码.',validType:['minLength[1]']">
        </div>

        <div>
            <label>新密码:</label>
            <input type="password" id="newPassword"
                   name="newPassword" class="easyui-validatebox textbox eu-input" maxLength="36"
                   data-options="required:true,missingMessage:'请输入新密码.',validType:['minLength[1]']">
        </div>
        <div>
            <label>确认密码:</label>
            <input type="password" id="newPassword2"
                   name="newPassword2" class="easyui-validatebox textbox eu-input" required="true"
                   missingMessage="请再次输入确认新密码." validType="equalTo['#newPassword']"
                   invalidMessage="两次输入密码不匹配.">
        </div>

    </form>
</div>

