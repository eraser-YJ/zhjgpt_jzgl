<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>项目管理</span>
            <span>进度及周报管理 > </span><span>项目计划模板管理</span>
        </div>
    </header>
    <section class="tree-fluid m-t-md">
        <aside class="tree-wrap" id="LeftHeight_box">

            <section class="panel" id="LeftHeight" style="overflow-y: auto; margin-top: 15px; padding: 0 15px !important;">
                <div style="display: flex; justify-content: space-around;">
                    <a class="a-icon i-new" style="margin:15px 5px!important;" href="javascript:void(0);" id="addStageBtn" style="margin-top: 15px;">添加阶段</a>
                    <a class="a-icon i-new" style="margin:15px 5px!important;" href="javascript:void(0);" id="editStageBtn">修改阶段</a>
                    <a class="a-icon i-remove m-r-xs" style="margin:15px 5px!important;" href="javascript:void(0);" id="deleteStageBtn">删除阶段</a>
                </div>
                <div id="treeDemo" class="ztree"></div>
            </section>
        </aside>
        <section class="tree-right">
            <section class="panel">
                <form class="table-wrap form-table" id="searchForm">
                    <input type="hidden" id="paramTemplateId" name="paramTemplateId" value="${templateId}" />
                    <input type="hidden" id="paramStageId" name="paramStageId" />
                    <input type="hidden" id="paramStageName" name="paramStageName" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td style="width: 80px">模板名称</td>
                            <td>
                                <input style="width: 200px;" type="text" id="templateName" name="templateName" value="${templateName}" />
                                <button class="btn" type="button" id="updateBtn">修改名称</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <button class="btn" type="button" id="addBtn">新增任务</button>
                            </td>
                            <td id="stageNameHtml"></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
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
<script src='${sysPath}/js/com/jc/csmp/project/plan/template/cmProjectPlanTemplateContentList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

