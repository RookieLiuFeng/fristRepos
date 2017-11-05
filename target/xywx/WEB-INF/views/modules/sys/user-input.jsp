<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    $(function() {
        loadSex();
    });
    //性别
    function loadSex(){
        $('#sex').combobox({
            url: '${ctxAdmin}/user/sexTypeCombobox?selectType=select',
            width: 200,
            editable:false,
            value:'2'
        });
    }

</script>
<div>
    <form id="user_form" class="dialog-form" method="post">
        <input type="hidden" id="id" name="id" />
        <div>
            <label>登录名:</label>
            <input type="text" id="loginName" name="loginName" maxLength="36"
                   class="easyui-validatebox textbox eu-input"
                   data-options="required:true,missingMessage:'请输入登录名.',validType:['minLength[1]']"/>
        </div>
        <div id="password_div">
            <label>密码:</label>
            <input type="password" id="password"
                   name="password" class="easyui-validatebox textbox eu-input" maxLength="36"
                   data-options="required:true,missingMessage:'请输入密码.',validType:['minLength[1]']">
        </div>
        <div id="repassword_div">
            <label>确认密码:</label>
            <input type="password" id="repassword"
                   name="repassword" class="easyui-validatebox textbox eu-input" required="true"
                   missingMessage="请再次填写密码." validType="equalTo['#password']"
                   invalidMessage="两次输入密码不匹配.">
        </div>

        <div>
            <label>姓名:</label>
            <input name="realname" type="text" maxLength="6" class="easyui-validatebox textbox eu-input" data-options="validType:['CHS','length[2,6]']" />
        </div>

        <div>
            <label>性别:</label>
            <input id="sex" name="sex" style="height: 28px;" />
        </div>
        <div>
            <label>入学时间:</label>
            <input id="enterSchoolTime" name="enterSchoolTime" type="text"  class="easyui-datetimebox eu-input" editable="false" required="required" style="width: 200px"  />
        </div>
        <div>
            <label>监护人:</label>
            <input name="guardianName" type="text" maxLength="6" class="easyui-validatebox textbox eu-input" data-options="validType:['CHS','length[2,6]']" />
        </div>
        <div>
            <label>身份证:</label>
            <input name="idcardno" type="text" class="easyui-validatebox textbox eu-input" validType="idcard" />
        </div>
        <div>
            <label>手机号:</label>
            <input name="telphone" type="text" class="easyui-validatebox textbox eu-input" validType="mobile">
        </div>
        <div>
            <label>出生日期:</label>
            <input id="birthday" name="birthday" type="text" class="easyui-datebox eu-input" editable="false" required="required" style="width: 200px" />
        </div>

    </form>
</div>
