<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/jctree.jsp"%>

<!-- TODO 面包屑 -->
<section class="scrollable padder jcGOA-section" id="scrollable">
	<section class="panel m-t-md" id="body">  
			<div class="table-wrap" >
				<table style="width:100%;">
					<tr>
						<td style="width:150px;border: 0px solid " valign="top" >
						    <ul>
						        <li><a href="${sysPath}/api/dlh/doc/guide.action" target="mainFrame">接入指南</a></li>
						        <c:forEach items="${objList}" var="itemData">
							        <li><a href="${sysPath}/api/dlh/doc/itf.action?id=${itemData.id}" target="mainFrame">${itemData.objName}</a></li>
							   	</c:forEach>
						    </ul>
						</td>
						<td>
						    <iframe id="mainFrame" name="mainFrame" src="${sysPath}/api/dlh/doc/guide.action" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="800px;"></iframe>
						</td>
					</tr>
				</table>
			</div>
	</section>
</section> 
<%@ include file="/WEB-INF/web/include/foot.jsp"%>