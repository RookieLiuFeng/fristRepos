<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<div>
    <form id="question_form" class="dialog-form" method="post" novalidate>
        <input type="hidden" name="id"/>
        <div>
            <label>题目:</label>
            <input name="questionName" values="${question.questionName}" type="text" maxLength="255"
                   class="easyui-validatebox textbox eu-input" data-options="required:true,missingMessage:'请输入题目.'"/>
        </div>
        <div>
            <label>题目类型:</label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" name="questionType" style="width: 20px;" value="0"/> 单选
            </label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" name="questionType" style="width: 20px;" value="1"/> 多选
            </label>
        </div>
        <div>
            <label>题目排序:</label>
            <input name="questionOrder" type="text" maxLength="3" class="easyui-validatebox textbox eu-input" validType="number" />
        </div>
        <c:forEach var="item" items="${question.surveyAnswers}" varStatus="status">
            <div>
                <label>选项:</label>
                <input type="hidden" name="surveyAnswers[${status.index}].id" value="${item.id}"/>
                <input type="text" name="surveyAnswers[${status.index}].answerName" value="${item.answerName}"
                       class="easyui-validatebox textbox eu-input"/>
                &nbsp;&nbsp;
                <span>分值:</span>
                <input name="surveyAnswers[${status.index}].score" value="${item.score}"
                       class="easyui-validatebox textbox eu-input" style="width:45px" maxLength="2" validType="number"/>
                &nbsp;&nbsp;
            </div>
        </c:forEach>


    </form>
</div>
<script>
    var t = "${question.questionType}";
    if (t != null && t.length > 0) {
        $("input[name='questionType'][value=${question.questionType}]").attr("checked", true);
    } else {
        $("input[name='questionType'][value='0']").attr("checked", true);
    }

</script>