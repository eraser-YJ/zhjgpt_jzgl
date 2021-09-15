/**
 * 地图详情数据处理
 */
var MapDetails = function() {	
	/**
	 * 获取地图详情数据
	 */
	this.getData=function(i){
		/*图片旁边的简介*/	
		var b=i.title.提供单位
		$(".mapDetails_title").html(i.title);
		
		if(b=="卫星中心"){
			var uu="http://sjfw.sasmac.cn/product/meta.imager?flag=1&metaId="+i.title.影像ID
			$(".mapDetails_picture").html("<img width=298 height=298 src="+uu+" w>");
		}else{
			$(".mapDetails_picture").html("<img  src="+i.title.url+">");
		}
		if(b!=undefined){
	        $(".mapDetails_summary").html("<span><a href='' style='text-decoration: none;color:blue;font-family: microsoft yahei;font-size:18px;'>提供单位："+b+"</a></span>")
		}else{
			 $(".mapDetails_summary").html("<span><a href='' style='text-decoration: none;color:blue;font-family: microsoft yahei;font-size:18px;'>提供单位：无</a></span>")
		}
		var p=i.title
		for(n in p){
		   if(n!="提供单位"&n!="url"&n!="分发单位链接地址"&n!="分发单位信息"&n!="分发单位"){
		   	$(".mapDetails_summary").append("<p style='line-height:25px;font-family: microsoft yahei;font-size:18px'>"+n+"："+p[n]+"</p>")
		   }
			
		}
		/**显示所有元数据***/
		var u=i.source
		for(m in u){
			$(".mapDetails_dataContent1").append("<div class='mapDetails_dataContent3' >"+m+"："+u[m]+"</div>")
		}
		/*****地图详情******/
		//类型数据
		var y=i.tag.resource
		for(t in y){
			$(".mapDetails5").append("<div class='mapDetails_dataContent2' >"+t+"："+y[t]+"</div>")
		}
		//时间类型
		/*var e=i.tag.time
		for(e1 in e){*/
			//$(".mapDetails8").append("<div class='mapDetails_dataContent2' >"+e1+"："+e[e1]+"</div>")
		//}
		if(i.source.更新日期!=undefined){
		  $(".mapDetails8").append("<div class='mapDetails_dataContent2' >更新日期："+i.source.更新日期+"</div>")
		}else{
			 $(".mapDetails8").append("<div class='mapDetails_dataContent2' >更新日期：无</div>")	
		}
		//空间数据类型
		var space=i.tag.space
		for(s in space){
			$(".mapDetails6").append("<div class='mapDetails_dataContent2' >"+s+"："+space[s]+"</div>")
		}
	}
	/***显示地图****/
//	//获取地图的
//	this.getMap=function(str){
//		 $(document).ready(function(){
//			 var map = new OLayers2({
//	                mapName : "map"
//	              });
//				 map.addPolygon(str, {
//			            id : "11"
//			        }, {
//			            strokeWidth : 1,
//			            strokeOpacity : 1,
//			            strokeColor : "#79c74f",
//			            fillColor : "#79c74f",
//			            fillOpacity : 0.5,
//
//			        });
//		    })	
//	}
	
	
	
	
	
}
