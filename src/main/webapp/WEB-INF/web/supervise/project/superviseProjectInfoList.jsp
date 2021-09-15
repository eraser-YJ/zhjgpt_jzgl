<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<style>
    .emptyContainer{ text-align: center; font-size: 20px; font-weight: bold; margin-top: 50px;}
    .lc-content{width:1040px; margin: 0 auto; }
    .work{ width: 1040px; height: 145px; background: #eeeced; margin: 30px auto; position: relative; border-radius: 10px;}
    .frame{display: inline-block; background: #bababa; width: 140px; height: 45px; line-height: 45px; border-radius: 8px; color: #fff; text-align: center; font-size: 14px;}
    .frame.active{ background: #0c9ae3; color: #fff; cursor:pointer; }
    .start{ position: absolute; margin-top: 50px; margin-left: 182px; }
    .startMoreLine { position: absolute; margin-top: 50px; margin-left: 82px; margin-top: 20px; }
    .moreItem{ width: 140px; position: absolute; top: 20px;}
    .moreItemDown{ position: relative; margin-top: 13px; }
    .moreItemUp{ position: relative; }
    .moreItemHe{ margin-top: 13px; }
    .item{position: absolute; top:47px;}
    .dottedLine {display: block; background: #FFFFFF; color:#000; border:2px dotted #000;}

    .fenLeft{ display: block; width: 48px; height: 70px; background: url(${sysPath}/images/flow/fenLeft.png) no-repeat; position: absolute; left: -45px; top: -11px; }
    .fenRight{ display: block; width: 48px; height: 80px; background: url(${sysPath}/images/flow/fenRight.png) no-repeat; position: absolute; left: 140px; top: -12px;}
    .lineLeft{ display: block; width: 45px; height: 20px; background: url(${sysPath}/images/flow/lineLeft.png) no-repeat; position: absolute; left: -44px; top: 15px; }
    .lineRight{ display: block; width: 45px; height: 20px; background: url(${sysPath}/images/flow/lineRight.png) no-repeat; position: absolute; left: 140px; top: 15px;}
    .heLeft{ display: block; width: 48px; height: 70px; background: url(${sysPath}/images/flow/heLeft.png) no-repeat; position: absolute; left: -45px; top: 17px; }
    .heRight{ display: block; width: 48px; height: 80px; background: url(${sysPath}/images/flow/heRight.png) no-repeat; position: absolute; left: 140px; top: 15px; }
    .zheFenLeft{ display: block; width: 48px; height: 230px; background: url(${sysPath}/images/flow/zheFenLeft.png) no-repeat; position: absolute; left: 140px; top: 20px; z-index: 100; }
    .zheNoFenLeft{ display: block; width: 48px; height: 230px; background: url(${sysPath}/images/flow/zheNoFenLeft.png) no-repeat; position: absolute; left: 140px; top: 20px; z-index: 100; }
    .zheFenRight{ display: block; width: 48px; height: 230px; background: url(${sysPath}/images/flow/zheFenRight.png) no-repeat; position: absolute; left: -45px; top: 20px; z-index: 100; }
    .zheNoFenRight{ display: block; width: 48px; height: 230px; background: url(${sysPath}/images/flow/zheNoFenRight.png) no-repeat; position: absolute; left: -45px; top: 20px; z-index: 100; }
</style>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="navigationMenu">
        <div class="crumbs">
            <span>全生命周期</span>
            <span>数据串联监管 > </span><span>项目动态监管</span>
        </div>
    </header>
    <section class="panel clearfix search-box search-shrinkage">
        <div class="search-line">
            <form class="table-wrap form-table" id="searchForm">
                <input type="hidden" id="paramProjectNumber" name="paramProjectNumber" />
                <input type="hidden" id="paramApprovalNumber" name="paramApprovalNumber" />
                <table class="table form-table table-td-striped" style="width:100% !important; margin-bottom:10px;">
                    <tbody>
                    <tr>
                        <td class="w140">项目统一编号</td>
                        <td><input type="text" id="projectNumber" name="projectNumber" /></td>
                        <td class="w140">项目名称</td>
                        <td><input type="text" id="projectName" name="projectName" /></td>
                    </tr>
                    </tbody>
                </table>
                <div class="btn-tiwc">
                    <button class="btn" type="button" id="queryBtn">查 询</button>
                    <button class="btn" type="button" id="resetBtn">重 置</button>
                </div>
            </form>
        </div>
    </section>
    <div style="display: flex;">
        <section class="panel" style="flex: 1;">
            <div class="table-wrap"><table class="table table-striped tab_height" id="gridTable"></table></div>
            <section class="clearfix" id="footer_height"></section>
        </section>
        <section class="panel" style="width: 70%; margin-left:10px;">
            <div>
                <ul class="nav-tabs">
                    <li class="tab-item-active">流程图</li>
                    <li>项目详细信息</li>
                </ul>
                <div class="tab-box" style="overflow-x:auto;">
                    <div>
                        <div class="emptyContainer">选择项目进行查看</div>
                        <div class="lc-content" style="display: none;">
                            <% int text = 140, start = 182, line = 45; %>
                            <!-- line1 -->
                            <div class="work">
                                <div class="start"><span class="frame" id="pt_project_approval" onclick="flowListPanel.search('pt_project_approval')">项目立项</span><i class="lineRight"></i></div>
                                <div class="item" style="left: <%=(start + text + line) %>px;"><span class="frame" id="pt_project_design" onclick="flowListPanel.search('pt_project_design')">勘察设计</span><i class="lineRight"></i></div>
                                <div class="item" style="left: <%=(start + text * 2 + line * 2) %>px;"><span class="frame" id="pt_company_projects_ztb" onclick="flowListPanel.search('pt_company_projects_ztb')">招投标</span><i class="lineRight"></i></div>
                                <div class="item" style="left: <%=(start + text * 3 + line * 3) %>px;"><span class="frame" id="pt_company_projects_htba" onclick="flowListPanel.search('pt_company_projects_htba')">合同备案</span><i class="zheNoFenLeft"></i></div>
                            </div>
                            <!-- line2 -->
                            <div class="work">
                                <div class="start"><span class="frame" id="pt_project_finish" onclick="flowListPanel.search('pt_project_finish')">竣工备案</span></div>
                                <div class="item" style="left: <%=(start + text + line) %>px;"><span class="frame" id="pt_project_safe" onclick="flowListPanel.search('pt_project_safe')">安全监管</span><i class="lineLeft"></i></div>
                                <div class="item" style="left: <%=(start + text * 2 + line * 2) %>px;"><span class="frame" id="pt_project_quality" onclick="flowListPanel.search('pt_project_quality')">质量监督</span><i class="lineLeft"></i></div>
                                <div class="item" style="left: <%=(start + text * 3 + line * 3) %>px;"><span class="frame" id="pt_company_projects_sgxk" onclick="flowListPanel.search('pt_company_projects_sgxk')">施工许可证</span><i class="lineLeft"></i></div>
                            </div>
                        </div>
                    </div>
                    <div>
                        <div class="emptyContainer">选择项目进行查看</div>
                        <div class="lc-content" style="display: none;">
                            <form class="table-wrap form-table">
                                <table class="table table-td-striped table-year" id="detailContainer"></table>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</section>
<!-- 列表  -->
<div class="modal fade panel" id="list-modal" aria-hidden="false">
    <div class="modal-dialog" style="width: 1100px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" onclick="$('#list-modal').modal('hide');">×</button>
                <h4 class="modal-title">列表查看</h4>
            </div>
            <div class="modal-body">
                <section class="panel" style=" margin-top: -20px;">
                    <div class="table-wrap" id="listContent">
                        <table class="table table-striped tab_height" id="listTable"></table>
                    </div>
                    <section class="clearfix"></section>
                </section>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/supervise/project/superviseProjectInfoList.js'></script>
<%@ include file="/WEB-INF/web/supervise/resource/superviseResourceDetail.jsp" %>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
