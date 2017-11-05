<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<script>
    window.UEDITOR_HOME_URL = ctxStatic + "/js/ueditor/";
</script>
<script type="text/javascript" src="${ctxStatic}/js/ueditor/ueditor.config.js?v=${sysInitTime}"></script>
<script type="text/javascript" src="${ctxStatic}/js/ueditor/ueditor.all.js"></script>
<style type="text/css">
    #preview,#preview img{
        width: 150px;
        height: 200px;
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
    <form id="book_form" class="dialog-form" method="post" enctype="multipart/form-data" novalidate>
        <input type="hidden" id="id" name="id"/>
        <div>
            <label>书籍名称:</label>
            <input type="text" id="bookName" name="bookName" maxLength="120"
                   class="easyui-validatebox textbox eu-input"
                   data-options="required:true,missingMessage:'请输入书籍名称.',validType:['minLength[1]']"/>
        </div>

        <div>
            <label>书籍作者:</label>
            <input type="text" id="bookAuthor" name="bookAuthor" maxLength="56"
                   class="easyui-validatebox textbox eu-input"
                   data-options="required:true,missingMessage:'请输入书籍名称.',validType:['minLength[1]']"/>
        </div>

     <%--   <div>
            <label>类型:</label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" name="istj" style="width: 20px;" value="0" /> 普通
            </label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" name="istj" style="width: 20px;" value="1" /> 推荐
            </label>
        </div>--%>

        <div>
            <label>书籍封面:</label>
            <div id="preview" style="display: inline-block;">
                <img src="${ctx}${book.bookCoverUrl}">
            </div>
            <a href="javascript:void(0);" class="file">选择封面<input type="file" name="file" onchange="preview(this)"></a>
        </div>

        <div>
            <label>内容类型:</label>
            <label style="text-align: left;width: 60px;">
                <input class="bookType" type="radio" name="bookType" style="width: 20px;" value="0"/>原文
            </label>
            <label style="text-align: left;width: 120px;">
                <input class="bookType" type="radio" name="bookType" style="width: 20px;" value="1"/>外部链接
            </label>
        </div>

        <div id="bookContentDiv">
            <label style="display: block;margin-bottom: 10px">书籍详情:</label>
            <!-- 加载编辑器的容器 -->
            <script id="container" name="bookDesc" type="text/plain">请编辑书籍详情</script>
        </div>
        <div id="bookLinkUrl">
            <label>外部链接:</label>
            <input type="text" id="linkUrl" name="linkUrl" maxLength="150"
                   class="easyui-validatebox textbox eu-input" style="width: 240px"
                   data-options="required:true,missingMessage:'请输入链接地址.'" validType="linkurl"/>
        </div>
    </form>
</div>
<!-- 实例化编辑器 -->
<script type="text/javascript">
    var $bookContentDiv = $("#bookContentDiv");
    var $bookLinkUrl = $("#bookLinkUrl");
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
        var selectedValue = $("input[name='bookType']:checked").val();
        if(selectedValue==0){
            $bookLinkUrl.hide();
            $bookContentDiv.show();
            $linkUrl.validatebox('remove'); //删除

        }else{
            $bookContentDiv.hide();
            $bookLinkUrl.show();
            $linkUrl.validatebox('reduce'); //恢复
        }
    }

    $(function () {
        $(".bookType").change(function () {
            changeRadio();
        });
    });
</script>

