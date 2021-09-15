<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script type="text/javascript">
    function oTableSetButtons( source) {
        var update = '<a class="a-icon i-new" href="#" onclick="type_.loadModal(\''+source.id+'\',false)" role="button" data-toggle="modal"><i class="fa fa-edit2" data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="编辑"></i></a>';
        var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"type_.deleteFun('"+source.id+"')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
        return  update+del;
    };
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in">
        <div class="con-heading fl" id="navigationMenu">
            <h1></h1>
            <div class="crumbs"></div>
        </div>
    </header>
    <section class="panel">
        <div class="table-wrap">
            <table class="table table-striped first-td-tc tab_height" id="typeTable">
                <thead>
                <tr>
                    <th>类型名称</th>
                    <th class="w100">操作</th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
        <section class="clearfix" id="footer_height">
            <section class="form-btn fl m-l">
                <a class="btn dark" href="#" role="button" onclick="type_.showModal()">新 增</a>
            </section>
        </section>
    </section>
</section>

<div class="modal fade panel" id="typeModal" aria-hidden="false">
    <div class="modal-dialog">
        <form id="typeForm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">新增</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="id" id="id">
                    <input type="hidden" name="opt" id="opt">
                    <div class="form-table">
                        <table class="table table-td-striped">
                            <tbody>
                            <tr>
                                <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;"><span class="required">*</span>类型名称</td>
                                <td style="width: 85%;"><input type="text" id="name" name="name" /></td>
                            </tr>
                            <tr>
                                <td style="text-align: right; background-color: #f9f8f5; width: 20%; font-weight: bold;">类型样式</td>
                                <td style="width: 85%;"><input type="text" id="styleStr" name="styleStr" /></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
                <div class="modal-footer no-all form-btn">
                    <button class="btn dark" type="button" onclick="type_.save()">保 存</button>
                    <button class="btn" type="button" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="${sysPath}/js/com/jc/workflow/type/type.js"></script>