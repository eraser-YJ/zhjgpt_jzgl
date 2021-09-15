var wfcommon = {};

wfcommon.setSuggestArea = function(inValue){
	$("#zhubansuggestTextareaContent").find("textarea").each(function(){
	   this.value = inValue;
	});
}