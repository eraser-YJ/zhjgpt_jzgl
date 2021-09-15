$("#subRoleForm").validate({
	ignore : 'ignore',
	rules : {
		deptId : {
			required : false,
		},
		roleName : {
			required : true,
			maxlength : 256
		},
		roleDescription : {
			required : false,
			maxlength : 255
		}
	}
});