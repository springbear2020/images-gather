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
        // Set text for page header
        $(".page-header").text($(this).text());
    });

    /*
    * =================================================================================================================
    * Class data import
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
                        // Set school name
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
                    // Set grade name
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
                    // Set class name
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
            url: contextPath + "people/all.do",
            type: "get",
            data: "classId=" + classId,
            dataType: "json",
            success: function (response) {
                var peopleList = response.resultMap.list;
                var $ancestor = $(".table-body");
                $ancestor.empty();
                // Traverse the people list and create element to display it
                $.each(peopleList, function (index, people) {
                    var $tr = $("<tr></tr>").appendTo($ancestor);
                    $("<td></td>").append(people.id).appendTo($tr);
                    $("<td></td>").append(people.name).appendTo($tr);
                    $("<td></td>").append(people.sex).appendTo($tr);
                    $("<td></td>").append(people.number).appendTo($tr);
                    $("<td></td>").append(people.phone).appendTo($tr);
                    $("<td></td>").append(people.email).appendTo($tr);
                    var $editIcon = $("<span></span>").addClass("glyphicon glyphicon-edit").attr("aria-hidden", true);
                    var $deleteIcon = $("<span></span>").addClass("glyphicon glyphicon-trash").attr("aria-hidden", true);
                    $("<a></a>").attr("role", "button").addClass("people-edit").append($editIcon).append("&nbsp;").appendTo($tr);
                    $("<a></a>").attr("role", "button").addClass("people-delete").append($deleteIcon).appendTo($tr);
                });

                // Display the table
                $(".class-people-table").attr("style","display: block; opacity: 1")

                $(".people-delete").click(function () {
                    alert("people delete");
                });
                $(".people-edit").click(function () {
                    alert("people edit");
                });
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
});