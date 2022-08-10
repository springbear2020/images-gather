$(function () {
    // Set the latest login data of the teacher
    function setLatestLoginData(response) {
        var teacherName = response.resultMap.user.teacher.name;
        var loginLog = response.resultMap.loginLog;
        $(".welcome-teacher").text(teacherName);
        $(".login-location").text(loginLog.location);
        $(".login-ip").text("IP：" + loginLog.ip);
        $(".login-datetime").text(loginLog.loginDatetime);
    }

    // Get the latest login data
    $.ajax({
        url: contextPath + "teacher.do",
        method: "get",
        dataType: "json",
        success: function (response) {
            if (CODE_SUCCESS == response.code) {
                setLatestLoginData(response);
            }
        },
        error: function () {
            showNoticeModal(CODE_WARN, "请求教师信息失败，请稍后重试");
        }
    });
});