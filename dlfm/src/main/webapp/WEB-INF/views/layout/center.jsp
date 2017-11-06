<%--
  Created by IntelliJ IDEA.
  User: yomg
  Date: 2017/5/16 0016
  Time: 13:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp"%>
<div id="layout_center_tabs" fit="true" style="overflow: hidden;">
</div>
<script>
    var $layout_center_tabs = $('#layout_center_tabs').tabs();
    
    function openTab(title,url) {
        eu.addTab($layout_center_tabs,title,url,true,"easyui-icon-app-second",null,true);
    }
</script>

