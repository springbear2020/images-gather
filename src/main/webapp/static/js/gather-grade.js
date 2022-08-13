$(function () {
    /*
     * =================================================================================================================
     * Show history date before today
     * =================================================================================================================
     */

    // Build the date of the top navigation bar
    function buildTopHistoryDate(currentDate, days) {
        var $ancestor = $(".nav-history-view");
        $ancestor.empty();

        // Generate a time string in days ahead of the current date, traverse, build elements then display
        var dateArray = goBackDays(new Date(), days);
        for (var i = 0; i < dateArray.length; i++) {
            var $span = $("<span></span>").addClass("glyphicon glyphicon-time").attr("aria-hidden", "true");
            var $a = $("<a></a>").addClass("nav-record-history").attr("role", "button").append($span).append(" " + dateArray[i]);
            $("<li></li>").addClass("text-center").append($a).appendTo($ancestor);
        }

        // Check the specified day history click event
        $(".nav-record-history").click(function () {
            $(".left-date-li").removeClass("active");
            var selectedDate = $(this).text();
            $(".page-header").text(selectedDate);
            getGradeUploadRecord(selectedDate);
        });
    }

    // Build the date of the left navigation bar
    function buildLeftHistoryDate(currentDate, days) {
        var $ancestor = $(".left-history-view");
        $ancestor.empty();

        // Generate a time string in days ahead of the current date, traverse, build elements then display
        var dateArray = goBackDays(new Date(), days);
        for (var i = 0; i <= dateArray.length; i++) {
            var $li = $("<li></li>").addClass("left-date-li");
            if (i === 0) {
                $li.addClass("active");
            }
            $("<a></a>").addClass("left-record-history").append(dateArray[i]).attr("role", "button").appendTo($li);
            $li.appendTo($ancestor);
        }

        // Check the specified day history click event
        $(".left-record-history").click(function () {
            $(".left-date-li").removeClass("active");
            $(this).parent().addClass("active");
            var selectedDate = $(this).text();
            $(".page-header").text(selectedDate);
            getGradeUploadRecord(selectedDate);
        });
    }

    // After page loaded successfully, display the date ahead of today
    $(".page-header").text(parseDate(new Date()));
    buildTopHistoryDate(new Date(), 7);
    buildLeftHistoryDate(new Date(), 14);

    /*
     * =================================================================================================================
     * Class list of the grade
     * =================================================================================================================
     */

    // Display the classes list of the grade, including class name and not completed people total numbers
    function displayClassList(classList) {
        // Container element
        var $container = $(".div-container");
        $container.empty();

        // Not completed total list
        var $total = $("<a></a>").attr("role", "button").addClass("list-group-item disabled").append("未完成人数").appendTo($container);
        $("<span></span>").addClass("badge total-numbers").appendTo($total);

        // Traverse the all class
        var totalNumbers = 0;
        $.each(classList, function (index, classInfo) {
            totalNumbers += classInfo.notCompletedNums;
            var $span = $("<span></span>").addClass("badge").append(classInfo.notCompletedNums);
            $("<a></a>").attr("role", "button").addClass("list-group-item list-class").attr("classId", classInfo.id).append(classInfo.className).append($span).appendTo($container);
        });

        // Total numbers
        $(".total-numbers").text(totalNumbers);
    }

    // Get the not upload record of the grade including classes
    function getGradeUploadRecord(selectedDate) {
        // Remove the class style selected
        $(".list-class").removeClass("active");
        // Hide the class upload details
        $(".class-not-completed").attr("style", "display: none;")

        // Get the grade not completed people numbers and each class not completed numbers
        $.ajax({
            url: contextPath + "record/grade.do",
            dataType: "json",
            data: "date=" + selectedDate,
            async: false,
            type: "get",
            success: function (response) {
                if (CODE_SUCCESS === response.code) {
                    displayClassList(response.resultMap.list);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求年级上传记录失败，请稍后重试");
            }
        });
    }

    getGradeUploadRecord($(".page-header").text());

    /*
     * =================================================================================================================
     * Class details
     * =================================================================================================================
     */

    // Build the students list who not signed in the system
    function buildNotLoginStudentList(notLoginNames) {
        var nums = notLoginNames.length;
        var $divContainer = $(".not-login-list");
        $divContainer.empty();
        $divContainer.append("未登录人员名单【" + nums + "】");

        $.each(notLoginNames, function (index, name) {
            $divContainer.append(name + " ")
        });
    }

    // Build the students list who signed in but not upload the images
    function buildNotUploadStudentList(notUploadNames) {
        var nums = notUploadNames.length;
        var $divContainer = $(".not-upload-list");
        $divContainer.empty();
        $divContainer.append("未上传人员名单【" + nums + "】");

        // Traverse all students and show it in the div container
        $.each(notUploadNames, function (index, name) {
            $divContainer.append(name + " ")
        });
    }

    // Build the students list who upload the images successfully
    function buildUploadedStudentsList(completedNames) {
        var nums = completedNames.length;

        var $divContainer = $(".uploaded-list");
        $divContainer.empty();
        $divContainer.append("已完成人员名单【" + nums + "】");

        // Traverse all students and show it in the div container
        $.each(completedNames, function (index, name) {
            $divContainer.append(name + " ")
        });
    }

    // Build all students images preview
    function buildImagesPreview(response) {
        var $ancestor = $(".container-image-preview");
        $ancestor.empty();

        // Students list who have uploaded the images successfully
        var uploadedList = response.resultMap.uploadedList;
        // Traverser the images have uploaded list
        $.each(uploadedList, function (index, upload) {
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

            // Student name
            $("<h2></h2>").addClass("title-real-name").append(upload.name).appendTo($ancestor);
            var $imgParent = $("<div></div>").addClass("row center-block placeholders");
            $("<img/>").addClass("img-thumbnail first-image").attr("width", "100").attr("height", "150").attr("src", healthAccessUrl).attr("alt", "健康码图片不存在").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail second-image").attr("width", "100").attr("height", "150").attr("src", scheduleAccessUrl).attr("alt", "行程码图片不存在").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail third-image").attr("width", "100").attr("height", "150").attr("src", closedAccessUrl).attr("alt", "密接查图片不存在").appendTo($imgParent);
            // Split line between students
            $("<hr/>").appendTo($imgParent);
            $imgParent.appendTo($ancestor);
        });

        // Students list who signed in the system but not upload the images
        var notUploadNames = response.resultMap.notUploadNames;
        // var notUploadImageUrl = response.resultMap.notUploadUrl + contextPath;
        $.each(notUploadNames, function (index, name) {
            // Student name
            $("<h2></h2>").addClass("title-real-name").append(name).appendTo($ancestor);
            var $imgParent = $("<div></div>").addClass("row center-block placeholders");
            $("<img/>").addClass("img-thumbnail first-image").attr("width", "100").attr("height", "150").attr("src", notUploadImageUrl).attr("alt", "健康码图片不存在").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail second-image").attr("width", "100").attr("height", "150").attr("src", notUploadImageUrl).attr("alt", "行程码图片不存在").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail third-image").attr("width", "100").attr("height", "150").attr("src", notUploadImageUrl).attr("alt", "密接查图片不存在").appendTo($imgParent);
            // Split line between students
            $("<hr/>").appendTo($imgParent);
            $imgParent.appendTo($ancestor);
        });

        // Students list who not sign in the system
        var notLoginNames = response.resultMap.notLoginNames;
        // var notLoginImageUrl = response.resultMap.notLoginUrl + contextPath;
        $.each(notLoginNames, function (index, name) {
            // Student name
            $("<h2></h2>").addClass("title-real-name").append(name).appendTo($ancestor);
            var $imgParent = $("<div></div>").addClass("row center-block placeholders");
            $("<img/>").addClass("img-thumbnail first-image").attr("width", "100").attr("height", "150").attr("src", notLoginImageUrl).attr("alt", "健康码图片不存在").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail second-image").attr("width", "100").attr("height", "150").attr("src", notLoginImageUrl).attr("alt", "行程码图片不存在").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail third-image").attr("width", "100").attr("height", "150").attr("src", notLoginImageUrl).attr("alt", "密接查图片不存在").appendTo($imgParent);
            // Split line between students
            $("<hr/>").appendTo($imgParent);
            $imgParent.appendTo($ancestor);
        });
    }

    // Get the class upload details, including not login, not upload and complete name list
    function getClassUploadRecord(selectedDate, classId) {
        $.ajax({
            url: contextPath + "record/class.do",
            dataType: "json",
            data: "date=" + selectedDate + "&classId=" + classId,
            type: "get",
            success: function (response) {
                if (CODE_SUCCESS === response.code) {
                    // Build the students list who not signed in the system
                    buildNotLoginStudentList(response.resultMap.notLoginNames);
                    // Build the students list who signed in but not upload the images
                    buildNotUploadStudentList(response.resultMap.notUploadNames);
                    // Build the students list who upload the images successfully
                    buildUploadedStudentsList(response.resultMap.completedNames);
                    // Build all students images preview
                    buildImagesPreview(response);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求班级上传记录失败，请稍后重试");
            }
        });
    }

    // Class change click event
    $(".list-class").click(function () {
        // Remove the class style selected
        $(".list-class").removeClass("active");
        // Display the class upload details
        $(".class-not-completed").attr("style", "display: block;")
        // Highlight current element
        $(this).addClass("active");
        var selectedDate = $(".page-header").text();
        var classId = $(this).attr("classId");
        getClassUploadRecord(selectedDate, classId);
    });
});