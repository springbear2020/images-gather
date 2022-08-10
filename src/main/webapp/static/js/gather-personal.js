$(function () {
    /* ================================================ Personal info =============================================== */

    // Display the info of the user
    function displayPersonal(user, person) {
        // Class info
        $(".li-school").text(person.school);
        $(".li-college").text(person.college == null ? "" : person.college);
        $(".li-grade").text(person.grade);
        $(".li-major").text(person.major == null ? "" : person.major);
        $(".li-class").text(person.className);

        // Personal info
        $(".div-student-name").text(person.name)
        var sex = person.sex;
        if ("男" == sex) {
            $("#personal-sex-man").attr("checked", true);
        } else if ("女" == sex) {
            $("#personal-sex-woman").attr("checked", true);
        } else {
            $("#personal-sex-secret").attr("checked", true);
        }
        $("#personal-phone").val(person.phone);
        $("#personal-email").val(person.email);

        // User info
        $(".div-username").text(user.username);
        $(".div-last-login-datetime").text(user.lastLoginDatetime);
        var userType = user.userType;
        if (USER_TYPE_STUDENT == userType) {
            $(".div-user-type").text("学生");
        } else if (USER_TYPE_MONITOR == userType) {
            $(".div-user-type").text("班长");
        } else if (USER_TYPE_TEACHER == userType) {
            $(".div-user-type").text("班主任");
        }
        var userStatus = user.userStatus;
        if (0 == userStatus) {
            $(".div-user-status").text("正常");
        } else {
            $(".div-user-status").text("异常");
        }
    }

    // Student info
    function getStudentInfo() {
        $.ajax({
            url: contextPath + "student.do",
            type: "get",
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS == response.code) {
                    displayPersonal(response.resultMap.item, response.resultMap.item.student);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求学生信息失败，请稍后重试");
            }
        });
    }

    // Teacher info
    function getTeacherInfo() {
        $.ajax({
            url: contextPath + "teacher.do",
            type: "get",
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS == response.code) {
                    displayPersonal(response.resultMap.user, response.resultMap.user.teacher);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求教师信息失败，请稍后重试");
            }
        });
    }

    // Display different data according to the user type
    var userType = getUrlParam("type");
    if ("1" == userType || "0" == userType) {
        getStudentInfo();
    } else if ("2" == userType) {
        getTeacherInfo();
    }

    /* ============================================ Update personal info ============================================ */

    // Update person
    function updatePersonalInfo(newSex, newPhone, newEmail, request) {
        // Ask the server to update the user's info
        $.ajax({
            url: contextPath + request,
            type: "post",
            data: "_method=put&newSex=" + newSex + "&newPhone=" + newPhone + "&newEmail=" + newEmail,
            dataType: "json",
            success: function (response) {
                showNoticeModal(response.code, response.msg);
                if (CODE_SUCCESS == response.code) {
                    // Refresh current page
                    let countingTime = 3;
                    let timer = setInterval(function () {
                        if (countingTime <= 0) {
                            window.location.href = contextPath + "static/html/personal.html?type=" + userType;
                            clearInterval(timer);
                        }
                        countingTime--;
                    }, 1000)
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求更新个人信息失败，请稍后重试");
            }
        })
    }

    $("#edit-personal-info").click(function () {
        // Open the edit element, including sex, phone and email
        $("#personal-sex-man").attr("disabled", false);
        $("#personal-sex-woman").attr("disabled", false);
        $("#personal-sex-secret").attr("disabled", false);
        $("#personal-phone").attr("disabled", false);
        $("#personal-email").attr("disabled", false);

        var originSex = $("input[name='sex']:checked").val();
        var originPhone = $("#personal-phone").val();
        var originEmail = $("#personal-email").val();

        // Let the edit button disappear and add a new save button to save the changes by the user
        function disappearEditAddSaveButton() {
            var $ancestor = $("#edit-save-info");
            $ancestor.empty();
            $("<a></a>").append("保存").attr("role", "button").addClass("btn btn-success").attr("id", "save-personal-info").appendTo($("<li></li>").appendTo($ancestor));

            // Save the changes
            $("#save-personal-info").click(function () {
                var newSex = $("input[name='sex']:checked").val();
                var newPhone = $("#personal-phone").val();
                var newEmail = $("#personal-email").val();

                // Judge whether the information have changed
                if (originSex == newSex && originPhone == newPhone && originEmail == newEmail) {
                    showNoticeModal(CODE_WARN, "信息未发生变更，无需保存");
                    return;
                }

                // Verify the format of the new phone
                var phoneRegexp = /^1[3-9]\d{9}$/;
                if (!phoneRegexp.test(newPhone)) {
                    showNoticeModal(CODE_WARN, "手机号格式不正确，请重新输入");
                    return;
                }

                // Verify the format of the new email
                var emailRegexp = new RegExp("^([a-z0-9_-]+)@([\\da-z-]+)\\.([a-z]{2,6})$");
                if (!emailRegexp.test(newEmail)) {
                    showNoticeModal(CODE_WARN, "邮箱地址格式不正确，请重新输入");
                    return;
                }

                // Update different info according to the user type
                if ("0" == userType || "1" == userType) {
                    updatePersonalInfo(newSex, newPhone, newEmail, "student.do");
                } else if ("2" == userType) {
                    updatePersonalInfo(newSex, newPhone, newEmail, "teacher.do");
                }
            });
        }

        // Listening the content change of the sex ration button
        $("input:radio[name='sex']").change(function () {
            disappearEditAddSaveButton();
        });
        // Listening the content change of the phone input
        $("#personal-phone").change(function () {
            disappearEditAddSaveButton();
        });
        // Listening the content email of the phone input
        $("#personal-email").change(function () {
            disappearEditAddSaveButton();
        });
    });
});