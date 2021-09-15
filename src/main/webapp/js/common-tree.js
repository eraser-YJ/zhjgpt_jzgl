$(document).ready(function(){
    var windowHeight = $("#content").height(),
        $container   = $("#scrollable");
//    if ($(".postscript")[0]) {
//    	$("#scrollable").scroll(function() {
//    		var a = $("#scrollable").scrollTop() >= 110 ? $("#scrollable").scrollTop() + 6 : 116;
//    		$(".postscript_box").css("top", a + 'px');
//
//    		if ($("#scrollable").scrollTop() >= 110) {
//    			$(".postscript_box").height(ps_defaultH + 110);
//
//    		} else {
//    			$(".postscript_box").height($("#scrollable").scrollTop() + ps_defaultH);
//    		}
//    	});
//    }
    var treeDom = $("#LeftHeight");
    function setTreeDemo(){
        //树高度定义
        if($("#treeDemo").length){
            $(".tree-right").css("padding-left","215px");
            //定义高度
            var leftTreeHeight = windowHeight - 60;
            treeDom.height(leftTreeHeight);
            treeDom.addClass('fixedNav');
            //$container.scroll(function() {
            //    var sTop = $container.scrollTop();
            //    treeDom[sTop >= 80?'addClass':'removeClass']('fixedNav').css('height',(sTop >= 60?(windowHeight - 40):leftTreeHeight)+"px")
            //});
        }
    }
    $(window).resize(function(){
        setTreeDemo();
    });
    setTreeDemo();
});