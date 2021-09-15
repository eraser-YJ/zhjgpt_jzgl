<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-WarnDetail" aria-hidden="false">
    <div class="modal-dialog" style="width:1500px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityWarnDetailFormTitle">详细信息</h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityWarnDetailForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="nowId" name="nowId" value="${id}"/>
                    <input type="hidden" id="nowDisPath" name="nowDisPath" value="${disPath}"/>
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td>项目</td>
                            <td colspan="3"> <input type="text" readonly id="targetProjectName" name="targetProjectName"/></td>
                            <td>设备编码</td>
                            <td> <input type="text" readonly id="targetCode" name="targetCode"/></td>
                        </tr>
                        <tr>
                            <td>报警原因</td>
                            <td colspan=3"> <input type="text" readonly id="warnReason" name="warnReason"/></td>
                            <td>报警发生时间</td>
                            <td> <input type="text" readonly id="warnTime" name="warnTime"/></td>
                        </tr>
                        <tr>
                            <td>开始时间</td>
                            <td> <input type="text" readonly id="str01" name="str01"/></td>
                            <td>结束时间</td>
                            <td> <input type="text" readonly id="str02" name="str02"/></td>
                            <td>事件等级</td>
                            <td> <input type="text" readonly id="str03" name="str03"/></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn" type="button" id="closeWarnDetailBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script>
    var warnInfoJsForm = {};

    warnInfoJsForm.init = function() {

        var nowId = $("#nowId").val();
        $.ajax({
            type : "GET",
            data : {id: nowId},
            dataType : "json",
            url : getRootPath() + "/warn/info/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityWarnDetailForm").fill(data);

                }
                warnInfoJsForm.operatorModal('show');
            }
        });
    };

    warnInfoJsForm.operatorModal = function (operator) {
        $('#form-modal-WarnDetail').modal(operator);
    };

    $(document).ready(function(){
        ie8StylePatch();
        warnInfoJsForm.init();
        $(".datepicker-input").each(function(){$(this).datepicker();});
        $('#closeWarnDetailBtn') .click(function () { warnInfoJsForm.operatorModal('hide'); });
    });
</script>