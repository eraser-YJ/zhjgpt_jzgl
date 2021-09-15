<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="c"%>

	<h1>${navigationname}</h1>
	<div class="crumbs">
	<a href="#" onclick="homeloadmenu()">首页</a>
   		<c:forEach items="${navigationMenuList}" var="navigationmenu">
   	  		<i></i>${navigationmenu.name}
   		</c:forEach>  
	</div>

                            