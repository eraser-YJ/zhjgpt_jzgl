<%--
  Created by IntelliJ IDEA.
  User: sunpeng
  Date: 16-4-28
  Time: 下午2:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@ include file="../common/nav.jsp" %>
<div class="container">
    <table id="demoTable" class="table table-striped">
        <thead>
        <tr>
            <th>请假天数</th>
            <th>business_key</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>当前办理人</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>
</body>
<script src="${sysPath}/js/com/jc/workflow/demo/todoList.js"></script>
</html>
