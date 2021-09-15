<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="c"%>
	
	<c:forEach items="${menuList}" var="resourceVo">
		<li>
			<a href="#" onclick="resource.loadMenuTree('${resourceVo.id}','divname${resourceVo.id }')">
				<c:if test="${resourceVo.menuType == 0 && resourceVo.isNextNode > 0}"><i class="fa"></i></c:if>
				<span>
					${resourceVo.name}
				</span>
				<div class="nav-tree-btn-wrap">
					<c:if test="${resourceVo.menuType == 0 }">
					<button class="a-icon i-new nav-tree-btn" href="#new-zy" onclick="resource.createMenu('${resourceVo.id }')" role="button" data-toggle="modal">新增资源</button>
					<button class="a-icon i-new i-new1 nav-tree-btn" href="#new-qx" onclick="resource.createMenuPower('${resourceVo.id }')" role="button" data-toggle="modal">新增操作</button>
					<button class="a-icon i-new i-new1 nav-tree-btn" href="#new-qy" onclick="resource.moveMenu('${resourceVo.id }')" role="button" data-toggle="modal">资源迁移</button>
					<button class="a-icon i-edit nav-tree-btn" href="#myModal-edit" onclick="resource.updateMenu('${resourceVo.id }','${resourceVo.menuType }')" role="button" data-toggle="modal">&nbsp;编辑&nbsp;</button>
					</c:if>
					<c:if test="${resourceVo.menuType == 1 }">
					<button class="a-icon i-edit nav-tree-btn" href="#new-qx" onclick="resource.updateMenu('${resourceVo.id }','${resourceVo.menuType }')" role="button" data-toggle="modal">&nbsp;编辑&nbsp;</button>
					</c:if>
					<button class="a-icon i-remove nav-tree-btn" href="#" onclick="resource.confirmDelMenu('${resourceVo.id }','${resourceVo.rootNode }')">&nbsp;删除&nbsp;</button>
				</div>
			</a>
			<ul  id="divname${resourceVo.id }" class="nav bg">
			</ul>
		</li>
	</c:forEach> 