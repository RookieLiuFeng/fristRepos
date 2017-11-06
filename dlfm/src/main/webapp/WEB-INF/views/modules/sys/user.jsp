<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/meta.jsp" %>

<script type="text/javascript">
    $(function() {
        loadUserType();
    });

    function loadUserType(){
        $('#userType').combobox({
            url: '${ctxAdmin}/user/userTypeCombobox?selectType=all',
            width: 100,
            editable:false,
            value:''
        });
    }

</script>
<%-- 隐藏iframe --%>
<iframe id="user_temp_iframe" style="display: none;"></iframe>
<div class="easyui-layout" fit="true" style="margin: 0px;border: 0px;overflow: hidden;width:100%;height:100%;">
    <div data-options="region:'north',collapsed:false,split:false,border:false"
         style="width:100%; overflow-y: hidden;">
        <div class="search">
            <form id="user_search_form">
                <span class="eu-label">姓名:</span>
                <input type="text" id="realname" name="realname" class="easyui-validatebox textbox eu-input"
                       placeholder="请输入用户姓名..." onkeydown="if(event.keyCode==13)search()" maxLength="25"
                       style="width: 160px"/>
                &nbsp;<span class="eu-label">类型:</span><input id="userType" name="userType" style="height: 28px;" />
                <a class="easyui-linkbutton" href="#"
                         data-options="plain:true,iconCls:'easyui-icon-search',onClick:search">查询</a>&nbsp;
                <a class="easyui-linkbutton" href="javascript:void(0);"
                   data-options="plain:true,iconCls:'easyui-icon-no'"
                   onclick="javascript:$user_search_form.form('reset');">重置查询</a>

                <a class="easyui-linkbutton" href="javascript:void(0);" data-options="plain:true,iconCls:'eu-icon-add'"
                   onclick="addUser();">添加用户</a>

                <a class="easyui-linkbutton" href="http://tgwx.share-win.info/files/excels/DlfmModel.xls"
                   data-options="plain:true,iconCls:'eu-icon-excel_white'">下载模版</a>

                <a class="easyui-linkbutton" href="javascript:void(0);"
                   data-options="plain:true,iconCls:'eu-icon-database_add'"
                   onclick="importExcel();">批量导入</a>
                <a class="easyui-linkbutton" href="javascript:void(0);"
                   data-options="plain:true,iconCls:'eu-icon-database_go'"
                   onclick="exportExcel();">Excel导出</a>

                <a class="easyui-linkbutton" href="javascript:void(0);"
                   data-options="plain:true,iconCls:'eu-icon-database_go'"
                   onclick="deleteUsers();">批量删除</a>

            </form>
        </div>
    </div>
    <%-- 中间部分 列表 --%>
    <div data-options="region:'center',split:false,border:false"
         style="padding: 0px; height: 100%;width:100%; overflow-y: hidden;">
        <table id="user_datagrid"></table>
    </div>
</div>


