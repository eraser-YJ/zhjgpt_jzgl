<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>
<script src="${sysPath}/js/com/sys/user/userAll.js" type="text/javascript"></script>

<script type="text/javascript">
	function userOTableSetButtons( source ) {
		var up = '<a class="a-icon i-new" href="#" onclick="userAll.userOrder('+source.rowNo+',1)" role="button" data-toggle="modal"><i class="fa fa-chevron-up" data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="向上"></i></a>';
		var down = '<a class="a-icon i-new" href="#" onclick="userAll.userOrder('+source.rowNo+',2)" role="button" data-toggle="modal"><i class="fa fa-chevron-down" data-toggle="tooltip" data-placement="top" title="" data-container="body" data-original-title="向下"></i></a>';
		var rowUserId = '<input type="hidden" id="rowUserId_'+source.rowNo+'" value="'+source.id+'">';
		var rowOrderNo = '<input type="hidden" id="rowOrderNo_'+source.rowNo+'" value="'+source.orderNo+'">';
		return up + down + rowUserId + rowOrderNo;
};
</script>

<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="header_1">
    <div class="con-heading fl" id="navigationMenu">
        <h1></h1>
        <div class="crumbs"></div>
    </div>
</header>
<section class="tree-fluid">
    <aside class="tree-wrap m-t-md" id="LeftHeight_box">
        <section class="panel" id="LeftHeight" style="overflow-y: auto;">
            <div class="panel-heading clearfix" id="header_2">
				<h2>选择部门</h2>
			</div>
            <div id="treeDemo" class="ztree"></div>
        </section>
    </aside>
    <section class="tree-right">
        <section class="panel search-shrinkage clearfix">
            <div class="search-line hide">
            <form class="table-wrap form-table" id="userListForm">
                <input type="hidden" id="deptId" name="deptId">
                <input type="hidden" id="deptIds" name="deptIds">
                <table class="table table-td-striped">
                    <tbody>
                        <tr>
                            <td class="w140">部门</td>
                            <td style="width:40%;">
                                <input type="text" id="deptName" name="deptName" readonly/>
                            </td>
                            <td class="w140">性别</td>
                            <td>
                             	<dic:select name="sex" id="sex" dictName="sex" parentCode="user" headName="-全部-" headValue="" defaultValue=""/>
                               
                            </td>
                        </tr>
                        <tr>
                            <td style="width:10%;">状态</td>
                            <td style="width:40%;">
                                <select id="status" name="status">
                                    <option value="status_0">启用</option>
                                    <%--<option value="status_2">锁定</option>
                                    <option value="status_3">删除</option>
                                    <option value="">-全部-</option>--%>
                                </select>
                            </td>
                            <td style="width:10%;">姓名</td>
                            <td>
                                <input type="text" id="displayName" name="displayName"/>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <section class="form-btn m-b-lg">
                    <button class="btn dark" type="button" id="queryUser">查 询</button>
                    <button class="btn" type="button" id="queryReset">重 置</button>
                </section>
            </form>
            </div>
            <%@ include file="/WEB-INF/web/include/searchConditionHide.jsp"%>
        </section>
        <section class="panel">
            <h2 class="panel-heading clearfix">查询结果</h2>
            <div class="table-wrap">
                <table class="table table-striped tab_height" id="userTable">
                    <thead>
                        <tr>
                           
                            <th class="w100">姓名</th>
                            <th class="w60">性别</th>
                            <th>部门</th>
                            <th>职务</th>
                            <th class="w100">状态</th>
                            <th class="w210">序号</th>
                            <th class="w100">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                       
                    </tbody>
                </table>
            </div>
            <section class="clearfix" id="footer_height">
                
                <!-- 分页信息 -->
            </section>
 		</section>
	</section>
</section> 
</section>
<div id="userEdit">
</div>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>