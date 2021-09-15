<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityTaskFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityTaskForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="token" name="token" value="${data.token}">
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <input type="hidden" id="templateId" name="templateId" />
                    <input type="hidden" id="stageId" name="stageId" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td class="w100"><span class='required'>*</span>任务编码</td>
                            <td><input type="text" id="taskNumber" name="taskNumber" /></td>
                            <td class="w100"><span class='required'>*</span>任务名称</td>
                            <td><input type="text" id="taskName" name="taskName" /></td>
                        </tr>
                        <tr>
                            <td class="w100">计划日历天</td>
                            <td><input type="text" id="planDay" name="planDay" /></td>
                            <td class="w100">计划工时天</td>
                            <td><input type="text" id="planWorkDay" name="planWorkDay" /></td>
                        </tr>
                        <tr>
                            <td class="w100">权重系数</td>
                            <td><input type="text" id="selfWeight" name="selfWeight" /></td>
                            <td class="w100"><span class='required'>*</span>排序</td>
                            <td><input type="text" id="queue" name="queue" /></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn dark" type="button" id="saveBtn">保 存</button>
                <button class="btn" type="button" id="closeBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/csmp/project/plan/template/cmProjectPlanTemplateTaskForm.js'></script>