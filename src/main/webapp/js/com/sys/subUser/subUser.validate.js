$("#subUserForm").validate({
    ignore:'ignore',
    rules: {
code:{
    required: false,
    maxlength:64
},
displayName:{
    required: true
},
loginName:{
    required: false,
    maxlength:50
},
sex:{
    required: false,
    maxlength:32
},
dutyId:{
    required: false,
    maxlength:10
},
deptId:{
    required: true,
},
status:{
    required: false,
    maxlength:32
},
leaderId:{
},
deptLeader:{
    required: false,
},
chargeLeader:{
    required: false,
},
weight:{
    required: false,
},
orderNo:{
    required: true,
    maxlength: 6,
    isPositive: true
},
  theme:{
    required: false,
    maxlength:100
}

    }
});