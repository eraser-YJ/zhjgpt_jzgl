$("#dlhUserForm").validate({
    ignore:'ignore',
    rules: {
		dlhUsercode:{
		    required: true,
		    maxlength:100
		},
		dlhUsername:{
		    required: true,
		    maxlength:200
		},
		dlhPassword:{
		    required: true,
		    maxlength:200
		},
		batchNum:{
		    required: true,
		},
		remark:{
		    required: false,
		    maxlength:2000
		}

    }
});