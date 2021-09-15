<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<%@ include file="/WEB-INF/web/include/webupload.jsp"%>
<script type="text/javascript">
function userOTableSetButtons( source) {
	var edit = '<shiro:hasPermission name="user:update"><a class="a-icon i-new" href="#" onclick="user.loadUpdateHtml(\''+ source.id+'\')" role="button" data-toggle="modal">' + finalParamEditText + '</a></shiro:hasPermission>';
	var initPwd = '<shiro:hasPermission name="user:initPwd"><a class="a-icon i-new" href="#" onclick="user.initPassword(\''+ source.id +'\')">初始化密码</a></shiro:hasPermission>';
	var del = '<shiro:hasPermission name="user:delete"><a class="a-icon i-remove" href="#" onclick="user.deleteUser(\''+ source.id+ '\')">' + finalParamDeleteText + '</a></shiro:hasPermission>';
	if(source.isSystemAdmin || source.isSystemSecurity || source.isSystemAudit  || source.isSystemManager ||source.status == "status_3"){
		return null;
    } else {
        return edit + initPwd + del +"<input type=\"hidden\" name=\"delete_status\" value="+ source.id + " >";
    }
};
</script>
<section class="scrollable padder jcGOA-section" id="scrollable">
    <header class="con-header pull-in" id="header_1"><div class="con-heading fl"><h1></h1><div class="crumbs"></div></div></header>
    <section class="tree-fluid">
        <aside class="tree-wrap m-t-md" id="LeftHeight_box">
            <section class="panel" id="LeftHeight" style="overflow-y: auto;">
                <div class="panel-heading clearfix" id="header_2"><h2>选择部门</h2></div>
                <div id="treeDemo" class="ztree"></div>
            </section>
        </aside>
        <section class="tree-right">
            <!-- 查询条件 start -->
            <section class="panel search-shrinkage clearfix">
                <div class="search-line">
                    <form class="table-wrap form-table" id="userListForm">
                        <input type="hidden" id="deptId" name="deptId">
                        <input type="hidden" id="deptIds" name="deptIds">
                        <input type="hidden" id="deptWeight" name="deptWeight">
                        <table class="table table-td-striped"><tbody>
                            <tr>
                                <td class="w140">部门</td>
                                <td style="width:40%;"><input type="text" id="deptName" name="deptName" readonly /></td>
                                <td class="w140">性别</td>
                                <td><dic:select name="sex" id="sex" dictName="sex" parentCode="user" headName="-全部-" headValue="" defaultValue=""/></td>
                            </tr>
                            <tr>
                                <td style="width:10%;">状态</td>
                                <td style="width:40%;"><select id="status" name="status"><option value="status_0">启用</option><option value="status_2">锁定</option></select></td>
                                <td style="width:10%;">姓名</td>
                                <td><input type="text" id="displayName" name="displayName"/></td>
                            </tr>
                        </tbody></table>
                        <section class="form-btn m-b-lg">
                            <button class="btn dark" type="button" id="queryUser">查 询</button>
                            <button class="btn" type="button" id="queryReset">重 置</button>
                        </section>
                    </form>
                </div>
            </section>
            <!-- 查询条件 end -->

            <!-- 表格 start -->
            <section class="panel">
                <h2 class="panel-heading clearfix">查询结果</h2>
                <div class="table-wrap">
                    <table class="table table-striped first-td-tc tab_height" id="userTable">
                        <thead>
                            <tr>
                                <th class="w46"><input type="checkbox" /></th>
                                <th class="w150">姓名</th>
                                <th class="w84">性别</th>
                                <th>部门</th>
                                <th>密级级别</th>
                                <th class="w100">状态</th>
                                <shiro:hasPermission name="user:update or user:delete or user:initPwd">
                                    <th class="w210">操作</th>
                                </shiro:hasPermission>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
                <section class="clearfix" id="footer_height">
                    <section class="form-btn fl m-l">
                        <shiro:hasPermission name="user:add"><button class="btn dark" id="showAddDiv" type="button" role="button" data-toggle="modal">新 增</button></shiro:hasPermission>
                        <shiro:hasPermission name="user:delete"><button class="btn" type="submit" id="deleteUsers">批量删除</button></shiro:hasPermission>
                    </section>
                </section>
 		    </section>
            <!-- 表格 end -->
	    </section>
    </section>
</section>
<div id="userEdit"></div>
<script src="${sysPath}/js/com/sys/user/user.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>