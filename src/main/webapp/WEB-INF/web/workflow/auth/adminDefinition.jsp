<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script type="text/javascript">
	function oTableSetButtons( source) {
		if (source.flowStatus == "END") {
				var historyButton = '<a class="a-icon i-new" target="_blank" href="${sysPath}/workflowHistory/showHistory.action?instanceId='+source.instanceId+'&definitionId='+source.definitionId+'">流程历史</a>';
			var deleteButton = '<a class="a-icon i-new" href="#" onclick="adminDefinition.deleteFlow(\''+source.instanceId+'\')" >删除</a>';
	        
	        return  historyButton+deleteButton;
		} else {
				var agentButton = '<a class="a-icon i-new" href="#" onclick="adminDefinition.showAgentModule(\''+source.taskId+'\',\''+source.instanceId+'\',\''+source.nodeId+'\',\''+source.definitionId+'\')" role="button">转办</a>';
	        var jumpButton = '<a class="a-icon i-new" href="#" onclick="adminDefinition.showJumpSelectNodeModule(\''+source.taskId+'\',\''+source.nodeId+'\',\''+source.definitionId+'\',\''+source.instanceId+'\')" role="button">跳转</a>';
	        var stopButton = '<a class="a-icon i-new" href="#" onclick="adminDefinition.stop(\''+source.taskId+'\',\''+source.instanceId+'\',\''+source.nodeId+'\',\''+source.definitionId+'\')" role="button">停止</a>';
	        var historyButton = '<a class="a-icon i-new" target="_blank" href="${sysPath}/workflowHistory/showHistory.action?instanceId='+source.instanceId+'&definitionId='+source.definitionId+'">流程历史</a>';
	        var deleteButton = '<a class="a-icon i-new" href="#" onclick="adminDefinition.deleteFlow(\''+source.instanceId+'\')" >删除</a>';
	        return  historyButton+agentButton+jumpButton+stopButton+deleteButton;
		}
	};
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in">
        <div class="con-heading fl" id="navigationMenu">
            <h1></h1>
            <div class="crumbs"></div>
        </div>
    </header>
    <section class="tree-fluid">
        <aside class="tree-wrap m-t-mdp" id="LeftHeight_box">
            <section class="panel" id="LeftHeight" style="overflow-y: auto;">
                <div class="panel-heading clearfix">
                    <h2>选择流程</h2>
                </div>
                <div id="defTree" class="ztree"></div>
            </section>
        </aside>
        <section class="tree-right" style="padding-left: 215px;">
            <section class="panel search-shrinkage clearfix">
                <div class="search-line hide">
                    <form class="table-wrap form-table" id="queryTaskForm">
                        <table class="table table-td-striped">
                            <tbody>
                            <tr>
                                <td style="width:10%;">流程名称</td>
                                <td style="width:40%;">
                                    <input type="text" id="titleQuery" name="title" >
                                </td>
                                <td style="width:10%;">创建时间</td>
                                <td style="width:40%;">
                                    <div class="input-group w-p100">
                                        <input class="datepicker-input" type="text" id="createDateBegin" name="createDateBegin" data-ref-obj="#createDateEnd lt" data-pick-time="false" data-date-format="yyyy-MM-dd">
                                        <div class="input-group-btn w30"> -</div>
                                        <input class="datepicker-input" type="text" id="createDateEnd" name="createDateEnd" data-ref-obj="#createDateBegin gt" data-pick-time="false" data-date-format="yyyy-MM-dd">
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <section class="form-btn m-b-lg">
                            <button class="btn dark" type="button" id="query">查 询</button>
                            <button class="btn" type="button" id="queryReset">重 置</button>
                        </section>
                    </form>
                </div>
                <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
            </section>
            <section class="panel">
                <div class="table-wrap">
                    <table class="table table-striped tab_height" id="taskTable">
                        <thead>
                        <tr>
                            <th>流程名称</th>
                            <th style="width: 80px">节点名称</th>
                            <th style="width: 200px">办理人</th>
                            <th style="width: 170px">开始时间</th>
                            <th style="width: 170px">流程状态</th>
                            <th class="w300">操作</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <section class="clearfix" id="footer_height">
                    <section class="form-btn fl m-l">
                    </section>
                </section>
            </section>
        </section>
    </section>
</section>

<div class="modal fade panel" id="agentUserModal" aria-hidden="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title">选择转办人</h4>
            </div>
            <div class="modal-body">
                <form id="agentForm" class="table-wrap form-table">
                    <table class="table table-td-striped table-h45 table-bordered b-light table-icon">
                        <tbody>
                        <tr>
                            <td style="width:30%">选择转办人</td>
                            <td><section style=display:inline><span id="agentUserDiv"></span></section></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer no-all form-btn">
                <button id="saveBtn" class="btn dark" type="button" onclick="adminDefinition.agent()">确 定</button>
                <button class="btn" data-dismiss="modal">取 消</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade panel" id="jumpNodeSelectModal" aria-hidden="false">
    <div class="modal-dialog w900">
        <form class="table-wrap form-table" id="jumpNodeForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title">选择跳转节点</h4>
                </div>
                <div class="modal-body" id="jumpSelectDiv" style="max-height: 489px;">
                    <table class='table'>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer form-btn">
                    <button type="button" class="btn dark" onclick="adminDefinition.showJumpSelectUserModule()">确 定</button>
                    <button type="button" class="btn" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>


<div class="modal fade panel" id="jumpUserSelectModal" aria-hidden="false">
    <div class="modal-dialog w900">
        <form class="table-wrap form-table" id="jumpUserForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title">跳转人员选择</h4>
                </div>
                <div class="modal-body" id="userSelectDiv" style="max-height: 489px;">
                    <table class='table table-td-striped'>
                        <tbody>
                            <tr>
                                <td style='width:18%;'><input type='hidden' id='confirmNodeId' value='"+confirmNodeId+"'/><span class='required'></span>人员选择</td>
                                <td><div id='jumpUserDiv'></div></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer form-btn">
                    <button type="button" class="btn dark" onclick="adminDefinition.jumpTask()">确 定</button>
                    <button type="button" class="btn" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="${sysPath}/js/com/workFlow/workFlowUtils.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/jc/workflow/auth/adminDefinition.js"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>