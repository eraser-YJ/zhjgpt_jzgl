$("#dlhDataobjectFieldForm").validate({
    ignore:'ignore',
    rules: {
objectId:{
    required: false,
},
modelId:{
    required: false,
},
fieldName:{
    required: true,
    maxlength:200
},
fieldCode:{
    required: true,
    maxlength:200
},
itemName:{
    required: true,
    maxlength:200
},
fieldSeq:{
    required: true,
},
fieldCheck:{
    required: false,
    maxlength:200
},
fieldListShow:{
    required: false,
},
  fieldFormShow:{
    required: false,
}

    }
});