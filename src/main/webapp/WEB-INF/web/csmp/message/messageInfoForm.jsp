<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title">消息查看</h4>
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
                            <td>阅读状态</td>
                            <td><input type="text" id="readStatusValue" name="readStatusValue" readonly="readonly" /></td>
                            <td>阅读时间</td>
                            <td><input type="text" id="readDate" name="readDate" readonly="readonly" /></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn" type="button" id="closeBtn">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/csmp/message/messageInfoForm.js'></script>