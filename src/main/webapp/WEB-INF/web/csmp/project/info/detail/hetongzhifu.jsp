<%@ page language="java" pageEncoding="UTF-8" import="com.jc.csmp.common.enums.WorkflowAuditStatusEnum"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="navigationMenu">
        <span>项目管理</span>
        <span>工程信息管理 > </span><span>项目查看 > </span><span>合同支付查询</span>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div style="background: #ededed;margin-top:10px;">
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=detail&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">详细信息</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=zhaobiao&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">招标信息</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=zhongbiao&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">中标信息</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=hetongbeian&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">合同备案</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=hetongzhifu&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl gxxxgl-active">合同支付情况</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=biangengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">变更单</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=qianzhengdan&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">工程签证单</a>
            <a onclick="parent.JCF.LoadPage({url: '/project/info/detailTab.action?id=${id}&p=wenti&projectNumber=${projectNumber}' });" href="javascript:void(0);" class="gcxxgl">问题协同</a>
        </div>
        <div class="search-line">
            <form class="table-wrap form-table" id="searchForm">
                <table class="table table-td-striped">
                    <tbody>
                    <tr>
                        <td style="width: 8%;">合同编号</td>
                        <td style="width: 25%;"><input type="text" id="contractCode" name="contractCode" /></td>
                        <td style="width: 8%;">合同名称</td>
                        <td style="width: 25%;"><input type="text" id="contractName" name="contractName" /></td>
                        <td style="width: 8%;">审核状态</td>
                        <td style="width: 25%;">
                            <select id="auditStatus" name="auditStatus">
                                <option value="">- 请选择 -</option>
                                <option value="<%=WorkflowAuditStatusEnum.ing.toString() %>"><%=WorkflowAuditStatusEnum.ing.getValue() %></option>
                                <option value="<%=WorkflowAuditStatusEnum.finish.toString() %>"><%=WorkflowAuditStatusEnum.finish.getValue() %></option>
                            </select>
                        </td>
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
</section>
<script src='${sysPath}/js/com/jc/csmp/project/info/detail/hetongzhifu.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>