<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script type="text/javascript">
    function oTableSetButtons( source) {
        var del = "<a class=\"a-icon i-remove\" href=\"#\" onclick=\"startAuth.deleteFun('"+source.id+"')\"><i class=\"fa fa-remove\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"\" data-container=\"body\" data-original-title=\"删除\"></i></a>";
        return  del;
    };
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header cass="con-header pull-in">
        <div class="con-heading fl" id="navigationMenu">
            <h1></h1>
            <div class="crumbs"></div>
        </div>
        <a class="btn dark fr" href="#" role="button" onclick="startAuth.showModal()">授 权</a>
    </header>
    <section class="tree-fluid">
        <aside class="tree-wrap m-t-mdp" id="LeftHeight_box">
            <section class="panel" id="LeftHeight" style="overflow-y: auto;">
                <div class="panel-heading clearfix">
                    <h2>选择流程</h2>
                </div>
                <div id="defTree" class="ztree"></div>
            </section>
        </aside>
        <section class="tree-right" style="padding-left: 215px;">
            <section class="panel">
                <div class="table-wrap">
                    <table class="table table-striped first-td-tc tab_height" id="roleTable">
                        <thead>
                        <tr>
                            <th>角色名称</th>
                            <th class="w100">操作</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <section class="clearfix" id="footer_height">
                    <section class="form-btn fl m-l">
                        <a class="btn dark fr" href="#" role="button" onclick="startAuth.showModal()">授 权</a>
                    </section>
                </section>
            </section>
        </section>
    </section>
</section>
<script src="${sysPath}/js/com/jc/workflow/auth/selectRole.js"></script>
<script src="${sysPath}/js/com/jc/workflow/auth/startAuthView.js"></script>