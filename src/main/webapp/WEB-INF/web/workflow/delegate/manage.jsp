<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script type="text/javascript">
    function oTableSetButtons( source) {
        var update = '<a class="a-icon i-new" href="#" onclick="delegate.loadModal(\''+source.id+'\')" role="button" data-toggle="modal"><i class="fa fa-edit2" data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="编辑"></i></a>';
        var stopBtnStr = '<a class="a-icon i-new" href="#" onclick="delegate.stop(\''+source.id+'\')" role="button">停用</a>';
        var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"delegate.deleteFun('"+source.id+"')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
        var result =del;
        if(source.useFlag == 1){
            result = update+stopBtnStr+del;
        }
        return  result;
    };
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in">
        <div class="con-heading fl" id="navigationMenu">
            <h1></h1>
            <div class="crumbs"></div>
        </div>
    </header>
    <section class="panel search-shrinkage clearfix">
        <div class="search-line hide">
            <form class="table-wrap form-table" id="delegateQueryForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td class="w140">委托流程</td>
                        <td>
                            <select id="definitionIdForQuery" name="definitionId" style="width: 100%;">
                                <option value="">全部</option>
                                <option value="all">所有流程</option>
                                <c:forEach var="definition" items="${definitions}">
                                    <option value="${definition.definitionId}">${definition.name}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td class="w140">委托人</td>
                        <td style="width: 45%">
                            <div id="delegateUserForQueryDiv"></div>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <section class="form-btn m-b-lg">
                    <button class="btn dark query-jump" type="button" id="queryForm">查 询</button>
                    <button class="btn" type="button" id="queryReset">重 置</button>
                </section>
            </form>
        </div>
        <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
    </section>
    <section class="panel">
        <div class="table-wrap">
            <table id="delegateTable" class="table table-striped first-td-tc tab_height">
                <thead>
                    <tr>
                        <th>委托人</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>委托流程</th>
                        <th>状态</th>
                        <th class="w200">操作</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
                <a class="btn dark" href="#" role="button" onclick="delegate.showModal()">新 增</a>
            </section>
        </section>
    </section>
</section>

<div class="modal fade panel" id="delegateModal" aria-hidden="false">
    <div class="modal-dialog">
        <form id="delegateForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">委托</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="id" name="id">
                    <input type="hidden" id="useFlag" name="useFlag">
                    <div class="form-table">
                        <table class="table table-td-striped">
                            <tbody>
                                <tr>
                                    <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>委托人</td>
                                    <td style="width: 85%;">
                                        <div id="delegateUserDiv"></div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>委托流程</td>
                                    <td style="width: 85%;">
                                        <div>
                                            <select id="definitionId" name="definitionId" class="form-control" style="width: 100%;">
                                                <option value="all">所有流程</option>
                                                <c:forEach var="definition" items="${definitions}">
                                                    <option value="${definition.definitionId}">${definition.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>开始时间</td>
                                    <td style="width: 85%;">
                                        <input class="form-control datepicker-input" type="text" id="startTime" name="startTime" data-date-format="yyyy-MM-dd" data-ref-obj="#endTime lt"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>结束时间</td>
                                    <td style="width: 85%;">
                                        <input class="form-control datepicker-input" type="text" id="endTime" name="endTime" data-date-format="yyyy-MM-dd" data-ref-obj="#startTime gt"/>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
                <div class="modal-footer no-all form-btn">
                    <button class="btn dark" type="button" onclick="delegate.save()">保 存</button>
                    <button class="btn" type="button" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="${sysPath}/js/com/jc/workflow/delegate/delegate.js"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>