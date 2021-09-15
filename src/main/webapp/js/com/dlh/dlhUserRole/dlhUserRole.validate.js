$("#dlhUserRoleForm").validate({
    ignore:'ignore',
    rules: {
userId:{
    required: false,
    maxlength:36
},
dataId:{
    required: false,
    maxlength:36
},
userName:{
    required: false,
    maxlength:200
},
objName:{
    required: false,
    maxlength:200
},
objUrl:{
    required: false,
    maxlength:64
},
batchNum:{
    required: false,
},
  isEnable:{
    required: false,
    maxlength:200
}

    }
});