$("#shortcutForm").validate({
    ignore:'ignore',
    rules: {
name:{
    required: true,
    maxlength:64,
},
icon:{
    required: false,
    maxlength:64,
},
subsystemid:{
    required: false,
},
menuid:{
    required: false,
},
queue:{
    required: true,
},
isShow:{
    required: false,
},
permission:{
    required: false,
    maxlength:200,
},
defaultType:{
    required: false,
    maxlength:200,
},
openType:{
    required: false,
    maxlength:200,
},
    }
});