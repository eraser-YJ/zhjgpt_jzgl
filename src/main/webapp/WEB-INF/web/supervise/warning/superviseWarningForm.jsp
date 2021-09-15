<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="token" name="token" value="${data.token}">
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td class="w100">项目统一编码</td>
                            <td><input type="text" id="projectNumber" name="projectNumber" readonly="readonly" /></td>
                            <td class="w100">项目名称</td>
                            <td><input type="text" id="projectName" name="projectName" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td>监测时间</td>
                            <td><input type="text" id="supervisionDate" name="supervisionDate" readonly="readonly" /></td>
                            <td>预警原因</td>
                            <td><input type="text" id="supervisionPointName" name="supervisionPointName" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td>预警内容</td>
                            <td colspan="3"><textarea id="warningContent" name="warningContent" style="height: 100px;" readonly="readonly"></textarea></td>
                        </tr>
                        <tr>
                            <td>处理状态</td>
                            <td colspan="3"><input type="text" id="statusValue" name="statusValue" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td id="disploseResultTitle">处理结果</td>
                            <td colspan="3"><textarea id="disploseResult" name="disploseResult" style="height: 100px;"></textarea></td>
                        </tr>
                        <tr>
                            <td>处理人</td>
                            <td><input type="text" id="disploseUserName" name="disploseUserName" readonly="readonly" /></td>
                            <td>处理时间</td>
                            <td><input type="text" id="disploseDate" name="disploseDate" readonly="readonly" /></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn dark" type="button" id="saveBtn" style="display:none;">保 存</button>
                <button class="btn" type="button" id="closeBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/supervise/warning/superviseWarningForm.js'></script>