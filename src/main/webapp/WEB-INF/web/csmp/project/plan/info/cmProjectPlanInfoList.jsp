<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>项目管理</span>
            <span>进度及周报管理 > </span><span>项目计划制定</span>
        </div>
    </header>
    <section class="tree-fluid m-t-md">
        <aside class="tree-wrap" id="LeftHeight_box">
            <a class="a-icon i-new" href="javascript:void(0);" id="addStageBtn" style="margin-top: 15px;">添加阶段</a>
            <a class="a-icon i-new" href="javascript:void(0);" id="editStageBtn">修改阶段</a>
            <a class="a-icon i-remove m-r-xs" href="javascript:void(0);" id="deleteStageBtn">删除阶段</a>
            <section class="panel" id="LeftHeight" style="overflow-y: auto; margin-top: -4px;">
                <div id="treeDemo" class="ztree"></div>
            </section>
        </aside>
        <section class="tree-right">
            <section class="panel">
                <form class="table-wrap form-table" id="searchForm" style="display:none;">
                    <input type="hidden" id="paramProjectId" name="paramProjectId" value="${projectId}" />
                    <input type="hidden" id="paramStageId" name="paramStageId" />
                    <input type="hidden" id="paramStageName" name="paramStageName" />
                </form>
                <button class="btn" style="background-color: #1572e8; border-color: #1572e8; color: white; margin-top: 10px; margin-bottom: 10px;" type="button" id="addBtn">新增计划</button>
                <button class="btn" style="background-color: #1572e8; border-color: #1572e8; color: white; margin-top: 10px; margin-bottom: 10px;" type="button" onclick="projectInfo.print('${projectId}');">打 印</button>
                <shiro:hasPermission name='planFileBtn'><button class="btn" style="background-color: #1572e8; border-color: #1572e8; color: white; margin-top: 10px; margin-bottom: 10px;" type="button" onclick="lodopFunction.createFile({type: 'plan', busId: $('#searchForm #paramProjectId').val()})">归 档</button></shiro:hasPermission>
                <button class="btn" style="background-color: #1572e8; border-color: #1572e8; color: white; margin-top: 10px; margin-bottom: 10px;" type="button" onclick="window.history.go(-1);">返 回</button>
                <span id="stageNameHtml" style="margin-left: 40px; font-weight: bold;"></span>
            </section>

            <section class="panel tab-content">
                <div class="tab-pane fade active in">
                    <div class="table-wrap">
                        <table class="table table-striped tab_height" id="gridTable"></table>
                    </div>
                    <section class="clearfix">
                        <section class="pagination m-r fr m-t-none"></section>
                    </section>
                </div>
            </section>
        </section>
    </section>
    <div id="taskModule"></div>
    <div id="stageModule"></div>
</section>
<%@ include file="/WEB-INF/web/print/projectPlan.jsp" %>
<script src='${sysPath}/js/com/jc/csmp/project/plan/info/cmProjectPlanInfoList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

