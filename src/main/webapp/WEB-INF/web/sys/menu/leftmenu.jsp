<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="c"%>
   <c:forEach items="${leftmenuList}" var="leftmenu">
      <li>
         <a href="#" onclick="$('#dataLoad').fadeIn('normal', function(){ if(loadrightmenu('${leftmenu.actionName}'))navigationbar(${leftmenu.id });});loadnodeMenu(${leftmenu.id },'divleft${leftmenu.id }', ${leftmenu.isNextNode > 0 });" >
             <c:if test="${ischildnode == '0' }">
             	<i class="${leftmenu.icon}"><b></b></i>
             	<c:if test="${leftmenu.isNextNode > 0 }">
             		<span class="pull-right">
              		<i class="fa fa-angle-down text"></i>
              		<i class="fa fa-angle-up text-active"></i>
              		</span>
              	</c:if>
             </c:if>
             <c:if test="${ischildnode != '0' }">
             	<i class="${leftmenu.icon}"></i>
             	<c:if test="${leftmenu.isNextNode > 0 }">
              		<i class="fa fa-angle-down text"></i>
              		<i class="fa fa-angle-up text-active"></i>
             	</c:if>
             </c:if>
             <span>${leftmenu.name}</span>
             
         </a>
         <c:if test="${leftmenu.isNextNode > 0 }">
         <ul class="nav lt" id="divleft${leftmenu.id }">
		 </ul>
		 </c:if>
       </li>
   </c:forEach>                           