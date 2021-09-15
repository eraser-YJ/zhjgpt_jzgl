<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<workflow:supervisor/>
<div id="formFoorDiv" class="m-l-md">
	<section id="formButton" class=" form-btn statistics-box">
		<c:choose>
			<c:when test="${workflowParam.buttonList!= null && fn:length(workflowParam.buttonList) != 0}">
				<c:forEach var="button" items="${workflowParam.buttonList}" varStatus="status">
					<c:set var="cssStr" value="btn"/>
					<c:if test="${status.index==0}">
						<c:set var="cssStr" value="btn dark"/>
					</c:if>
					<c:choose>
						<c:when test="${button.type== 'goBack'}">
							<script type="text/javascript">
								var worlflowButtonHistoryObj = JCFF.getTop().JCF.histObj;
								if(worlflowButtonHistoryObj.size() > 1){
									$("#formButton").append("<button class='${cssStr}' onclick='formDetail.showRoute(\"${button.type}\")' type='button'>${button.name}</button>")
								}
							</script>
<%--
							<button class='${cssStr}' onclick='formDetail.showRoute("${button.type}")' type="button">${button.name}</button>
--%>
						</c:when>
						<c:otherwise>
							<button class='${cssStr}' onclick='formDetail.showRoute("${button.type}")' type="button">${button.name}</button>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:when>
		</c:choose>
	</section>
</div>

