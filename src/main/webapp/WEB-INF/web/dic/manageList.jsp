<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in">
        <div class="con-heading fl" id="navigationMenu">
            <h1></h1>
            <div class="crumbs"></div>
        </div>
    </header>
    <section class="tree-fluid">
        <aside class="tree-wrap m-t-md" id="LeftHeight_box">
            <section class="panel" id="LeftHeight" style="overflow-y: auto;">
                <div class="panel-heading clearfix" id="header_2">
                    <h2>字典类型</h2>
                </div>
                <div id="treeDemo" class="ztree"></div>
            </section>
        </aside>
        <section class="tree-right">
            <section class="panel">
                <div class="table-wrap form-table">
                    <form class="table-wrap form-table" id="manageListForm" name="manageListForm" >
                        <table class="table table-td-striped"><tbody>
                            <tr>
                                <td class="w140">字典内容</td>
                                <td id="value" class="w140"></td>
                                <td class="w140">字典编码</td>
                                <td id="code" class="w140"></td>
                                <td class="w140">父字典类型</td>
                                <td id="parentId" class="w140" ></td>
                            </tr>
                        </tbody></table>
                    </form>
                </div>
            </section>
            <section class="panel">
                <h2 class="panel-heading clearfix">字典项</h2>
                <div class="table-wrap">
                    <table class="table table-striped tab_height" id="dicsTable"></table>
                </div>
                <section class="clearfix">
                    <section class="form-btn fl m-l">
                        <a class="btn dark fr" href="#myModal-edit" role="button" id="dicsTop" data-toggle="modal">新 增</a>
                    </section>
                    <section class="pagination m-r fr m-t-none">
                    </section>
                    <input type="hidden" id="dicparentId" value="${parentId}">
                    <input type="hidden" id="dicparentType" value="${parentType}">
                </section>
            </section>
        </section>
    </section>
</section>
<div id="dicsEdit"></div>
<script src="${sysPath}/js/com/sys/dic/diclist.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>
