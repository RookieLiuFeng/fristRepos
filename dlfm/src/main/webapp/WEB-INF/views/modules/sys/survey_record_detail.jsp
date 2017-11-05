<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp" %>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">--%>
<%--<html xmlns="http://www.w3.org/1999/xhtml">--%>
<%--<head>--%>
    <%--<title>${survey.surveyName}</title>--%>
    <%--<meta charset="utf-8">--%>
    <%--<meta http-equiv="X-UA-Compatible" content="IE=edge">--%>
    <%--&lt;%&ndash;<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">&ndash;%&gt;--%>
    <link rel="stylesheet" href="//cdn.bootcss.com/weui/1.1.1/style/weui.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/jquery-weui/1.0.1/css/jquery-weui.min.css">
    <script src="//cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
    <%--<script src="//cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>--%>
    <script src="//cdn.bootcss.com/jquery-weui/1.0.1/js/jquery-weui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/app/modules/dlfm/css/survey.css?v=${sysInitTime}">
    <script type="text/javascript" charset="utf-8">
        var ctx = "${ctx}";
        var ctxStatic = "${ctxStatic}";
        var ctxAdmin = "${ctxAdmin}";
    </script>
<%--</head>--%>
<%--<body ontouchstart>--%>
<form id="surveyForm" method="post" action="">
    <div id="surveyContent">
        <%--<div id="surveyDesc" class="formfield">--%>
            <%--<span class="description">${survey.surveyDesc}</span>--%>
        <%--</div>--%>
        <%--<c:if test="${not empty survey.surveyTip}">--%>
            <div id="surveyNotify" class="surveynotify">
                <b>提示：</b> 此答卷仅供查看，不可修改。<%--${survey.surveyTip}--%>
            </div>
        <%--</c:if>--%>

        <div id="surveyQuestion">
            <fieldset id="surveyFieldset" class="fieldset">
                <c:forEach var="surveyQuestion" items="${survey.surveyQuestions}" varStatus="status">
                    <c:choose>
                        <c:when test="${surveyQuestion.questionType == 1}">
                            <div class="field ui-field-contain" id="div${status.index+1}" req="1"
                                 topic="${status.index+1}" data-qid="${surveyQuestion.id}"
                                 data-role="fieldcontain" type="4">
                                <div class="field-label">
                                        ${status.index+1}. ${surveyQuestion.questionName}
                                    <span class="req">*</span>
                                    <span class="qtypetip">&nbsp;[多选题]</span>
                                </div>

                                <div class="ui-controlgroup">
                                    <c:forEach var="answer" items="${surveyQuestion.surveyAnswers}"
                                               varStatus="anserIndex">
                                        <div class="ui-checkbox"><span class="jqcheckwrapper">
                                         <input type="checkbox" value="${answer.id}"
                                                id="q${status.index+1}_${anserIndex.index+1}"
                                                name="q${status.index+1}" style="display:none;">
                                         <a class="jqcheck" href="javascript:void(0);"></a></span>
                                            <label for="q${status.index+1}_${anserIndex.index+1}">${answer.answerName}</label>
                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="errorMessage"></div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="field ui-field-contain" id="div${status.index+1}" req="1"
                                 topic="${status.index+1}" data-qid="${surveyQuestion.id}"
                                 data-role="fieldcontain" type="3">
                                <div class="field-label">
                                        ${status.index+1}. ${surveyQuestion.questionName}
                                    <span class="req">*</span>
                                </div>
                                <div class="ui-controlgroup">
                                    <c:forEach var="answer" items="${surveyQuestion.surveyAnswers}"
                                               varStatus="anserIndex">
                                        <div class="ui-radio"><span class="jqradiowrapper">
                                         <input type="radio" value="${answer.id}"
                                                id="q${status.index+1}_${anserIndex.index+1}"
                                                name="q${status.index+1}" style="display:none;">
                                         <a class="jqradio" href="javascript:void(0);"></a></span>
                                            <label for="q${status.index+1}_${anserIndex.index+1}">${answer.answerName}</label>
                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="errorMessage"></div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </fieldset>
        </div>
        <div class="footer" style="display: none;">
            <div class="divSubmit">
                <a id="ctlNext" href="javascript:void(0);" class="button blue">提交</a>
            </div>
        </div>
    </div>
</form>

<a href="javascript:void(0);" class="scrolltop" style="display:none;"></a>

<script src="${ctxStatic}/app/modules/dlfm/js/survey.js?v=${sysInitTime}" type="text/javascript"></script>
<script>
    var surveyId = "${survey.id}";
    var userid = "${userid}";

    $(function () {
        $('.ui-radio').unbind('click');
        $('.ui-checkbox').unbind('click');

        $.ajax({
            type: 'post',
            url: ctxAdmin + '/survey/record/detail',
            dataType: 'json',
            data: {
                recordid: ${recordid}
            },
            async: true,
            success: function (data) {
                if (data.code == 1) {
                    $.each(data.obj, function(index, value) {
                        var answerid = {};
                        answeridList = value.answerid.split(',');
                        $.each(answeridList, function(index, value){
                            $('input[value=' + value + ']').parent('.jqradiowrapper').parent().addClass('focuschoice');
                            $('input[value=' + value + ']').next().addClass('jqchecked');
                        });
                    });
                }
            },
            error: function() {
                eu.showAlertMsg('网络开了小差，请稍后重试', 'error');
            }
        });
    });
</script>
<%--</body>
</html>--%>
