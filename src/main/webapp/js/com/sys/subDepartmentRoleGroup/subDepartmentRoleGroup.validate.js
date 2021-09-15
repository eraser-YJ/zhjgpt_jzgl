$("#subDepartmentRoleGroupForm").validate({
	ignore : 'ignore',
	rules : {
		deptId : {
			required : false,
		},
		roleGroupId : {
			required : false,
		},
		groupName : {
			required : false,
			maxlength : 256
		},
		weight : {
			required : false,
		},
		secret : {
			required : false,
			maxlength : 32
		}

	}
});