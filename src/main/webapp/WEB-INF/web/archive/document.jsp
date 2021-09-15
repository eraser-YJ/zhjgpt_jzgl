<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>

 <section class="scrollable padder" id="scrollable" style="height:100%;">
                            <header class="con-header pull-in" id="navigationMenu">
                                    <h1>文档管理</h1>
                                    <div class="crumbs">
                                        <a href="#" onclick="JCFF.loadHome();">首页</a><i></i>文档管理
                                    </div>
                            </header>
                            <section class="m-t-md panel personal-con">
                            	<%--<div class="personal-con-left">
                                	<div class="personal-icon">
                                		<b class="rounded">
                                        	<i class="fa fa-document-gl"></i>
                                            文档管理
                                        </b>
                                   	</div>
                                </div>--%>
                            	<%--<div class="personal-con-right">
                                	<div class="m-b-lg personal-img"><img src="../../images/demoimg/document.jpg" width="1920" height="215"  alt=""/></div>
                                    <div class="m-l-lg personal-right-con">
                                    	<p><i class="fa fa-stop fl"></i>文档管理为组织内部的档案资料进行自动或手工归档管理，使文档电子化。实现个人文档、公共文档的平行管控，对于组织内部公共文档的访问权限、个人收藏、文档历史审计等业务需求提供服务。</p>

                                        &lt;%&ndash;显示菜单分类导航 &ndash;%&gt;
                                        <div >
                                            <c:forEach items="${loadTypeList}" var="type" >
                                                <c:if test="${type.isShow eq 1 && not empty type.viewVectoring}">
                                                    <h2 class="panel-heading clearfix ">${type.typeName}</h2>:
                                                    <c:forEach items="${type.viewVectoring}" var="menu" >
                                                        <a class="btn new-folder" href="#" role="button" data-toggle="modal" id="${menu.menuId}" onClick="JCFF.loadPage({url:'${menu.actionName}'});">${menu.menuName}</a>
                                                    </c:forEach>
                                                    <br>
                                                </c:if>
                                                <c:if test="${type.isShow eq 0 && not empty type.viewVectoring}">
                                                    <c:forEach items="${type.viewVectoring}" var="menu" >
                                                        <a class="btn new-folder" href="#" role="button" data-toggle="modal" id="${menu.menuId}" onClick="JCFF.loadPage({url:'${menu.actionName}'});">${menu.menuName}</a>
                                                    </c:forEach>
                                                    <br>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                              </div>--%>
                                <div class="personal-con-top">
                                    <div class="personal-icon">
                                        <b class="rounded">
                                            <i class="fa fa-document-gl m-r-sm"></i>文档管理</b>
                                    </div>
                                </div>
                                    <div class="personal-con-middle">
                                        <%--<div class="m-b-lg personal-img"><img src="../../images/demoimg/interaction.jpg" width="1920" height="215" alt=""/></div>--%>
                                        <div class="m-l-lg personal-right-con">


                                            <%--显示菜单分类导航 --%>
                                            <div >
                                                <c:forEach items="${loadTypeList}" var="type" >
                                                    <c:if test="${type.isShow eq 1 && not empty type.viewVectoring}">
                                                        <div class="clearfix">
                                                            <div class="content-title m-b m-r-xl fl"><div class="heading-title">${type.typeName}</div><div class="triangle"></div></div>
                                                            <c:forEach items="${type.viewVectoring}" var="menu" >
                                                                <div class="fl"><a class="btn-padding m-l-md m-b" href="#" role="button" data-toggle="modal" id="${menu.menuId}" onClick="JCFF.loadPage({url:'${menu.actionName}'});">${menu.menuName}</a></div>
                                                            </c:forEach>
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${type.isShow eq 0 && not empty type.viewVectoring}">
                                                        <c:forEach items="${type.viewVectoring}" var="menu" >
                                                            <a class="btn-padding m-l-md m-b" href="#" role="button" data-toggle="modal" id="${menu.menuId}" onClick="JCFF.loadPage({url:'${menu.actionName}'});">${menu.menuName}</a>
                                                        </c:forEach>
                                                    </c:if>
                                                </c:forEach>
                                            </div>


                                        </div>
                                    </div>
                                    <div class="personal-con-bottom m-l-md">
                                        <p><i class="fa fa-stop fl"></i>文档管理为组织内部的档案资料进行自动或手工归档管理，使文档电子化。实现个人文档、公共文档的平行管控，对于组织内部公共文档的访问权限、个人收藏、文档历史审计等业务需求提供服务。</p>
                                    </div>
                            </section>
                  </section>
<script>
  $(function(){
	ie8InRounded();
	var content_height=$("#content").height();
	/*$(".personal-con").height(content_height-$("#navigationMenu").height()-75)*/
   })
</script>