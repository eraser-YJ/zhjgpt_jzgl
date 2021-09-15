/*
*加载数据
  * */
var layerConfig;
define([
	"dojo/dom-construct",
 	"dojo/request/xhr",
    "dojo/request",
    "dojo/domReady!"
],
 function (domConstruct,xhr,request) {
 		var initData=null;
 		initData={
 			
           
		    init: function(ztName){
					var configFile = getRootPath() + "/TDTLib/config/"+ztName+".json";
					console.log(configFile);
				
					var def = xhr(configFile, {handleAs: "json",sync:true});
					def.then(function(appConfig) {
						layerConfig=appConfig;
				   });
				   return layerConfig;
  			},
 		};
          
		 return initData;
});

