//Easyui 扩展
/**
 * 定义全局对象，easyui的扩展命名空间.
 */
var eu = $.extend({}, eu);
eu.showMsg = function (msg) {
    $.messager.show({title: '提示信息！', msg: msg, timeout: 3000, showType: 'slide'})
};
eu.showAlertMsg = function (msgString, msgType) {
    $.messager.alert('提示信息！', msgString, msgType)
};
eu.showProMsg = function (msg, time) {
    $.messager.progress({title: '提示信息！', msg: msg});
    setTimeout(function () {
        $.messager.progress('close')
    }, time)
};
eu.showTopCenterMsg = function (msg) {
    $.messager.show({
        title: '<span style="color: red;">提示信息！</span>',
        msg: msg,
        showType: 'show',
        style: {right: '', top: document.body.scrollTop + document.documentElement.scrollTop, bottom: ''}
    })
};

eu.openLoading = function (msg) {
    $("<div class=\"datagrid-mask\"></div>").css({
        display: "block",
        width: "100%",
        height: $(window).height()
    }).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在加载，请稍候...").appendTo("body").css({
        display: "block",
        left: ($(document.body).outerWidth(true) - 190) / 2,
        top: ($(window).height() - 45) / 2
    });
};
eu.closeLoading = function () {
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
};

