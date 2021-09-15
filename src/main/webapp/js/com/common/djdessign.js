var DESSeal = {
		getDES:function(){
			return document.getElementById('DES');
		},
		addSeal:function() {
			var des = DESSeal.getDES();
			des.EnableOfficeCtrl(1);
			var a = des.DoAction(1);
			//alert(a);
		}
};