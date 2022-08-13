$(function () {
    /*
     * =================================================================================================================
     * Home page dispatch
     * =================================================================================================================
     */
    homePageDispatch(getUrlParam("type"));

    /*
     * =================================================================================================================
     * View personal information
     * =================================================================================================================
     */

    // Display the information of the user
    function displayPersonalProfile(user, people) {
        // Class info
        $(".li-school").text(people.school == null ? "" : people.school);
        $(".li-grade").text(people.grade == null ? "" : people.grade);
        $(".li-class").text(people.className == null ? "" : people.className);

        // Personal info
        $(".div-student-name").text(people.name)
        var sex = people.sex;
        if ("男" == sex) {
            $("#personal-sex-man").attr("checked", true);
        } else if ("女" == sex) {
            $("#personal-sex-woman").attr("checked", true);
        } else {
            $("#personal-sex-secret").attr("checked", true);
        }
        $("#personal-phone").val(people.phone == null ? "" : people.phone);
        $("#personal-email").val(people.email == null ? "" : people.email);

        // User info
        $(".div-username").text(user.username);
        $(".div-last-login-datetime").text(user.lastLoginDatetime);
        var userType = user.userType;
        if (USER_TYPE_STUDENT == userType) {
            $(".div-user-type").text("学生");
        } else if (USER_TYPE_MONITOR == userType) {
            $(".div-user-type").text("班长");
        } else if (USER_TYPE_HEAD_TEACHER == userType) {
            $(".div-user-type").text("班主任");
        } else if (USER_TYPE_GRADE_TEACHER == userType) {
            $(".div-user-type").text("年级主任");
        }
        var userStatus = user.userStatus;
        if (USER_STATUS_NORMAL == userStatus) {
            $(".div-user-status").text("正常");
        } else {
            $(".div-user-status").text("异常");
        }
    }

    $.ajax({
        url: contextPath + "people.do",
        type: "get",
        dataType: "json",
        success: function (response) {
            if (CODE_SUCCESS == response.code) {
                displayPersonalProfile(response.resultMap.item, response.resultMap.item.people);
            } else {
                showNoticeModal(response.code, response.msg);
            }
        },
        error: function () {
            showNoticeModal(CODE_WARN, "请求个人信息失败，请稍后重试");
        }
    });

    /*
     * =================================================================================================================
     * Update personal profile
     * =================================================================================================================
     */

    // Edit button click event
    $("#edit-personal-info").click(function () {
        // Original information before edit
        var originSex = $("input[name='sex']:checked").val();
        var originPhone = $("#personal-phone").val();
        var originEmail = $("#personal-email").val();

        // Open the edit element, including sex, phone and email
        $("#personal-sex-man").attr("disabled", false);
        $("#personal-sex-woman").attr("disabled", false);
        $("#personal-sex-secret").attr("disabled", false);
        $("#personal-phone").attr("disabled", false);
        $("#personal-email").attr("disabled", false);

        // Let the edit button disappear and add a new save button to save the changes caused by the user
        var $ancestor = $("#edit-save-info");
        $ancestor.empty();
        $("<a></a>").append("保存").attr("role", "button").addClass("btn btn-success").attr("id", "save-personal-info").appendTo($("<li></li>").appendTo($ancestor));

        saveProfileChanges(originSex, originPhone, originEmail);
    });

    // Save the info changes entered by user
    function saveProfileChanges(originSex, originPhone, originEmail) {
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

            // Ask the server to update the user's personal info
            $.ajax({
                url: contextPath + "people.do",
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
                                window.location.href = contextPath + "static/html/profile.html?type=" + response.resultMap.item;
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
        });
    }
});
