$(document).ready(function () {
//初始化校验方法
    $("#roleForm").validate({
        ignore: ".ignore",
        rules: {
            name:
                {
                    required: true,
                    maxlength: 10,
                    minlength: 2,
                    specialChar: true,
                    remote: {
                        url: getRootPath() + "/sys/role/checkRoleName.action?time=" + new Date().getTime(),
                        type: "get",
                        dataType: 'json',
                        data: {
                            'deptIds': function () {
                                return $("#roleListForm #deptId").val();
                            },
                            'name': function () {
                                return $("#roleForm #name").val();
                            },
                            'nameOld': function () {
                                return $("#roleForm #nameOld").val();
                            }
                        }
                    }
                },
            extStr5:
                {
                    required: true,
                    maxlength: 10,
                    minlength: 2,
                    specialChar: true,
                    remote: {
                        url: getRootPath() + "/sys/role/checkRoleName.action?time=" + new Date().getTime(),
                        type: "get",
                        dataType: 'json',
                        data: {
                            'deptIds': function () {
                                return $("#roleListForm #deptId").val();
                            },
                            'name': function () {
                                return $("#roleForm #name").val();
                            },
                            'nameOld': function () {
                                return $("#roleForm #nameOld").val();
                            }
                        }
                    }
                },
            weight:
                {
                    required: true,
                    digits: true,
                    min: 30,
                    max: 100,
                    maxlength: 3
                },
            bestrideWeight:
                {
                    required: true,
                    digits: true,
                    min: 30,
                    max: 100,
                    maxlength: 3
                },
            description:
                {
                    required: true,
                    specialChar: true,
                    maxlength: 255,
                    minlength: 2
                }

        },
        messages: {
            name: {remote: "角色名称已存在"},
            adminSideV: {required: "请选择管理员机构树"}
        }
    });

    $("#roleBlocksForm").validate({
        rules: {}
    });

});