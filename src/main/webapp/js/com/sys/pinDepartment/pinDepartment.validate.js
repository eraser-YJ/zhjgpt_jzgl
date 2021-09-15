$("#pinDepartmentForm").validate({
    ignore:'ignore',
    rules: {
deptId:{
    required: false,
},
deptName:{
    required: false,
    maxlength:64,
},
deptInitials:{
    required: false,
    maxlength:64,
},
deptAbbreviate:{
    required: false,
    maxlength:64,
},
deptFull:{
    required: false,
    maxlength:64,
},
    }
});