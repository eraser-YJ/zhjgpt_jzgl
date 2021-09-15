/**
 * 将表单转化为查询表单
 */
function convertFormToQuery(formId){
    $("#"+formId).find(".form-control").each(function(){
        var item = $(this);
        var parent = item.parent();
        if(item.is("select")){
            parent = parent.parent();
            parent.find("div[fill='content']").remove();
            item.parent().css("display","none");
            parent.append("<div fill='content'>"+item.val()+"</div>")
        }else{
            parent.find("div[fill='content']").remove();
            item.css("display","none");
            parent.append("<div fill='content'>"+item.val()+"</div>")
        }
    });
}

/**
 * 将表单转换为编辑
 * @param formId
 */
function convertFormToEdit(formId){
    $("#"+formId).find(".form-control").each(function(){
        var item = $(this);
        var parent = item.parent();
        if(item.is("select")){
            parent = parent.parent();
            parent.find("div[fill='content']").remove();
            item.parent().css("display","");
        }else{
            parent.find("div[fill='content']").remove();
            item.css("display","");
        }
    });
}
/**
 *  跳转到流程设计器页面方法
 */
function goDesigner(){
    var url = getRootPath()+'/def/designer.action';
    window.open(url);
}

