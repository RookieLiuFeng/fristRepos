<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/meta.jsp"%>

<div class="easyui-layout" fit="true" style="margin: 0px;border: 0px;overflow: hidden;width:100%;height:100%;">
    <div data-options="region:'north',collapsed:false,split:false,border:false"
         style="width:100%; overflow-y: hidden;">
        <div class="search">
            <form id="news_search_form">
                <span class="eu-label">新闻标题:</span>
                <input type="text" id="titleName" name="titleName" class="easyui-validatebox textbox eu-input"
                       placeholder="请输入新闻标题..." onkeydown="if(event.keyCode==13)search()" maxLength="25"
                       style="width: 160px"/>
                &nbsp;<a class="easyui-linkbutton" href="#"
                         data-options="plain:true,iconCls:'easyui-icon-search',onClick:search">查询</a>&nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);"
                   data-options="plain:true,iconCls:'easyui-icon-no'"
                   onclick="javascript:$news_search_form.form('reset');">重置查询</a>
                &nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'eu-icon-add'"
                   onclick="showDialog();">添加新闻</a>
            </form>
        </div>
    </div>
    <%-- 中间部分 列表 --%>
    <div data-options="region:'center',split:false,border:false"
         style="padding: 0px; height: 100%;width:100%; overflow-y: hidden;">
        <table id="news_datagrid"></table>
    </div>
</div>


<script>
    var $news_datagrid;
    var $news_search_form;
    $(function () {
        $news_search_form = $('#news_search_form').form();
        //数据列表
        $news_datagrid = $('#news_datagrid').datagrid({
            url: ctxAdmin + '/news/newsDatagrid',
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
                    {field: 'newsName', title: '新闻标题', width: 250},
                    {field: 'createTime', title: '发布时间', width: 150},
                    {field: 'browseCount', title: '浏览次数', width: 80},
                    {field: 'url', title: '访问地址', width: 300,
                        formatter: function (value, rowData, rowIndex) {
                            var url = '';
                            if(rowData.newsType == 0){
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
                            return '<a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:\'eu-icon-database_edit\'" onclick="showDialog(' + rowIndex + ');">编辑</a>&nbsp;<a class="easyui-linkbutton" href="javascript:void(0)" data-options="plain:true,iconCls:\'eu-icon-database_del\'" onclick="del(' + rowIndex + ');">删除</a>';
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
     * 新闻查询
     */
    function search() {
        $news_datagrid.datagrid('load', $.serializeObject($news_search_form));
    }

    // 删除新闻
    function del(indexRow) {
        var row = getRow(indexRow);
        $.messager.confirm('确认提示！', '您确定要删除此新闻?', function (r) {
            if (r) {
                $.ajax({
                    url: ctxAdmin + '/news/delete/'+row.id,
                    type: 'post',
                    traditional: true,
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 1) {
                            $news_datagrid.datagrid('load');	// reload the news data
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
        $news_form = $('#news_form').form({
            url: ctxAdmin + '/news/bsave',
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
                    $news_dialog.dialog('destroy');//销毁对话框
                    $news_datagrid.datagrid('reload');//重新加载列表数据
                    eu.showMsg(json.msg);//操作结果提示
                } else {
                    eu.showAlertMsg(json.msg, 'error');
                }
            }
        });
    }

    function getRow(rowIndex) {
        var rows = $news_datagrid.datagrid('getRows');
        return rows[rowIndex];
    }

    function showDialog(rowIndex) {
        var row;
        var inputUrl = ctxAdmin + "/news/input";
        if (rowIndex != undefined) {
            row = getRow(rowIndex);
            inputUrl = inputUrl + "?id=" + row.id;
        }

        //弹出对话窗口
        $news_dialog = $('<div/>').dialog({
            title: '新闻操作',
            top: 20,
            width: 960,
            height: 420,
            modal: false,
            maximizable: true,
            maximized:true,
            href: inputUrl,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'easyui-icon-save',
                    handler: function () {
                        $news_form.submit();
                    }
                },
                {
                    text: '关闭',
                    iconCls: 'easyui-icon-cancel',
                    handler: function () {
                        $news_dialog.dialog('destroy');
                    }
                }
            ],
            onClose: function () {
                $news_dialog.dialog('destroy');
            },
            onLoad: function () {
                formInit();
                if (row) {
                    $("input[name='newsName']").attr('readonly', 'readonly');
                    $("input[name='newsName']").css('background', '#eee');
                    $news_form.form('myLoad', row);
                    editor.ready(function() {
                        editor.setContent(row.newsContent);
                    });

                }else{
                    $("input[name='newsType']:eq(0)").attr("checked", 'checked');//状态 初始化值
                }
                changeRadio();

            }
        });

    }
</script>


