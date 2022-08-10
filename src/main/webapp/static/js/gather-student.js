$(function () {
    /* ================================================== Completed ================================================= */

    // Display three images
    function displayImages(response, studentName) {
        var upload = response.resultMap.item;

        $(".span-uploaded").text(studentName + "，今日【两码一查】已完成");
        $(".span-uploaded-datetime").text("上传时间：" + upload.uploadDatetime);

        // Health image access url
        var localHealthUrl = upload.localHealthUrl;
        var cloudHealthUrl = upload.cloudHealthUrl;
        var healthAccessUrl = cloudHealthUrl.length == 0 ? contextPath + localHealthUrl : cloudHealthUrl;
        healthAccessUrl = healthAccessUrl == contextPath ? invalidImageUrl : healthAccessUrl;
        // Schedule image access url
        var localScheduleUrl = upload.localScheduleUrl;
        var cloudScheduleUrl = upload.cloudScheduleUrl;
        var scheduleAccessUrl = cloudScheduleUrl.length == 0 ? contextPath + localScheduleUrl : cloudScheduleUrl;
        scheduleAccessUrl = scheduleAccessUrl == contextPath ? invalidImageUrl : scheduleAccessUrl;
        // Closed image access url
        var localClosedUrl = upload.localClosedUrl;
        var cloudClosedUrl = upload.cloudClosedUrl;
        var closedAccessUrl = cloudClosedUrl.length == 0 ? contextPath + localClosedUrl : cloudClosedUrl;
        closedAccessUrl = closedAccessUrl == contextPath ? invalidImageUrl : closedAccessUrl;

        $(".img-health").attr("src", healthAccessUrl);
        $(".img-schedule").attr("src", scheduleAccessUrl);
        $(".img-closed").attr("src", closedAccessUrl);

        // Display the completed box
        $(".div-completed").attr("style", "display: block");
    }

    // Get the student's upload of today
    $.ajax({
        url: contextPath + "record/upload/today.do",
        type: "get",
        dataType: "json",
        success: function (response) {
            // Welcome
            var studentName = response.resultMap.user.student.name;
            $(".welcome-user").text(studentName + "，欢迎登录！");

            var userType = response.resultMap.user.userType;
            if (0 !== userType) {
                $(".li-class").attr("style", "display: block");
            }
            // Uploaded
            if (CODE_SUCCESS == response.code) {
                displayImages(response, studentName);
            }
        },
        error: function () {
            showNoticeModal(CODE_WARN, "请求今日上传记录失败，请稍后重试");
        }
    });
});