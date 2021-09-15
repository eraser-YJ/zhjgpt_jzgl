<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="sysPath" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" href="${sysPath}/process-editor/editor-app/libs/bootstrap_3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="${sysPath}/process-editor/editor-app/libs/bootstrap_3.1.1/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="${sysPath}/process-editor/editor-app/libs/select2_4.0.2/select2.css">
<link rel="stylesheet" href="${sysPath}/process-editor/editor-app/libs/ztree_3.5.23/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="${sysPath}/process-editor/editor-app/libs/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<script src="${sysPath}/process-editor/editor-app/libs/jquery_1.11.0/jquery.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/jquery_1.11.0/jquery.plugin.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/bootstrap_3.1.1/js/bootstrap.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/select2_4.0.2/select2.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/ztree_3.5.23/jquery.ztree.all.min.js"></script>
<script src="${sysPath}/process-editor/editor-app/libs/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script src="${sysPath}/js/com/jc/workflow/common.js"></script>

<script>
    /**
     * 获得项目根路径
     * @returns {string}
     */
    function getRootPath(){
        return "${sysPath}";
    }

    $.fn.modal.Constructor.prototype.enforceFocus = function () { };

    function showConvertUserModule(){
        $("#convertUserModal").modal("show");
    }

    function convertUser(){
        var username = $("#username_").val();
        if(username==""||username.length==0){
            alert("请输入用户名");
            return;
        }
        $.post("${sysPath}/system/convertUser.action",{username:username},function(){
            $("#convertUserModal").modal("hide");
            $("#curUsername").html(username);
            location.href = location.href;
        });
    }
</script>

<nav class="navbar navbar-default" style="display:none">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">工作流</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="${sysPath}/form/manage.action">表单管理</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">流程管理<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="${sysPath}/type/manage.action">流程类型管理</a></li>
                        <li><a href="${sysPath}/definitionAuth/manageStartAuthView.action">流程权限管理</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">样例中心<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="${sysPath}/demo/manage.action">发起流程</a></li>
                        <li><a href="${sysPath}/demo/toTodoList.action">待办查询</a></li>
                        <li><a href="${sysPath}/demo/toDoneList.action">已办查询</a></li>
                    </ul>
                </li>
                <li><a href="${sysPath}/delegate/manage.action">委托管理</a></li>
                <li><a href="javascript:void(0);" onclick="goDesigner()">流程设计</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="javascript:void(0);" onclick="showConvertUserModule()" style="cursor: hand">当前登录人:<span id="curUsername">${sessionScope.username}</span></a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="modal fade " id="convertUserModal" role="dialog" aria-labelledby="convertUserModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="convertUserModalLabel">切换用户</h4>
            </div>
            <div class="modal-body">
                <form id="convertForm" class="form-signin" method="post">
                    <div class="form-group">
                        <label for="username_" class="col-sm-3 control-label">登录名</label>
                        <div class="col-sm-9">
                            <input type="text" id="username_" class="form-control" value="${sessionScope.username}">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="convertUser()">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>