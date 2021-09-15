<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%

String  url = "http://192.168.0.128:1010/dh/";
String path=(String)request.getAttribute("path");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
<script>
    $(document).ready(function(){
        window.location.href='<%=url%><%=path%>'
    });

</script>
</html>
