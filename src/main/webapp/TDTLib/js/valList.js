$('#resTypeList a').click(function() {
    if ($(this).attr('id') == 2) {//水准点
        initData("szd");
    }else if ($(this).attr('id') == 3) {//三角点
        initData("sjd");
    }else if ($(this).attr('id') == 4) {//GNSS成果
        initData("GNSS");
    }else if ($(this).attr('id') == 5) {//矢量地图--默认1万
        initData("sldt_1w");
    }else if ($(this).attr('id') == 6) {//DEM--默认1万
        initData("DEM_1w");
    }else if ($(this).attr('id') == 7) {//DEM--分幅正射影像
        initData("DOM_1w");
    }else if ($(this).attr('id') == 8) {//默认2018
        initData("yxsj_2018");
    }else if ($(this).attr('id') == 11) {//模拟地形图-默认1万2000年
        initData("mndxt_1w2000");
    }
    
    $(this).parent().prev().html($(this).html() + '<i class="icon-res icon-down5"></i>');
   //$('.listPage').html(appendC(szd));
})
function appendC() {
	var ary=layerConfig.queryFileds;
    var str = '';
    for (var i = 0; i < ary.length; i++) {
        if (ary[i].format == 'date') {
            str += '<div class="startdate">' +
                '<input type="text" class="laydate-icon" id="start-date" placeholder="起始日期" readonly="readonly" tempid="sysidFormData"><i class="icon-cale icon-calendar24"></i></div>' +
                '<div id="spliceID" style="margin-top:4px;margin-left:2px;margin-right:2px;color:#AAAAAA;float:left;">─</div>' +
                '<div class="enddate"><input type="text" class="laydate-icon" id="end-date" placeholder="结束日期" readonly="readonly" tempid="sysidFormData"><i class="icon-cale icon-calendar24"></i></div>'
        } else {
            ary[i].list = '';
            for (var j = 0; j < ary[i].values.length; j++) {
                ary[i].list += '<a class="valBox" title="' + ary[i].values[j].name + '" >' + ary[i].values[j].name + '</a>'
            }
            str += '<div class="attrItem" id="' + ary[i].name + '">' +
                '<div class="itemValue">' + ary[i].dName + '<i class="icon-res icon-down5"></i></div>' +
                '<div class="valList" data-abc="' + ary[i].name + '">' + ary[i].list +
                '<div class="clearAttrBtn">清除选择' +
                '<div></div>' +
                '</div>' +
                '</div>' +
                '</div>'
                str+='<input type="hidden" name="' + ary[i].name + '" id="'+ ary[i].name + '_InputValue" value="" />';
        }
        
    }
    return str;
}

$('.listPage').delegate("a", "click", function() {
    $(this).parent().prev().html($(this).html() + '<i class="icon-res icon-down5"></i>');
    var selectValue = $(this).attr("title");
    var inputId=$(this).parent().data('abc')+"_InputValue";
    $("#"+inputId).attr("value",selectValue);//推荐这种写法,可正常赋值
})

$('.attrsBox').delegate(".clearAttrBtn", "click", function() {
    for (var i = 0; i < layerConfig.queryFileds.length; i++) {
            if ($(this).parent().data('abc') == szd[i].name) {
                $(this).parent().prev().html(szd[i].dName + '<i class="icon-res icon-down5"></i>');
                var inputId=$(this).parent().data('abc')+"_InputValue";
    			$("#"+inputId).attr("value","");//清空
            }
    }
})

function createScale(){
	if(layerConfig.idMapScale&&layerConfig.idMapScale!=""){
		var trHtml="";
		var view;
        for (var j = 0; j < layerConfig.idMapScale.length; j++) {
            trHtml += '<a class="valBox" value="' + layerConfig.idMapScale[j].value + '" title="' + layerConfig.idMapScale[j].name + '" >' + layerConfig.idMapScale[j].name + '</a>'
        	if(layerConfig.idMapScale[j].flag){
        		view=layerConfig.idMapScale[j].name;
        	}
        }
        var str = '<div class="itemValue">' + view + '<i class="icon-res icon-down5"></i></div>' +
            		'<div class="valList"  id="scaleList">' + trHtml +'</div>' ;
		
		$('#createScale').html(str);
		$('#createScale').show();
	}else{
		$('#createScale').hide();
	}
}
$('#createScale').delegate("a", "click", function() {
	
    $(this).parent().prev().html($(this).html() + '<i class="icon-res icon-down5"></i>');
    var value = $(this).attr("value");
    initData(value);

})
/*清空隐藏标签数据*/
function clearInputValue(){
	var inputArrys = $(".attrsBox input[type=hidden]");
	for (var i=0;i<inputArrys.length;i++) {
		inputArrys[i].value=''; 
	}
}