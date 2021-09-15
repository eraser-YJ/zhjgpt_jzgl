$("#dlhDatamodelForm").validate({
    ignore:'ignore',
    rules: {
		tableCode:{
		    required: true,
		    maxlength:64
		},
		dbSource:{
		    required: true,
		    maxlength:64
		},
		tableName:{
		    required: true,
		    maxlength:200
		},
		  remark:{
		    required: false,
		    maxlength:2000
		}

    }
});