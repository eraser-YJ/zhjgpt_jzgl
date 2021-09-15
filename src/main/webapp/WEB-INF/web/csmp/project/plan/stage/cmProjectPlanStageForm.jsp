<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
    <div class="modal-dialog w600">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityStageFormTitle"></h4>
            </div>
            <div class="modal-body" id="formBody">
                <form class="table-wrap form-table" id="entityStageForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="token" name="token" value="${data.token}">
                    <input type="hidden" id="parentId" name="parentId">
                    <input type="hidden" id="projectId" name="projectId">
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                            <tr id="parentNameTr">
                                <td class="w100">上级阶段</td>
                                <td><input type="text" id="parentName" name="parentName" readonly="readonly" /></td>
                            </tr>
                            <tr>
                                <td class="w100"><span class='required'>*</span>阶段名称</td>
                                <td><input type="text" id="stageName" name="stageName" /></td>
                            </tr>
                            <tr>
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
<script src='${sysPath}/js/com/jc/csmp/project/plan/stage/cmProjectPlanStageForm.js'></script>