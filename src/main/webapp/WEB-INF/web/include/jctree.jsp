<link href="${sysPath}/css/jctree/jctree.min.css?v=1561561" rel="stylesheet" type="text/css">
<c:choose>
    <c:when test="${systemState=='debug'}">
        <script src="${sysPath}/js/jctree/ChinesePY.js?v=cfc45da250" type="text/javascript"></script>
        <script src="${sysPath}/js/jctree/jquery.ztree.all-3.5.min.js?v=45fb3b8ac4" type="text/javascript"></script>
        <script src="${sysPath}/js/jctree/select2.all.js?v=c817ef7a56" type="text/javascript"></script>
        <script src="${sysPath}/js/jctree/jctree.js?v=bd7546583c233f" type="text/javascript"></script>
        <script src="${sysPath}/js/lib/common/showOnlinePerson.js?v=bd7546583cge2g" type="text/javascript"></script>
    </c:when>
    <c:otherwise>
        <script src="${sysPath}/js/lib/app/jctree.min.js?v=998bbbf48d" type="text/javascript"></script>
    </c:otherwise>
</c:choose>
<script src='${sysPath}/js/com/jc/csmp/common/plugin/choose-company.js'></script>
<script src='${sysPath}/js/com/jc/common/operator-tree.js'></script>