<script type="text/javascript" charset="UTF-8">
    var $user_datagrid;
    var $user_search_form;
    $(function () {
        $user_search_form = $('#user_search_form').form();
        //数据列表
        $user_datagrid = $('#user_datagrid').datagrid({
            url: ctxAdmin + '/user/userDatagrid',
            fit: true,
            pagination: true,//底部分页
            rownumbers: true,//显示行数
            fitColumns: true,//自适应列宽
            striped: true,//显示条纹
            pageSize: 20,//每页记录数
            idField: 'id',
            frozen: true,
            collapsible: true,
            pageList: [10, 20, 50, 100, 1000, 99999],
            frozenColumns: [
                [
                    {field: 'ck', checkbox: true},
                    {field: 'id', title: '主键', hidden: true, width: 80} ,
                    {field: 'realname', title: '姓名', width: 80},
                    {field: 'sexView', title: '性别', width: 60},
                    {field: 'loginName', title: '登录名', width: 120},
                    {field: 'enterSchoolTime', title: '入学时间', width: 200},
                    {field: 'guardianName', title: '监护人', width: 120},
                    {field: 'telphone', title: '监护人电话', width: 150},
                    {field: 'idcardno', title: '身份证', width: 200},
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
                $(this).datagrid('unselectAll');
                $(this).datagrid('selectRow', rowIndex);

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
        $user_datagrid.datagrid('load', $.serializeObject($user_search_form));
    }

    /**
     * 用户添加
     */
    function addUser() {
        showDialog();
    }

    // 删除用户
    function del(indexRow) {
        var row = getRow(indexRow);
        $.messager.confirm('确认提示！', '您确定要删除此用户?', function (r) {
            if (r) {
                $.ajax({
                    url: ctxAdmin + '/user/_delete/' + row.id,
                    type: 'post',
                    traditional: true,
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 1) {
                            $user_datagrid.datagrid('load');	// reload the user data
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
        $user_form = $('#user_form').form({
            url: ctxAdmin + '/user/save',
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
                    $user_dialog.dialog('destroy');//销毁对话框
                    $user_datagrid.datagrid('reload');//重新加载列表数据
                    eu.showMsg(json.msg);//操作结果提示
                } else {
                    eu.showAlertMsg(json.msg, 'error');
                }
            },
            onLoadError: function () {
                console.log("表单初始化失败!");
            }
        });
    }

    function getRow(rowIndex) {
        var rows = $user_datagrid.datagrid('getRows');
        return rows[rowIndex];
    }

    function showDialog(rowIndex) {
        var row;
        var inputUrl = ctxAdmin + "/user/input";
        if (rowIndex != undefined) {
            row = getRow(rowIndex);
            inputUrl = inputUrl + "?id=" + row.id;
        }

        //弹出对话窗口
        $user_dialog = $('<div/>').dialog({
            title: '用户操作',
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
                        $user_form.submit();
                    }
                },
                {
                    text: '关闭',
                    iconCls: 'easyui-icon-cancel',
                    handler: function () {
                        $user_dialog.dialog('destroy');
                    }
                }
            ],
            onClose: function () {
                $user_dialog.dialog('destroy');
            },
            onLoad: function () {
                formInit();
                if (row) {
                    /*$("input[name='loginName']").attr('readonly', 'readonly');
                    $("input[name='loginName']").css('background', '#eee');*/
                    $('#password_div').css('display', 'none');
                    $('#repassword_div').css('display', 'none');
                    $('#password_div input').removeAttr('class');
                    $('#repassword_div input').removeAttr('class');
                    $user_form.form('load', row);
                }

            }
        });

    }


    function importFormInit() {
        $user_import_form = $('#user_import_form').form({
            url: ctxAdmin + '/user/importExcel',
            onSubmit: function (param) {
                $.messager.progress({
                    title: '提示信息！',
                    text: '数据处理中，请稍后....'
                });
                return $(this).form('validate');
            },
            success: function (data) {
                $.messager.progress('close');
                var json = $.parseJSON(data);
                if (json.code == 1) {
                    $user_import_dialog.dialog('destroy');//销毁对话框
                    $user_datagrid.datagrid('reload');//重新加载列表数据
                    eu.showMsg(json.msg);//操作结果提示
                } else {
                    eu.showAlertMsg(json.msg, 'error');
                }
            }
        });
    }

    //导入
    function importExcel() {
        $user_import_dialog = $('<div/>').dialog({//基于中心面板
            title: 'Excel导入',
            top: 20,
            height: 200,
            width: 500,
            modal: true,
            maximizable: true,
            href: ctxAdmin + '/user/import',
            buttons: [
                {
                    text: '保存',
                    iconCls: 'easyui-icon-save',
                    handler: function () {
                        $user_import_form.submit();
                    }
                },
                {
                    text: '关闭',
                    iconCls: 'easyui-icon-cancel',
                    handler: function () {
                        $user_import_dialog.dialog('destroy');
                    }
                }
            ],
            onClose: function () {
                $user_import_dialog.dialog('destroy');
            },
            onLoad: function () {
                importFormInit();
            }
        });
    }

    //导出Excel
    function exportExcel() {
        console.log("导出用户");
        $('#user_temp_iframe').attr('src', ctxAdmin + '/user/exportExcel');
    }

    //删除用户
    function deleteUsers() {
        var rows = $user_datagrid.datagrid('getSelections');
        if (rows.length > 0) {
            $.messager.confirm('确认提示！', '您确定要删除选中的所有行?', function (r) {
                if (r) {
                    var ids = new Array();
                    $.each(rows, function (i, row) {
                        console.log("删除的Id:"+row.id);
                        ids[i] = row.id;
                    });
                    $.ajax({
                        url: ctxAdmin + '/user/deleteUsers',
                        type: 'post',
                        data: {userids: ids},
                        traditional: true,
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == 1) {
                                $user_datagrid.datagrid('load');	// reload the user data
                                eu.showMsg(data.msg);//操作结果提示
                            } else {
                                eu.showAlertMsg(data.msg, 'error');
                            }
                        }
                    });
                }
            });
        } else {
            eu.showMsg("您未选择任何操作对象，请选择一行数据！");
        }
    }
</script>


