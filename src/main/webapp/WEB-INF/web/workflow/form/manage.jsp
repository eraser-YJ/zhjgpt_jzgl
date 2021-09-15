<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script type="text/javascript">
    function oTableSetButtons( source) {
        var update = '<a class="a-icon i-new" href="#" onclick="form_.loadModal(\''+source.id+'\')" role="button" data-toggle="modal"><i class="fa fa-edit2" data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="编辑"></i></a>';
        var manageItemBtnStr = '<a class="a-icon i-new" href="#" onclick="form_.formitem.showFormitemModal(\''+source.id+'\')" role="button">表单项管理</a>';
        var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"form_.deleteFun('"+source.id+"')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
        return update+manageItemBtnStr+del;
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
            <form class="table-wrap form-table" id="formQueryForm">
                <table class="table table-td-striped">
                    <tbody>
                        <tr>
                            <td class="w140">表单名称</td>
                            <td>
                                <input type="text" id="nameQuery" name="name" >
                            </td>
                            <td class="w140">表名</td>
                            <td>
                                <input type="text" id="tableNameQuery" name="tableName" >
                            </td>
                        </tr>
                    </tbody>
                </table>
                <section class="form-btn m-b-lg">
                    <button class="btn dark query-jump" type="button" id="queryForm">查 询</button>
                    <button class="btn" type="reset" id="queryReset">重 置</button>
                </section>
            </form>
        </div>
        <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
    </section>
    <section class="panel clearfix ">
        <div class="table-wrap">
            <table class="table table-striped first-td-tc tab_height" id="formTable">
                <thead>
                <tr>
                    <th>表单名称</th>
                    <th style="width:20%">表名</th>
                    <th style="width:20%">创建时间</th>
                    <th class="w200">操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
                <a class="btn dark" href="#" role="button" onclick="form_.showModal()">新 增</a>
            </section>
        </section>
    </section>
</section>

<div class="modal fade panel" id="formitemModal" aria-hidden="false">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 800px">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">表单项管理</h4>
            </div>
            <div class="modal-body">
                <div class="form-table">
                    <table id="formitemTable" class="table table-striped">
                        <thead>
                        <tr>
                            <th>名称</th>
                            <th style="width:160px">控件名称</th>
                            <th style="width:140px">控件类型</th>
                            <th class="w140">操作</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer no-all form-btn">
                <button type="button" class="btn dark btn-primary" onclick="form_.formitem.showFormitemSaveModal()">新增</button>
                <button type="button" class="btn btn-primary" onclick="form_.formitem.importFromDatabase()">从数据库导入</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade panel" id="formitemUpdateModal" aria-hidden="false">
    <div class="modal-dialog">
        <form id="formitemForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" aria-label="Close" onclick="form_.formitem.closeFormitemUpdateModal()"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">编辑表单项</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="formitemId" name="id">
                    <input type="hidden" id="formId" name="formId">
                    <input type="hidden" id="columnName" name="columnName">
                    <div class="form-table">
                        <table class="table table-td-striped">
                            <tbody>
                            <tr>
                                <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>名称</td>
                                <td style="width: 85%;"><input type="text" id="itemName" name="itemName" /></td>
                            </tr>
                            <tr>
                                <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>控件名称</td>
                                <td style="width: 85%;"><input type="text" id="formName" name="formName" /></td>
                            </tr>
                            <tr>
                                <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>控件类型</td>
                                <td style="width: 85%;">
                                    <select id="type" name="type" class="form-control">
                                        <option value="1">文本输入框</option>
                                        <option value="2">文本输入域</option>
                                        <option value="3">时间输入框</option>
                                        <option value="4">单选</option>
                                        <option value="5">多选</option>
                                        <option value="6">下拉选择框</option>
                                        <option value="7">人员选择框</option>
                                        <option value="8">文件上传</option>
                                        <option value="9">富文本编辑器</option>
                                        <option value="12">意见域</option>
                                        <option value="10">动态添加行</option>
                                        <option value="11">动态添加行-列</option>
                                        <option value="13">按钮</option>
                                        <option value="14">容器</option>
                                    </select>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
                <div class="modal-footer no-all form-btn">
                    <button class="btn dark" type="button" onclick="form_.formitem.save()">保 存</button>
                    <button class="btn" type="button" onclick="form_.formitem.closeFormitemUpdateModal()">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>
<div class="modal fade panel" id="formModal" aria-hidden="false">
    <div class="modal-dialog">
        <form id="formForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">表单</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="id" id="id">
                    <div class="form-table">
                        <table class="table table-td-striped">
                            <tbody>
                            <tr>
                                <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>表单名称</td>
                                <td style="width: 85%;"><input type="text" id="name" name="name" class="form-control"/></td>
                            </tr>
                            <tr>
                                <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>对应表名</td>
                                 <td style="width: 85%;">
                                    <div>
                                        <select id="tableName" name="tableName" class="form-control" style="width: 100%;">
                                            <c:forEach var="table" items="${tables}">
                                                <option value="${table.tableName}">${table.tableName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>开始流程表单url</td>
                                <td style="width: 85%;">
                                    <input type="text" id="startUrl" name="startUrl" class="form-control"/>
                                </td>
                            </tr>
                            <tr>
                                <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>加载流程表单url</td>
                                <td style="width: 85%;">
                                    <input type="text" id="loadUrl" name="loadUrl" class="form-control" />
                                </td>
                            </tr>
                            <tr>
                                <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>列表url</td>
                                <td style="width: 85%;">
                                    <input type="text" id="listUrl" name="listUrl" class="form-control"/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
                <div class="modal-footer no-all form-btn">
                    <button class="btn dark" type="button" onclick="form_.save()">保 存</button>
                    <button class="btn" type="button" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="${sysPath}/js/com/jc/workflow/form/form.js"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>