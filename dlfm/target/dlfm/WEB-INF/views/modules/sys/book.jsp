<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/meta.jsp" %>
<div class="easyui-layout" fit="true" style="margin: 0px;border: 0px;overflow: hidden;width:100%;height:100%;">
    <div data-options="region:'north',collapsed:false,split:false,border:false"
         style="width:100%; overflow-y: hidden;">
        <div class="search">
            <form id="book_search_form">
                <span class="eu-label">书籍名称:</span>
                <input type="text" id="sc_bookName" name="sc_bookName" class="easyui-validatebox textbox eu-input"
                       placeholder="请输入书籍名称..." onkeydown="if(event.keyCode==13)search()" maxLength="25"
                       style="width: 160px"/>
                &nbsp;<a class="easyui-linkbutton" href="#"
                         data-options="plain:true,iconCls:'easyui-icon-search',onClick:search">查询</a>&nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);"
                   data-options="plain:true,iconCls:'easyui-icon-no'"
                   onclick="javascript:$book_search_form.form('reset');">重置查询</a>
                &nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'eu-icon-add'"
                   onclick="javascript:showDialog();">添加书籍</a>
            </form>
        </div>
    </div>
    <%-- 中间部分 列表 --%>
    <div data-options="region:'center',split:false,border:false"
         style="padding: 0px; height: 100%;width:100%; overflow-y: hidden;">
        <table id="book_datagrid"></table>
    </div>
</div>


<script>
    var $book_datagrid;
    var $book_search_form;
    $(function () {
        $book_search_form = $('#book_search_form').form();
        //数据列表
        $book_datagrid = $('#book_datagrid').datagrid({
            url: ctxAdmin + '/book/${bookPeriod.id}_bookDatagrid',
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
                    {field: 'bookName', title: '书籍名称', width: 150},
                    {field: 'bookAuthor', title: '书籍作者', width: 100},
                    {field: 'createTime', title: '发布时间', width: 150},
                    {field: 'browseCount', title: '浏览次数', width: 80},
                    {
                        field: 'url', title: '访问地址', width: 300,
                        formatter: function (value, rowData, rowIndex) {
                            var url = '';
                            if (rowData.bookType == 0) {
                                url = rowData.browseUrl;
                            } else {
                                url = rowData.linkUrl;
                            }
                            return url;
                        }
                    },
                    {
                        field: 'periodName', title: '所属期刊', width: 200,
                        formatter: function (value, rowData, rowIndex) {
                            return '${bookPeriod.periodName}';
                        }
                    },
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
     * 用户查询
     */
    function search() {
        $book_datagrid.datagrid('load', $.serializeObject($book_search_form));
    }

    // 删除用户
    function del(indexRow) {
        var row = getRow(indexRow);
        $.messager.confirm('确认提示！', '您确定要删除此书籍?', function (r) {
            if (r) {
                $.ajax({
                    url: ctxAdmin + '/book/delete/' + row.id,
                    type: 'post',
                    traditional: true,
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 1) {
                            $book_datagrid.datagrid('load');	// reload the book data
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
        console.log("初始化数据表单.")
        $book_form = $('#book_form').form({
            url: ctxAdmin + '/book/bsave/${bookPeriod.id}',
            onSubmit: function (param) {
                $.messager.progress({
                    title: '提示信息！',
                    text: '数据处理中，请稍后....'
                });
                var isValid = $(this).form('validate');
                if (!isValid) {
                    $.messager.progress('close');
                }
                console.log("验证结果:"+isValid);
                return isValid;
            },
            success: function (data) {
                $.messager.progress('close');
                var json = $.parseJSON(data);
                if (json.code == 1) {
                    $book_dialog.dialog('destroy');//销毁对话框
                    $book_datagrid.datagrid('reload');//重新加载列表数据
                    eu.showMsg(json.msg);//操作结果提示
                } else {
                    eu.showAlertMsg(json.msg, 'error');
                }
            }
        });
    }

    function getRow(rowIndex) {
        var rows = $book_datagrid.datagrid('getRows');
        return rows[rowIndex];
    }

    function showDialog(rowIndex) {
        var row;
        var inputUrl = ctxAdmin + "/book/input";
        if (rowIndex != undefined) {
            row = getRow(rowIndex);
            inputUrl = inputUrl + "?id=" + row.id;
        }

        //弹出对话窗口
        $book_dialog = $('<div/>').dialog({
            title: '书籍操作',
            top: 20,
            width: 540,
            height: 420,
            modal: true,
            maximizable: true,
            maximized: true,
            href: inputUrl,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'easyui-icon-save',
                    handler: function () {
                        console.log("点击了保存.");
                        $book_form.submit();
                    }
                },
                {
                    text: '关闭',
                    iconCls: 'easyui-icon-cancel',
                    handler: function () {
                        $book_dialog.dialog('destroy');
                    }
                }
            ],
            onClose: function () {
                $book_dialog.dialog('destroy');
            },
            onLoad: function () {
                formInit();
                if (row) {
                    $("input[name='bookName']").attr('readonly', 'readonly');
                    $("input[name='bookName']").css('background', '#eee');
                    $book_form.form('myLoad', row);
                    editor.ready(function () {
                        editor.setContent(row.bookDesc);
                    });

                } else {
                   // $("input[name='istj']:eq(0)").attr("checked", 'checked');//状态 初始化值
                    $("input[name='bookType']:eq(0)").attr("checked", 'checked');//状态 初始化值
                }
                changeRadio();

            }
        });

    }
</script>


