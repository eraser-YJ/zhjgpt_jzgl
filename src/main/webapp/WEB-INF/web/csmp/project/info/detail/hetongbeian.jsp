<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="navigationMenu">
        <div class="crumbs" id="crumbsHeaderTitle">
            <span>项目管理</span>
            <span>工程信息管理 > </span><span>项目查看 > </span><span>合同查询</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div style="background: #ededed;margin-top:10px;">
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=detail&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">详细信息</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=zhaobiao&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">招标信息</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=zhongbiao&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">中标信息</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=hetongbeian&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl gxxxgl-active">合同备案</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=hetongzhifu&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">合同支付情况</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=biangengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">变更单</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=qianzhengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">工程签证单</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=wenti&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">问题协同</a>
        </div>
        <div class="search-line">
            <form class="table-wrap form-table" id="searchForm">
                <input type="hidden" id="projectId" name="projectId" value="${id}" />
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width: 8%">合同编号</td>
                        <td style="width: 25%"><input type="text" id="contractCode" name="contractCode" /></td>
                        <td>合同名称</td>
                        <td><input type="text" id="contractName" name="contractName" /></td>
                        <td>合同类型</td>
                        <td><dic:select name="contractType" id="contractType" dictName="contract_type" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
                    </tr>
                    <tr>
                        <td>合同开始结束时间</td>
                        <td>
                            <div class="input-group w-p100">
                                <input class="datepicker-input" type="text" id="startDate" name="startDate" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#endDate lt">
                                <div class="input-group-btn w30">-</div>
                                <input class="datepicker-input" type="text" id="endDate" name="endDate" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#startDate gt">
                            </div>
                        </td>
                        <td>签订时间</td>
                        <td>
                            <div class="input-group w-p100">
                                <input class="datepicker-input" type="text" id="signDateBegin" name="signDateBegin" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#signDateEnd lt">
                                <div class="input-group-btn w30">-</div>
                                <input class="datepicker-input" type="text" id="signDateEnd" name="signDateEnd" data-pick-time="false" data-date-format="yyyy-MM-dd" data-ref-obj="#signDateBegin gt">
                            </div>
                        </td>
                        <td>审核状态</td>
                        <td><dic:select name="auditStatus" id="auditStatus" dictName="workflow_audit_state" parentCode="csmp" defaultValue="" headType="1" headValue="" /></td>
                    </tr>
                    </tbody>
                </table>
                <div  class="btn-tiwc">
                    <button class="btn" type="button" id="queryBtn">查 询</button>
                    <button class="btn" type="button" id="resetBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <section class="panel ">
        <div class="table-wrap">
            <table class="table table-striped tab_height" id="gridTable"></table>
        </div>
        <section class="clearfix" id="footer_height"></section>
    </section>
    <div id="formModule"></div>
    <div id="chooseCompanyModule"></div>
</section>
<script>
    function oTableSetButtons(source) {
        var look = "<a class=\"a-icon i-edit\" href=\"javascript:void(0);\" onclick=\"cmContractInfoJsList.loadModuleForLook('"+ source.id+ "')\" role=\"button\">查看</a>";
        var history = "<a class=\"a-icon i-edit\" href=\"javascript:void(0);\" onclick=\"footerToolsModule.workflowHistory('"+ source.piId+ "')\" role=\"button\">流程历史</a>";
        return look + history;
    }
</script>
<script src='${sysPath}/js/com/jc/csmp/project/info/detail/hetongbeian.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>