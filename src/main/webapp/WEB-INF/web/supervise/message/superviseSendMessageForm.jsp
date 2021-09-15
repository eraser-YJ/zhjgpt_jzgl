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
                    <input type="hidden" id="warningId" name="warningId" />
                    <input type="hidden" id="token" name="token" value="${data.token}">
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td class="w100">督办标题</td>
                            <td><input type="text" id="title" name="title" /></td>
                        </tr>
                        <tr>
                            <td>督办内容</td>
                            <td colspan="3"><textarea id="content" name="content" style="height: 100px;"></textarea></td>
                        </tr>
                        <tr>
                            <td>接收组织</td>
                            <td><select id="receiveDeptId" name="receiveDeptId"></select></td>
                        </tr>
                        <tr>
                            <td>接收人</td>
                            <td>
                                <input type="text" id="receiveName" name="receiveName" readonly="readonly" />
                                <input type="hidden" id="receiveId" name="receiveId" />
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn dark" type="button" id="saveBtn">保 存</button>
                <button class="btn" type="button" id="closeBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/supervise/message/superviseSendMessageForm.js'></script>