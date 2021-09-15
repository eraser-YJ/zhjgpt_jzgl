$(document).ready(function(){
	var deptAdd = $("#departmentForm").validate({
		rules: {
			name: { required: true, specialChar: true, maxlength: 255 },
			deptDesc: { required: false, maxlength: 255 },
			weight: { required: true, digits: true, min:1, max:100, maxlength: 3 },
			queue: { required: true, digits: true, min:1, maxlength: 5 },
			code: { required: true, maxlength: 4, minlength: 4 }
		}
	});

	var deptUpdate = $("#departmentUpdateForm").validate({
		rules: {
			name: { required: true, specialChar: true, maxlength: 255 },
			deptDesc: { required: false, maxlength: 255 },
			weight: { required: true, digits: true, min:1, max:100, maxlength: 3 },
			queue: { required: true, digits : true, min:1, maxlength: 5 }
	    }
	});
});
