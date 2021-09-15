<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="c"%>
<section class="m-b clearfix">
    <span class="layout-title fl m-r-md">设置快捷</span>
    <section class="fl">
		<c:forEach items="${portletList}" var="protletVo">
			<c:if test="${protletVo.viewType == 'shortcut' }">
			<label class="checkbox inline">
		   	<input type="checkbox" value="${protletVo.id}" name="checkbox" id="checkbox_${protletVo.id}" onclick="portalset.setshortcut(this,'${protletVo.letTitle}','${protletVo.functionId}','${protletVo.viewType}')"/>${protletVo.letTitle}
			</label>
			</c:if>
		</c:forEach>
    </section>
</section>
<section class="m-b clearfix dis-table" >
	<span class="layout-title m-r-md dis-table-cell fl">设置模块</span>
	<section class="dis-table-cell fl" style="max-width:85%;">
		<c:forEach items="${portletList}" var="protletVos">
			<c:if test="${protletVos.viewType != 'shortcut' }">
			<label class="checkbox inline">
		   	<input type="checkbox" value="${protletVos.id}" name="checkboxportal" id="checkbox_${protletVos.id}" onclick="portalset.setportlet(this,'${protletVos.letTitle}','${protletVos.functionId}','${protletVos.viewType}')"/>${protletVos.letTitle}
			</label>
			</c:if>
		</c:forEach> 
	</section>
</section>