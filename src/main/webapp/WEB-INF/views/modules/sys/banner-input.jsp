<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
    #preview,#preview img{
        width: 350px;
        height: 168px;
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
    <form id="banner_form" class="dialog-form" method="post" enctype="multipart/form-data" novalidate>
        <input type="hidden" id="id" name="id" value="${banner.id}" />
        <div>
            <label>banner描述:</label>
            <input type="text" id="bannerDesc" name="bannerDesc" maxLength="120" value="${banner.bannerDesc}"
                   class="easyui-validatebox textbox eu-input"
                   data-options="required:true,missingMessage:'请输入banner描述.',validType:['minLength[1]']"/>
        </div>
        <div>
            <label>banner图片:</label>
            <div id="preview" style="display: inline-block;">
                <img
                        src="${ctx}${banner.bannerUrl}">
            </div>
            <a href="javascript:void(0);" class="file">选择图片 <input type="file"
                                                            name="file" onchange="preview(this)">
            </a>
        </div>
        <div>
            <label>外部链接:</label>
            <input type="text" name="linkUrl" maxLength="150" value="${banner.linkUrl}"
                   class="easyui-validatebox textbox eu-input" style="width: 240px"
                   data-options="required:true,missingMessage:'请输入链接地址.'" validType="linkurl"/>
        </div>
    </form>
</div>

<script type="text/javascript">

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
</script>

