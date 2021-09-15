$("#dlhDataobjectForm").validate({
    ignore:'ignore',
    rules: {
modelId:{
    required: true,
},
objUrl:{
    required: true,
    maxlength:64
},
objName:{
    required: true,
    maxlength:200
},
disListStyle:{
    required: false,
    maxlength:200
},
disFormStyle:{
    required: false,
    maxlength:200
},
  remark:{
    required: false,
    maxlength:2000
}

    }
});