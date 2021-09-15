<%@ page language="java" pageEncoding="UTF-8" import="com.jc.supervise.warning.enums.SupervisionWarningStatusEnums" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="navigationMenu">
        <div class="crumbs">
            <span>全生命周期</span>
            <span>数据串联监管 > </span><span>预警纠错</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <form class="table-wrap form-table" id="searchForm" >
                <table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
                    <tbody>
                    <tr>
                        <td class="w100">项目统一编号</td>
                        <td><input type="text" id="projectNumber" name="projectNumber" /></td>
                        <td class="w100">项目名称</td>
                        <td><input type="text" id="projectName" name="projectName" /></td>
                        <td class="w100">处理状态</td>
                        <td>
                            <select id="status" name="status">
                                <option value="">- 全部 -</option>
                                <option value="<%=SupervisionWarningStatusEnums.no.toString() %>"><%=SupervisionWarningStatusEnums.no.getValue() %></option>
                                <option value="<%=SupervisionWarningStatusEnums.reopen.toString() %>"><%=SupervisionWarningStatusEnums.reopen.getValue() %></option>
                                <option value="<%=SupervisionWarningStatusEnums.finish.toString() %>"><%=SupervisionWarningStatusEnums.finish.getValue() %></option>
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
        <div class="table-wrap"><table class="table table-striped tab_height" id="gridTable"></table></div>
        <section class="clearfix" id="footer_height"></section>
    </section>
    <div id="formModule"></div>
</section>
<script src='${sysPath}/js/com/jc/supervise/warning/superviseWarningList.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>