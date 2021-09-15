$("#dlhDatamodelItemForm").validate({
    ignore:'ignore',
    rules: {
    	dbCode:{
		    required: true,
		    maxlength:16
		},
		dbType:{
		    required: true,
		    maxlength:64
		},
		dbAddress:{
		    required: true,
		    maxlength:200
		},
		dbUsername:{
		    required: true,
		    maxlength:64
		},
		dbPassword:{
		    required: false,
		},
		dbTxt:{
		    required: false,
		    maxlength:200
		}

    }
});