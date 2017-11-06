<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>密码修改</title>
    <%@include file="/common/wxmeta.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/app/modules/dlfm/css/default_new.css?v=${sysInitTime}">
    <style>
        .password_left_v {
            width: 80px;
            color: #222;
            font-size: 16px;
        }

        .password_ipt {
            outline: none;
            border: 0;
            font-size: 15px;
            color: #555555;
        }
    </style>
</head>
<body ontouchstart>
<div class="module pl10">
    <div class="module-cell-h46">
        <p class="password_left_v">原密码</p>
        <div class="module-cell_bd">
            <input id="orgpassword" name="orgpassword" class="password_ipt" type="password" placeholder="原密码"/>
        </div>
    </div>
    <div class="module-cell-h46">
        <p class="password_left_v">新密码</p>
        <div class="module-cell_bd">
            <input id="newpassword" name="newpassword" class="password_ipt" type="password" placeholder="新密码"/>
        </div>
    </div>
    <div class="module-cell-h46">
        <p class="password_left_v">再次输入</p>
        <div class="module-cell_bd">
            <input id="repassword" name="repassword" class="password_ipt" type="password" placeholder="再次输入"/>
        </div>
    </div>
</div>
<div style="padding: 25px 15px;">
    <a id="editPdBtn" class="weui-btn weui-btn_primary">
        修改密码
    </a>
</div>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script>
    $(function () {
        var pageManger = {
            $editPdBtn: $("#editPdBtn"),
            $orgpassword: $("#orgpassword"),
            $newpassword: $("#newpassword"),
            $repassword: $("#repassword"),
            initEvent: function () {
                var self = this;
                self.$editPdBtn.on('click', function () {
                    var orgpassword = $.trim(self.$orgpassword.val());
                    var newpassword = $.trim(self.$newpassword.val());
                    var repassword = $.trim(self.$repassword.val());
                    if (!IsNullEmpty(orgpassword)) {
                        $.alert("原密码不能为空!");
                        return;
                    }
                    if (!IsNullEmpty(newpassword)) {
                        $.alert("新密码不能为空!");
                        return;
                    }
                    if (newpassword != repassword) {
                        $.alert("两次输入的新密码不一致");
                        return;
                    }
                    setTimeout(function () {
                        $.modal({
                            title: "友情提示",
                            text: "是否确定修改当前密码?",
                            buttons: [
                                {text: "取消", className: "default"},
                                {
                                    text: "确定", onClick: function () {
                                    self.editPassword(orgpassword, newpassword);
                                }
                                }
                            ]
                        });
                    }, 200);

                })
            },
            editPassword: function (a, b) {
                var self = this;
                $.ajax({
                    type: 'POST',
                    url: ctxFront + '/mycenter/editpassword.json',
                    dataType: 'json',
                    data: {orgpassword: a, newpassword: b},
                    async: true,
                    success: function (data) {
                        // console.log(JSON.stringify(data));
                        if (data.code == 1) {
                            setTimeout(function () {
                                $.alert("修改密码成功!");
                            }, 200);
                        } else {
                            setTimeout(function () {
                                $.alert("原始密码错误!");
                            }, 200);
                        }
                    },
                    error: function (xhr, type) {
                        $.hideLoading();
                        setTimeout(function () {
                            $.alert("发生未知错误!");
                        }, 200);

                    },
                    beforeSend: function (xhr, settings) {
                        $.showLoading();
                    },
                    complete: function (xhr, status) {
                        $.hideLoading();
                    }
                });
            }
        }

        function init() {
            pageManger.initEvent();
        }

        init();

    });
</script>
</body>
</html>
