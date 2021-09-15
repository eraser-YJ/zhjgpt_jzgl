<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="sysPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>流程历史</title>
    <script>
        function getRootPath(){
            return "${sysPath}";
        }
    </script>
    <!-- Bootstrap -->
    <!--[if lt IE 9]>
    <script src="${sysPath}/process-editor/editor-app/libs/html5shiv.js"></script>
    <![endif]-->

<body>
<div id="workflowHistoryContainer"></div>
<script type="text/javascript" src="${sysPath}/js/com/jc/workflow/history/raphael.min.js"></script>
<script type="text/javascript" src="${sysPath}/js/com/jc/workflow/history/workflowHistory.js"></script>
<script type="text/javascript">
    window.onload = function(){
        //初始化
        new WorkflowHistory('workflowHistoryContainer',${jsonStr});
    }
</script>
</body>
</html>