eu.addTab = function (tabs, title, url, closeAble, iconCls, tools, refresh) {
    var closable = true;
    if (undefined != typeof closeAble) {
        closable = closeAble
    }
    if (typeof tabs == 'string') {
        tabs = $('#' + tabs).tabs()
    }
    if (iconCls == 'undefined') {
        iconCls = ''
    }
    var params = {
        tab: {title: title, closable: closable, iconCls: iconCls, cache: true, tools: tools},
        iframe: {src: url, id: title},
        which: title
    };
    if (!tabs.tabs('exists', title)) {
        tabs.tabs('addIframeTab', params)
    } else {
        tabs.tabs('select', title);
        if (refresh) {
            tabs.tabs('updateIframeTab', params)
        }
    }
};
eu.datagridHeaderCenter = function () {
    $(".datagrid-header div").css('textAlign', 'center');
};
eu.panelCenter = function () {
    $(".panel-title").css('text-align', 'center')
};
eu.changeTheme = function (themeName) {
    var $easyuiTheme = $('#easyuiTheme');
    var url = $easyuiTheme.attr('href');
    var href = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName + '/easyui.css';
    $easyuiTheme.attr('href', href);
    var $iframe = $('iframe');
    if ($iframe.length > 0) {
        for (var i = 0; i < $iframe.length; i += 1) {
            var ifr = $iframe[i];
            $(ifr).contents().find('#easyuiTheme').attr('href', href)
        }
    }
    $.cookie('easyuiThemeName', themeName, {expires: 7})
};
eu.unSelected = function (id, rowIndex, rowData) {
    var selected = $('#' + id).datagrid('getSelections');
    if (jQuery.inArray(rowData, selected) != -1) {
        $('#' + id).datagrid('unselectRow', rowIndex)
    } else {
        $('#' + id).datagrid('selectRow', rowIndex)
    }
};
eu.destroyMessage = function () {
    $(".messager-body").window('destroy')
};
$.extend($.messager, {
    showBySite: function (options, param) {
        var site = $.extend({
            left: "",
            top: "",
            right: 0,
            bottom: -document.body.scrollTop - document.documentElement.scrollTop
        }, param || {});
        var win = $("body > div .messager-body");
        if (win.length <= 0) {
            $.messager.show(options)
        }
        win = $("body > div .messager-body");
        win.window("window").css({
            left: site.left,
            top: site.top,
            right: site.right,
            zIndex: $.fn.window.defaults.zIndex++,
            bottom: site.bottom
        })
    }
});
$.extend($.fn.combo.methods, {
    disableTextbox: function (jq, param) {
        return jq.each(function () {
            param = param || {};
            var textbox = $(this).combo("textbox");
            var that = this;
            var panel = $(this).combo("panel");
            var data = $(this).data('combo');
            if (param.stopArrowFocus) {
                data.stopArrowFocus = param.stopArrowFocus;
                var arrowbox = $.data(this, 'combo').combo.find('span.combo-arrow');
                arrowbox.unbind('click.combo').bind('click.combo', function () {
                    if (panel.is(":visible")) {
                        $(that).combo('hidePanel')
                    } else {
                        $("div.combo-panel").panel("close");
                        $(that).combo('showPanel')
                    }
                });
                textbox.unbind('mousedown.mycombo').bind('mousedown.mycombo', function (e) {
                    e.preventDefault()
                })
            }
            textbox.prop(param.stoptype ? param.stoptype : 'disabled', true);
            data.stoptype = param.stoptype ? param.stoptype : 'disabled'
        })
    },
    enableTextbox: function (jq) {
        return jq.each(function () {
            var textbox = $(this).combo("textbox");
            var data = $(this).data('combo');
            if (data.stopArrowFocus) {
                var that = this;
                var panel = $(this).combo("panel");
                var arrowbox = $.data(this, 'combo').combo.find('span.combo-arrow');
                arrowbox.unbind('click.combo').bind('click.combo', function () {
                    if (panel.is(":visible")) {
                        $(that).combo('hidePanel')
                    } else {
                        $("div.combo-panel").panel("close");
                        $(that).combo('showPanel')
                    }
                    textbox.focus()
                });
                textbox.unbind('mousedown.mycombo');
                data.stopArrowFocus = null
            }
            textbox.prop(data.stoptype, false);
            data.stoptype = null
        })
    }
});
$.extend($.fn.form.methods, {
    getData: function (jq, params) {
        var formArray = jq.serializeArray();
        var oRet = {};
        for (var i in formArray) {
            if (typeof(oRet[formArray[i].name]) == 'undefined') {
                if (params) {
                    oRet[formArray[i].name] = (formArray[i].value == "true" || formArray[i].value == "false") ? formArray[i].value == "true" : formArray[i].value
                } else {
                    oRet[formArray[i].name] = formArray[i].value
                }
            } else {
                if (params) {
                    oRet[formArray[i].name] = (formArray[i].value == "true" || formArray[i].value == "false") ? formArray[i].value == "true" : formArray[i].value
                } else {
                    oRet[formArray[i].name] += "," + formArray[i].value
                }
            }
        }
        return oRet
    },
    disable: function (jq, isDisabled) {
        var formId = jq.attr('id');
        var attr = "disable";
        var attr_r = true;
        if (!isDisabled) {
            attr = "enable";
            attr_r = false
        }
        $("form[id='" + formId + "'] :text").attr("disabled", isDisabled);
        $("form[id='" + formId + "'] textarea").attr("disabled", isDisabled);
        $("form[id='" + formId + "'] select").attr("disabled", isDisabled);
        $("form[id='" + formId + "'] :radio").attr("disabled", isDisabled);
        $("form[id='" + formId + "'] :checkbox").attr("disabled", isDisabled);
        $("#" + formId + " input[class='combobox-f combo-f']").each(function () {
            if (this.id) {
                $("#" + this.id).combobox(attr);
                $("#" + this.id).combobox('readonly', attr_r)
            }
        });
        $("#" + formId + " select[class='combobox-f combo-f']").each(function () {
            if (this.id) {
                $("#" + this.id).combobox(attr);
                $("#" + this.id).combobox('readonly', attr_r)
            }
        });
        $("#" + formId + " input[class='datebox-f combo-f']").each(function () {
            if (this.id) {
                $("#" + this.id).datebox(attr);
                $("#" + this.id).datebox('readonly', attr_r)
            }
        })
    }
});
$.extend($.fn.tabs.methods, {
    loadTabIframe: function (jq, params) {
        return jq.each(function () {
            var $tab = $(this).tabs('getTab', params.which);
            if ($tab == null) {
                return
            }
            var $tabBody = $tab.panel('body');
            var $frame = $('iframe', $tabBody);
            if ($frame.length > 0) {
                $frame[0].contentWindow.document.write('');
                $frame[0].contentWindow.close();
                $frame.remove();
                if ($.support.leadingWhitespace) {
                    try {
                        CollectGarbage()
                    } catch (e) {
                    }
                }
            }
            $tabBody.html('');
            $tabBody.css({'overflow': 'hidden', 'position': 'relative'});
            var $mask = $('<div style="position:absolute;z-index:2;width:100%;height:100%;background:#ccc;z-index:1000;opacity:0.3;filter:alpha(opacity=30);"><div>').appendTo($tabBody);
            var $maskMessage = $('<div class="mask-message" style="z-index:3;width:auto;height:16px;line-height:16px;position:absolute;top:50%;left:50%;margin-top:-20px;margin-left:-92px;border:2px solid #d4d4d4;padding: 12px 5px 10px 30px;background: #ffffff url(\'' + ctxStatic + '/js/jquery-easyui/themes/default/images/loading.gif\') no-repeat scroll 5px center;">' + (params.iframe.message || '页面加载中...') + '</div>').appendTo($tabBody);
            var $containterMask = $('<div style="position:absolute;width:100%;height:100%;z-index:1;background:#fff;"></div>').appendTo($tabBody);
            var $containter = $('<div style="position:absolute;width:100%;height:100%;z-index:0;"></div>').appendTo($tabBody);
            var iframe = document.createElement("iframe");
            iframe.src = params.iframe.src;
            iframe.id = params.iframe.id;
            iframe.frameBorder = params.iframe.frameBorder || 0;
            iframe.height = params.iframe.height || '100%';
            iframe.width = params.iframe.width || '100%';
            var finist = function () {
                $([$mask[0], $maskMessage[0]]).fadeOut(params.iframe.delay || 'fast', function () {
                    $(this).remove();
                    if ($(this).hasClass('mask-message')) {
                        $containterMask.fadeOut(params.iframe.delay || 'fast', function () {
                            $(this).remove()
                        })
                    }
                })
            };
            if (iframe.attachEvent) {
                iframe.attachEvent("onload", finist)
            } else {
                iframe.onload = finist
            }
            $containter[0].appendChild(iframe)
        })
    },
    addIframeTab: function (jq, params) {
        return jq.each(function () {
            if (params.tab.href) {
                delete params.tab.href
            }
            $(this).tabs('add', params.tab);
            $(this).tabs('loadTabIframe', {'which': params.tab.title, 'iframe': params.iframe})
        })
    },
    updateIframeTab: function (jq, params) {
        return jq.each(function () {
            params.iframe = params.iframe || {};
            if (!params.iframe.src) {
                var $tab = $(this).tabs('getTab', params.which);
                if ($tab == null) {
                    return
                }
                var $tabBody = $tab.panel('body');
                var $iframe = $tabBody.find('iframe');
                if ($iframe.length === 0) {
                    return
                }
                $.extend(params.iframe, {'src': $iframe.attr('src')})
            }
            $(this).tabs('loadTabIframe', params)
        })
    },
    setTabTitle: function (jq, opts) {
        return jq.each(function () {
            var tab = opts.tab;
            var options = tab.panel("options");
            var tab = options.tab;
            options.title = opts.title;
            var title = tab.find("span.tabs-title");
            title.html(opts.title)
        })
    }
});
$.fn.panel.defaults.loadingMessage = '加载中....';
$.fn.datagrid.defaults.loadMsg = '加载中....';
var removeEasyuiTipFunction = function () {
    window.setTimeout(function () {
        $('div.validatebox-tip').remove()
    }, 0)
};
$.fn.panel.defaults.onClose = removeEasyuiTipFunction;
$.fn.window.defaults.onClose = removeEasyuiTipFunction;
$.fn.dialog.defaults.onClose = removeEasyuiTipFunction;
var createGridHeaderContextMenu = function (e, field) {
    e.preventDefault();
    var grid = $(this);
    var headerContextMenu = this.headerContextMenu;
    if (!headerContextMenu) {
        var tmenu = $('<div style="width:100px;"></div>').appendTo('body');
        var fields = grid.datagrid('getColumnFields');
        for (var i = 0; i < fields.length; i += 1) {
            var fildOption = grid.datagrid('getColumnOption', fields[i]);
            var title = fildOption.title;
            var field = fildOption.field;
            if (field == 'ck') {
                title = "全选"
            }
            if (!fildOption.hidden) {
                $('<div iconCls="easyui-icon-ok" field="' + fields[i] + '"/>').html(title).appendTo(tmenu)
            } else {
                $('<div iconCls="easyui--icon-empty" field="' + fields[i] + '"/>').html(title).appendTo(tmenu)
            }
        }
        headerContextMenu = this.headerContextMenu = tmenu.menu({
            onClick: function (item) {
                var field = $(item.target).attr('field');
                if (item.iconCls == 'easyui-icon-ok') {
                    if (fields.length > 1) {
                        grid.datagrid('hideColumn', field);
                        $(this).menu('setIcon', {target: item.target, iconCls: 'easyui-icon-empty'})
                    }
                } else {
                    grid.datagrid('showColumn', field);
                    $(this).menu('setIcon', {target: item.target, iconCls: 'easyui-icon-ok'})
                }
            }
        })
    }
    headerContextMenu.menu('show', {left: e.pageX, top: e.pageY})
};
$.fn.datagrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
$.fn.treegrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
var easyuiPanelOnMove = function (left, top) {
    var l = left;
    var t = top;
    if (l < 1) {
        l = 1
    }
    if (t < 1) {
        t = 1
    }
    var width = parseInt($(this).parent().css('width')) + 14;
    var height = parseInt($(this).parent().css('height')) + 14;
    var right = l + width;
    var buttom = t + height;
    var browserWidth = $(document).width();
    var browserHeight = $(document).height();
    if (right > browserWidth) {
        l = browserWidth - width
    }
    if (buttom > browserHeight) {
        t = browserHeight - height
    }
    $(this).parent().css({left: l, top: t})
};
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onBeforeDestroy = function () {
    var frame = $('iframe', this);
    try {
        if (frame.length > 0) {
            frame[0].contentWindow.document.write('');
            frame[0].contentWindow.close();
            frame.remove();
            if ($.support.leadingWhitespace) {
                try {
                    CollectGarbage()
                } catch (e) {
                }
            }
        }
    } catch (e) {
    }
};
$.extend($.fn.datagrid.methods, {
    addEditor: function (jq, param) {
        if (param instanceof Array) {
            $.each(param, function (index, item) {
                var e = $(jq).datagrid('getColumnOption', item.field);
                e.editor = item.editor
            })
        } else {
            var e = $(jq).datagrid('getColumnOption', param.field);
            e.editor = param.editor
        }
    },
    removeEditor: function (jq, param) {
        if (param instanceof Array) {
            $.each(param, function (index, item) {
                var e = $(jq).datagrid('getColumnOption', item);
                e.editor = {}
            })
        } else {
            var e = $(jq).datagrid('getColumnOption', param);
            e.editor = {}
        }
    },
    setColumnTitle: function (jq, option) {
        if (option.field) {
            return jq.each(function () {
                var $panel = $(this).datagrid("getPanel");
                var $field = $('td[field=' + option.field + ']', $panel);
                if ($field.length) {
                    var $span = $("span", $field).eq(0);
                    $span.html(option.text)
                }
            })
        }
        return jq
    },
    columnMoving: function (jq) {
        return jq.each(function () {
            var target = this;
            var cells = $(this).datagrid('getPanel').find('div.datagrid-header td[field]');
            cells.draggable({
                revert: true,
                cursor: 'pointer',
                edge: 5,
                proxy: function (source) {
                    var p = $('<div class="tree-node-proxy tree-dnd-no" style="position:absolute;border:1px solid #ff0000"/>').appendTo('body');
                    p.html($(source).text());
                    p.hide();
                    return p
                },
                onBeforeDrag: function (e) {
                    e.data.startLeft = $(this).offset().left;
                    e.data.startTop = $(this).offset().top
                },
                onStartDrag: function () {
                    $(this).draggable('proxy').css({left: -10000, top: -10000})
                },
                onDrag: function (e) {
                    $(this).draggable('proxy').show().css({left: e.pageX + 15, top: e.pageY + 15});
                    return false
                }
            }).droppable({
                accept: 'td[field]',
                onDragOver: function (e, source) {
                    $(source).draggable('proxy').removeClass('tree-dnd-no').addClass('tree-dnd-yes');
                    $(this).css('border-left', '1px solid #ff0000')
                },
                onDragLeave: function (e, source) {
                    $(source).draggable('proxy').removeClass('tree-dnd-yes').addClass('tree-dnd-no');
                    $(this).css('border-left', 0)
                },
                onDrop: function (e, source) {
                    $(this).css('border-left', 0);
                    var fromField = $(source).attr('field');
                    var toField = $(this).attr('field');
                    setTimeout(function () {
                        moveField(fromField, toField);
                        $(target).datagrid();
                        $(target).datagrid('columnMoving')
                    }, 0)
                }
            });

            function moveField(from, to) {
                var columns = $(target).datagrid('options').columns;
                var cc = columns[0];
                var c = _remove(from);
                if (c) {
                    _insert(to, c)
                }
                $(target).datagrid('reload');

                function _remove(field) {
                    for (var i = 0; i < cc.length; i += 1) {
                        if (cc[i].field == field) {
                            var c = cc[i];
                            cc.splice(i, 1);
                            return c
                        }
                    }
                    return null
                }

                function _insert(field, c) {
                    var newcc = [];
                    for (var i = 0; i < cc.length; i += 1) {
                        if (cc[i].field == field) {
                            newcc.push(c)
                        }
                        newcc.push(cc[i])
                    }
                    columns[0] = newcc
                }
            }
        })
    },
    fixRownumber: function (jq) {
        return jq.each(function () {
            var panel = $(this).datagrid("getPanel");
            var clone = $(".datagrid-cell-rownumber", panel).last().clone();
            clone.css({"position": "absolute", left: -1000}).appendTo("body");
            var width = clone.width("auto").width();
            if (width > 25) {
                $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).width(width + 5);
                $(this).datagrid("resize");
                clone.remove();
                clone = null
            } else {
                $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).removeAttr("style")
            }
        })
    },
    keyCtr: function (jq) {
        return jq.each(function () {
            var grid = $(this);
            grid.datagrid('getPanel').panel('panel').attr('tabindex', 1).bind('keydown', function (e) {
                switch (e.keyCode) {
                    case 38:
                        var selected = grid.datagrid('getSelected');
                        if (selected) {
                            var index = grid.datagrid('getRowIndex', selected);
                            grid.datagrid('selectRow', index - 1)
                        } else {
                            var rows = grid.datagrid('getRows');
                            grid.datagrid('selectRow', rows.length - 1)
                        }
                        break;
                    case 40:
                        var selected = grid.datagrid('getSelected');
                        if (selected) {
                            var index = grid.datagrid('getRowIndex', selected);
                            grid.datagrid('selectRow', index + 1)
                        } else {
                            grid.datagrid('selectRow', 0)
                        }
                        break
                }
            })
        })
    }
});
var gridautoMergeCellsOptions = {
    autoMergeCells: function (jq, fields) {
        return jq.each(function () {
            var target = $(this);
            if (!fields) {
                fields = target.datagrid("getColumnFields")
            }
            var rows = target.datagrid("getRows");
            var i = 0,
                j = 0,
                temp = {};
            for (i; i < rows.length; i += 1) {
                var row = rows[i];
                j = 0;
                for (j; j < fields.length; j += 1) {
                    var field = fields[j];
                    var tf = temp[field];
                    if (!tf) {
                        tf = temp[field] = {};
                        tf[row[field]] = [i]
                    } else {
                        var tfv = tf[row[field]];
                        if (tfv) {
                            tfv.push(i)
                        } else {
                            tfv = tf[row[field]] = [i]
                        }
                    }
                }
            }
            $.each(temp, function (field, colunm) {
                $.each(colunm, function () {
                    var group = this;
                    if (group.length > 1) {
                        var before, after, megerIndex = group[0];
                        for (var i = 0; i < group.length; i += 1) {
                            before = group[i];
                            after = group[i + 1];
                            if (after && (after - before) == 1) {
                                continue
                            }
                            var rowspan = before - megerIndex + 1;
                            if (rowspan > 1) {
                                target.datagrid('mergeCells', {index: megerIndex, field: field, rowspan: rowspan})
                            }
                            if (after && (after - before) != 1) {
                                megerIndex = after
                            }
                        }
                    }
                })
            })
        })
    }
};
var gridTooltipOptions = {
    showTooltip: function (jq, params) {
        if (!params) {
            params = {}
        }
        var defaultParams = {onlyShowNowrap: true};
        params = $.extend(params, defaultParams);

        function showTip(showParams, td, e, dg) {
            if ($(td).text() == "") {
                return
            }
            var options = dg.data('datagrid');
            showParams.content = '<div class="tipcontent">' + showParams.content + '</div>';
            $(td).tooltip({
                content: showParams.content,
                trackMouse: true,
                position: params.position,
                onHide: function () {
                    $(this).tooltip('destroy')
                },
                onUpdate: function (p) {
                    var tip = $(this).tooltip('tip');
                    if (parseInt(tip.css('width')) > 500) {
                        tip.css('width', 500)
                    }
                },
                onShow: function () {
                    var tip = $(this).tooltip('tip');
                    if (showParams.tipStyler) {
                        tip.css(showParams.tipStyler)
                    }
                    if (showParams.contentStyler) {
                        tip.find('div.tipcontent').css(showParams.contentStyler)
                    }
                    if (showParams.attachToMouse) {
                        fixPosition(tip, params.position, options)
                    }
                }
            }).tooltip('show')
        };

        function bindEvent(delegateEle, td, grid) {
            var options = grid.data('datagrid');
            $(delegateEle).undelegate(td, 'mouseover').undelegate(td, 'mouseout').undelegate(td, 'mousemove').delegate(td, {
                'mouseover': function (e) {
                    if ($(this).attr('field') === undefined) {
                        return
                    }
                    options.factContent = $(this).find('>div').clone().css({
                        'margin-left': '-5000px',
                        'width': 'auto',
                        'display': 'inline',
                        'position': 'absolute'
                    }).appendTo('body');
                    var factContentWidth = options.factContent.width();
                    params.content = $(this).find('>div').clone().css({'width': 'auto'}).html();
                    if (params.onlyShowNowrap && params.fields === undefined) {
                        if (factContentWidth > $(this).width()) {
                            showTip(params, this, e, grid)
                        }
                    } else {
                        showTip(params, this, e, grid)
                    }
                },
                'mouseout': function (e) {
                    if (options.factContent) {
                        options.factContent.remove();
                        options.factContent = null
                    }
                }
            })
        };
        return jq.each(function () {
            var grid = $(this);
            var options = $(this).data('datagrid');
            if (!options.tooltip) {
                var panel = grid.datagrid('getPanel').panel('panel');
                panel.find('.datagrid-body').each(function () {
                    var delegateEle = $(this).find('> div.datagrid-body-inner').length ? $(this).find('> div.datagrid-body-inner')[0] : this;
                    if (params.fields && typeof params.fields == 'object') {
                        $.each(params.fields, function () {
                            var field = this;
                            bindEvent(delegateEle, 'td[field=' + field + ']', grid)
                        })
                    } else {
                        bindEvent(delegateEle, 'td', grid)
                    }
                })
            }
        })
    },
    closeTooltip: function (jq) {
        return jq.each(function () {
            var data = $(this).data('datagrid');
            if (data.factContent) {
                data.factContent.remove();
                data.factContent = null
            }
            var panel = $(this).datagrid('getPanel').panel('panel');
            panel.find('.datagrid-body').undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove')
        })
    }
};
$.extend($.fn.datagrid.methods, gridTooltipOptions, gridautoMergeCellsOptions);
$.extend($.fn.treegrid.methods, gridTooltipOptions);
$.extend($.fn.tree.methods, {
    getCheckedExt: function (jq) {
        var checked = $(jq).tree("getChecked");
        var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
        $.each(checkbox2, function () {
            var node = $.extend({}, $.data(this, "tree-node"), {target: this});
            checked.push(node)
        });
        return checked
    },
    getSolidExt: function (jq) {
        var checked = [];
        var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
        $.each(checkbox2, function () {
            var node = $.extend({}, $.data(this, "tree-node"), {target: this});
            checked.push(node)
        });
        return checked
    },
    getLevel: function (jq, target) {
        var l = $(target).parentsUntil("ul.tree", "ul");
        return l.length + 1
    },
    setQueryParams: function (jq, params) {
        return jq.each(function () {
            $(this).tree("options").queryParams = params
        })
    },
    unSelect: function (jq, target) {
        return jq.each(function () {
            $(target).removeClass("tree-node-selected")
        })
    }
});
eu.myCascadeCheck = function (tree, node) {
    if (tree == 'undefined' || node == 'undefined') {
        return
    }
    if (node.checked) {
        node.checked = false;
        tree.tree('uncheck', node.target)
    } else {
        node.checked = true;
        tree.tree('check', node.target)
    }
    var tempNode;
    tempNode = node;
    while (tempNode) {
        var parentNode = tree.tree('getParent', tempNode.target);
        tempNode = parentNode;
        if (tempNode) {
            tree.tree('check', tempNode.target)
        }
    }
    tempNode = node;
    if (tempNode) {
        var childNodes = tree.tree('getChildren', tempNode.target);
        var childNode;
        for (var i = 0; i <= childNodes.length; i += 1) {
            childNode = childNodes[i];
            if (childNode) {
                if (tempNode.checked) {
                    tree.tree('check', childNode.target)
                } else {
                    tree.tree('uncheck', childNode.target)
                }
            }
        }
    }
};
$.fn.tree.defaults.loadFilter = function (data, parent) {
    var opt = $(this).data().tree.options;
    var idFiled, textFiled, parentField;
    if (opt.parentField) {
        idFiled = opt.idFiled || 'id';
        textFiled = opt.textFiled || 'text';
        parentField = opt.parentField;
        var i, l, treeData = [],
            tmpMap = [];
        for (i = 0, l = data.length; i < l; i += 1) {
            tmpMap[data[i][idFiled]] = data[i]
        }
        for (i = 0, l = data.length; i < l; i += 1) {
            if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
                if (!tmpMap[data[i][parentField]]['children']) {
                    tmpMap[data[i][parentField]]['children'] = []
                }
                data[i]['text'] = data[i][textFiled];
                tmpMap[data[i][parentField]]['children'].push(data[i])
            } else {
                data[i]['text'] = data[i][textFiled];
                treeData.push(data[i])
            }
        }
        return treeData
    }
    return data
};
$.fn.treegrid.defaults.loadFilter = function (data, parentId) {
    var opt = $(this).data().treegrid.options;
    var idFiled, textFiled, parentField;
    if (opt.parentField) {
        idFiled = opt.idFiled || 'id';
        textFiled = opt.textFiled || 'text';
        parentField = opt.parentField;
        var i, l, treeData = [],
            tmpMap = [];
        for (i = 0, l = data.length; i < l; i += 1) {
            tmpMap[data[i][idFiled]] = data[i]
        }
        for (i = 0, l = data.length; i < l; i += 1) {
            if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
                if (!tmpMap[data[i][parentField]]['children']) {
                    tmpMap[data[i][parentField]]['children'] = []
                }
                data[i]['text'] = data[i][textFiled];
                tmpMap[data[i][parentField]]['children'].push(data[i])
            } else {
                data[i]['text'] = data[i][textFiled];
                treeData.push(data[i])
            }
        }
        return treeData
    }
    return data
};
$.fn.combotree.defaults.loadFilter = $.fn.tree.defaults.loadFilter;
$.modalDialog = function (options) {
    var opts = $.extend({
        title: '', width: 840, height: 680, modal: true, onClose: function () {
            $(this).dialog('destroy')
        }
    }, options);
    opts.modal = true;
    return $.modalDialog.handler = $('<div/>').dialog(opts)
};
$.extend($.fn.dialog.methods, {
    mymove: function (jq, newposition) {
        return jq.each(function () {
            $(this).dialog('move', newposition)
        })
    }
});
$.extend($.fn.datagrid.defaults.editors, {
    combogrid: {
        init: function (container, options) {
            var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
            input.combogrid(options);
            return input
        },
        destroy: function (target) {
            $(target).combogrid('destroy')
        },
        getValue: function (target) {
            return $(target).combogrid('getValue')
        },
        setValue: function (target, value) {
            $(target).combogrid('setValue', value)
        },
        resize: function (target, width) {
            $(target).combogrid('resize', width)
        }
    },
    combocheckboxtree: {
        init: function (container, options) {
            var editor = $('<input />').appendTo(container);
            options.multiple = true;
            editor.combotree(options);
            return editor
        },
        destroy: function (target) {
            $(target).combotree('destroy')
        },
        getValue: function (target) {
            return $(target).combotree('getValues').join(',')
        },
        setValue: function (target, value) {
            $(target).combotree('setValues', $.getList(value))
        },
        resize: function (target, width) {
            $(target).combotree('resize', width)
        }
    },
    datebox: {
        init: function (container, options) {
            var editor = $('<input type="text">').appendTo(container);
            editor.datebox(options);
            return editor
        },
        destroy: function (target) {
            $(target).datebox('destroy')
        },
        getValue: function (target) {
            return $(target).datebox('getValue')
        },
        setValue: function (target, value) {
            $(target).datebox('setValue', value)
        },
        resize: function (target, width) {
            $(target).datebox('resize', width)
        }
    },
    datetimebox: {
        init: function (container, options) {
            var editor = $('<input />').appendTo(container);
            editor.datetimebox(options);
            return editor
        },
        destroy: function (target) {
            $(target).datetimebox('destroy')
        },
        getValue: function (target) {
            return $(target).datetimebox('getValue')
        },
        setValue: function (target, value) {
            $(target).datetimebox('setValue', value)
        },
        resize: function (target, width) {
            $(target).datetimebox('resize', width)
        }
    },
    my97: {
        init: function (container, options) {
            var editor = $('<input />').appendTo(container);
            editor.my97(options);
            return editor
        },
        getValue: function (target) {
            return $(target).val()
        },
        setValue: function (target, value) {
            $(target).val(value)
        },
        setDisabled: function (target, width) {
            $(target).my97('setDisabled', width)
        }
    },
    multiplecombobox: {
        init: function (container, options) {
            var editor = $('<input />').appendTo(container);
            options.multiple = true;
            editor.combobox(options);
            return editor
        },
        destroy: function (target) {
            $(target).combobox('destroy')
        },
        getValue: function (target) {
            return $(target).combobox('getValues').join(',')
        },
        setValue: function (target, value) {
            $(target).combobox('setValues', $.getList(value))
        },
        resize: function (target, width) {
            $(target).combobox('resize', width)
        }
    },
    numberspinner: {
        init: function (container, options) {
            var input = $('<input type="text">').appendTo(container);
            return input.numberspinner(options)
        }, destroy: function (target) {
            $(target).numberspinner('destroy')
        }, getValue: function (target) {
            return $(target).numberspinner('getValue')
        }, setValue: function (target, value) {
            $(target).numberspinner('setValue', value)
        }, resize: function (target, width) {
            $(target).numberspinner('resize', width)
        }
    },
    timespinner: {
        init: function (container, options) {
            var input = $('<input/>').appendTo(container);
            input.timespinner(options);
            return input
        },
        getValue: function (target) {
            var val = $(target).timespinner('getValue')
        },
        setValue: function (target, value) {
            $(target).timespinner('setValue', value)
        },
        resize: function (target, width) {
            var input = $(target);
            if ($.boxModel == true) {
                input.resize('resize', width - (input.outerWidth() - input.width()))
            } else {
                input.resize('resize', width)
            }
        }
    },
    password: {
        init: function (container, options) {
            var input = $('<input class="datagrid-editable-input" type="password"/>').appendTo(container);
            if (!$.fn.validatebox.defaults.rules.safepass) {
                $.extend($.fn.validatebox.defaults.rules, {
                    safepass: {
                        validator: function (value, param) {
                            return !(/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/.test(value))
                        }, message: '密码由字母和数字组成，至少6位'
                    }
                })
            }
            input.validatebox(options);
            return input
        },
        getValue: function (target) {
            alert($(target).val());
            alert(target.value);
            return $(target).val()
        },
        setValue: function (target, value) {
            $(target).val(value)
        },
        resize: function (target, width) {
            var input = $(target);
            if ($.boxModel == true) {
                input.width(width - (input.outerWidth() - input.width()))
            } else {
                input.width(width)
            }
        }
    }
});
$.extend($.fn.datagrid.defaults.view, {
    render: function (target, container, frozen) {
        var state = $.data(target, "datagrid");
        var opts = state.options;
        var rows = state.data.rows;
        var fields = $(target).datagrid("getColumnFields", frozen);
        if (frozen) {
            if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))) {
                return
            }
        }
        var table = ["<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"];
        for (var i = 0; i < rows.length; i += 1) {
            var cls = (i % 2 && opts.striped) ? "class=\"datagrid-row datagrid-row-alt\"" : "class=\"datagrid-row\"";
            var styleValue = opts.rowStyler ? opts.rowStyler.call(target, i, rows[i]) : "";
            var style = styleValue ? "style=\"" + styleValue + "\"" : "";
            var rowId = state.rowIdPrefix + "-" + (frozen ? 1 : 2) + "-" + i;
            table.push("<tr id=\"" + rowId + "\" datagrid-row-index=\"" + i + "\" " + cls + " " + style + ">");
            table.push(this.renderRow.call(this, target, fields, frozen, i, rows[i]));
            table.push("</tr>")
        }
        table.push("</tbody></table>");
        $(container).html(table.join(""));
        $.parser.parse(container)
    },
    renderRow: function (target, fields, frozen, rowIndex, rowData) {
        var opts = $.data(target, "datagrid").options;
        var cc = [];
        if (frozen && opts.rownumbers) {
            var rownumber = rowIndex + 1;
            if (opts.pagination) {
                rownumber += (opts.pageNumber - 1) * opts.pageSize
            }
            cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">" + rownumber + "</div></td>")
        }
        for (var i = 0; i < fields.length; i += 1) {
            var field = fields[i];
            var col = $(target).datagrid("getColumnOption", field);
            if (col) {
                var value = jQuery.proxy(function () {
                    try {
                        return eval('this.' + field)
                    } catch (e) {
                        return ""
                    }
                }, rowData)();
                var styleValue = col.styler ? (col.styler(value, rowData, rowIndex) || "") : "";
                var style = col.hidden ? "style=\"display:none;" + styleValue + "\"" : (styleValue ? "style=\"" + styleValue + "\"" : "");
                cc.push("<td field=\"" + field + "\" " + style + ">");
                if (col.checkbox) {
                    var style = ""
                } else {
                    var style = styleValue;
                    if (col.align) {
                        style += ";text-align:" + col.align + ";"
                    }
                    if (!opts.nowrap) {
                        style += ";white-space:normal;height:auto;"
                    } else {
                        if (opts.autoRowHeight) {
                            style += ";height:auto;"
                        }
                    }
                }
                cc.push("<div style=\"" + style + "\" ");
                if (col.checkbox) {
                    cc.push("class=\"datagrid-cell-check ")
                } else {
                    cc.push("class=\"datagrid-cell " + col.cellClass)
                }
                cc.push("\">");
                if (col.checkbox) {
                    cc.push("<input type=\"checkbox\" name=\"" + field + "\" value=\"" + (value != undefined ? value : "") + "\"/>")
                } else {
                    if (col.formatter) {
                        cc.push(col.formatter(value, rowData, rowIndex))
                    } else {
                        cc.push(value)
                    }
                }
                cc.push("</div>");
                cc.push("</td>")
            }
        }
        return cc.join("")
    }
});
$.fn.datebox.defaults.formatter = function (date) {
    var vDate = new Date(date);
    return $.formatDate(vDate, 'yyyy-MM-dd')
};
$.fn.datebox.defaults.parser = function (s) {
    if (!s) {
        return new Date()
    }
    var ss = s.split('-');
    var y = parseInt(ss[0], 10);
    var m = parseInt(ss[1], 10);
    var d = parseInt(ss[2], 10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
        return new Date(y, m - 1, d)
    } else {
        return new Date()
    }
};
$.fn.datetimebox.defaults.formatter = function (date) {
    return $.formatDate(date, 'yyyy-MM-dd HH:mm:ss')
};
$.fn.datetimebox.defaults.parser = function (s) {
    if (!s) {
        return new Date()
    }
    var ss = s.split(' ')[0].split('-');
    var y = parseInt(ss[0], 10);
    var M = parseInt(ss[1], 10);
    var d = parseInt(ss[2], 10);
    var ts = s.split(' ')[1].split(':');
    var H = parseInt(ts[0], 10);
    var m = parseInt(ts[1], 10);
    var s = parseInt(ts[2], 10);
    if (!isNaN(y) && !isNaN(M) && !isNaN(d)) {
        return new Date(y, M - 1, d, H, m, s)
    } else {
        return new Date()
    }
};
var easyuiErrorFunction = function (XMLHttpRequest) {
    $.messager.progress('close');
    var data = XMLHttpRequest.responseText;
    if (!data) {
        eu.showAlertMsg('服务器无响应.', 'error')
    } else {
        eu.showAlertMsg(data, 'error')
    }
};
$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
$.fn.form.defaults.onLoadError = easyuiErrorFunction;
$.extend($.fn.layout.methods, {
    removeHeader: function (jq, region) {
        return jq.each(function () {
            var panel = $(this).layout("panel", region);
            if (panel) {
                panel.panel('removeHeader');
                panel.panel('resize')
            }
        })
    },
    addHeader: function (jq, params) {
        return jq.each(function () {
            var panel = $(this).layout("panel", params.region);
            var opts = panel.panel("options");
            if (panel) {
                var title = params.title ? params.title : opts.title;
                panel.panel('addHeader', {title: title});
                panel.panel('resize')
            }
        })
    },
    isVisible: function (jq, params) {
        var panels = $.data(jq[0], 'layout').panels;
        var pp = panels[params];
        if (!pp) {
            return false
        }
        if (pp.length) {
            return pp.panel('panel').is(':visible')
        } else {
            return false
        }
    },
    hidden: function (jq, params) {
        return jq.each(function () {
            var opts = $.data(this, 'layout').options;
            var panels = $.data(this, 'layout').panels;
            if (!opts.regionState) {
                opts.regionState = {}
            }
            var region = params;

            function hide(dom, region, doResize) {
                var first = region.substring(0, 1);
                var others = region.substring(1);
                var expand = 'expand' + first.toUpperCase() + others;
                if (panels[expand]) {
                    if ($(dom).layout('isVisible', expand)) {
                        opts.regionState[region] = 1;
                        panels[expand].panel('close')
                    } else if ($(dom).layout('isVisible', region)) {
                        opts.regionState[region] = 0;
                        panels[region].panel('close')
                    }
                } else {
                    panels[region].panel('close')
                }
                if (doResize) {
                    $(dom).layout('resize')
                }
            };
            if (region.toLowerCase() == 'all') {
                hide(this, 'east', false);
                hide(this, 'north', false);
                hide(this, 'west', false);
                hide(this, 'south', true)
            } else {
                hide(this, region, true)
            }
        })
    },
    show: function (jq, params) {
        return jq.each(function () {
            var opts = $.data(this, 'layout').options;
            var panels = $.data(this, 'layout').panels;
            var region = params;

            function show(dom, region, doResize) {
                var first = region.substring(0, 1);
                var others = region.substring(1);
                var expand = 'expand' + first.toUpperCase() + others;
                if (panels[expand]) {
                    if (!$(dom).layout('isVisible', expand)) {
                        if (!$(dom).layout('isVisible', region)) {
                            if (opts.regionState[region] == 1) {
                                panels[expand].panel('open')
                            } else {
                                panels[region].panel('open')
                            }
                        }
                    }
                } else {
                    panels[region].panel('open')
                }
                if (doResize) {
                    $(dom).layout('resize')
                }
            };
            if (region.toLowerCase() == 'all') {
                show(this, 'east', false);
                show(this, 'north', false);
                show(this, 'west', false);
                show(this, 'south', true)
            } else {
                show(this, region, true)
            }
        })
    },
    setRegionSize: function (jq, params) {
        return jq.each(function () {
            if (params.region == "center") {
                return
            }
            var panel = $(this).layout('panel', params.region);
            var optsOfPanel = panel.panel('options');
            if (params.region == "north" || params.region == "south") {
                optsOfPanel.height = params.value
            } else {
                optsOfPanel.width = params.value
            }
            $(this).layout('resize')
        })
    },
    setHeaderIcon: function (jq, params) {
        return jq.each(function () {
            if (params.region == "center") {
                return
            }
            var panel = $(this).layout('panel', params.region);
            var title = panel.panel('header').find('>div.panel-title');
            var icon = panel.panel('header').find('>div.panel-icon');
            if (icon.length === 0) {
                if (title.length && params.iconCls != null) {
                    $('<div class="panel-icon ' + params.iconCls + '"></div>').insertBefore(title);
                    title.addClass('panel-with-icon')
                }
            } else {
                if (params.iconCls == null) {
                    icon.remove();
                    title.removeClass('panel-with-icon')
                } else {
                    icon.attr('class', '').addClass('panel-icon ' + params.iconCls)
                }
            }
        })
    },
    setSplitActivateState: function (jq, params) {
        return jq.each(function () {
            if (params.region == "center") {
                return
            }
            $(this).layout('panel', params.region).panel('panel').resizable(params.disabled ? 'disable' : 'enable')
        })
    },
    setSplitVisiableState: function (jq, params) {
        return jq.each(function () {
            if (params.region == "center") {
                return
            }
            var panel = $(this).layout('panel', params.region);
            panel.panel('options').split = params.visible;
            if (params.visible) {
                panel.panel('panel').addClass('layout-split-north')
            } else {
                panel.panel('panel').removeClass('layout-split-north')
            }
            $(this).layout('resize')
        })
    },
    setRegionToolVisableState: function (jq, params) {
        return jq.each(function () {
            if (params.region == "center") {
                return
            }
            var panels = $.data(this, 'layout').panels;
            var panel = panels[params.region];
            var tool = panel.panel('header').find('>div.panel-tool');
            tool.css({display: params.visible ? 'block' : 'none'});
            var first = params.region.substring(0, 1);
            var others = params.region.substring(1);
            var expand = 'expand' + first.toUpperCase() + others;
            if (panels[expand]) {
                panels[expand].panel('header').find('>div.panel-tool').css({display: params.visible ? 'block' : 'none'})
            }
        })
    },
    fullCenter: function (jq) {
        return jq.each(function () {
            var layout = $(this);
            var center = layout.layout('panel', 'center');
            center.panel('maximize');
            center.parent().css('z-index', 10);
            $(window).on('resize.full', function () {
                layout.layout('unFullCenter').layout('resize')
            })
        })
    },
    unFullCenter: function (jq) {
        return jq.each(function () {
            var center = $(this).layout('panel', 'center');
            center.parent().css('z-index', 'inherit');
            center.panel('restore');
            $(window).off('resize.full')
        })
    }
});
$.extend($.fn.window.methods, {
    shake: function (jq, params) {
        return jq.each(function () {
            var extent = params && params['extent'] ? params['extent'] : 1;
            var interval = params && params['interval'] ? params['interval'] : 13;
            var style = $(this).closest('div.window')[0].style;
            if ($(this).data("window").shadow) {
                var shadowStyle = $(this).data("window").shadow[0].style
            }
            _p = [4 * extent, 6 * extent, 8 * extent, 6 * extent, 4 * extent, 0, -4 * extent, -6 * extent, -8 * extent, -6 * extent, -4 * extent, 0], _fx = function () {
                style.marginLeft = _p.shift() + 'px';
                if (shadowStyle) {
                    shadowStyle.marginTop = 0
                }
                if (_p.length <= 0) {
                    style.marginLeft = 0;
                    if (shadowStyle) {
                        shadowStyle.marginLeft = 0
                    }
                    clearInterval(_timerId);
                    _timerId = null, _p = null, _fx = null
                }
            };
            _p = _p.concat(_p.concat(_p));
            _timerId = setInterval(_fx, interval)
        })
    }
});
$.extend($.fn.menu.methods, {
    showItem: function (jq, text) {
        return jq.each(function () {
            var item = $(this).menu('findItem', text);
            $(item.target).show()
        })
    },
    hideItem: function (jq, text) {
        return jq.each(function () {
            var item = $(this).menu('findItem', text);
            $(item.target).hide()
        })
    }
});
$.extend($.fn.dialog.methods, {
    addButtonsItem: function (jq, items) {
        return jq.each(function () {
            var buttonbar = $(this).children("div.dialog-button");
            for (var i = 0; i < items.length; i += 1) {
                var item = items[i];
                var btn = $("<a href=\"javascript:void(0)\"></a>");
                btn[0].onclick = eval(item.handler || function () {
                    });
                btn.css("float", "left").appendTo(buttonbar).linkbutton($.extend({}, item, {plain: false}))
            }
            buttonbar = null
        })
    },
    removeButtonsItem: function (jq, param) {
        return jq.each(function () {
            var btns = $(this).children("div.dialog-button").children("a");
            var cbtn = null;
            if (typeof param == "number") {
                cbtn = btns.eq(param)
            } else if (typeof param == "string") {
                var text = null;
                btns.each(function () {
                    text = $(this).data().linkbutton.options.text;
                    if (text == param) {
                        cbtn = $(this);
                        text = null;
                        return
                    }
                })
            }
            if (cbtn) {
                var prev = cbtn.prev()[0];
                var next = cbtn.next()[0];
                if (prev && next && prev.nodeName == "DIV" && prev.nodeName == next.nodeName) {
                    $(prev).remove()
                } else if (next && next.nodeName == "DIV") {
                    $(next).remove()
                } else if (prev && prev.nodeName == "DIV") {
                    $(prev).remove()
                }
                cbtn.remove();
                cbtn = null
            }
        })
    }
});
$.ajaxSetup({
    type: 'POST', error: function (XMLHttpRequest, textStatus, errorThrown) {
        $.messager.progress('close');
        if (!data) {
            eu.showAlertMsg('服务器无响应.', 'error')
        } else if (data.msg) {
            eu.showAlertMsg(data.msg, 'error')
        } else {
            eu.showAlertMsg(data, 'error')
        }
    }
});
$.extend($.fn.form.methods, {
    myLoad: function (jq, param) {
        return jq.each(function () {
            load(this, param)
        });

        function load(target, param) {
            if (!$.data(target, "form")) {
                $.data(target, "form", {options: $.extend({}, $.fn.form.defaults)})
            }
            var options = $.data(target, "form").options;
            if (typeof param == "string") {
                var params = {};
                if (options.onBeforeLoad.call(target, params) == false) {
                    return
                }
                $.ajax({
                    url: param, data: params, dataType: "json", success: function (rsp) {
                        loadData(rsp)
                    }, error: function () {
                        options.onLoadError.apply(target, arguments)
                    }
                })
            } else {
                loadData(param)
            }

            function loadData(dd) {
                var form = $(target);
                var formFields = form.find("input[name],select[name],textarea[name]");
                formFields.each(function () {
                    var name = this.name;
                    var value = jQuery.proxy(function () {
                        try {
                            return eval('this.' + name)
                        } catch (e) {
                            return ""
                        }
                    }, dd)();
                    var rr = setNormalVal(name, value);
                    if (!rr.length) {
                        var f = form.find("input[numberboxName=\"" + name + "\"]");
                        if (f.length) {
                            f.numberbox("setValue", value)
                        } else {
                            $("input[name=\"" + name + "\"]", form).val(value);
                            $("textarea[name=\"" + name + "\"]", form).val(value);
                            $("select[name=\"" + name + "\"]", form).val(value)
                        }
                    }
                    setPlugsVal(name, value)
                });
                options.onLoadSuccess.call(target, dd);
                $(target).form("validate")
            };

            function setNormalVal(key, val) {
                var rr = $(target).find("input[name=\"" + key + "\"][type=radio], input[name=\"" + key + "\"][type=checkbox]");
                rr._propAttr("checked", false);
                rr.each(function () {
                    var f = $(this);
                    if (f.val() == String(val) || $.inArray(f.val(), val) >= 0) {
                        f._propAttr("checked", true)
                    }
                });
                return rr
            };

            function setPlugsVal(key, val) {
                var form = $(target);
                var cc = ["combobox", "combotree", "combogrid", "datetimebox", "datebox", "combo"];
                var c = form.find("[comboName=\"" + key + "\"]");
                if (c.length) {
                    for (var i = 0; i < cc.length; i += 1) {
                        var combo = cc[i];
                        if (c.hasClass(combo + "-f")) {
                            if (c[combo]("options").multiple) {
                                c[combo]("setValues", val)
                            } else {
                                c[combo]("setValue", val)
                            }
                            return
                        }
                    }
                }
            }
        }
    }
});