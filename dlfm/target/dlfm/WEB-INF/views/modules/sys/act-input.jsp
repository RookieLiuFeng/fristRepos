<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<script>
    window.UEDITOR_HOME_URL = ctxStatic + "/js/ueditor/";
</script>
<script type="text/javascript" src="${ctxStatic}/js/ueditor/ueditor.config.js?v=${sysInitTime}"></script>
<script type="text/javascript" src="${ctxStatic}/js/ueditor/ueditor.all.js"></script>
<style type="text/css">
    #preview,#preview img{
        width: 240px;
        height: 160px;
    }
    #preview img{
        vertical-align: middle;
    }

    #preview {
        border: 1px solid #f2f2f2;
    }

    .file {
        position: relative;
        display: inline-block;
        background: #D0EEFF;
        border: 1px solid #99D3F5;
        border-radius: 4px;
        padding: 4px 12px;
        overflow: hidden;
        color: #1E88C7;
        text-decoration: none;
        text-indent: 0;
        line-height: 20px;
    }

    .file input {
        position: absolute;
        font-size: 100px;
        right: 0;
        top: 0;
        opacity: 0;
    }

    .file:hover {
        background: #AADFFD;
        border-color: #78C3F3;
        color: #004974;
        text-decoration: none;
    }
</style>
<div>
    <form id="act_form" class="dialog-form" method="post" enctype="multipart/form-data" novalidate>
        <input type="hidden" id="id" name="id"/>
        <div>
            <label>活动名称:</label>
            <input type="text" id="actName" name="actName" maxLength="120"
                   class="easyui-validatebox textbox eu-input"
                   data-options="required:true,missingMessage:'请输入活动名称.',validType:['minLength[1]']"/>
        </div>

        <div>
            <label>活动日期:</label>
            <input id="actTime" name="actTime" type="text" class="easyui-datebox eu-input" editable="false" required="required" style="width: 200px" />
        </div>

        <div>
            <label>活动封面:</label>
            <div id="preview" style="display: inline-block;">
                <img src="${ctx}${act.actCoverUrl}">
            </div>
            <a href="javascript:void(0);" class="file">选择封面<input type="file" name="file" onchange="preview(this)"></a>
        </div>
        <div>
            <label>内容类型:</label>
            <label style="text-align: left;width: 60px;">
                <input class="actType" type="radio" name="actType" style="width: 20px;" value="0"/>原文
            </label>
            <label style="text-align: left;width: 120px;">
                <input class="actType" type="radio" name="actType" style="width: 20px;" value="1"/>外部链接
            </label>
        </div>

        <div id="actContentDiv">
            <label style="display: block;margin-bottom: 10px">活动详情:</label>
            <!-- 加载编辑器的容器 -->
            <script id="container" name="actDesc" type="text/plain">请编辑活动详情</script>
        </div>
        <div id="actLinkUrl">
            <label>外部链接:</label>
            <input type="text" id="linkUrl" name="linkUrl" maxLength="120"
                   class="easyui-validatebox textbox eu-input" style="width: 240px"
                   data-options="required:true,missingMessage:'请输入链接地址.'" validType="linkurl"/>
        </div>
    </form>
</div>
<!-- 实例化编辑器 -->
<script type="text/javascript">
    var $actContentDiv = $("#actContentDiv");
    var $actLinkUrl = $("#actLinkUrl");
    var $linkUrl = $("#linkUrl");
    var editor = new UE.ui.Editor({initialFrameHeight: 320});
    editor.render("container");

    function preview(file) {
        var prevDiv = document.getElementById('preview');
        if(file.files && file.files[0]) {
            var reader = new FileReader();
            reader.onload = function(evt) {
                prevDiv.innerHTML = '<img src="' + evt.target.result + '" />';
            };
            reader.readAsDataURL(file.files[0]);
        } else {
            prevDiv.innerHTML = '<div class="img" style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\'' + file.value + '\'"></div>';
        }
    }

    function changeRadio() {
        var selectedValue = $("input[name='actType']:checked").val();
        if(selectedValue==0){
            $actLinkUrl.hide();
            $actContentDiv.show();
            $linkUrl.validatebox('remove'); //删除

        }else{
            $actContentDiv.hide();
            $actLinkUrl.show();
            $linkUrl.validatebox('reduce'); //恢复
        }
    }

    $(function () {
        $(".actType").change(function () {
            changeRadio();
        });
    });
</script>

