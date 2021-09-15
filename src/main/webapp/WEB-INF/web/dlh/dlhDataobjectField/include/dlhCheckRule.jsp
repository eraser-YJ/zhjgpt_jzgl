<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>
<%@ taglib prefix="dicex" uri="/WEB-INF/tlds/dicex-tags.tld" %>
<div class="modal fade" id="CheckRuleModal" aria-hidden="false">
	<div class="modal-dialog w900" style="width:600px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h4 class="modal-title">规则管理</h4>
			</div>
			<div class="modal-body" style="height:700px;">
			<div>
				<dicex:ruleDic name="ruleTypeSelItem" id="ruleTypeSelItem"  headName="-请选择-"  headValue="" cssStyle="margin-top:10px;"/>
				<button class="btn dark" type="button" id="ruleSelect" onclick="myCheckRuleModalFun.selectItem()">添 加</button> 
			</div>
			<table class="table table-td-striped">
				<thead>
					<tr>			        
				        <th style="text-align:center;width:180px;">规则名</th>
				        <th style="text-align:center">参数</th>
				        <th style="text-align:center;width:60px;">操作</th>
			        </tr>
		        </thead>
		    <tbody id="CheckRuleModalBody">
		    </tbody>
		   </table>
			</div>
			<div class="modal-footer form-btn">
				<button class="btn dark" type="button" id="saveSelect" onclick="myCheckRuleModalFun.save()">保 存</button> 
				<button class="btn" type="button" id="saveClose" onclick="myCheckRuleModalFun.closeDlg()">关 闭</button> 
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
   var myCheckRuleModalFun = {};
	myCheckRuleModalFun.callback; 
	//显示
	myCheckRuleModalFun.show = function(callbackFun,jsonStr){
		$("#CheckRuleModalBody").html("");
		myCheckRuleModalFun.callback = callbackFun;
		$('#CheckRuleModal').modal('show');
		myCheckRuleModalFun.init(JSON.parse(jsonStr));
	}
	//添加
	myCheckRuleModalFun.index=0;
    myCheckRuleModalFun.selectItem = function () {
    	 var ruleTypeSelItemValue = $("#ruleTypeSelItem").val();
    	 if(ruleTypeSelItemValue == ''){
    	 	return
    	 }
    	 var index = ruleTypeSelItemValue.indexOf("_");
    	 var code = ruleTypeSelItemValue.substring(0,index);
    	 var defaultValue = ruleTypeSelItemValue.substring(index+1);
    	 myCheckRuleModalFun.addLine(code,defaultValue);
    }
    
    myCheckRuleModalFun.init = function (dataList) {
    	if(dataList){
    		for(var index = 0;index < dataList.length;index ++){
				myCheckRuleModalFun.addLine(dataList[index].code,dataList[index].value);
			}
    	}		
    }
    
    myCheckRuleModalFun.addLine = function (code,value) {
    	 myCheckRuleModalFun.index++;    	
    	 var html = "<tr id='trObj_"+myCheckRuleModalFun.index+"'>";
    	 html +="<td style='padding: 1px;'>";
    	 html +="<input type='text' id='checkRuleCode' name='checkRuleCode' value='"+code+"' readonly style='width:100%;height:100%;margin-bottom: 0px;'>";    	
    	 html +="</td>";
    	 html +="<td  style='padding: 1px;'>";
    	 html +="<input type='text' id='checkRuleValue' name='checkRuleValue' operationCode='"+code+"' value='"+value+"' style='width:100%;height:100%;margin-bottom: 0px;'>";
    	 html +="</td>";
    	 html +="<td  style='padding: 1px;text-align:center'>";
    	 html +="<input type='button' id='checkRuleDelBtn' name='checkRuleDelBtn' value='X' onclick='myCheckRuleModalFun.del("+myCheckRuleModalFun.index+")'>";
    	 html +="</td>";
    	 html +="</tr>"; 
    	 $("#CheckRuleModalBody").append(html);
    }
     //保存
    myCheckRuleModalFun.del = function (trIndex) { 
    	$("#trObj_"+trIndex).remove();        
    }
    //保存
    myCheckRuleModalFun.save = function () {
    	var cond = [];
    	var hasValueNull = false;
    	$("input[name='checkRuleValue']").each(function(objIndex,object){		
			var value = $(object).val();
			if(value != ''){
				var code = $(object).attr("operationCode");
				var value = $(object).val();
				cond.push({"code":code,"value":value})
			} else {
				hasValueNull = true;
			}
		});
		if(hasValueNull){
			alert("请输入检查项值")
			return;
		}
    	var jsonStr = JSON. stringify(cond);
    	myCheckRuleModalFun.callback(jsonStr);    	
		$("#CheckRuleModalBody").html("");
    	$('#CheckRuleModal').modal('hide');        
    }
    //保存
    myCheckRuleModalFun.closeDlg = function () { 
		$("#CheckRuleModalBody").html("");
    	$('#CheckRuleModal').modal('hide');        
    }
</script>
