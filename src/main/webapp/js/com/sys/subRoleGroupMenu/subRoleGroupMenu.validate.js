$("#subRoleGroupMenuForm").validate({
    ignore:'ignore',
    rules: {
roleGroupId:{
    required: false,
},
menuId:{
    required: false,
},
weight:{
    required: false,
},
  secret:{
    required: false,
    maxlength:32
}

    }
});