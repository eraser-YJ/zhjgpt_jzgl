
/**
 * 流程定义相关
 */
var processDefinition = {
};

processDefinition.openList = function(definitionId,type,entrance){
	var ajaxData = {
			definitionId:definitionId
	}
	$.get(getRootPath()+"/form/getFormByDefinitionId.action",ajaxData,function(data){
		var url = "/"+data.form.listUrl;
		if(url.indexOf("?")!=-1){
			url+="&definitionId="+definitionId;
		}else{
			url+="?definitionId="+definitionId;
		}
		JCFF.loadPage({url:url});
	});
}