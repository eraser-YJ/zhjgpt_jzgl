<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Lzrykqxx" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityLzrykqxxFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityLzrykqxxForm">
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
                            <td>项目</td>
                            <td>
                                <input type="text" readonly id="project_name" name="project_name"/>
                            </td>
                        </tr>
                        <tr>
                            <td>所属班组</td>
                            <td >
                                <input type="text" readonly id="class_bzmc" name="class_bzmc"/>
                            </td>
                            <td>考勤时间</td>
                            <td >
                                <input type="text" readonly id="lzry_kqny" name="lzry_kqny"/>
                            </td>
                        </tr>
                        <tr>
                            <td>考勤状态</td>
                            <td >
                                <input type="text" readonly id="lzry_cqts" name="lzry_cqts"/>
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
                <button class="btn" type="button" id="closeLzrykqxxBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<script>
    var myLzrykqxxJsForm = {};

    myLzrykqxxJsForm.init = function(config) {
        $('#entityLzrykqxxFormTitle').html(config.title);
        $("#entityLzrykqxxForm #id").val(config.id);
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
                    $("#entityLzrykqxxForm").fill(data);
                    if(data.lzry_kqny){
                        var crtTime = new Date(data.lzry_kqny);
                        $("#entityLzrykqxxForm #lzry_kqny").val(dateFtt("yyyy-MM-dd", crtTime))
                    }
                    if(data.dataSrc){
                        $("#entityLzrykqxxForm #dataSrc").val(keyFun.dataSrcValue(data.dataSrc))
                    }
                    myLzrykqxxJsForm.operatorModal('show');
                }
            }
        });
    };

    myLzrykqxxJsForm.operatorModal = function (operator) {
        $('#form-modal-Lzrykqxx').modal(operator);
    };

    $(document).ready(function(){
        ie8StylePatch();
        $(".datepicker-input").each(function(){$(this).datepicker();});
        $('#closeLzrykqxxBtn') .click(function () { myLzrykqxxJsForm.operatorModal('hide'); });
    });
</script>