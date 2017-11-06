<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div>
    <form id="book_period_form" class="dialog-form" method="post">
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
    </form>
</div>
