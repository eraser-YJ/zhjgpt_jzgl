<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/include/taglib.jsp"%>


<link type="text/css" rel="stylesheet" href="${sysPath}/css/jsmind/jsmind.css" />
<style type="text/css">
	#jsmind_container{
        width: 90%;
		height:600px;
		border:solid 1px #ccc;
		/*background:#f4f4f4;*/
		background:#f4f4f4;
	}
</style>



<section class="scrollable padder jcGOA-section" id="scrollable">

    <div id="jsmind_container"></div>
    <div id="loadMindPageDiv"></div>
</section>

<script type="text/javascript" src="${sysPath}/js/jsmind/jsmind.js"></script>
<script type="text/javascript" src="${sysPath}/js/jsmind/jsmind.draggable.js"></script>
<script type="text/javascript" src="${sysPath}/js/jsmind/features/jsmind.shell.js"></script>
<script type="text/javascript">
    var _jm = {};
	function initJsmind() {
        $.ajax({
            type : "GET",
            url : getRootPath()+"/dlh/dlhQuery/getMindData.action",
            data : {"dlh_data_src_": "${dlh_data_src_}","dlhDataId":"${dlhDataId}"},
            dataType : "json",
            success : function(data) {
                load_jsmind(data);

            }
        });

    }
    function load_jsmind(data){
        var mind = {
            "meta":{
                "name":"关系图",
                "author":"hizzgdev@163.com",
                "version":"0.2",
            },
            "format":"node_array",
            "data":data
        };
        var options = {
            container:'jsmind_container',
            editable:false,
            theme:'primary'
        }
        _jm = jsMind.show(options,mind);
        addallclick();
        // jm.set_readonly(true);
        // var mind_data = jm.get_data();
        // alert(mind_data);
       /* jm.add_node("sub2","sub23", "new node", {"background-color":"red"});
        jm.set_node_color('sub21', 'green', '#ccc');*/
    }


	function addallclick(){
	    var nodes = document.getElementsByTagName("jmnode");
	    for(var index=0;index<nodes.length;index++){
	        var node = nodes[index];
	        $(node).on("click",function (){
	            // debugger;
                var selected_node = _jm.get_selected_node();
                openMindPageListBtn(selected_node.id,selected_node.data.objUrl);
                // debugger;
            });
        }



	}
    function openMindPageListBtn(id,dlh_data_src_){
        $("#loadMindPageDiv").html("")
        $("#loadMindPageDiv").load(getRootPath()+"/dlh/dlhQuery/loadDetail.action?viewType=view&dlhDataId="+id+"&dlh_data_src_="+dlh_data_src_+"&n_"+(new Date().getTime()),null,function(){
            dlhTableMapForm.show();
        });
        //window.location.href=getRootPath()+"/dlh/dlhQuery/loadDetail.action?dlhDataId="+id+"&dlh_data_src_="+dlh_data_src_+"&n_"+(new Date().getTime());
    }



    /*$(document).ready(function(){
        initJsmind();

    });*/
    // initJsmind();
    initJsmind();

</script>
<%@ include file="/WEB-INF/web/include/foot.jsp"%>