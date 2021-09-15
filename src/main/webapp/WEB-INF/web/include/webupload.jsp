<%@ page language="java" pageEncoding="UTF-8"%>
<!-- webuploader -->
<link rel="stylesheet" type="text/css" href="${sysPath}/css/webupload/webuploader.css">
<c:choose>
    <c:when test="${systemState=='debug'}">
        <script src="${sysPath}/js/webupload/webuploader.js?v=7d1f4d41e2"></script>
        <script src="${sysPath}/js/webupload/upload.js?v=c378bcfd14"></script>
    </c:when>
    <c:otherwise>
        <script src="${sysPath}/js/lib/app/webuploader.min.js?v=c198400d48"></script>
    </c:otherwise>
</c:choose>
