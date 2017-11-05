<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/meta.jsp"%>
<div class="easyui-layout" fit="true" style="margin: 0px;border: 0px;overflow: hidden;width:100%;height:100%;">
    <div data-options="region:'north',collapsed:false,split:false,border:false"
         style="width:100%; overflow-y: hidden;">
        <div class="search">
            <form id="survey_search_form">
                <span class="eu-label">问卷名称:</span>
                <input type="text" id="sc_surveyName" name="sc_surveyName" class="easyui-validatebox textbox eu-input"
                       placeholder="请输入问卷名称..." onkeydown="if(event.keyCode==13)search()" maxLength="25"
                       style="width: 160px"/>
                &nbsp;<a class="easyui-linkbutton" href="#"
                         data-options="plain:true,iconCls:'easyui-icon-search',onClick:search">查询</a>&nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);"
                   data-options="plain:true,iconCls:'easyui-icon-no'"
                   onclick="javascript:$survey_search_form.form('reset');">重置查询</a>
                &nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'eu-icon-add'"
                   onclick="javascript:showDialog();">添加问卷</a>
            </form>
        </div>
    </div>
    <%-- 中间部分 列表 --%>
    <div data-options="region:'center',split:false,border:false"
         style="padding: 0px; height: 100%;width:100%; overflow-y: hidden;">
        <table id="survey_datagrid"></table>
    </div>
</div>


<script>
    var $survey_datagrid;
    var $survey_search_form;
    $(function () {
        $survey_search_form = $('#survey_search_form').form();
        //数据列表
        $survey_datagrid = $('#survey_datagrid').datagrid({
            url: ctxAdmin + '/survey/surveyDatagrid',
            fit: true,
            pagination: true,//底部分页
            rownumbers: true,//显示行数
            fitColumns: false,//自适应列宽
            striped: true,//显示条纹
            pageSize: 20,//每页记录数
            idField: 'id',
            frozen: true,
            collapsible: true,
            pageList: [10, 20, 50, 100, 1000, 99999],
            frozenColumns: [
                [
                    {field: 'surveyName', title: '问卷名称', width: 150},
                    {field: 'surveyDesc', title: '问卷描述', width: 300},
                    {field: 'questionCount', title: '题目数量', width: 60},
                    {field: 'surveyStarttm', title: '开始时间', width: 140},
                    {field: 'surveyEndtm', title: '结束时间', width: 140},
                    {field: 'surveyCount', title: '问卷人次', width: 60},
                    {
                        field: 'cz',
                        title: '操作',
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            return '<a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:\'eu-icon-database_edit\'" onclick="showDialog(' + rowIndex + ');">编辑</a>' +
                                '&nbsp;<a class="easyui-linkbutton" href="javascript:void(0)" data-options="plain:true,iconCls:\'eu-icon-database_del\'" onclick="del(' + rowIndex + ');">删除</a> ' +
                                '&nbsp;<a class="easyui-linkbutton" href="javascript:void(0)" data-options="plain:true,iconCls:\'eu-icon-database_gear\'" onclick="questionPage('+rowIndex+');">题库</a>'+
                                '&nbsp;<a class="easyui-linkbutton" href="javascript:void(0)" data-options="plain:true,iconCls:\'eu-icon-database_gear\'" onclick="surveyStat('+rowIndex+');">问卷统计</a>';

                        }
                    }

                ]
            ],
            onLoadSuccess: function () {
                $(this).datagrid('clearSelections');//取消所有的已选择项
                $(this).datagrid('unselectAll');//取消全选按钮为全选状态
            },
            onRowContextMenu: function (e, rowIndex, rowData) {
                e.preventDefault();
                // $(this).datagrid('unselectAll');
                // $(this).datagrid('selectRow', rowIndex);

            }, onClickRow: function (rowIndex, rowData) {
                $(this).datagrid('unselectRow', rowIndex);
            },
            onDblClickRow: function (rowIndex, rowData) {

            }
        }).datagrid('showTooltip');

    });
    /**
     * 用户查询
     */
    function search() {
        $survey_datagrid.datagrid('load', $.serializeObject($survey_search_form));
    }


    // 删除用户
    function del(indexRow) {
        var row = getRow(indexRow);
        $.messager.confirm('确认提示！', '您确定要删除此问卷?', function (r) {
            if (r) {
                $.ajax({
                    url: ctxAdmin + '/survey/delete/'+row.id,
                    type: 'post',
                    traditional: true,
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 1) {
                            $survey_datagrid.datagrid('load');	// reload the survey data
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
        $survey_form = $('#survey_form').form({
            url: ctxAdmin + '/survey/save',
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
                    $survey_dialog.dialog('destroy');//销毁对话框
                    $survey_datagrid.datagrid('reload');//重新加载列表数据
                    eu.showMsg(json.msg);//操作结果提示
                } else {
                    eu.showAlertMsg(json.msg, 'error');
                }
            }
        });
    }

    function getRow(rowIndex) {
        var rows = $survey_datagrid.datagrid('getRows');
        return rows[rowIndex];
    }

    function showDialog(rowIndex) {
        var row;
        var inputUrl = ctxAdmin + "/survey/input";
        if (rowIndex != undefined) {
            row = getRow(rowIndex);
            inputUrl = inputUrl + "?id=" + row.id;
        }

        //弹出对话窗口
        $survey_dialog = $('<div/>').dialog({
            title: '问卷操作',
            top: 20,
            width: 540,
            height: 420,
            modal: true,
            maximizable: true,
            href: inputUrl,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'easyui-icon-save',
                    handler: function () {
                        $survey_form.submit();
                    }
                },
                {
                    text: '关闭',
                    iconCls: 'easyui-icon-cancel',
                    handler: function () {
                        $survey_dialog.dialog('destroy');
                    }
                }
            ],
            onClose: function () {
                $survey_dialog.dialog('destroy');
            },
            onLoad: function () {
                formInit();
                if (row) {
                    $("input[name='surveyName']").attr('readonly', 'readonly');
                    $("input[name='surveyName']").css('background', '#eee');
                    $survey_form.form('load', row);
                }

            }
        });

    }


    function questionPage(rowIndex) {
      var row = getRow(rowIndex);
      parent.openTab("题库管理",ctxAdmin+'/question/'+row.id+'_index');
    }

    function surveyStat(rowIndex) {
        var row = getRow(rowIndex);
        parent.openTab("问卷统计",ctxAdmin+'/survey/stat/'+row.id+'_index');
    }

</script>


