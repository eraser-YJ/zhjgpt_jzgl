<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal-Ryjbxx" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityRyjbxxFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityRyjbxxForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="nowId" name="nowId" value="${id}"/>
                    <input type="hidden" id="nowDisPath" name="nowDisPath" value="${disPath}"/>
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td>项目编码</td>
                            <td>
                                <input type="text" readonly id="project_number" name="project_number"/>
                            </td>
                            <td>数据来源</td>
                            <td>
                                <input type="text" readonly id="dataSrc" name="dataSrc"/>
                            </td>
                        </tr>
                        <tr>
                            <td>项目名称</td>
                            <td colspan="3">
                                <input type="text" readonly id="project_name" name="project_name"/>
                            </td>
                        </tr>
                        <tr>
                            <td>姓名</td>
                            <td>
                                <input type="text" readonly id="person_name" name="person_name"/>
                            </td>
                            <td>身份证号</td>
                            <td>
                                <input type="text" readonly id="person_cert_num" name="person_cert_num"/>
                            </td>
                        </tr>
                        <tr>
                            <td>性别</td>
                            <td>
                                <input type="text" readonly id="person_sex_value" name="person_sex_value"/>
                            </td>
                            <td>民族</td>
                            <td>
                                <input type="text" readonly id="person_nation_value" name="person_nation_value"/>
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
                            <td>注册类型</td>
                            <td>
                                <input type="text" readonly id="reg_type" name="reg_type"/>
                            </td>
                            <td>注册证书号</td>
                            <td>
                                <input type="text" readonly id="reg_num" name="reg_num"/>
                            </td>
                        </tr>
                        <tr>
                            <td>联系电话</td>
                            <td >
                                <input type="text" readonly id="person_tel" name="person_tel"/>
                            </td>
                            <td>岗位类型</td>
                            <td>
                                <input type="text" readonly id="gwlx" name="gwlx"/>
                            </td>
                        </tr>
                        <tr>
                            <td>联系人</td>
                            <td >
                                <input type="text" readonly id="person_lxr" name="person_lxr"/>
                            </td>
                            <td>联系地址</td>
                            <td >
                                <input type="text" readonly id="person_address" name="person_address"/>
                            </td>
                        </tr>
                        <tr>
                            <td>班组名称</td>
                            <td >
                                <input type="text" readonly id="class_bzmc" name="class_bzmc"/>
                            </td>
                            <td>班组工种</td>
                            <td >
                                <input type="text" readonly id="class_bzgz" name="class_bzgz"/>
                            </td>
                        </tr>
                        <tr>
                            <td>进场日期</td>
                            <td >
                                <input type="text" readonly id="class_jcrq" name="class_jcrq"/>
                            </td>
                            <td>出场日期</td>
                            <td >
                                <input type="text" readonly id="class_ccrq" name="class_ccrq"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn" type="button" id="closeRyjbxxBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/common/datetools.js'></script>
<script src='${sysPath}/js/com/jc/common/kit.js'></script>
<script>
    var myRyjbxxJsForm = {};

    myRyjbxxJsForm.init = function(config) {
        $('#entityRyjbxxFormTitle').html(config.title);
        $("#entityRyjbxxForm #id").val(config.id);
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
                    $("#entityRyjbxxForm").fill(data);
                    if(data.dataSrc){
                        $("#entityRyjbxxForm #dataSrc").val(keyFun.dataSrcValue(data.dataSrc))
                    }
                    if(data.class_jcrq){
                        var crtTime = new Date(data.class_jcrq);
                        $("#entityRyjbxxForm #class_jcrq").val(dateFtt("yyyy-MM-dd", crtTime))
                    }
                    if(data.class_ccrq){
                        var crtTime = new Date(data.class_ccrq);
                        $("#entityRyjbxxForm #class_ccrq").val(dateFtt("yyyy-MM-dd", crtTime))
                    }
                    myRyjbxxJsForm.operatorModal('show');
                }
            }
        });
    };

    myRyjbxxJsForm.operatorModal = function (operator) {
        $('#form-modal-Ryjbxx').modal(operator);
    };

    $(document).ready(function(){
        ie8StylePatch();
        $(".datepicker-input").each(function(){$(this).datepicker();});
        $('#closeRyjbxxBtn') .click(function () { myRyjbxxJsForm.operatorModal('hide'); });
    });
</script>