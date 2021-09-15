<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Fbdw" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityFbdwFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityFbdwForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="nowId" name="nowId" value="${id}"/>
                    <input type="hidden" id="nowDisPath" name="nowDisPath" value="${disPath}"/>
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td>数据来源</td>
                            <td>
                                <input type="text" readonly id="dataSrc" name="dataSrc"/>
                            </td>
                            <td>参建企业类型</td>
                            <td>
                                <input type="text" readonly id="company_type_value" name="company_type_value"/>
                            </td>
                        </tr>
                        <tr>
                            <td>参建企业名称</td>
                            <td colspan="3">
                                <input type="text" readonly id="company_name" name="company_name"/>
                            </td>
                        </tr>
                        <tr>
                            <td>项目名称</td>
                            <td>
                                <input type="text" readonly id="project_name" name="project_name"/>
                            </td>
                            <td>参建企业统一<br>社会信用代码</td>
                            <td>
                                <input type="text" readonly id="contract_time_type" name="contract_time_type"/>
                            </td>
                        </tr>

                        <tr>
                            <td>负责人姓名</td>
                            <td>
                                <input type="text" readonly id="person_num" name="person_num"/>
                            </td>
                            <td>联系电话</td>
                            <td>
                                <input type="text" readonly id="person_tel" name="person_tel"/>
                            </td>
                        </tr>
                        <tr>
                            <td>进场时间</td>
                            <td>
                                <input type="text" readonly id="supervise_star" name="supervise_star"/>
                            </td>
                            <td>退场时间</td>
                            <td>
                                <input type="text" readonly id="supervise_end" name="supervise_end"/>
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
                <button class="btn" type="button" id="closeFbdwBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<script>
    var myFbdwJsForm = {};

    myFbdwJsForm.init = function(config) {
        $('#entityFbdwFormTitle').html(config.title);
        $("#entityFbdwForm #id").val(config.id);
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
                    $("#entityFbdwForm").fill(data);
                    if(data.supervise_star){
                        var crtTime = new Date(data.supervise_star);
                        $("#entityFbdwForm #supervise_star").val(dateFtt("yyyy-MM-dd", crtTime))
                    }
                    if(data.supervise_end){
                        var crtTime = new Date(data.supervise_end);
                        $("#entityFbdwForm #supervise_end").val(dateFtt("yyyy-MM-dd", crtTime))
                    }
                    if(data.dataSrc){
                        $("#entityFbdwForm #dataSrc").val(keyFun.dataSrcValue(data.dataSrc))
                    }
                    myFbdwJsForm.initAttach(data);
                    myFbdwJsForm.operatorModal('show');
                }
            }
        });
    };

    myFbdwJsForm.operatorModal = function (operator) {
        $('#form-modal-Fbdw').modal(operator);
    };

    myFbdwJsForm.initAttach = function (data) {
        //keyFun.addAttach("pt_company_supervise",data.id,"attachFileList");
    };

    $(document).ready(function(){
        ie8StylePatch();
        $(".datepicker-input").each(function(){$(this).datepicker();});
        $('#closeFbdwBtn') .click(function () { myFbdwJsForm.operatorModal('hide'); });
    });
</script>