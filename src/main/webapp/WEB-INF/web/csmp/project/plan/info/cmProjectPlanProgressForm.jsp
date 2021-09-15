<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
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
                    <input type="hidden" id="projectId" name="projectId" />
                    <input type="hidden" id="stageId" name="stageId" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td class="w100">计划编码</td>
                            <td><input type="text" id="planCode" name="planCode" readonly="readonly" /></td>
                            <td class="w100">计划名称</td>
                            <td><input type="text" id="planName" name="planName" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td class="w100">投资金额(万元)</td>
                            <td><input type="text" id="outputValue" name="outputValue" readonly="readonly" /></td>
                            <td class="w100">权重系数</td>
                            <td><input type="text" id="selfWeight" name="selfWeight" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td class="w100">责任单位</td>
                            <td><input type="text" id="dutyCompanyValue" name="dutyCompanyValue" readonly="readonly" /> </td>
                            <td class="w100">责任人</td>
                            <td><input type="text" id="dutyPerson" name="dutyPerson" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td class="w100">计划开始时间</td>
                            <td><input type="text" id="planStartDate" name="planStartDate" readonly="readonly" /></td></td>
                            <td class="w100">计划结束时间</td>
                            <td><input type="text" id="planEndDate" name="planEndDate" readonly="readonly" /></td></td>
                        </tr>
                        <tr>
                            <td class="w100">计划日历天</td>
                            <td><input type="text" id="planDay" name="planDay" readonly="readonly" /></td>
                            <td class="w100">计划工时天</td>
                            <td><input type="text" id="planWorkDay" name="planWorkDay" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td class="w100"><span class='required'>*</span>实际开始时间</td>
                            <td><input type="text" id="actualStartDate" name="actualStartDate" data-date-format="yyyy-MM-dd" data-pick-time="false" class="datepicker-input" /></td></td>
                            <td class="w100">实际结束时间</td>
                            <td><input type="text" id="actualEndDate" name="actualEndDate" data-date-format="yyyy-MM-dd" data-pick-time="false" class="datepicker-input" /></td></td>
                        </tr>
                        <tr>
                            <td class="w100"><span class='required'>*</span>完成比例(%)</td>
                            <td><input type="text" id="completionRatio" name="completionRatio" /></td>
                            <td class="w100">排序</td>
                            <td><input type="text" id="queue" name="queue" readonly="readonly" /></td>
                            <%--<td class="w100"><span class='required'>*</span>完成投资金额(万元)</td>
                            <td><input type="text" id="completionMoney" name="completionMoney" /></td>--%>
                        </tr>
                        <tr>
                            <td>备注</td>
                            <td colspan="3"><textarea id="remark" name="remark" style="height: 80px;"></textarea></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn dark" type="button" id="modifyBtn">更 新</button>
                <button class="btn" type="button" onclick="cmProjectPlanProgressFormPanel.operatorModal('hide');">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/plan/info/cmProjectPlanProgressForm.js'></script>