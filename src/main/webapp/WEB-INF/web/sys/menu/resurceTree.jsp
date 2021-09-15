<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@include file="/WEB-INF/web/include/jctree.jsp"%>
<script >
$.ajaxSetup ({
	 cache: false //设置成false将不会从浏览器缓存读取信息
	});
</script>

<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="navigationMenu">
	<h1>资源设置</h1>
	<div class="crumbs"></div>
</header>
<div class="nav-primary nav-tree m-t-md" id="nav-primary">
	<ul class="nav" id="resurceTree">
		<li>
			<a href="#" onclick="resource.loadMenuTree(-1,'firstmenu')">
				<i class="fa"></i>
				<span id="menuall" >
					全部
				</span>
				<div class="nav-tree-btn-wrap">
					<%--<button class="a-icon i-new nav-tree-btn" href="#new-zy" onclick="resource.createMenu(-1)" role="button" data-toggle="modal" >新增资源</button>--%>
				</div>
			</a>
			<ul class="nav lt" id="firstmenu">
				
			</ul>
		</li>
	</ul>
</div>                            
</section>                       
    <div class="modal fade panel" id="myModal-edit" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title">编辑</h4>
                </div>
                <form class="form-horizontal" id="menuEditForm">
                <input type="hidden" id="menuType" name = "menuType" value="0" />
                <input type="hidden" id="token" name="token" value="0">
                <div class="modal-body">
                    <div class="table-wrap form-table" id="editFormdiv">
                        <table class="table table-td-striped">
                            <tbody>
                               
                                <tr>
                                    <td style="width:33%;">上级节点 <input type="hidden" id="id" name = "id"  />
                					<input type="hidden" id="editparentId" name = "parentId" /></td>
                                    <td><input type="text" disabled="disabled" id= "parentname" name = "parentname" /></td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>菜单名称</td>
                                    <td><input type="text" id="name" name = "name" maxlength="8"/></td>
                                </tr>
                                <tr>  
                                    <td>对应action名称</td>
                                    <td><input type="text" id="actionName" name = "actionName" /></td>
                                </tr>
                                <tr>  
                                    <td>class名称</td>
                                    <td><input type="text" id="icon" name = "icon" /></td>
                                </tr>
                                <tr>  
                                    <td><span class="required">*</span>排序</td>
                                    <td><input type="text" id="queue" name = "queue" /></td>
                                </tr>
                                <tr>  
                                    <td>权限标识</td>
                                    <td><input type="text" id="permission" name = "permission" /></td>
                                </tr>
                                <tr>  
                                    <td>是否显示</td>
                                    <td>
                                        <label class="radio inline">
                                             <input type="radio" name="isShow" checked value="0" /> 显示
                                        </label>
                                        <label class="radio inline">
                                             <input type="radio" name="isShow" value="1" /> 不显示
                                        </label>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                </form>
                <div class="modal-footer form-btn">
                    <button class="btn dark" id="MenuEditBtn" type="button">保 存</button>
                    <button class="btn" type="button" onclick="$('#myModal-edit').modal('hide');">关 闭</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade panel" id="new-zy" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
            	<form class="form-horizontal" id="menuAddForm">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title">新增</h4>
                </div>
                <input type="hidden" id="id" name = "id"  />
                <input type="hidden" id="menuType" name = "menuType" value="0" />
                <input type="hidden" id="token" name="token" value="0">
                <div class="modal-body">
                    <div class="table-wrap form-table" id="addFormDiv">
                        <table class="table table-td-striped">
                            <tbody>
                                
                                <tr>
                                    <td style="width:33%;">上级节点<input type="hidden" id="addparentId" name = "parentId" />
                                    <input type="hidden" id="addrootNode" name = "rootNode" /></td>
                                    <td><input type="text" disabled="disabled" id= "parentname" name = "parentname" /></td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>菜单名称</td>
                                    <td><input type="text" id="name" name = "name" maxlength="8" /></td>
                                </tr>
                                <tr>  
                                    <td>对应action名称</td>
                                    <td><input type="text" id="actionName" name = "actionName" /></td>
                                </tr>
                                <tr>  
                                    <td>class名称</td>
                                    <td><input type="text" id="icon" name = "icon" /></td>
                                </tr>
                                <tr>  
                                    <td><span class="required">*</span>排序</td>
                                    <td><input type="text" id="queue" name = "queue" /></td>
                                </tr>
                                <tr>  
                                    <td>权限标识</td>
                                    <td><input type="text" id="permission" name = "permission" /></td>
                                </tr>
                                <tr>  
                                    <td>菜单显示状态</td>
                                    <td>
                                        <label class="radio inline">
                                             <input type="radio" name="isShow" checked value="0" /> 显示
                                        </label>
                                        <label class="radio inline">
                                             <input type="radio" name="isShow" value="1" /> 不显示
                                        </label>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                </form>
                <div class="modal-footer form-btn">
                    <button class="btn dark" id="MenuAddBtn" type="button">保 存</button>
                    <button class="btn" type="button" onclick="$('#new-zy').modal('hide');">关 闭</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade panel" id="new-qx" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
            	<form class="table-wrap form-table" id="menuPowerForm">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title" id="titlename">操作</h4>
                </div>
                <input type="hidden" id="menuType" name = "menuType" value="1" />
                <input type="hidden" id="isShow" name = "isShow" value="1" />
                <input type="hidden" id="token" name="token" value="0">
                <div class="modal-body">
                    <div class="table-wrap form-table" id="powerFormDiv">
                        <table class="table table-td-striped">
                            <tbody>
                                <tr>
                                    <td style="width:33%;">菜单名称<input type="hidden" id="idPower" name = "id"  />
                                    <input type="hidden" id="powerparentId" name = "parentId" />
                                    <input type="hidden" id="powerrootNode" name = "rootNode" /></td>
                                    <td><input type="text" disabled="disabled" id= "parentname" name = "parentname" /></td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>操作名称</td>
                                    <td><input type="text" id="name" name = "name" maxlength="8" /></td>
                                </tr>
                                <tr>  
                                    <td>操作标识</td>
                                    <td><input type="text" id="permission" name = "permission" /></td>
                                </tr>
                                <tr>  
                                    <td><span class="required">*</span>排序</td>
                                    <td><input type="text" id="queue" name = "queue" /></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                </form>
                <div class="modal-footer form-btn">
                    <button class="btn dark" id="MenuPowerBtn" type="button">保 存</button>
                    <button class="btn" type="button" onclick="$('#new-qx').modal('hide');">关 闭</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade panel" id="new-qy" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
            	<form class="table-wrap form-table" id="moveMenuForm">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">×</button>
                    <h4 class="modal-title">资源迁移</h4>
                </div>
                <input type="hidden" id="menuType" name = "menuType" value="0" />
                <input type="hidden" id="token" name="token" value="0">
                <div class="modal-body">
                    <div class="table-wrap form-table" id="moveFormDiv">
                        <table class="table table-td-striped">
                            <tbody>
                            	<tr>
                                    <td style="width:18%;">上级节点
                                    <td colspan="3"><input type="text" disabled="disabled" id= "leadernode" name = "leadernode" /></td>
                                </tr>
                                <tr>
                                    <td style="width:18%;">迁移资源<input type="hidden" id="idmove" name = "id"  />
                                    <td colspan="3"><input type="text" disabled="disabled" id= "movename" name = "name" /></td>
                                </tr>
                                <tr>
                                    <td><span class="required">*</span>迁移至节点名称
                                    </td>
                                    <td style="width:40%;">
                                    	<div style="overflow: auto;">
                                    		<ul id="moveMenutree" class="ztree" style="margin-top:0;height:160px;"></ul>
                                    	</div>
                                    </td>
                                    <td>
                                    </td>
                                    <td style="width:37%;">
                                    	<input type="hidden" id="moveparentId" name = "parentId" value=""/>
                                    	<input type="text" id="moveparentName" name = "parentName" class="moveMenuVal" readonly="readonly" value="" style=" text-align:left;height:160px;" />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                </form>
                <div class="modal-footer form-btn">
                    <button class="btn dark" id="moveMenuBtn" type="button">保 存</button>
                    <button class="btn" type="button" onclick="$('#new-qy').modal('hide');">关 闭</button>
                </div>
            </div>
        </div>
    </div>

<script src="${sysPath}/js/com/sys/menu/menuResource.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/menu/menuResource.validate.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>