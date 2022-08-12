$(function () {
    /*
     * =================================================================================================================
     * Welcome student info and whether task of today is completed
     * =================================================================================================================
     */
    var USER_TYPE = -1;

    // Display three images
    function displayImages(upload, name) {
        // Display the completed box
        $(".span-uploaded").text(name + "，今日【两码一查】已完成");
        $(".span-uploaded-datetime").text("上传时间：" + upload.uploadDatetime);
        $(".div-completed").attr("style", "display: block");

        // Health image access url
        var localHealthUrl = upload.localHealthUrl == null ? "" : upload.localHealthUrl;
        var cloudHealthUrl = upload.cloudHealthUrl == null ? "" : upload.cloudHealthUrl;
        var healthAccessUrl = cloudHealthUrl.length == 0 ? contextPath + localHealthUrl : cloudHealthUrl;
        healthAccessUrl = healthAccessUrl == contextPath ? invalidImageUrl : healthAccessUrl;
        // Schedule image access url
        var localScheduleUrl = upload.localScheduleUrl == null ? "" : upload.localScheduleUrl;
        var cloudScheduleUrl = upload.cloudScheduleUrl == null ? "" : upload.cloudScheduleUrl;
        var scheduleAccessUrl = cloudScheduleUrl.length == 0 ? contextPath + localScheduleUrl : cloudScheduleUrl;
        scheduleAccessUrl = scheduleAccessUrl == contextPath ? invalidImageUrl : scheduleAccessUrl;
        // Closed image access url
        var localClosedUrl = upload.localClosedUrl == null ? "" : upload.localClosedUrl;
        var cloudClosedUrl = upload.cloudClosedUrl == null ? "" : upload.cloudClosedUrl;
        var closedAccessUrl = cloudClosedUrl.length == 0 ? contextPath + localClosedUrl : cloudClosedUrl;
        closedAccessUrl = closedAccessUrl == contextPath ? invalidImageUrl : closedAccessUrl;

        $(".img-health").attr("src", healthAccessUrl);
        $(".img-schedule").attr("src", scheduleAccessUrl);
        $(".img-closed").attr("src", closedAccessUrl);
    }

    // Get the student's upload of today
    $.ajax({
        url: contextPath + "record/upload/today.do",
        type: "get",
        async: false,
        dataType: "json",
        success: function (response) {
            // Welcome
            var name = response.resultMap.name;
            $(".welcome").text(name + "，欢迎登录！");

            // Common student don't have the privilege to view the class upload record
            var userType = response.resultMap.userType;
            USER_TYPE = userType;
            if (USER_TYPE_STUDENT === userType) {
                $(".li-class").attr("style", "display: none");
            }
            // Task of today completed
            if (CODE_SUCCESS == response.code) {
                displayImages(response.resultMap.upload, name);
            }
        },
        error: function () {
            showNoticeModal(CODE_WARN, "请求今日上传记录失败，请稍后重试");
        }
    });

    /*
     * =================================================================================================================
     * Page dispatcher
     * =================================================================================================================
     */
    pageDispatcher(USER_TYPE);
});