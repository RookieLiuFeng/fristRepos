<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="panel-fit">
<head>
    <title>董李凤美微信管理平台</title>
    <%@include file="/common/meta.jsp"%>
</head>
<body class="easyui-layout layout panel-noscroll">
<%-- north顶部Logo区域 --%>
<div id="north" data-options="region:'north',border:false,split:false,href:'${ctxAdmin}/index/north'"
     class="top_layout">
</div>

<%-- west菜单栏 --%>
<div id="west" data-options="region:'west',title:'导航菜单',split:false,href:'${ctxAdmin}/index/west'"
     style="width: 200px;overflow: hidden;">
</div>

<%-- center主面板 --%>
<div data-options="region:'center',split:false,border:false,href:'${ctxAdmin}/common/layout/center'" style="overflow: hidden;">
</div>
<script type="text/javascript">



</script>
</body>
</html>
