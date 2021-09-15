<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-WeeklyPlan" aria-hidden="false">
    <div class="modal-dialog w1100">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityTaskFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityTaskForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="modifyDate" name="modifyDate" >
                    <input type="hidden" id="token" name="token" value="${token}"/>
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td class="w100">实际开始时间</td>
                            <td><input type="text" id="actualStartDate" name="actualStartDate" data-date-format="yyyy-MM-dd" data-pick-time="false" class="datepicker-input" /></td></td>
                            <td class="w100">实际结束时间</td>
                            <td><input type="text" id="actualEndDate" name="actualEndDate" data-date-format="yyyy-MM-dd" data-pick-time="false" class="datepicker-input" /></td></td>
                        </tr>
                        <tr>
                            <td class="w100">计划日历天</td>
                            <td><input type="text" id="planDay" name="planDay" /></td>
                            <td class="w100">计划工时天</td>
                            <td><input type="text" id="planWorkDay" name="planWorkDay" /></td>
                        </tr>
                        <tr>
                            <td class="w100">实际进度</td>
                            <td><input type="text" id="completionRatio" name="completionRatio" /></td>
                        </tr>
                        <tr>
                            <td>备注</td>
                            <td colspan="5"><textarea id="remark" name="remark" style="height: 80px;"></textarea></td>
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
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script src="${sysPath}/js/webupload/liuUpload.js" type="text/javascript"></script>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script src='${sysPath}/js/com/jc/common/common-jquery-validate.js'></script>
<script src='${sysPath}/js/com/jc/csmp/project/weekly/cmProjectPlanForm.js'></script>