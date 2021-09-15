$("#dlhDatamodelItemForm").validate({
    ignore:'ignore',
    rules: {
		modelId:{
		    required: true,
		},
		itemName:{
		    required: true,
		    maxlength:64
		},
		itemComment:{
		    required: true,
		    maxlength:200
		},
		itemType:{
		    required: true,
		    maxlength:64
		},
		itemLen:{
		    required: false,
		},
		itemKey:{
		    required: false,
		    maxlength:200
		},
		itemSeq:{
		    required: true,
		},
		itemCheck:{
		    required: false,
		    maxlength:200
		}

    }
});