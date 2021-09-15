<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1">
        <div class="crumbs">
            <span>系统配置</span>
            <span>附件配置信息</span>
        </div>
    </header>
    <section class="tree-fluid m-t-md">
        <aside class="tree-wrap" id="LeftHeight_box">

            <section class="panel" id="LeftHeight" style="overflow-y: auto; margin-top:15px 5px 0 0 !important;padding: 0 !important; ">
                <div style="display: flex; justify-content: space-around;">
                     <a class="a-icon i-new" style="margin: 15px 0 10px 0 !important;" href="javascript:void(0);" onclick="attachConfigModule.operatorItem('add');"><i class="fa fa-plus2" data-toggle="tooltip" data-placement="top" data-container="body" data-original-title="添加附件分类" ></i></a>
                     <a class="a-icon i-new" style="margin: 15px 0 10px 0 !important;" href="javascript:void(0);" onclick="attachConfigModule.operatorItem('edit');"><i class="fa fa-edit2" data-toggle="tooltip" data-placement="top" data-container="body" data-original-title="修改附件分类"></i></a>
                     <a class="a-icon i-remove m-r-xs"  style="margin: 15px 0 10px 0 !important;" href="javascript:void(0);" onclick="attachConfigModule.operatorItem('delete');"><i class="fa fa-remove" data-toggle="tooltip" data-placement="top" data-container="body" data-original-title="删除附件分类"></i></a>
                </div>
                <div id="treeDemo" class="ztree"></div>
            </section>
        </aside>
        <section class="tree-right">
            <%--<section class="panel">--%>
                <%--<form class="table-wrap form-table" id="searchForm">--%>
                    <%--<input type="hidden" id="paramItemId" name="paramItemId" />--%>
                    <%--<div class="btn-tiwc"><button class="btn" type="button" onclick="attachConfigModule.operatorAttach('', 'add');">新增配置</button></div>--%>
                <%--</form>--%>
            <%--</section>--%>
            <section class="panel tab-content">
                <section class="panel" style="margin: 0 !important;">
                    <form class="table-wrap form-table" id="searchForm" style="padding: 15px 0 0 5px  !important;zoom:1;">
                        <input type="hidden" id="paramItemId" name="paramItemId" />
                        <div class="btn-tiwc" style="float: left !important;padding-bottom: 10px;"><button class="btn" style="background: #1572e8; color:#fff;" type="button" onclick="attachConfigModule.operatorAttach('', 'add');">新增配置</button></div>
                    </form>
                </section>
                <div class="tab-pane fade active in">
                    <div class="table-wrap">
                        <table class="table table-striped tab_height" id="gridTable"></table>
                    </div>
                    <section class="clearfix">
                        <section class="pagination m-r fr m-t-none"></section>
                    </section>
                </div>
            </section>
        </section>
    </section>
    <div id="taskModule"></div>
    <div id="stageModule"></div>
</section>
<div class="modal fade panel" id="item-modal" aria-hidden="false">
    <div class="modal-dialog w530">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="$('#item-modal').modal('hide');">×</button>
                <h4 class="modal-title">附件分类维护</h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="itemForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td style="width: 15%"><span class='required'>*</span>附件分类名</td>
                            <td><input type="text" id="itemClassify" name="itemClassify" /></td>
                        </tr>
                        <tr>
                            <td><span class='required'>*</span>附件分类编码</td>
                            <td><input type="text" id="itemCode" name="itemCode" /></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn dark" type="button" onclick="attachConfigModule.saveOrUpdateItem();">保 存</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade panel" id="attach-modal" aria-hidden="false">
    <div class="modal-dialog w530">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="$('#attach-modal').modal('hide');">×</button>
                <h4 class="modal-title">附件信息配置</h4>
            </div>
            <div class="modal-body">
                <form class="table-wrap form-table" id="attachForm">
                    <input type="hidden" id="id" name="id" />
                    <input type="hidden" id="itemId" name="itemId" />
                    <input type="hidden" id="modifyDate" name="modifyDate" />
                    <table class="table table-td-striped">
                        <tbody>
                        <tr>
                            <td style="width: 15%"><span class='required'>*</span>附件名称</td>
                            <td><input type="text" id="itemAttach" name="itemAttach" /></td>
                        </tr>
                        <tr>
                            <td style="width: 15%"><span class='required'>*</span>是否必填</td>
                            <td>
                                <select id="isRequired" name="isRequired">
                                    <option value="1" selected>否</option>
                                    <option value="0">是</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 15%"><span class='required'>*</span>是否多个</td>
                            <td>
                                <select id="isCheckbox" name="isCheckbox">
                                    <option value="0" selected>是</option>
                                    <option value="1">否</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 15%"><span class='required'>*</span>排序</td>
                            <td><input type="text" id="extNum1" name="extNum1" /></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer form-btn" style="text-align: center; width: 100%;">
                <button class="btn dark" type="button" onclick="attachConfigModule.saveOrUpdateAttach();">保 存</button>
            </div>
        </div>
    </div>
</div>
<script src='${sysPath}/js/com/jc/csmp/config/attach/attachConfig.js'></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>

