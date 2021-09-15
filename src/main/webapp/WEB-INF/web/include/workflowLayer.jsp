<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal fade panel" id="submitNodesModal" aria-hidden="false">
    <div class="modal-dialog w900">
        <form class="table-wrap form-table" id="filingForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title" id="actionTitle">路由选择</h4>
                </div>
                <div class="modal-body" style="max-height: 489px;">
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td style="width:18%;"><span class="required">*</span>审批路由</td>
                            <td>
                                <div id="nodesDiv">
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer form-btn">
                    <button type="button" id="submitBtn" class="btn dark" >确 定</button>
                    <button type="button" class="btn" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>



<div class="modal fade panel" id="submitUserSelectModal" aria-hidden="false">
    <div class="modal-dialog w900">
        <form class="table-wrap form-table" id="filingForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title" id="actionTitle">人员选择</h4>
                </div>
                <div class="modal-body" id="userSelectDiv" style="max-height: 489px;">
                </div>
                <div class="modal-footer form-btn">
                    <button type="button" id="submitBtn" class="btn dark" >确 定</button>
                    <button type="button" class="btn" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>


<div class="modal fade panel" id="rejectSelectModal" aria-hidden="false">
    <div class="modal-dialog w900">
        <form class="table-wrap form-table" id="filingForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title" id="actionTitle">退回选择</h4>
                </div>
                <div class="modal-body" id="rejectSelectDiv" style="max-height: 489px;">
                </div>
                <div class="modal-footer form-btn">
                    <button type="button" id="submitBtn" class="btn dark" >确 定</button>
                    <button type="button" class="btn" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>


<div class="modal fade panel" id="gotoNodeSelectModal" aria-hidden="false">
    <div class="modal-dialog w900">
        <form class="table-wrap form-table" id="filingForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title" id="actionTitle">跳转选择</h4>
                </div>
                <div class="modal-body" id="gotoSelectDiv" style="max-height: 489px;">
                </div>
                <div class="modal-footer form-btn">
                    <button type="button" id="submitBtn" class="btn dark" >确 定</button>
                    <button type="button" class="btn" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>


<div class="modal fade panel" id="gotoUserSelectModal" aria-hidden="false">
    <div class="modal-dialog w900">
        <form class="table-wrap form-table" id="filingForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title" id="actionTitle">人员选择</h4>
                </div>
                <div class="modal-body" id="userSelectDiv" style="max-height: 489px;">
                </div>
                <div class="modal-footer form-btn">
                    <button type="button" id="submitBtn" class="btn dark" >确 定</button>
                    <button type="button" class="btn" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>


<div class="modal fade panel" id="moveSelectModal" aria-hidden="false">
    <div class="modal-dialog w900">
        <form class="table-wrap form-table" id="filingForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title" id="actionTitle">转办选择</h4>
                </div>
                <div class="modal-body" id="userSelectDiv" style="max-height: 489px;">
                </div>
                <div class="modal-footer form-btn">
                    <button type="button" id="submitBtn" class="btn dark" >确 定</button>
                    <button type="button" class="btn" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="${sysPath}/js/com/jc/workflow/form.all.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/jc/workflow/postscript.js"></script>