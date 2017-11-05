<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div>
    <form id="survey_form" class="dialog-form" method="post">
        <input type="hidden" id="id" name="id" />
        <div>
            <label>问卷名称:</label>
            <input type="text" id="surveyName" name="surveyName" maxLength="36"
                   class="easyui-validatebox textbox eu-input"
                   data-options="required:true,missingMessage:'请输入问卷名称.',validType:['minLength[1]']"/>
        </div>

        <div>
            <label>开始时间:</label>
            <input id="surveyStarttm" name="surveyStarttm" type="text"  class="easyui-datetimebox eu-input" editable="false" required="required" style="width: 200px"/>
        </div>
        <div>
            <label>结束时间:</label>
            <input id="surveyEndtm" name="surveyEndtm" type="text" class="easyui-datetimebox eu-input" editable="false" required="required"   style="width: 200px" />
        </div>
        <div>
            <label style="vertical-align: top;">问卷描述:</label>
            <input name="surveyDesc" class="easyui-textbox" data-options="multiline:true" style="width:260px;height:100px;">
        </div>
    </form>
</div>
