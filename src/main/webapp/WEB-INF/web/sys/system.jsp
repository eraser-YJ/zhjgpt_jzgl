<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>

<section class="scrollable padder" id="scrollable">
     <%--<header class="con-header pull-in" id="navigationMenu">
           <h1>系统管理</h1>
           <div class="crumbs">
               <a href="#" onclick="homeloadmenu()">首页</a><i></i>系统管理
           </div>
   </header>--%>
   <section class="m-t-md panel personal-con">
   	<div class="personal-con-left">
       	<div class="personal-icon">
       		<b class="rounded">
               	<i class="fa fa-system"></i>
                   	系统管理
               </b>
          	</div>
       </div>
   	<div class="personal-con-right">
       	<div class="m-b-lg personal-img"><img src="${sysPath}/images/demoimg/system.jpg" width="1920" height="215"  alt=""/></div>
           <div class="m-l-lg personal-right-con">
           	<p><i class="fa fa-stop fl"></i>系统管理提供了系统运行的基础管理；</p>
           	<p><i class="fa fa-stop fl"></i>可以设立组织机构、用户及角色、并给用户赋予角色；提供操作日志、系统任务等业务功能；</p>
           	<p><i class="fa fa-stop fl"></i>各级组织通过权限模型设计与分级、数据权限管理体系设计实现既共享又独立运行的垂直管理服务。</p>
           </div>
     </div>
   </section>
</section>

<script>
  $(function(){
	ie8InRounded();
	var content_height=$("#content").height();
	$(".personal-con").height(content_height-$("#navigationMenu").height()-35);
   })
</script>