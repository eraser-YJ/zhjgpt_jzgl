<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/taglib.jsp" %>
<%@ taglib prefix="dicex" uri="/WEB-INF/tlds/dicex-tags.tld" %>
<div class="modal fade panel" id="form-modal" aria-hidden="false">
    <div class="modal-dialog w900">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">×</button>
                <h4 class="modal-title" id="entityFormTitle"></h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="entityForm">
                    <input type="hidden" id="id" name="id"/>
                    <input type="hidden" id="modifyDate" name="modifyDate"/>
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td style="width: 120px;"><span class='required'>*</span>年度</td>
                            <td>
                                <select id="planYear" name="planYear">
                                </select>
                            </td>
                            <td style="width: 120px;"><span class='required'>*</span>类型</td>
                            <td>
                                <dicex:reqTypeTag name="seqno" id="seqno"  headName="-请选择-" headValue=""/>
                            </td>
                        </tr>
                        <tr>
                            <td ><span class='required'>*</span>名称</td>
                            <td colspan="3">
                                <input type="text" id="extStr4" name="extStr4">
                            </td>
                        </tr>
                        <tr>
                            <td><span class='required'>*</span>开始日期</td>
                            <td>
                                <input class="datepicker-input" type="text" id="requestStartDate" name="requestStartDate" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#requestEndDate lt">
                            </td>
                            <td><span class='required'>*</span>结束日期</td>
                            <td>
                                <input class="datepicker-input" type="text" id="requestEndDate" name="requestEndDate" data-pick-time="false" data-date-format="yyyy-MM-dd"  data-ref-obj="#requestStartDate gt">
                            </td>

                        </tr>
                        <tr>
                            <td>备注</td>
                            <td colspan="3">
                                <textarea id="remark" name="remark" rows="3"></textarea>
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
<script src='${sysPath}/js/com/jc/csmp/plan/common/commonplan.js'></script>
<script src='${sysPath}/js/com/jc/csmp/plan/ctrl/projectYearPlanCtrlForm.js'></script>