/**
 * 校验授权是否过期
 */
function linceseMes(){
	$.ajax({
		type : "GET",
		url : getRootPath()+"/licenseMes",
		data : null,
		dataType : "json",
		success : function(data) {
			if (data) {
				if(data.state){
					$('#licenseMes').html(data.mes);
					$('#dLinceseModal').modal('show');
				}
			}
		}
	});
}
