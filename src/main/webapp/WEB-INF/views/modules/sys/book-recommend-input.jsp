<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
    window.UEDITOR_HOME_URL = ctxStatic + "/js/ueditor/";
</script>
<script type="text/javascript" src="${ctxStatic}/js/ueditor/ueditor.config.js?v=${sysInitTime}"></script>
<script type="text/javascript" src="${ctxStatic}/js/ueditor/ueditor.all.js"></script>
<div>
    <form id="book_recommend_form" class="dialog-form" method="post">
        <input type="hidden" id="id" name="id" />
        <div>
            <label>期刊名称:</label>
            <input type="text" name="periodName" maxLength="36"
                   class="easyui-validatebox textbox eu-input"
                   data-options="required:true,missingMessage:'请输入期刊名称.',validType:['minLength[1]']"/>
        </div>

        <div style="display: none;">
            <label>类型:</label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" name="periodType" style="width: 20px;" value="0" /> 新书
            </label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" name="periodType" style="width: 20px;" value="1" /> 推荐
            </label>
        </div>

        <div>
            <label>发布时间:</label>
            <input  name="releaseTime" type="text"  class="easyui-datetimebox eu-input" editable="false" required="required" style="width: 200px"/>
        </div>
        <div>
            <label style="vertical-align: top;">期刊描述:</label>
            <input name="periodDesc" class="easyui-textbox" data-options="multiline:true" style="width:260px;height:100px;">
        </div>

        <div id="newsContentDiv">
            <label style="display: block;margin-bottom: 10px">期刊详情:</label>
            <!-- 加载编辑器的容器 -->
            <script id="container" name="periodInfo" type="text/plain">请编辑期刊详情</script>
        </div>
    </form>
</div>

<!-- 实例化编辑器 -->
<script type="text/javascript">
    var $newsContentDiv = $("#newsContentDiv");
    var $newsLinkUrl = $("#newsLinkUrl");
    var $linkUrl = $("#linkUrl");
    var editor = new UE.ui.Editor({initialFrameHeight: 320});
    editor.render("container");

    function preview(file) {
        var prevDiv = document.getElementById('preview');
        if (file.files && file.files[0]) {
            var reader = new FileReader();
            reader.onload = function (evt) {
                prevDiv.innerHTML = '<img src="' + evt.target.result + '" />';
            };
            reader.readAsDataURL(file.files[0]);
        } else {
            prevDiv.innerHTML = '<div class="img" style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\'' + file.value + '\'"></div>';
        }
    }

    function changeRadio() {
        var selectedValue = $("input[name='newsType']:checked").val();
        if (selectedValue == 0) {
            $newsLinkUrl.hide();
            $newsContentDiv.show();
            $linkUrl.validatebox('remove'); //删除
        } else {
            $newsContentDiv.hide();
            $newsLinkUrl.show();
            $linkUrl.validatebox('reduce'); //恢复
        }
    }

    $(function () {
        $(".newsType").change(function () {
            changeRadio();
        });
    });
</script>