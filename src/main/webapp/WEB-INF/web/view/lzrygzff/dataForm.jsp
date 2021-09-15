<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Lzrygzff" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityLzrygzffFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityLzrygzffForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="nowId" name="nowId" value="${id}"/>
                    <input type="hidden" id="nowDisPath" name="nowDisPath" value="${disPath}"/>
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td>姓名</td>
                            <td>
                                <input type="text" readonly id="lzry_xm" name="lzry_xm"/>
                            </td>
                            <td>身份证号</td>
                            <td>
                                <input type="text" readonly id="lzry_sfz_num" name="lzry_sfz_num"/>
                            </td>
                        </tr>
                        <tr>
                            <td>性别</td>
                            <td>
                                <input type="text" readonly id="lzry_sex_value" name="lzry_sex_value"/>
                            </td>
                            <td>状态</td>
                            <td>
                                <input type="text" readonly id="pay_status" name="pay_status"/>
                            </td>
                        </tr>

                        <tr>
                            <td>工资月份</td>
                            <td>
                                <input type="text" readonly id="pay_moth" name="pay_moth"/>
                            </td>
                            <td>实发金额</td>
                            <td>
                                <input type="text" readonly id="pay_count" name="pay_count"/>
                            </td>
                        </tr>
                        <tr>
                            <td>项目名称</td>
                            <td>
                                <input type="text" readonly id="project_name" name="project_name"/>
                            </td>
                            <td>数据来源</td>
                            <td>
                                <input type="text" readonly id="dataSrc" name="dataSrc"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn" type="button" id="closeLzrygzffBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<script>
    var myLzrygzffJsForm = {};

    myLzrygzffJsForm.init = function(config) {
        $('#entityLzrygzffFormTitle').html(config.title);
        $("#entityLzrygzffForm #id").val(config.id);
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
                    $("#entityLzrygzffForm").fill(data);
                    if(data.dataSrc){
                        $("#entityLzrygzffForm #dataSrc").val(keyFun.dataSrcValue(data.dataSrc))
                    }
                    myLzrygzffJsForm.operatorModal('show');
                }
            }
        });
    };

    myLzrygzffJsForm.operatorModal = function (operator) {
        $('#form-modal-Lzrygzff').modal(operator);
    };

    $(document).ready(function(){
        ie8StylePatch();
        $(".datepicker-input").each(function(){$(this).datepicker();});
        $('#closeLzrygzffBtn') .click(function () { myLzrygzffJsForm.operatorModal('hide'); });
    });
</script>