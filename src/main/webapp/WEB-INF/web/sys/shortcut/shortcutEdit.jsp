<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>

<div class="modal fade panel" id="myModal-edit" aria-hidden="false">
	<div class="modal-dialog w900">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">快捷方式表</h4>
			</div>
			<div class="modal-body">
				<form class="table-wrap form-table" id="shortcutForm">
					<input type="hidden" id="id" name="id">
					<input type="hidden" id="token" name="token" value="${data.token}">
					<input type="hidden" id="modifyDate" name="modifyDate">
<table class="table table-td-striped">
	<tbody>
	<tr >
		<td class="w100">
<span class='required'>*</span>快捷方式名称
		</td>
		<td class="w300">
	<input type="text" id="name" name = "name" value="" placeholder="必填">
 
		</td>
		<td class="w100">
图标类型名称
		</td>
		<td class="w300">
	<input type="text" id="icon" name = "icon" value="">
 
		</td>
	</tr>
	<tr >
		<td class="w100">
子系统名称
		</td>
		<td class="w300">
 			<div id="menuTreeForOne"></div>
		</td>
		<td class="w100">
菜单名称
		</td>
		<td class="w300">
 			<div id="menuTree"></div>
		</td>
	</tr>
	<tr >
		
		<td class="w100">
菜单打开方式
		</td>
		<td class="w300">

	<input type="radio" id='openType1' name='openType' checked="checked" value='1' label='内部打开'/>内部打开
	<input type="radio" id='openType2' name='openType' value='2' label='弹出打开'/>弹出打开


		</td>
		<td class="w100">
可见标识
		</td>
		<td class="w300">

	<input type="radio" id='isShow1' name='isShow' checked="checked" value='1' label='可见'/>可见
	<input type="radio" id='isShow2' name='isShow' value='2' label='不可见'/>不可见



		</td>
	</tr>
	<tr >
		<td class="w100">
权限标识
		</td>
		<td class="w300">
	<input type="text" id="permission" name = "permission" value="" >		
		</td>
		<td class="w100">
<span class='required'>*</span>排序
		</td>
		<td class="w300">
	<input type="text" id="queue" name = "queue" value="" placeholder="必填">
 
		</td>
	</tr>
	</tbody>
</table>
				</form>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="saveOrModify">保存继续</button>
				<button class="btn" type="button" id="saveAndClose">保存退出</button>
				<button class="btn" type="button" id="formDivClose">关 闭</button>
			</div>
		</div>
	</div>
</div>

<script src="${sysPath}/js/com/sys/shortcut/shortcutEdit.js" type="text/javascript"></script>
<script src="${sysPath}/js/com/sys/shortcut/shortcut.validate.js" type="text/javascript"></script>
