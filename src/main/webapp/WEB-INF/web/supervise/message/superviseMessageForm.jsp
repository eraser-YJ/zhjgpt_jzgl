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
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td class="w100">标题</td>
                            <td colspan="3"><input type="text" id="title" name="title" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td>内容</td>
                            <td colspan="3"><textarea id="content" name="content" style="height: 100px;" readonly="readonly"></textarea></td>
                        </tr>
                        <tr>
                            <td>发送人</td>
                            <td><input type="text" id="senderName" name="senderName" readonly="readonly" /></td>
                            <td>发送人所属部门</td>
                            <td><input type="text" id="senderDeptName" name="senderDeptName" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td>接收人</td>
                            <td><input type="text" id="receiveName" name="receiveName" readonly="readonly" /></td>
                            <td>接收人所属部门</td>
                            <td><input type="text" id="receiveDeptName" name="receiveDeptName" readonly="readonly" /></td>
                        </tr>
                        <tr>
                            <td id="handleResultTr">办理结果</td>
                            <td colspan="3"><textarea id="handleResult" name="handleResult" style="height: 100px;"></textarea></td>
                        </tr>
                        <tr>
                            <td>办理状态</td>
                            <td><input type="text" id="handleStatusValue" name="handleStatusValue" readonly="readonly" /></td>
                            <td>办理时间</td>
                            <td><input type="text" id="handleDate" name="handleDate" readonly="readonly" /></td>
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
<script src='${sysPath}/js/com/jc/supervise/message/superviseMessageForm.js'></script>