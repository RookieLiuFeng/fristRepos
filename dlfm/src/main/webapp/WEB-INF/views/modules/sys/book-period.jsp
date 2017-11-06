<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/meta.jsp"%>

<script type="text/javascript">
    $(function() {
        loadPublish();
    });

    function loadPublish(){
        $('#publishType').combobox({
            url: '${ctxAdmin}/bookPeriod/publishTypeCombobox?selectType=all',
            width: 80,
            editable:false,
            value:''
        });
    }

</script>
<div class="easyui-layout" fit="true" style="margin: 0px;border: 0px;overflow: hidden;width:100%;height:100%;">
    <div data-options="region:'north',collapsed:false,split:false,border:false"
         style="width:100%; overflow-y: hidden;">
        <div class="search">
            <form id="book_period_search_form">
                <span class="eu-label">期刊名称:</span>
                <input type="text" name="sc_bookPeriodName" class="easyui-validatebox textbox eu-input"
                       placeholder="请输入期刊名称..." onkeydown="if(event.keyCode==13)search()" maxLength="25"
                       style="width: 160px"/>
                &nbsp;<span class="eu-label">类型:</span><input id="publishType" name="sc_publishType" style="height: 28px;" />
                <a class="easyui-linkbutton" href="#"
                         data-options="plain:true,iconCls:'easyui-icon-search',onClick:search">查询</a>&nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);"
                   data-options="plain:true,iconCls:'easyui-icon-no'"
                   onclick="javascript:$book_period_search_form.form('reset');">重置查询</a>
                &nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'eu-icon-add'"
                   onclick="javascript:showDialog();">添加期刊</a>
            </form>
        </div>
    </div>
    <%-- 中间部分 列表 --%>
    <div data-options="region:'center',split:false,border:false"
         style="padding: 0px; height: 100%;width:100%; overflow-y: hidden;">
        <table id="book_period_datagrid"></table>
    </div>
</div>
<script>
    var $book_period_datagrid;
    var $book_period_search_form;
    $(function () {
        $book_period_search_form = $('#book_period_search_form').form();
        //数据列表
        $book_period_datagrid = $('#book_period_datagrid').datagrid({
            url: ctxAdmin + '/bookPeriod/bookPeriodDatagrid',
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
                    {field: 'periodName', title: '期刊名称', width: 200},
                    {field: 'periodDesc', title: '期刊描述', width: 300},
                    {field: 'bookCount', title: '书籍数量', width: 60},
                    {field: 'releaseTime', title: '发布时间', width: 150},
                    {field: 'periodTypeView', title: '期刊类型', width: 80},
                    {field: 'statusView', title: '是否发布', width: 80,align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            if(rowData.status===1){
                                return '<span style="color:#1da762;font-size: 12px">' + value + '</span>'
                            }else{
                                return '<span style="color:#e73333;font-size: 12px">' + value + '</span>'
                            }


                        }},
                    {
                        field: 'cz',
                        title: '操作',
                        width: 180,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var str = '';
                            if(rowData.status===1){
                                str = '&nbsp;<a class="easyui-linkbutton" href="javascript:void(0)" data-options="plain:true,iconCls:\'eu-icon-database_del\'" onclick="del(' + rowIndex + ');">下架</a> ';
                            }else{
                                str = '&nbsp;<a class="easyui-linkbutton" href="javascript:void(0)" data-options="plain:true,iconCls:\'eu-icon-database_del\'" onclick="publish(' + rowIndex + ');">发布</a> '
                            }
                            return '<a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:\'eu-icon-database_edit\'" onclick="showDialog(' + rowIndex + ');">编辑</a>' +
                                str+
                                '&nbsp;<a class="easyui-linkbutton" href="javascript:void(0)" data-options="plain:true,iconCls:\'eu-icon-database_gear\'" onclick="bookPage('+rowIndex+');">书库</a>';

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

    function search() {
        $book_period_datagrid.datagrid('load', $.serializeObject($book_period_search_form));
    }


    // 删除
    function del(indexRow) {
        var row = getRow(indexRow);
        $.messager.confirm('确认提示！', '您确定要删除此期刊?', function (r) {
            if (r) {
                $.ajax({
                    url: ctxAdmin + '/bookPeriod/delete/'+row.id,
                    type: 'post',
                    traditional: true,
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 1) {
                            $book_period_datagrid.datagrid('load');	// reload the book_period data
                            eu.showMsg(data.msg);//操作结果提示
                        } else {
                            eu.showAlertMsg(data.msg, 'error');
                        }
                    }
                });
            }
        });

    }
    // 发布
    function publish(indexRow) {
        var row = getRow(indexRow);
        $.messager.confirm('确认提示！', '您确定要发布此期刊?', function (r) {
            if (r) {
                $.ajax({
                    url: ctxAdmin + '/bookPeriod/publish/'+row.id,
                    type: 'post',
                    traditional: true,
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 1) {
                            $book_period_datagrid.datagrid('load');	// reload the book_period data
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
        $book_period_form = $('#book_period_form').form({
            url: ctxAdmin + '/bookPeriod/save',
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
                    $book_period_dialog.dialog('destroy');//销毁对话框
                    $book_period_datagrid.datagrid('reload');//重新加载列表数据
                    eu.showMsg(json.msg);//操作结果提示
                } else {
                    eu.showAlertMsg(json.msg, 'error');
                }
            }
        });
    }

    function getRow(rowIndex) {
        var rows = $book_period_datagrid.datagrid('getRows');
        return rows[rowIndex];
    }

    function showDialog(rowIndex) {
        var row;
        var inputUrl = ctxAdmin + "/bookPeriod/input";
        if (rowIndex != undefined) {
            row = getRow(rowIndex);
            inputUrl = inputUrl + "?id=" + row.id;
        }

        //弹出对话窗口
        $book_period_dialog = $('<div/>').dialog({
            title: '操作',
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
                        $book_period_form.submit();
                    }
                },
                {
                    text: '关闭',
                    iconCls: 'easyui-icon-cancel',
                    handler: function () {
                        $book_period_dialog.dialog('destroy');
                    }
                }
            ],
            onClose: function () {
                $book_period_dialog.dialog('destroy');
            },
            onLoad: function () {
                formInit();
                if (row) {
                   // $("input[name='periodName']").attr('readonly', 'readonly');
                   // $("input[name='periodName']").css('background', '#eee');
                    $book_period_form.form('load', row);
                }else{
                    $("input[name='periodType']:eq(0)").attr("checked", 'checked');//状态 初始化值
                }

            }
        });

    }

    function bookPage(rowIndex) {
      var row = getRow(rowIndex);
      parent.openTab("书籍管理",ctxAdmin+'/book/'+row.id+'_index');
    }
</script>


