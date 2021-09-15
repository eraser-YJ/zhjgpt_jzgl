<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <style type="text/css">
        div#rMenu {position:absolute; visibility:hidden; top:0; background-color: #555;text-align: left;padding: 2px;}
        div#rMenu ul li{
            margin: 1px 0;
            padding: 0 5px;
            cursor: pointer;
            list-style: none outside none;
            background-color: #DFDFDF;
        }
    </style>
</head>
<body>
    <%@ include file="../common/nav.jsp" %>
    <div class="container">
        <div class="zTreeDemoBackground left" style="float:left;width: 200px;">
            <ul id="defTree" class="ztree"></ul>
        </div>
        <div id="desinger" style="margin-left:100px;float: left;width: 800px;height:600px; ">
            <textarea id="designerTextarea" style="width: 100%;height: 100%"></textarea>
            <a class="btn btn-default" href="#" role="button" onclick="def_.designer.submit()">提交</a>
        </div>
    </div>
    <div id="rMenu">
        <ul style="padding:0px; margin:0px;">
            <li type="type" onclick="def_.defTree.rMenuCreateProcess()">新增流程</li>
            <li type="def" onclick="">复制流程</li>
            <li type="def" onclick="def_.defTree.rMenuDeleteProcess()">删除流程</li>
        </ul>
    </div>

</body>
<script src="${sysPath}/js/com/jc/workflow/def/def.js"></script>
</html>