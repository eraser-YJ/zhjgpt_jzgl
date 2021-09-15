$("#logoForm").validate({
    ignore:'ignore',
    rules: {
logoName:{
    required: true,
    maxlength:20,
},
logoSign:{
    required: true,
    maxlength:20,
},
logoPath:{
    required: false,
    maxlength:64,
},
    }
});