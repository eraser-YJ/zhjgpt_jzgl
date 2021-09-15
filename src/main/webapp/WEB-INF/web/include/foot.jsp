<%@ page language="java" pageEncoding="UTF-8"%>
<script src='${sysPath}/js/com/jc/common/footer-tools.js'></script>
</body>
<script type="text/javascript">
JCFF.$document.ready(function(){
	ie8StylePatch();
	iePlaceholderPatch();
	searchConditionControl();
	openIframeMenu();
	var JCFL = null;

	$('#dataLoad_mail').fadeOut();

	if(JCFF.getTop().JCF){
		//获取desktop中初始化的菜单对象
		JCFL = JCFF.getTop().JCF.histObj;

		//判断是否是
		var oidMenu = JCFL.getLastData() ? JCFL.getLastData().id : '';

		var oldMenuId = (oidMenu && oidMenu.split('jcleftmenu_')[1]) || '${menuId}';
		//打开左菜单对应的功能
		JCFL.sidemenu.goActivity(oldMenuId);

		//$('#navigationMenu').html(JCFL.sidemenu.getCrumbs(oldMenuId));
	}

	JCFF.$document
			.on("click",function(e){
				hiddenOtherModules(e);
				if(JCFF.version3()){
					if(JCFL) JCFL.sidemenu.searchHide();
				}
			})
			.on("keydown",function(e){
				if(e.which==27) {
					$(".bootstrap-datetimepicker-widget").hide();
				}else if (e.keyCode == 8 && e.target.tagName != "INPUT" && e.target.tagName != "TEXTAREA"){
						return false;
				}
	});
});

</script>
</html>
