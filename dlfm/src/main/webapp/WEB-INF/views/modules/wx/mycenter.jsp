<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>董李凤美</title>
    <%@include file="/common/wxmeta.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/app/modules/dlfm/css/default_new.css?v=${sysInitTime}">
    <style>
        .module-user_info {
            display: flex;
            display: -webkit-flex;
            background: #fff;
            font-size: 17px;
            color: #222;
            padding: 0 10px;
            height: 72px;
            align-items: center;
        }

        .module-user_info:after {
            content: " ";
            position: absolute;
            left: 0;
            bottom: 0;
            right: 0;
            border-bottom: 1px solid #E8E8E8;
            -webkit-transform-origin: 0 100%;
            transform-origin: 0 100%;
            -webkit-transform: scaleY(0.5);
            transform: scaleY(0.5);
        }

        .user_photo {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            vertical-align: middle;
        }

        .user_layout {
            flex: 1;
            -webkit-flex: 1;
            padding-left: 10px;
            line-height: 1.6;
        }

        .user_layout .user_name {
            font-size: 16px;
        }

        .user_goz {
            color: #999;
            font-size: 13px;
            margin-top: 5px;
        }

        .user_right_view {
            position: relative;
            font-size: 13px;
            color: #999;
            height: 62px;
            line-height: 62px;
            padding-right: 18px;
        }

        .user-arrow {
            position: absolute;
            top: 23px;
            right: 0px;
            fill: #999;
            display: inline-block;
        }

        .personal-cell {
            height: 46px;
            line-height: 46px;
            align-items: center;
            padding-left: 10px;
            padding-right: 10px;
        }

        .personal-cell:active {
            background: rgba(0, 0, 0, 0.12);
        }

        .personal-cell img {
            width: 24px;
            height: 24px;
            vertical-align: middle;
        }

        .personal-title {
            margin-left: 10px;
            color: #333;
            font-size: 15px;
        }

        .personal-cell_arrow {
            color: #999;
            fill: #999;
        }

        .module-cell.personal-cell:last-child:after {
            display: none;
        }
    </style>

    <script>

    </script>

</head>
<body>
<div hidden>
    <svg xmlns="http://www.w3.org/2000/svg">
        <symbol id="icon-arrow-l" viewBox="0 0 8 16">
            <path d="M.146 7.646a.5.5 0 0 0 0 .708l7 7a.5.5 0 0 0 .708-.708l-7-7v.708l7-7a.5.5 0 0 0-.708-.708l-7 7z"></path>
        </symbol>
        <symbol id="icon-arrow-r" viewBox="0 0 7 12">
            <path d="M6.146 6.354v-.708l-5.5 5.5a.5.5 0 0 0 .708.708l5.5-5.5a.5.5 0 0 0 0-.708l-5.5-5.5a.5.5 0 1 0-.708.708l5.5 5.5z"></path>
        </symbol>
        <symbol id="icon-more" viewBox="0 0 18 13">
            <g>
                <rect width="18" height="1" rx=".5"></rect>
                <rect y="6" width="18" height="1" rx=".5"></rect>
                <rect y="12" width="18" height="1" rx=".5"></rect>
            </g>
        </symbol>
    </svg>
</div>
<div class="module module-nomargin">
    <div class="module-user_info">
        <img class="user_photo lazy"  style="vertical-align:middle" src="${ctxStatic}/images/avatar_default.png" data-original="${user.headimgurl}"/>
        <div class="user_layout">
            <p class="user_name">姓名:${user.realname}</p>
            <p class="user_goz">上海市董李凤美康健学校</p>
        </div>
        <div class="user_right_view">
            <!--<p>三年级二班</p>-->
            <svg class="icon user-arrow" aria-hidden="true">
                <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
            </svg>
        </div>
    </div>
</div>
<div class="module tbline">
    <a class="module-cell personal-cell btmline" href="${ctxfront}/survey">
        <img src="${ctxStatic}/images/my_value.png"/>
        <div class="module-cell_bd">
            <p class="personal-title">问卷调查</p>
        </div>
        <div class="personal-cell_fr">
            <svg class="icon personal-cell_arrow" aria-hidden="true">
                <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
            </svg>
        </div>
    </a>
    <a class="module-cell personal-cell btmline">
        <img src="${ctxStatic}/images/my_message.png"/>
        <div class="module-cell_bd">
            <p class="personal-title">我的消息</p>
        </div>
        <div class="personal-cell_fr">
            <svg class="icon personal-cell_arrow" aria-hidden="true">
                <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
            </svg>
        </div>
    </a>
    <a class="module-cell personal-cell btmline" href="${ctxfront}/mycenter/editpassword">
        <img src="${ctxStatic}/images/my_password.png"/>
        <div class="module-cell_bd">
            <p class="personal-title">密码修改</p>
        </div>
        <div class="personal-cell_fr">
            <svg class="icon personal-cell_arrow" aria-hidden="true">
                <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
            </svg>
        </div>
    </a>
    <div class="module-cell personal-cell btmline">
        <img src="${ctxStatic}/images/my_setting.png"/>
        <div class="module-cell_bd">
            <p class="personal-title">设置</p>
        </div>
        <div class="personal-cell_fr">
            <svg class="icon personal-cell_arrow" aria-hidden="true">
                <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
            </svg>
        </div>
    </div>

</div>
<div class="module tbline">
    <a id="removeBindBtn" href="javascript:void(0)" class="module-cell personal-cell flex-jc-c">
        <div class="module-cell_bd tc" style="font-size: 16px;color: #E64340;">
            <span>解除绑定</span>
        </div>
    </a>
</div>
<script src="https://cdn.bootcss.com/jquery_lazyload/1.9.7/jquery.lazyload.min.js"></script>
<script>
    $(function () {
        var pageManager = {
            $removeBindBtn :$("#removeBindBtn"),
            bindEvent:function () {
                var self = this;
                this.$removeBindBtn.on("click",function () {
                    $.modal({
                        title: "友情提示",
                        text: "是否解除当前帐号的微信绑定?",
                        buttons: [
                            { text: "取消", className: "default"},
                            { text: "确定", onClick: function(){
                                   self.removeBind();
                                }

                            }
                        ]
                    });

                })
            },
            removeBind:function () {
                var self = this;
                $.ajax({
                    type: 'POST',
                    url: ctxFront + '/mycenter/removeBindWx.json',
                    dataType: 'json',
                    async: true,
                    success: function (data) {
                        // console.log(JSON.stringify(data));
                        $.hideLoading();
                        if(data.code == 1){
                           window.location.reload();
                        }
                    },
                    error: function (xhr, type) {
                        $.hideLoading();
                        setTimeout(function () {
                            $.alert("发生未知错误!");
                        },200);

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
            $("img.lazy").lazyload();
            pageManager.bindEvent();

        }

        init();

    });
</script>
</body>
</html>
