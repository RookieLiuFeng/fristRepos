var $login_password_form;

function initLoginPasswordForm(){
    $login_password_form = $('#login_password_form').form({
        url: ctxAdmin+'/user/updateUserPassword',
        onSubmit: function(param){
            param.upateOperate = '1';
            return $(this).form('validate');
        },
        success: function(data){
            var json = eval('('+ data+')');
            if (json.code == 1){
                $login_about_dialog.dialog('close');
                eu.showMsg(json.msg);//操作结果提示
            } else if(json.code == 2){
                $.messager.alert('提示信息！', json.msg, 'warning',function(){
                    var userId = $('#login_password_form_id').val();
                    $(this).form('clear');
                    $('#login_password_form_id').val(userId);
                    if(json.obj){
                        $('#login_password_form input[name="'+json.obj+'"]').focus();
                    }
                });
            }else {
                eu.showAlertMsg(json.msg,'error');
            }
        }
    });
}

function editLoginUserPassword(){
    //弹出对话窗口
    $login_about_dialog = $('<div/>').dialog({
        title:'&nbsp;修改用户密码',
        width : 460,
        height : 240,
        modal : true,
        iconCls:'easyui-icon-edit',
        href : ctxAdmin+'/common/layout/north-password',
        buttons : [{
            text : '保存',
            iconCls : 'easyui-icon-save',
            handler : function() {
                $login_password_form.submit();
            }
        },{
            text : '关闭',
            iconCls : 'easyui-icon-cancel',
            handler : function() {
                $login_about_dialog.dialog('destroy');
            }
        }],
        onClose : function() {
            $login_about_dialog.dialog('destroy');
        },
        onLoad:function(){
            initLoginPasswordForm();
        }
    });
}

//注销
function loginout() {
    $.messager.confirm('确认提示！', '您确定要退出系统吗？', function (r) {
        if (r) {
            window.location.href=ctxAdmin+"/login/loginout";
        }
    });
}

