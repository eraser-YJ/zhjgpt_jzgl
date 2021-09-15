$("#subRoleGroupForm").validate({
	ignore : 'ignore',
	rules : {
		groupName : {
			required : true,
			maxlength : 256
		},
		groupDescription : {
			required : false,
			maxlength : 512
		}
	}
});