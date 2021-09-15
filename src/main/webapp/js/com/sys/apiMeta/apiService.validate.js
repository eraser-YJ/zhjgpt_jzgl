$("#apiServiceForm").validate({
    ignore:'ignore',
    rules: {
uuid:{
    required: false,
    maxlength:36,
},
subsystem:{
    required: true,
    maxlength:20,
},
appName:{
    required: true,
    maxlength:20,
},
apiName:{
    required: true,
    maxlength:50,
},
uri:{
    required: true,
    maxlength:100,
},
params:{
    required: true,
    maxlength:200,
},
    }
});