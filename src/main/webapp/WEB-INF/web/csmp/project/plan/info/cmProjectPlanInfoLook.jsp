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
                            <td class="w100">实际开始时间</td>
                            <td><input type="text" id="actualStartDate" name="actualStartDate" readonly="readonly" /></td></td>
                            <td class="w100">实际结束时间</td>
                            <td><input type="text" id="actualEndDate" name="actualEndDate" readonly="readonly" /></td></td>
                        </tr>
                        <tr>
                            <td class="w100">计划日历天</td>
                            <td><input type="text" id="planDay" name="planDay" readonly="readonly" /></td>
                            <td class="w100">计划工时天</td>
                            <td><input type="text" id="planWorkDay" name="planWorkDay" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td class="w100">完成比例(%)</td>
                            <td><input type="text" id="completionRatio" name="completionRatio" readonly="readonly" /></td>
                            <td class="w100">完成投资金额(万元)</td>
                            <td><input type="text" id="completionMoney" name="completionMoney" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td class="w100">排序</td>
                            <td><input type="text" id="queue" name="queue" /></td>
                            <td></td><td></td>
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
                <button class="btn" type="button" onclick="cmProjectPlanLookModule.close();">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/common-tools.js'></script>
<script>
    var cmProjectPlanLookModule = {};
    cmProjectPlanLookModule.init = function(config) {
        new CommonToolsLib.CommonTools({}).$listenerEventCode8();
        $('#entityTaskFormTitle').html(config.title);
        $.ajax({
            type : "GET", data : {id: config.id}, dataType : "json",
            url : getRootPath() + "/project/plan/info/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityTaskForm").fill(data);
                    if (data.completionRatio == null) {
                        $('#entityTaskForm #completionRatio').val(0);
                    }
                    if (data.completionMoney == null) {
                        $('#entityTaskForm #completionMoney').val(0);
                    }
                    $('#form-modal').modal('show');
                }
            }
        });
    };

    cmProjectPlanLookModule.close = function () {
        $('#form-modal').modal('hide');
    };
</script>