<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Htxx" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityHtxxFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityHtxxForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="nowId" name="nowId" value="${id}"/>
                    <input type="hidden" id="nowDisPath" name="nowDisPath" value="${disPath}"/>
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td>合同编号</td>
                            <td>
                                <input type="text" readonly id="contract_num" name="contract_num"/>
                            </td>
                            <td>数据来源</td>
                            <td>
                                <input type="text" readonly id="dataSrc" name="dataSrc"/>
                            </td>
                        </tr>
                        <tr>
                            <td>企业名称</td>
                            <td colspan="3">
                                <input type="text" readonly id="contract_qymc" name="contract_qymc"/>
                            </td>
                        </tr>
                        <tr>
                            <td>项目名称</td>
                            <td>
                                <input type="text" readonly id="contract_project_name" name="contract_project_name"/>
                            </td>
                            <td>合同期限类型</td>
                            <td>
                                <input type="text" readonly id="contract_time_type_value" name="contract_time_type_value"/>
                            </td>
                        </tr>
                        <tr>
                            <td>人员姓名</td>
                            <td>
                                <input type="text" readonly id="contract_rymc" name="contract_rymc"/>
                            </td>
                            <td>身份证号</td>
                            <td>
                                <input type="text" readonly id="person_num" name="person_num"/>
                            </td>
                        </tr>
                        <tr>
                            <td>计量单位</td>
                            <td>
                                <input type="text" readonly id="contract_jldw_value" name="contract_jldw_value"/>
                            </td>
                            <td>计量单价</td>
                            <td>
                                <input type="text" readonly id="contract_jldj" name="contract_jldj"/>
                            </td>
                        </tr>
                        <tr>
                            <td>开始日期</td>
                            <td>
                                <input type="text" readonly id="contract_star" name="contract_star"/>
                            </td>
                            <td>结束日期</td>
                            <td>
                                <input type="text" readonly id="contract_end" name="contract_end"/>
                            </td>
                        </tr>
                        <%--<tr>--%>
                            <%--<td>附件</td>--%>
                            <%--<td colspan="3">--%>
                                <%--<div id="attachFileList" style="width:100%"></div>--%>
                            <%--</td>--%>
                        <%--</tr>--%>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn" type="button" id="closeHtxxBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<script>
    var myHtxxJsForm = {};

    myHtxxJsForm.init = function(config) {
        $('#entityHtxxFormTitle').html(config.title);
        $("#entityHtxxForm #id").val(config.id);
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
                    $("#entityHtxxForm").fill(data);
                    if(data.dataSrc){
                        $("#entityHtxxForm #dataSrc").val(keyFun.dataSrcValue(data.dataSrc))
                    }
                    if(data.contract_star){
                        var crtTime = new Date(data.contract_star);
                        $("#entityHtxxForm #contract_star").val(dateFtt("yyyy-MM-dd", crtTime))
                    }
                    if(data.contract_end){
                        var crtTime = new Date(data.contract_end);
                        $("#entityHtxxForm #contract_end").val(dateFtt("yyyy-MM-dd", crtTime))
                    }
                    myHtxxJsForm.initAttach(data);
                    myHtxxJsForm.operatorModal('show');
                }
            }
        });
    };

    myHtxxJsForm.operatorModal = function (operator) {
        $('#form-modal-Htxx').modal(operator);
    };

    myHtxxJsForm.initAttach = function (data) {
        //keyFun.addAttach("pt_contract_info",data.id,"attachFileList");
        //keyFun.addAttach("pt_company_projects","0852111","attachFileList");
    };

    $(document).ready(function(){
        ie8StylePatch();
        $(".datepicker-input").each(function(){$(this).datepicker();});
        $('#closeHtxxBtn') .click(function () { myHtxxJsForm.operatorModal('hide'); });
    });
</script>