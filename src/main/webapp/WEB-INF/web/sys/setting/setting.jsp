<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
<header class="con-header pull-in" id="navigationMenu">
	<h1></h1>
	<div class="crumbs"></div>
</header>
<section class="panel m-t-md search-box clearfix">
	<h2 class="panel-heading clearfix">系统参数</h2>
	<form class="table-wrap form-table" id="settingForm">
		<table class="table table-td-striped">
			<tbody>
				<c:forEach items="${settingList}" var="settingvo">
					<c:choose>
						<c:when test="${settingvo.settingInputType == 'radio'}">
							<tr>
								<td class="w240">${settingvo.settingComment}</td>
								<td>
									<c:forEach items="${settingvo.inputDefaultValue}" var="valueAndname" >
									<label class="radio inline">
										<input type="radio" name="${settingvo.settingKey}" value="${valueAndname[0]}" />${valueAndname[1]}
									</label>
									</c:forEach>
									<br/>${settingvo.settingInputRemark}
								</td>
							</tr>
						</c:when>
						<c:when test="${settingvo.settingInputType == 'select'}">
							<tr>
								<td class="w240">${settingvo.settingComment}</td>
								<td>
									<select id="${settingvo.settingKey}" name="${settingvo.settingKey}">
										<c:forEach items="${settingvo.inputDefaultValue}" var="valueAndname" >
										<option value='${valueAndname[0]}'>${valueAndname[1]}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<td>${settingvo.settingComment}</td>
								<td>
									<input type="text" id="${settingvo.settingKey}" name = "${settingvo.settingKey}" maxlength="${settingvo.settingInputSize}" class="input m-l-none" />${settingvo.settingInputRemark}</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</tbody>
		</table>
		<section class="form-btn m-b-lg">
			<button class="btn dark ok" type="button" id="settingbtn">保 存</button>
		</section>
	</form>
</section>
</section>
<script src="${sysPath}/js/com/sys/setting/setting.js" type="text/javascript"></script>
<script>
	$(document).ready(function(){
	//初始化校验方法
		$("#settingForm").validate({
			rules: {
				<c:forEach items="${settingList}" var="settingvalid">
					${settingvalid.settingInputValidate}
				</c:forEach>
				isMsgService:
				{
					required: false,
					maxlength: 1
				},
				msgPrefix:
				{
					required: false,
					maxlength: 20
				},
				suggestionType:
				{
					required: false,
					maxlength: 1
				},
				showIdentifyingCode:
				{
					required: false,
					maxlength: 1
				},
				useIpBanding:
				{
					required: false,
					maxlength: 1
				},
				maxErrorCount:
				{
					required: true,
					maxlength: 1,
					rangeDigits: [0,6]
				},
				lockTime:
				{
					required: true,
					rangeDigits : [4,100000]
				},
				worklogDay:
				{
					required: true,
					rangeDigits: [-1,31]
				},
				loginType:
				{
					required: false,
					maxlength: 1
				},
				netKey:
				{
					required: false,
					maxlength: 1
				},
				controlPrint:
				{
					required: false,
					maxlength: 1
				},
				signType:
				{
					required: false,
					maxlength: 1
				},
				filePath:
				{
					required: false,
					maxlength: 200,
					isNotChinese : true
				},
				photoPatn:
				{
					required: false,
					maxlength: 200,
					isNotChinese :true
				},
				emailSaveTime:
				{
					required: true,
					rangeDigits: [0,100]
				}
			},
			messages: {
				maxErrorCount: {rangeDigits: "只能输入[1-5]之间的整数"},
				emailSaveTime: {rangeDigits: "只能输入[1,99]之间的整数"},
				worklogDay: {rangeDigits: "只能输入[0,30]之间的整数"}
			}
		});
		//只能输入[5-99999]之间的数字
		jQuery.validator.addMethod("rangeDigits", function(value, element, param) {
			var reg1 = true;
			var reg2 = true;
			if(param[0] >= value){
				reg1 = false;

			}
			if(param[1] <= value){
				reg2 = false;
			}
			return (this.optional(element) || /^\d+$/.test(value)) && reg1 && reg2;
		}, "只能输入[5-99999]之间的整数");


	});
</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>