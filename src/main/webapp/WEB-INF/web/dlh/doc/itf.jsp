<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/head.jsp"%>
<section class="scrollable padder jcGOA-section" id="scrollable">
<section class="panel search-shrinkage clearfix">
<div class="search-line" style="padding-left:50px;">
<h3>接口名称：${head.objName}</h3>
${head.remark}
<h5>1.1 接口说明</h5>
String sendData(String action,String json)<br>
全部接口的调用方法名称详细
<h5>1.2 参数样式</h5>
action：${head.objUrl}<br><br>
json：加密后的参数，数据结构如下
<table style="width:900px;" class="table">
	<tr>
		<td style="width:80px;">序号</td>
		<td style="width:160px;">数据项</td>
		<td style="width:100px;">类型</td>
		<td>说明</td>
	</tr>
	<tr>
		<td>1</td>
		<td>jsonData</td>
		<td>String</td>
		<td>参数只有一个，json字符串，下面为json结构</td>
	</tr>
</table>
<br>
<h5>1.3 json结构说明</h5>
<table style="width:900px;" class="table">
	<tr>
		<td style="width:80px;">序号</td>
		<td style="width:160px;">数据项</td>
		<td style="width:100px;">类型</td>
		<td>说明</td>
	</tr>
	<tr>
		<td>1</td>
		<td>info</td>
		<td>String</td>
		<td>具体数据json数组</td>
	</tr>
	<tr>
		<td>2</td>
		<td>rows</td>
		<td>String</td>
		<td>数据条数</td>
	</tr>
</table>
<br>
<h5>info具体数据项</h5>
<table style="width:900px;" class="table">
	<tr>
		<td style="width:80px;">序号</td>
		<td style="width:160px;">数据项</td>
		<td style="width:160px;">名称</td>
		<td style="width:100px;">类型</td>
		<td style="width:80px;">长度</td>
		<td>说明</td>
	</tr>
	<c:forEach items="${itemList}" var="itemData" varStatus="abcxx">
		<tr>
			<td>${abcxx.count}</td>
			<td>${itemData.fieldCode}</td>
			<td>${itemData.fieldName}</td>
			<td>${itemData.itemType}</td>
			<td>${itemData.itemLen}</td>
			<td>
			<c:forEach items="${itemData.dicList}" var="itemDicData" varStatus="abcxxDic">
				${itemDicData.value}(${itemDicData.code})<br>
			</c:forEach>
			</td>
		</tr>
   	</c:forEach>
</table>
<br>
<h5>1.4 返回结果(JSON)</h5>
<table style="width:900px;" class="table">
	<tr>
		<td style="width:80px;">序号</td>
		<td style="width:160px;">数据项</td>
		<td style="width:100px;">类型</td>
		<td>说明</td>
	</tr>
	<tr>
		<td>1</td>
		<td>code</td>
		<td>String</td>
		<td>返回状态码</td>
	</tr>
	<tr>
		<td>2</td>
		<td>msg</td>
		<td>String</td>
		<td>返回信息</td>
	</tr>
</table>
<br>
<br>
</div>
</section>
</section> 

<%@ include file="/WEB-INF/web/include/foot.jsp"%>