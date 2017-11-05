<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/meta.jsp"%>
<iframe id="survey_temp_iframe" style="display: none;"></iframe>
<div class="easyui-layout" fit="true" style="margin: 0px;border: 0px;overflow: hidden;width:100%;height:100%;">
    <div data-options="region:'north',collapsed:false,split:false,border:false"
         style="width:100%; overflow-y: hidden;">
        <div class="search">
            <form id="record_search_form">
                <span class="eu-label">参与者姓名:</span>
                <input id="surveyId" name="surveyId" value="${survey.id}" hidden="hidden" />
                <input type="text" id="name" name="name" class="easyui-validatebox textbox eu-input"
                       placeholder="请输入参与者姓名..." onkeydown="if(event.keyCode==13)search()" maxLength="25"
                       style="width: 160px"/>
                &nbsp;<a class="easyui-linkbutton" href="#"
                         data-options="plain:true,iconCls:'easyui-icon-search',onClick:search">查询</a>
                &nbsp;<a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'easyui-icon-no'"
                   onclick="javascript:$record_search_form.form('reset');">重置查询</a>
                &nbsp;<a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'eu-icon-database_go'"
                         onclick="exportExcelForRecord()">导出</a>
            </form>
        </div>
    </div>
    <%-- 中间部分 列表 --%>
    <div data-options="region:'center',split:false,border:false"
         style="padding: 0px; height: 100%;width:100%; overflow-y: hidden;">
        <table id="record_datagrid"></table>
    </div>
</div>


<script>
    var $record_datagrid;
    var $record_search_form;
    $(function () {
        $record_search_form = $('#record_search_form').form();
        //数据列表
        $record_datagrid = $('#record_datagrid').datagrid({
            url: ctxAdmin + '/survey/record/datagrid',
            queryParams: $.serializeObject($record_search_form),
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
//                    {field: 'id', title: 'id', width: 10, hidden: false},
                    {field: 'name', title: '姓名', width: 80},
                    {field: 'userType', title: '身份', width: 80},
                    {field: 'sex', title: '性别', width: 40},
                    {field: 'finishTime', title: '完成时间', width: 200},
                    {field: 'statu', title: '是否有效', width: 80},
                    {field: 'score', title: '成绩', width: 60},
                    {field: 'completeness', title: '完成度', width: 60},
                    {
                        field: 'cz',
                        title: '操作',
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            return '<a class="easyui-linkbutton" href="javascript:void(0)" data-options="plain:true,iconCls:\'eu-icon-database_gear\'" onclick="recordDetail('+rowIndex+');">明细</a>';

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
        $record_datagrid.datagrid('reload',$.serializeObject($record_search_form));
    }


    function getRow(rowIndex) {
        var rows = $record_datagrid.datagrid('getRows');
        return rows[rowIndex];
    }

    function recordDetail(rowIndex) {
      var row = getRow(rowIndex);
      console.log(row.id);
      parent.openTab("答卷明细",ctxAdmin+'/survey/record/detail/'+row.id+'_index/${survey.id}');
    }

    function exportExcelForRecord() {
        console.log("导出参与者名单");
        $('#survey_temp_iframe').attr('src', ctxAdmin + '/survey/record/exportExcel?surveyid='+${survey.id});
    }

</script>


