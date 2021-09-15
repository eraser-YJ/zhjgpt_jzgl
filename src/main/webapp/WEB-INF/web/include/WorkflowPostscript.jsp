<%@ page language="java" pageEncoding="UTF-8"%>

<div class="postscript_box clearfix" id="postscriptID">
        <div class="table-wrap clearfix">
            <a href="#" class="btn fr ps-btn diologReply" onclick='postscript.showAddPostscript(0)'><i class="fa fa-plus"></i> 增加附言 </a>
        </div>
        <c:set var="postscriptNums" value="0"/>
        <form id="postscriptForm">
	        <input type="hidden" id="postscriptId" name="postscriptId" />
	        <input type="hidden" id="postscriptUserName" name="postscriptUserName" value="<shiro:principal property="displayName"/>" />
	        <div id="postscriptDiv" class="dialog m-r-sm m-l m-t">
	        	<ul>
	        	<c:forEach var="postscriptList" items="${workflowBean.postscriptMap.postscriptList }" varStatus="status">
			 			<li class='clearfix m-b input-group'>
			 			<p class='dialog-text input-group'>
			 			<i class='fa fa-comment input-group-btn p-r-m'></i>
			 			<strong>${postscriptList.createUserName}:</strong>${postscriptList.content}
			 			<span>${postscriptList.realCreateDate}</span>
			 			</p>
			 			<div class='input-group-btn'>
			 			<a href='#' class='a-icon i-new diologReply m-r-xs'  onclick='postscript.showAddPostscript("${postscriptList.id}")'><i class='fa fa-comment'></i>回 复</a>
			 			<c:if test="${workflowBean.loginUserId==postscriptList.createUser}">
							<a href='#' class='a-icon i-remove'><i onclick='postscript.deletePostscript(2,1,"${postscriptList.id}")' class='fa fa-remove' data-toggle='tooltip' data-placement='top' title='' data-container='body' data-original-title='删除'></i></a>
						</c:if>
						</div>
					<c:forEach var="postscriptReplyList" items="${workflowBean.postscriptMap.postscriptReplyList }" varStatus="statusReplay">
						<c:if test="${postscriptList.id==postscriptReplyList.postscriptId}">
							<div class='dis-row'>
							<p class='dialog-text input-group'>
							<i class='fa fa-comment input-group-btn p-r'></i>
							<strong>${postscriptReplyList.createUserName}:</strong>${postscriptReplyList.content}
							<span>${postscriptReplyList.realCreateDate}</span>
							</p>
							<c:if test="${workflowBean.loginUserId==postscriptReplyList.createUser}">
								<div class='input-group-btn'><a class='a-icon i-remove fr' href='javascript:;'><i onclick='postscript.deletePostscript(2,2,"${postscriptList.id}","${postscriptReplyList.id}")' class='fa fa-remove' data-toggle='tooltip' title='' data-placement='top' data-container='body' data-original-title='删除'></i></a></div>
							</c:if>
							</div>
						</c:if>
					</c:forEach>
					<c:set var="postscriptNums" value="${fn:length(workflowBean.postscriptMap.postscriptList)+fn:length(workflowBean.postscriptMap.postscriptReplyList)}"/>
				</c:forEach>
				</ul>
				<div class='replyBox m-t hide'>
					<textarea id='content' name='content' rows='5' style='width:90%'></textarea>
					<section class='form-btn m-b-lg m-t'>
					<button class='btn dark' type='button' onclick="postscript.savePostscript()">确 定</button>
					<button class='btn replyCancel' type='reset'>取 消</button>
					</section>
				</div>
	        </div>
        </form>
        <div class="postscript" id="postscriptClick">
            <i class="fa fa-caret-left"></i>
            <span>附言</span>
            <b class="rounded badgefive badge bg-red" id="postscriptNums">${postscriptNums} </b>
        </div>
    </div>