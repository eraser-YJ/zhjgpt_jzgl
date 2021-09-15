<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>

</head>
<body>
<%@ include file="../common/nav.jsp" %>
<div class="container">
    <form id="demoForm">
    <%@ include file="../workflowVar.jsp" %>
    <input type="hidden" id="id" name="id" />
    <input type="hidden" id="demoJson"	value='${demoJson}'/>
    <table class="table">
        <tr>
            <td>请假天数</td>
            <td><input type="text" class="form-control" id="days" name="days"></td>
        </tr>
        <tr>
            <td>开始时间</td>
            <td><input type="text" class="form-control" id="start_time" name="start_time"></td>
        </tr>
        <tr>
            <td>结束时间</td>
            <td><input type="text" class="form-control" id="end_time" name="end_time"></td>
        </tr>
        <tr>
            <td>请假原因</td>
            <td><textarea id="reason" class="form-control" name="reason"></textarea></td>
        </tr>
        <tr>
            <td>
                <input class="button" type="button" id="submitBtn" value="提交" />

                <input class="button" type="button" id="saveBtn" value="暂存" />

                <input class="button" type="button" id="rejectBtn" value="退回" />

                <input class="button" type="button" id="getBackBtn" value="拿回" />

                <input class="button" type="button" id="gotoBtn" value="跳转" />

                <input class="button" type="button" id="moveBtn" value="转办" />

                <input class="button" type="button" id="stopBtn" value="中止" />

                <input class="button" type="button" id="viewFlowImg" value="查看流程图"/>

                <input class="button" type="button" id="viewFlowHistory" value="查看历史信息"/>
            </td>
        </tr>
    </table>
    </form>
</div>


<div class="modal fade" id="demoSubmitModal" tabindex="-1" role="dialog" aria-labelledby="formModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="formModalLabel">提交设置</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="confirmNodeId_" class="col-sm-3 control-label">审批路由</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" value="sequenceFlow" id="confirmRouteId_" name="confirmRouteId_" placeholder="审批路由">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="confirmNodeId_" class="col-sm-3 control-label">审批节点</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" value="userTask" id="confirmNodeId_" name="confirmNodeId_" placeholder="审批节点">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="confirmUserId_" class="col-sm-3 control-label">审批人</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="confirmUserId_" name="confirmUserId_" placeholder="审批人(,分隔)">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="demoForm.save('SUBMIT')">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="demoMoveModal" tabindex="-1" role="dialog" aria-labelledby="formModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="formModalLabel">转办设置</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="confirmUserId_" class="col-sm-3 control-label">转办给</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="confirmUserId_" name="confirmUserId_" placeholder="审批人(,分隔)">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="demoForm.save('MOVE')">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="demoGetBackModal" tabindex="-1" role="dialog" aria-labelledby="formModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="formModalLabel">拿回设置</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">拿回到</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" value="userTask" id="confirmNodeId_" name="confirmNodeId_" placeholder="拿回到~节点">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="demoForm.save('GETBACK')">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="demoRejectModal" tabindex="-1" role="dialog" aria-labelledby="formModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" >退回设置</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">退回到</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" value="userTask" id="confirmNodeId_" name="confirmNodeId_" placeholder="退回到~节点">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="demoForm.save('REJECT')">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="demoGotoModal" tabindex="-1" role="dialog" aria-labelledby="formModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" >跳转设置</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">跳转到</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" value="userTask" id="confirmNodeId_" name="confirmNodeId_" placeholder="跳转到~节点">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="demoForm.save('GOTO')">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="demoStopModal" tabindex="-1" role="dialog" aria-labelledby="formModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" >中止设置</h4>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="demoForm.save('STOP')">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

</body>

<script src="${sysPath}/js/com/jc/workflow/demo/demoForm.js"></script>
</html>