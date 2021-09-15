$("#tabTreeForm").validate({
    ignore:'ignore',
    rules: {
sysPermission:{
    required: true,
    maxlength:10,
},
tabTitle:{
    required: true,
    maxlength:8,
},
tabUrl:{
    required: true,
    maxlength:64,
},
tabFlag:{
    required: true,
    maxlength:20,
},
    }
});