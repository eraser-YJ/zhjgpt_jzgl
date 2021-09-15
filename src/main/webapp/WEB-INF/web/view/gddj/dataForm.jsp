<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Gddj" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityGddjFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityGddjForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="nowId" name="nowId" value="${id}"/>
                    <input type="hidden" id="nowDisPath" name="nowDisPath" value="${disPath}"/>
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td>项目编码</td>
                            <td>
                                <input type="text" readonly id="projectNumber" name="projectNumber"/>
                            </td>
                            <td>项目名称</td>
                            <td>
                                <input type="text" readonly id="projectName" name="projectName"/>
                            </td>
                        </tr>
                        <tr>
                            <td>省平台对接情况</td>
                            <td>
                                <input type="text" readonly id="src1Value" name="src1Value"/>
                            </td>
                            <td>市平台对接情况</td>
                            <td>
                                <input type="text" readonly id="src2Value" name="src2Value"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn" type="button" id="closeGddjBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<script>
    var myGddjJsForm = {};

    myGddjJsForm.init = function(config) {
        $('#entityGddjFormTitle').html(config.title);
        $("#entityGddjForm #id").val(config.id);
        var nowId = $("#nowId").val();
        var nowDisPath = $("#nowDisPath").val();
        $.ajax({
            type : "GET",
            data : {id: nowId,disPath:nowDisPath},
            dataType : "json",
            url : getRootPath() + "/mock/view/get.action",
            success : function(data) {
                if (data) {
                    hideErrorMessage();
                    $("#entityGddjForm").fill(data);
                    if(data.src1>0){
                        $("#src1Value").val("已对接");
                    } else {
                        $("#src1Value").val("");
                    }
                    if(data.src2>0){
                        $("#src2Value").val("已对接");
                    } else {
                        $("#src2Value").val("");
                    }
                    myGddjJsForm.operatorModal('show');
                }
            }
        });
    };

    myGddjJsForm.operatorModal = function (operator) {
        $('#form-modal-Gddj').modal(operator);
    };

    $(document).ready(function(){
        ie8StylePatch();
        $(".datepicker-input").each(function(){$(this).datepicker();});
        $('#closeGddjBtn') .click(function () { myGddjJsForm.operatorModal('hide'); });
    });
</script>