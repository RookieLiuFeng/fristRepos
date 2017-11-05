<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/meta.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/table-stat.css">
<iframe id="survey_temp_iframe" style="display: none;"></iframe>
<div class="easyui-layout" fit="true" style="margin: 0px;border: 0px;overflow: hidden;width:100%;height:100%;">
    <div data-options="region:'north',collapsed:false,split:false,border:false"
         style="width:100%; overflow-y: hidden;">
        <div class="search" style="position: relative; font-size: large;text-align: center;">
            &nbsp;<span style="color: #f43838;font-size: larger;height: 40px;" class="eu-label">所属问卷：${survey.surveyName}</span>&nbsp;
            <div style="">参与调查人数：${survey.surveyCount}</div>
            <a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'eu-icon-database_white'" onclick="surveyResult();"
               style="float: right;position: absolute;top: 5px;right: 17%">名单</a>
            <a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'eu-icon-database_go'" onclick="exportExcelForSurvey();"
               style="float: right;position: absolute;top: 5px;right: 12%">导出</a>
        </div>
    </div>
    <%-- 中间部分 列表 --%>
    <div id="table-container" data-options="region:'center',split:false,border:false"
         style="padding: 10px 0px; height: 100%;width:100%;">

    </div>
</div>


<script>
    $(function () {
      $.ajax({
          url:ctxAdmin + '/survey/stat/question',
          type:'GET',
          data:{
              'surveyid':${survey.id},
          },
          dataType:'json',
          traditional: true,
          success: function (data) {
              if (data.code == 1) {
                  var appendStr='';
                  $.each(data.obj,function (index,value) {
                      var appendChildStr='';
                      $.each(value.answerVoList,function (index,value) {
                          appendChildStr+='<tr>\n' +
                              '                <td class="col1">'+value.answerName+'</td>\n' +
                              '                <td class="col2">'+value.chooseNumber+'</td>\n' +
                              '                <td class="col3">\n' +
                              '                    <div>\n' +
                              '                        <div class="ratio-down">\n' +
                              '                            <div class="ratio-up" style="width:'+(value.ratio)+'%"></div>\n' +
                              '                        </div>\n' +
                              '                    </div>\n' +
                              '                    <span class="ratio" style="margin-left: 5%;background: none">'+(value.ratio)+'%</span>\n' +
                              '                </td>\n' +
                              '            </tr>\n';
                      });

                      appendStr += '<table cellpadding="0" cellspacing="0" style="border-collapse: collapse;width: 80%;margin: auto;font-size: 14px;font-family: \'microsoft yahei\', \'Lucida Grande\', Helvetica, Arial, Verdana, sans-serif;">\n' +
                          '            <caption style="margin:5px 0;line-height: 24px;"><span>第'+(index+1)+'题：</span>'+value.questionName+'<span style="color:#0066FF;">&nbsp;&nbsp;['+value.questionType+']</span></caption>\n' +
                          '            <tbody>\n' +
                          '            <tr class="table-header">\n' +
                          '                <th class="col1">选项</th>\n' +
                          '                <th class="col2">小计</th>\n' +
                          '                <th class="col3">比例</th>\n' +
                          '            </tr>\n' +appendChildStr+
                          '            <tr class="table-foot">\n' +
                          '                <td class="col1">本题有效填写人次</td>\n' +
                          '                <td class="col2">${survey.surveyCount}</td>\n' +
                          '                <td class="col3"></td>\n' +
                          '            </tr>\n' +
                          '            </tbody>\n' +
                          '        </table>'
                  });
                  $('#table-container').html(appendStr);
              } else {
                  eu.showAlertMsg('出现未知错误，请稍后再试！', 'error');
              }
          }
      });
    });

    //问卷结果导出为Excel
    function exportExcelForSurvey() {
        console.log("导出问卷结果");
        $('#survey_temp_iframe').attr('src', ctxAdmin + '/survey/stat/exportExcelForSurvey?surveyid='+${survey.id});
    }

    function surveyResult() {
        parent.openTab("参与问卷名单",ctxAdmin+'/survey/record/${survey.id}_index');
    }
</script>