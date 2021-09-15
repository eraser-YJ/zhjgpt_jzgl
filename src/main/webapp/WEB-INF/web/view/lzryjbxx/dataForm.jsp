<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Lzryjbxx" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityLzryjbxxFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityLzryjbxxForm">
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
                            <td>联系电话</td>
                            <td>
                                <input type="text" readonly id="lzry_tel" name="lzry_tel"/>
                            </td>
                        </tr>
                        <tr>
                            <td>学历</td>
                            <td>
                                <input type="text" readonly id="person_education_value" name="person_education_value"/>
                            </td>
                            <td>学位</td>
                            <td>
                                <input type="text" readonly id="person_xuewei_value" name="person_xuewei_value"/>
                            </td>
                        </tr>
                        <tr>
                            <td>参与项目数</td>
                            <td>
                                <input type="text" readonly id="projectCount" name="projectCount"/>
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
                <button class="btn" type="button" id="closeLzryjbxxBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<script>
    var myLzryjbxxJsForm = {};

    myLzryjbxxJsForm.init = function(config) {
        $('#entityLzryjbxxFormTitle').html(config.title);
        $("#entityLzryjbxxForm #id").val(config.id);
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
                    $("#entityLzryjbxxForm").fill(data);
                    if(data.dataSrc){
                        $("#entityLzryjbxxForm #dataSrc").val(keyFun.dataSrcValue(data.dataSrc))
                    }
                    myLzryjbxxJsForm.operatorModal('show');
                }
            }
        });
    };

    myLzryjbxxJsForm.operatorModal = function (operator) {
        $('#form-modal-Lzryjbxx').modal(operator);
    };

    $(document).ready(function(){
        ie8StylePatch();
        $(".datepicker-input").each(function(){$(this).datepicker();});
        $('#closeLzryjbxxBtn') .click(function () { myLzryjbxxJsForm.operatorModal('hide'); });
    });
</script>