<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Bzxx" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityBzxxFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityBzxxForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="nowId" name="nowId" value="${id}"/>
                    <input type="hidden" id="nowDisPath" name="nowDisPath" value="${disPath}"/>
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>

                        <tr>
                            <td class="w140">班组编码</td>
                            <td>
                                <input type="text" readonly id="class_num" name="class_num"/>
                            </td>
                            <td class="w140">数据来源</td>
                            <td>
                                <input type="text" readonly id="dataSrc" name="dataSrc"/>
                            </td>
                        </tr>

                        <tr>
                            <td>班组名称</td>
                            <td colspan="3">
                                <input type="text" readonly id="class_bzmc" name="class_bzmc"/>
                            </td>
                        </tr>
                        <tr>
                            <td>班组工种</td>
                            <td>
                                <input type="text" readonly id="class_bzgz" name="class_bzgz"/>
                            </td>
                            <td>项目名称</td>
                            <td>
                                <input type="text" readonly id="projectName" name="projectName"/>
                            </td>
                        </tr>
                        <tr>
                            <td>进场日期</td>
                            <td>
                                <input type="text" readonly id="class_jcrq" name="class_jcrq"/>
                            </td>
                            <td>离场日期</td>
                            <td>
                                <input type="text" readonly id="class_ccrq" name="class_ccrq"/>
                            </td>
                        </tr>
                        <tr>
                            <td>班长名称</td>
                            <td>
                                <input type="text" readonly id="class_map_rymc" name="class_map_rymc"/>
                            </td>
                            <td>身份证号</td>
                            <td>
                                <input type="text" readonly id="person_cert_num" name="person_cert_num"/>
                            </td>
                        </tr>
                        <tr>
                            <td>证件类型</td>
                            <td>
                                <input type="text" readonly id="person_cert_type_value" name="person_cert_type_value"/>
                            </td>
                            <td>联系电话</td>
                            <td>
                                <input type="text" readonly id="person_tel" name="person_tel"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn" type="button" id="closeBzxxBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<script>
    var myBzxxJsForm = {};

    myBzxxJsForm.init = function(config) {
        $('#entityBzxxFormTitle').html(config.title);
        $("#entityBzxxForm #id").val(config.id);
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
                    $("#entityBzxxForm").fill(data);
                    if(data.dataSrc){
                        $("#entityBzxxForm #dataSrc").val(keyFun.dataSrcValue(data.dataSrc))
                    }
                    if(data.class_jcrq){
                        var crtTime = new Date(data.class_jcrq);
                        $("#entityBzxxForm #class_jcrq").val(dateFtt("yyyy-MM-dd", crtTime))
                    }
                    if(data.class_ccrq){
                        var crtTime = new Date(data.class_ccrq);
                        $("#entityBzxxForm #class_ccrq").val(dateFtt("yyyy-MM-dd", crtTime))
                    }
                    myBzxxJsForm.initAttach(data);
                    myBzxxJsForm.operatorModal('show');
                }
            }
        });
    };

    myBzxxJsForm.initAttach = function (data) {
        //keyFun.addAttach("pt_class_info",data.id,"attachFileList");
    };

    myBzxxJsForm.operatorModal = function (operator) {
        $('#form-modal-Bzxx').modal(operator);
    };

    $(document).ready(function(){
        ie8StylePatch();
        $(".datepicker-input").each(function(){$(this).datepicker();});
        $('#closeBzxxBtn') .click(function () { myBzxxJsForm.operatorModal('hide'); });
    });
</script>