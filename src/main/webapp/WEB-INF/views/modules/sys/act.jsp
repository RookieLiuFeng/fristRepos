<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/meta.jsp"%>

<div class="easyui-layout" fit="true" style="margin: 0px;border: 0px;overflow: hidden;width:100%;height:100%;">
    <div data-options="region:'north',collapsed:false,split:false,border:false"
         style="width:100%; overflow-y: hidden;">
        <div class="search">
            <form id="act_search_form">
                <span class="eu-label">子活动名称:</span>
                <input type="text" name="sc_actName" class="easyui-validatebox textbox eu-input"
                       placeholder="请输入关键字" onkeydown="if(event.keyCode==13)search()" maxLength="25"
                       style="width: 160px"/>
                &nbsp;<a class="easyui-linkbutton" href="#"
                         data-options="plain:true,iconCls:'easyui-icon-search',onClick:search">查询</a>&nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);"
                   data-options="plain:true,iconCls:'easyui-icon-no'"
                   onclick="javascript:$act_search_form.form('reset');">重置查询</a>
                &nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'eu-icon-add'"
                   onclick="javascript:showDialog();">添加子活动</a>
            </form>
        </div>
    </div>
    <%-- 中间部分 列表 --%>
    <div data-options="region:'center',split:false,border:false"
         style="padding: 0px; height: 100%;width:100%; overflow-y: hidden;">
        <table id="act_datagrid"></table>
    </div>
</div>


<script>
    var $act_datagrid;
    var $act_search_form;
    $(function () {
        $act_search_form = $('#act_search_form').form();
        //数据列表
        $act_datagrid = $('#act_datagrid').datagrid({
            url: ctxAdmin + '/act/${actRoot.id}_actDatagrid',
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
                    {field: 'actName', title: '活动名称', width: 150},
                    {field: 'actTime', title: '活动日期', width: 100},
                    {field: 'createTime', title: '发布时间', width: 150},
                    {field: 'url', title: '访问地址', width: 300,
                        formatter: function (value, rowData, rowIndex) {
                            var url = '';
                            if(rowData.actType == 0){
                                url = rowData.browseUrl;
                            }else{
                                url = rowData.linkUrl;
                            }
                            return url;
                        }},
                    {
                        field: 'cz',
                        title: '操作',
                        width: 120,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            return '<a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:\'eu-icon-database_edit\'" onclick="showDialog(' + rowIndex + ');">编辑</a>' +
                                '&nbsp;<a class="easyui-linkbutton" href="javascript:void(0)" data-options="plain:true,iconCls:\'eu-icon-database_del\'" onclick="del(' + rowIndex + ');">删除</a>' +
                                '&nbsp;<a class="easyui-linkbutton" href="javascript:void(0)" data-options="plain:true,iconCls:\'eu-icon-database_gear\'" onclick="childActsPage('+rowIndex+');">子活动</a>';
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
        $act_datagrid.datagrid('load', $.serializeObject($act_search_form));
    }

    // 删除用户
    function del(indexRow) {
        var row = getRow(indexRow);
        $.messager.confirm('确认提示！', '您确定要删除此子活动?', function (r) {
            if (r) {
                $.ajax({
                    url: ctxAdmin + '/act/delete/'+row.id,
                    type: 'post',
                    traditional: true,
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 1) {
                            $act_datagrid.datagrid('load');	// reload the act data
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
        $act_form = $('#act_form').form({
            url: ctxAdmin + '/act/bsave/${actRoot.id}',
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
                    $act_dialog.dialog('destroy');//销毁对话框
                    $act_datagrid.datagrid('reload');//重新加载列表数据
                    eu.showMsg(json.msg);//操作结果提示
                } else {
                    eu.showAlertMsg(json.msg, 'error');
                }
            }
        });
    }

    function getRow(rowIndex) {
        var rows = $act_datagrid.datagrid('getRows');
        return rows[rowIndex];
    }

    function showDialog(rowIndex) {
        var row;
        var inputUrl = ctxAdmin + "/act/input";
        if (rowIndex != undefined) {
            row = getRow(rowIndex);
            inputUrl = inputUrl + "?id=" + row.id;
        }

        //弹出对话窗口
        $act_dialog = $('<div/>').dialog({
            title: '操作',
            top: 20,
            width: 540,
            height: 420,
            modal: true,
            maximizable: true,
            maximized:true,
            href: inputUrl,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'easyui-icon-save',
                    handler: function () {
                        $act_form.submit();
                    }
                },
                {
                    text: '关闭',
                    iconCls: 'easyui-icon-cancel',
                    handler: function () {
                        $act_dialog.dialog('destroy');
                    }
                }
            ],
            onClose: function () {
                $act_dialog.dialog('destroy');
            },
            onLoad: function () {
                formInit();
                if (row) {
                    $("input[name='actName']").attr('readonly', 'readonly');
                    $("input[name='actName']").css('background', '#eee');
                    $act_form.form('myLoad', row);
                    editor.ready(function() {
                        editor.setContent(row.actDesc);
                    });

                }else{
                    $("input[name='actType']:eq(0)").attr("checked", 'checked');//状态 初始化值
                }
                changeRadio();

            }
        });
    }

    function childActsPage(rowIndex) {
        var row = getRow(rowIndex);
        parent.openTab("子活动管理",ctxAdmin+'/act/'+row.id+'_index');
    }
</script>


