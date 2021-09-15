<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="titledic">字典设置</h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="dicForm">
                    <input type="hidden" id="id" name="id">
                    <input type="hidden" id="token" name="token" value="${data.token}">
                    <input type="hidden" id="modifyDate" name="modifyDate">
                    <input type="hidden" name="parentId" id="parentId"  value="${parentId }">
                    <input type="hidden" name="parentType" id="parentType"  value="${parentType }">
                    <input type='hidden' id="typeFlag" name="typeFlag" value="1">
                    <input type='hidden' id="dicFlag" name="dicFlag" value="1">
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td style="width: 30%;"><span class="required">*</span>内容</td>
                            <td style="width: 70%;">
                                <input type="text" maxlength="20" name="value" id="value" >
                            </td>
                        </tr>
                        <tr>
                            <td><span class="required">*</span>编码</td>
                            <td>
                                <input type="text" maxlength="20" name="code" id="code" >
                                <input type="hidden" id="codeOld" name="codeOld"/>
                            </td>
                        </tr>
                        <tr>
                            <td><span class="required">*</span>排序</td>
                            <td>
                                <input type="text" maxlength="2" name="orderFlag" id="orderFlag"  >
                            </td>
                        </tr>
                        <tr>
                            <td>是否启用</td>
                            <td>
                                <input type="radio" name="useFlag" id="useFlag1" value="1" checked/> 启用
                                <input type="radio" name="useFlag" id="useFlag2" value="0" /> 不启用
                            </td>
                        </tr>
                        <tr>
                            <td>是否设为默认值</td>
                            <td>
                                <input type="radio" name="defaultValue" id="defaultValue1"  value="1" />是
                                <input type="radio" name="defaultValue" id="defaultValue2"  value="0" checked />否
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn">
                <button class="btn dark" type="button" id="dicsbtn">保 存</button>
                <button class="btn" type="button" onclick="$('#myModal-edit').modal('hide');">关 闭</button>
            </div>
        </div>
    </div>
</div>
<script src="${sysPath}/js/com/sys/dic/dicsEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/dic/dics.validate.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/dic/Data.dictionary.js" type="text/javascript"></script>