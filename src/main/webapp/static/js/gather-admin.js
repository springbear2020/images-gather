$(function () {
    /*
    * =================================================================================================================
    * Management item change event
    * =================================================================================================================
    */
    $(".manage-item").click(function () {
        // Highlight the current item
        $(".li-management-item").removeClass("active");
        $(this).parent().addClass("active");
    });

    /*
    * =================================================================================================================
    * Class data import batch
    * =================================================================================================================
    */
    // File content change event
    $(".excel-file").on('change', function (e) {
        // Check the format of the file user chosen
        var fileName = $(this).val();
        var fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (!(fileSuffix == ".xls" || fileSuffix == ".xlsx")) {
            showNoticeModal(CODE_WARN, "请选择 Excel 格式文件");
            return false;
        }

        var fileObj = e.target.files[0];
        var formData = new FormData();
        formData.append("file", fileObj);

        showNoticeModal(CODE_INFO, "文件上传处理中，请稍等···");

        // Ask server for save the excel file and import the data
        $.ajax({
            url: contextPath + "transfer/class.do",
            type: "post",
            dataType: "json",
            data: formData,
            cache: false,
            processData: false,
            contentType: false,
            success: function (response) {
                showNoticeModal(response.code, response.msg);
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求上传文件失败，请稍后重试");
            }
        })
    });

    /*
    * =================================================================================================================
    * Class information details view
    * =================================================================================================================
    */

    // Display all schools' information
    function displaySchools() {
        $.ajax({
            url: contextPath + "school.do",
            type: "get",
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS == response.code) {
                    var schoolList = response.resultMap.list;
                    var $ancestor = $(".school-list");
                    $ancestor.empty();
                    // Traverse all school list and create element to display it
                    $.each(schoolList, function (index, school) {
                        var $li = $("<li></li>").appendTo($ancestor);
                        $("<a></a>").attr("role", "button").addClass("school-select").attr("schoolId", school.id).append(school.school).appendTo($li);
                    });
                    // Choose school to view grades
                    $(".school-select").click(function () {
                        $(".school-query").text($(this).text());
                        var schoolId = $(this).attr("schoolId");
                        displayGrades(schoolId);
                    });
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求学校信息失败，请稍后重试");
            }
        });
    }

    // Display all grades' information
    function displayGrades(schoolId) {
        $.ajax({
            url: contextPath + "grade.do",
            data: "schoolId=" + schoolId,
            type: "get",
            dataType: "json",
            success: function (response) {
                var gradeList = response.resultMap.list;
                var $ancestor = $(".grade-list");
                $ancestor.empty();
                // Traverse all grade list and create element to display it
                $.each(gradeList, function (index, grade) {
                    var $li = $("<li></li>").appendTo($ancestor);
                    $("<a></a>").attr("role", "button").addClass("grade-select").attr("gradeId", grade.id).append(grade.grade).appendTo($li);
                });
                // Choose grade to view classes
                $(".grade-select").click(function () {
                    $(".grade-query").text($(this).text());
                    var gradeId = $(this).attr("gradeId");
                    displayClasses(gradeId);
                });
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求年级信息失败，请稍后重试");
            }
        });
    }

    // Display all classes information
    function displayClasses(gradeId) {
        $.ajax({
            url: contextPath + "class.do",
            type: "get",
            data: "gradeId=" + gradeId,
            dataType: "json",
            success: function (response) {
                var classList = response.resultMap.list;
                var $ancestor = $(".class-list");
                $ancestor.empty();
                // Traverse all grade list and create element to display it
                $.each(classList, function (index, aClass) {
                    var $li = $("<li></li>").appendTo($ancestor);
                    $("<a></a>").attr("role", "button").addClass("class-select").attr("classId", aClass.id).append(aClass.className).appendTo($li);
                });
                // Choose class to view people list in this class
                $(".class-select").click(function () {
                    $(".class-query").text($(this).text());
                    var classId = $(this).attr("classId");
                    displayClassPeopleList(classId);
                });
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求班级信息失败，请稍后重试");
            }
        });
    }

    // Display all people list in selected class name
    function displayClassPeopleList(classId) {
        $.ajax({
            url: contextPath + "user/class.do",
            type: "get",
            data: "classId=" + classId,
            dataType: "json",
            success: function (response) {
                var userList = response.resultMap.list;
                var $ancestor = $(".table-body");
                $ancestor.empty();
                // Traverse the people list and create element to display it
                $.each(userList, function (index, user) {
                    var $tr = $("<tr></tr>").appendTo($ancestor);
                    $("<td></td>").append(user.id).appendTo($tr);
                    $("<td></td>").append(user.name).appendTo($tr);
                    $("<td></td>").append(user.sex).appendTo($tr);
                    $("<td></td>").append(user.username).appendTo($tr);
                    $("<td></td>").append(user.phone).appendTo($tr);
                    $("<td></td>").append(user.email).appendTo($tr);
                    var $td = $("<td></td>").appendTo($tr);
                    var $editIcon = $("<span></span>").addClass("glyphicon glyphicon-user").attr("aria-hidden", true);
                    $("<a></a>").attr("role", "button").addClass("people-edit").append($editIcon).appendTo($td);
                });

                // Display the data table
                $(".class-people-table").attr("style", "display: block; opacity: 1")
                // People info management
                peopleManagement();
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求班级人员名单失败，请稍后重试");
            }
        });
    }

    // School query
    $(".school-query").click(function () {
        displaySchools();
    });

    /*
    * =================================================================================================================
    * People information management
    * =================================================================================================================
    */

    // Edit role of people or
    function peopleManagement() {
        // Edit people role
        $(".people-edit").click(function () {
            var $tr = $(this).parent().parent().children();
            var name = $tr.eq(1).text();
            var userId = $tr.eq(0).text();
            // Set user id for radio button
            $("input[type=radio][name=role-manage]").attr("userId", userId);

            // Get user information
            $.ajax({
                url: contextPath + "user/" + userId + ".do",
                type: "get",
                dataType: "json",
                success: function (response) {
                    if (CODE_SUCCESS == response.code) {
                        // Set people name for modal title
                        $(".people-name").text(name);
                        // Check different role according to the user type
                        var userType = response.resultMap.item.userType;
                        switch (userType) {
                            case USER_TYPE_STUDENT:
                                $("#people-student").attr("checked", true);
                                break;
                            case USER_TYPE_MONITOR:
                                $("#people-monitor").attr("checked", true);
                                break;
                            case USER_TYPE_HEAD_TEACHER:
                                $("#people-head-teacher").attr("checked", true);
                                break;
                        }
                        // Open the modal
                        $("#people-info-modal").modal({
                            backdrop: "static"
                        });
                    } else {
                        showNoticeModal(response.code, response.msg);
                    }
                },
                error: function () {
                    showNoticeModal(CODE_WARN, "请求查询用户信息失败，请稍后重试");
                }
            });
        });

        // Save people role
        $(".save-people-role").click(function () {
            var $checked = $("input[type=radio][name=role-manage]:checked");
            var userType = $checked.val();
            var userId = $checked.attr("userId");

            //  Ask server to save the user type
            $.ajax({
                url: contextPath + "user/" + userId + ".do",
                type: "post",
                data: "_method=put&newUserType=" + userType,
                dataType: "json",
                success: function (response) {
                    if (CODE_SUCCESS == response.code) {
                        // Hide the modal
                        $("#people-info-modal").modal('hide');
                    }
                    showNoticeModal(response.code, response.msg);
                },
                error: function () {
                    showNoticeModal(CODE_WARN, "请求修改用户类型失败，请稍后重试");
                }
            });
        });
    }

    /*
    * =================================================================================================================
    * Add grade teacher
    * =================================================================================================================
    */
    // Prevent the default submit behavior of the form
    $("form").on("submit", function () {
        return false;
    });

    // Display all school list for choose
    function allSchoolList() {
        // All school list
        $.ajax({
            url: contextPath + "school.do",
            type: "get",
            async: false,
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS == response.code) {
                    var schoolList = response.resultMap.list;
                    var $ancestor = $("#school-select");
                    $ancestor.empty();
                    // Traverse all school list and create element to display it
                    $.each(schoolList, function (index, school) {
                        $("<option></option>").append(school.school).attr("schoolId", school.id).appendTo($ancestor);
                    });
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求学校信息失败，请稍后重试");
            }
        });
    }

    // Display all grade list of the school for choose
    function gradeListOfSchool(schoolId) {
        // All grade list of school
        $.ajax({
            url: contextPath + "grade.do",
            data: "schoolId=" + schoolId,
            type: "get",
            async: false,
            dataType: "json",
            success: function (response) {
                var gradeList = response.resultMap.list;
                var $ancestor = $("#grade-select");
                $ancestor.empty();
                // Traverse all grade list and create element to display it
                $.each(gradeList, function (index, grade) {
                    $("<option></option>").append(grade.grade).attr("gradeId", grade.id).appendTo($ancestor);
                });
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求年级信息失败，请稍后重试");
            }
        });
    }

    // Get school and grade info then open the modal
    $(".grade-teacher-manage").click(function () {
        allSchoolList();
        var schoolId = $("#school-select option:selected").attr("schoolId");
        gradeListOfSchool(schoolId);
        $("#grade-teacher-add").modal({
            backdrop: "static"
        });
    });

    // Choose different school event
    $("#school-select").on('change', function () {
        var schoolId = $("#school-select option:selected").attr("schoolId");
        gradeListOfSchool(schoolId);
    });

    // Grade teacher add form submit
    $("#form-add-grade-teacher").submit(function () {
        var params = $("#form-add-grade-teacher").serialize();
        var schoolId = $("#school-select option:selected").attr("schoolId");
        var gradeId = $("#grade-select option:selected").attr("gradeId");
        params = params + "&schoolId=" + schoolId + "&gradeId=" + gradeId;

        // Save grade teacher and register user
        $.ajax({
            url: contextPath + "user.do",
            type: "post",
            data: params,
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS == response.code) {
                    $("#grade-teacher-add").modal('hide');
                }
                showNoticeModal(response.code, response.msg);
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求新增年级主任失败，请稍后重试");
            }
        });
    });
});