<%@ page language="java" pageEncoding="UTF-8" import="com.jc.csmp.project.plan.domain.CmProjectPlanTemplate,java.util.*" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<% List<CmProjectPlanTemplate> chooseTemplateList = (List<CmProjectPlanTemplate>)request.getAttribute("templateList"); %>
<div class="modal fade panel" id="choose-modal" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="$('#choose-modal').modal('hide');">×</button>
                <h4 class="modal-title">选择模板</h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="chooseTemplateForm">
                    <input type="hidden" id="chooseProjectId" name="chooseProjectId" value="${projectId}" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td class="w100"><span class='required'>*</span>计划模板</td>
                            <td>
                                <select id="chooseTemplateId" name="chooseTemplateId">
                                    <option value="">-请选择模板-</option>
                                    <% for (CmProjectPlanTemplate cpt : chooseTemplateList) { %>
                                    <option value="<%=cpt.getId() %>"><%=cpt.getTemplateName() %></option>
                                    <% } %>
                                </select>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn dark" type="button" onclick="projectPlanChooseTemplateModule.submit();">选 择</button>
                <button class="btn" type="button" onclick="$('#choose-modal').modal('hide');">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script>
    var projectPlanChooseTemplateModule = {};
    projectPlanChooseTemplateModule.callback = null;
    projectPlanChooseTemplateModule.init = function(config) {
        projectPlanChooseTemplateModule.callback = config.callback;
        $('#choose-modal').modal('show');
    };
    projectPlanChooseTemplateModule.submit = function () {
        if ($('#chooseTemplateForm #chooseTemplateId').val() == "") {
            msgBox.tip({ type: "fail", content: "请选择模板" });
            return;
        }
        var projectId = $('#chooseTemplateForm #chooseProjectId').val();
        $.ajax({
            type: "POST", dataType: "json",
            url: getRootPath() + "/project/plan/info/chooseTemplate.action",
            data: {"projectId": projectId, templateId: $('#chooseTemplateForm #chooseTemplateId').val()},
            success : function(data) {
                if (data.code == 0) {
                    projectPlanChooseTemplateModule.callback(projectId);
                } else {
                    msgBox.tip({ type: "fail", content: data.msg });
                    return;
                }
            }
        });
    };
</script>