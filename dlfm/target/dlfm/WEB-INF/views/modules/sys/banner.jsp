<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/meta.jsp" %>
<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.lazyload.min.js"></script>
<style>
    .sun_content {
        border: 1px solid #c3d9e0;
        padding: 2px;
        text-align: left;
        margin: 2px;
        height: 100%;
        overflow-y: auto;
    }

    html {
        -ms-text-size-adjust: 100%;
        -webkit-text-size-adjust: 100%;
    }

    body {
        line-height: 1.6;
        font-family: -apple-system-font, "Helvetica Neue", sans-serif;
    }

    * {
        margin: 0;
        padding: 0;
    }

    a img {
        border: 0;
    }

    a {
        text-decoration: none;
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
    }

    ul,
    li {
        list-style: none;
    }

    .act_list .act_list_item {
        width: 350px;
        float: left;
        margin-bottom: 20px;
    }

    .act_list:after {
        content: '';
        display: table;
        clear: both;
    }

    .act_list .act_list_item .act_content {
        display: block;
        width: calc(100% - 10px);
        text-align: center;
        border: 1px solid #E5E5E5;
        background: #fff;
    }

    .banner_img {
        width: 100%;
        height: 168px;
    }

    .act_list_item p {
        font-size: 14px;
        color: #444;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }

    .act_list_item .act_list_item_div {
        text-align: left;
        padding: 0px 5px;
        margin-top: 2px;
    }
</style>
<div class="easyui-layout" fit="true" style="margin: 0px;border: 0px;overflow: hidden;width:100%;height:100%;">
    <div data-options="region:'north',collapsed:false,split:false,border:false"
         style="width:100%; overflow-y: hidden;">
        <div class="search">
            <form id="banner_search_form">
                <span class="eu-label">描述:</span>
                <input type="text" id="sc_bannerDesc" name="sc_bannerDesc" class="easyui-validatebox textbox eu-input"
                       placeholder="请输入描述信息..." onkeydown="if(event.keyCode==13)search()" maxLength="25"
                       style="width: 160px"/>
                &nbsp;<a class="easyui-linkbutton" href="#"
                         data-options="plain:true,iconCls:'easyui-icon-search',onClick:search">查询</a>&nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);"
                   data-options="plain:true,iconCls:'easyui-icon-no'"
                   onclick="javascript:$banner_search_form.form('reset');">重置查询</a>
                &nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'eu-icon-add'"
                   onclick="javascript:showDialog();">添加banner</a>
            </form>
        </div>
    </div>
    <%-- 中间部分 列表 --%>
    <div data-options="region:'center',split:false,border:false"
         style="padding: 0px; height: 100%;width:100%; overflow-y: hidden;">
        <div class="sun_content">
            <ul id="bannerList" class="act_list" style="padding: 5px;position: relative;">
            </ul>
        </div>
    </div>
</div>


<script>
    var  $banner_search_form = $("#banner_search_form").form();

    function search() {
          getBannerList($.serializeObject($banner_search_form));
    }
    function del(d) {
        var id = $(d).data("banner");
        $.messager.confirm('确认提示！', '您确定要删除此banner?', function (r) {
            if (r) {
                $.ajax({
                    url: ctxAdmin + '/banner/delete/'+id,
                    type: 'post',
                    traditional: true,
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 1) {
                            getBannerList($.serializeObject($banner_search_form));
                            eu.showMsg(data.msg);//操作结果提示
                        } else {
                            eu.showAlertMsg(data.msg, 'error');
                        }
                    }
                });
            }
        });
    }

    function formInit() {
        $banner_form = $('#banner_form').form({
            url: ctxAdmin + '/banner/bsave',
            onSubmit: function (param) {
                $.messager.progress({
                    title: '提示信息！',
                    text: '数据处理中，请稍后....'
                });
                var isValid = $(this).form('validate');
                if (!isValid) {
                    $.messager.progress('close');
                }
                return isValid;
            },
            success: function (data) {
                $.messager.progress('close');
                var json = $.parseJSON(data);
                if (json.code == 1) {
                    $banner_dialog.dialog('destroy');//销毁对话框
                    getBannerList($.serializeObject($banner_search_form));
                    eu.showMsg(json.msg);//操作结果提示
                } else {
                    eu.showAlertMsg(json.msg, 'error');
                }
            }
        });
    }


    function showDialog(b) {
        var inputUrl = ctxAdmin + "/banner/input";
        if (b != undefined) {
            var id = $(b).data("banner");
            inputUrl = inputUrl + "?id=" + id;
        }

        //弹出对话窗口
        $banner_dialog = $('<div/>').dialog({
            title: '操作',
            top: 20,
            width: 960,
            height: 420,
            modal: false,
            maximizable: true,
            maximized:true,
            href: inputUrl,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'easyui-icon-save',
                    handler: function () {
                        $banner_form.submit();
                    }
                },
                {
                    text: '关闭',
                    iconCls: 'easyui-icon-cancel',
                    handler: function () {
                        $banner_dialog.dialog('destroy');
                    }
                }
            ],
            onClose: function () {
                $banner_dialog.dialog('destroy');
            },
            onLoad: function () {
                formInit();
                if (b) {
                    $("input[name='bannerDesc']").attr('readonly', 'readonly');
                    $("input[name='bannerDesc']").css('background', '#eee');
                }

            }
        });

    }

    function getBannerList(d) {
        $.ajax({
            type: 'post',
            url: ctxAdmin + "/banner/list",
            dataType: 'json',
            data: d,
            async: true,
            success: function (data) {
                if (data != null) {
                    var c = [];
                    $.each(data, function (index, item) {
                          c.push('<li class="act_list_item"><div class="act_content"><img class="banner_img" src="${ctxStatic}/images/zanwu.jpg" data-original="${ctx}' + item.bannerUrl + '"><div class="act_list_item_div"><p style="font-size:14px">描述：'+item.bannerDesc+'</p><p style="font-size:14px">时间：'+item.createTime+'</p><p style="font-size:14px;vertical-align:bottom">操作:&nbsp;&nbsp;<a class="banner-eidt-btn" href="javascript:void(0);" data-banner="'+item.id+'" onClick="showDialog(this)">编辑</a> &nbsp;&nbsp;<a class="banner-del-btn" href="javascript:void(0);" data-banner="'+item.id+'" onClick="del(this)">删除</a></p></div></div></li>');

                    });
                    $("#bannerList").html(c.join(''));
                    $(".banner-eidt-btn").linkbutton({
                        plain:true,
                        iconCls: 'eu-icon-database_edit'
                    });
                    $(".banner-del-btn").linkbutton({
                        plain:true,
                        iconCls: 'eu-icon-database_del'
                    });

                    $(".banner_img").lazyload();
                }

            },
            error: function (xhr, type) {
                eu.closeLoading();
            },
            beforeSend: function (xhr, settings) {
               eu.openLoading();
            },
            complete: function (xhr, status) {
                eu.closeLoading();
            }
        });
    }

    $(function () {
        getBannerList();
    })
</script>


