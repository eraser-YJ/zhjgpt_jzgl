<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>

</head>
<body>
<%@ include file="../common/nav.jsp" %>
<div class="container">
    <table class="table">
        <c:forEach items="${definitionList}" var="definition">
        <tr>
            <td>${definition.name}</td>
            <td>${definition.definitionId}</td>
            <td><a href="${sysPath}/demo/toStartProcess.action?definitionId_=${definition.definitionId}&definitionKey_=${definition.definitionKey}">发起</a></td>
        </tr>
        </c:forEach>
    </table>
</div>

</body>
</html>