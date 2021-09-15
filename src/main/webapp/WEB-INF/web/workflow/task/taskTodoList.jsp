<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="c"%>
<%@ taglib prefix="fn" uri="fn" %>
<%@ include file="/WEB-INF/web/include/head.jsp"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<section class="scrollable padder jcGOA-section" id="scrollable">
	<header class="con-header pull-in">
	    <div class="con-heading fl" id="navigationMenu">
	         <h1>待办流程</h1>
	     </div>
	</header>
	<c:choose>
	   <c:when test="${empty types}">
	   		<section class="panel m-t-md" id="prompt_sub">
	            <div class="h500">
	            	<div class="g-z-hint img-rounded">
	            		<img src="${sysPath}/images/demoimg/no-staff.png"  width="70px;">
	                    <div class="g-z-div">
	                        <span class="g-z-span">抱歉！<br>您暂无待办信息</span><br>
	                    </div>
	            	</div>
	            </div>
	        </section>
	   </c:when>
	   <c:otherwise>
	   	<section class="panel m-t-md clearfix">
		    <div class="clearfix">
		    	<c:forEach items="${types}" var="type" varStatus="status">
		    		<div class="pm pm_box${(status.index)%4+1} fl clearfix">
		             <div class="pm-tit ">
		                 <h2>${type.key}(${fn:length(type.value.taskList)})</h2>
		                 <i class="fa ${type.value.styleStr}"></i>
		             </div>
		             <div class="pm-item ">
		                 <ul>
		                    <c:forEach items="${todoList[type.key]}" var="todoBean">
		                    		<li><a href="javascript:void(0)" onclick="processDefinition.openList('${todoBean.value[0].definitionKey}','${type.key}')">${todoBean.key}(${fn:length(todoBean.value)})</a></li>
		                    </c:forEach>
		                 </ul>
		             </div>
		        	</div>
		        </c:forEach>
		    </div>
		</section>
	   </c:otherwise>
	</c:choose>
</section>

<script type='text/javascript' src="${sysPath}/js/com/jc/workflow/task/taskListCenter.js"></script>
  
<%@ include file="/WEB-INF/web/include/foot.jsp"%>    
