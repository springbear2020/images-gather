$(function () {
    /* =================================================== Commons ================================================== */
    // Response code agreed with the server
    var CODE_SUCCESS = 0;
    var CODE_INFO = 1;
    var CODE_WARN = 2;
    var CODE_ERROR = 3;
    // Open the notice modal and show message
    var showNoticeModal = function (code, msg) {
        var $noticeContent = $("#h-notice-content");
        // Clear the style existed
        $noticeContent.parent().removeClass("alert-info alert-success alert-warning alert-danger");
        // Add different style of the modal
        if (CODE_SUCCESS === code) {
            $noticeContent.parent().addClass("alert-success");
        } else if (CODE_INFO === code) {
            $noticeContent.parent().addClass("alert-info");
        } else if (CODE_WARN === code) {
            $noticeContent.parent().addClass("alert-warning");
        } else if (CODE_ERROR === code) {
            $noticeContent.parent().addClass("alert-danger");
        }
        // Set the content of notice then show it
        $noticeContent.text(msg);
        $("#div-notice-modal").modal('show');

        // Counting down to close the notice modal
        var countingTime = 3;
        var timer = setInterval(function () {
            countingTime--;
            if (countingTime <= 0) {
                $("#div-notice-modal").modal('hide');
                clearInterval(timer);
            }
        }, 1000)
    };

    // Context path
    var contextPath = $("#context-path").val();
    var invalidImageUrl = "https://s1.ax1x.com/2022/07/06/jU0fW6.png";
    var notLoginImageUrl = "https://s1.ax1x.com/2022/07/06/jUBDht.png";
    var notUploadImageUrl = "https://s1.ax1x.com/2022/07/06/jUBy1f.png";

    /* ====================================== Display the class upload record ======================================= */

    // Parse the JavaScript date like this '2022-07-06' format
    function parseDate(date) {
        var nowMonth = date.getMonth() + 1;
        var strDate = date.getDate();
        var separator = "-";
        if (nowMonth >= 1 && nowMonth <= 9) {
            nowMonth = "0" + nowMonth;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        return date.getFullYear() + separator + nowMonth + separator + strDate;
    }

    // Build the students list who not signed in the system
    function buildNotLoginStudentList(response) {
        var notLoginStudentList = response.resultMap.notLoginList;
        var notLoginStudentsSize = notLoginStudentList.length;

        var $divContainer = $("#not-login-list");
        $divContainer.empty();
        $divContainer.append("未登录人员名单【" + notLoginStudentsSize + "】");

        // Traverse all students and show it in the div container
        $.each(notLoginStudentList, function (index, student) {
            $divContainer.append(student.name + " ")
        });
    }

    // Build the students list who signed in but not upload the images
    function buildNotUploadStudentList(response) {
        var notUploadStudentList = response.resultMap.notUploadList;
        var notUploadStudentsSize = notUploadStudentList.length;

        var $divContainer = $("#not-upload-list");
        $divContainer.empty();
        $divContainer.append("未上传人员名单【" + notUploadStudentsSize + "】");

        // Traverse all students and show it in the div container
        $.each(notUploadStudentList, function (index, student) {
            $divContainer.append(student.name + " ")
        });
    }

    // Build the students list who upload the images successfully
    function buildUploadedStudentsList(response) {
        var uploadedList = response.resultMap.uploadedList;
        var uploadedSize = uploadedList.length;

        var $divContainer = $("#uploaded-list");
        $divContainer.empty();
        $divContainer.append("已完成人员名单【" + uploadedSize + "】");

        // Traverse all students and show it in the div container
        $.each(uploadedList, function (index, upload) {
            $divContainer.append(upload.student.name + " ")
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

            // Student name
            $("<h2></h2>").addClass("title-real-name").append(upload.student.name).appendTo($ancestor);
            var $imgParent = $("<div></div>").addClass("row center-block placeholders");
            $("<img/>").addClass("img-thumbnail first-image").attr("width", "100").attr("height", "150").attr("src", healthAccessUrl).attr("alt", "健康码图片链接已失效").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail second-image").attr("width", "100").attr("height", "150").attr("src", scheduleAccessUrl).attr("alt", "行程码图片链接已失效").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail third-image").attr("width", "100").attr("height", "150").attr("src", closedAccessUrl).attr("alt", "密接查图片链接已失效").appendTo($imgParent);
            // Split line between students
            $("<hr/>").appendTo($imgParent);
            $imgParent.appendTo($ancestor);
        });

        // Students list who signed in the system but not upload the images
        var notUploadStudentList = response.resultMap.notUploadList;
        // var notUploadImageUrl = response.resultMap.notUploadUrl + contextPath;
        $.each(notUploadStudentList, function (index, student) {
            // Student name
            $("<h2></h2>").addClass("title-real-name").append(student.name).appendTo($ancestor);
            var $imgParent = $("<div></div>").addClass("row center-block placeholders");
            $("<img/>").addClass("img-thumbnail first-image").attr("width", "100").attr("height", "150").attr("src", notUploadImageUrl).attr("alt", "健康码图片链接已失效").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail second-image").attr("width", "100").attr("height", "150").attr("src", notUploadImageUrl).attr("alt", "行程码图片链接已失效").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail third-image").attr("width", "100").attr("height", "150").attr("src", notUploadImageUrl).attr("alt", "密接查图片链接已失效").appendTo($imgParent);
            // Split line between students
            $("<hr/>").appendTo($imgParent);
            $imgParent.appendTo($ancestor);
        });

        // Students list who not sign in the system
        var notLoginStudentList = response.resultMap.notLoginList;
        // var notLoginImageUrl = response.resultMap.notLoginUrl + contextPath;
        $.each(notLoginStudentList, function (index, student) {
            // Student name
            $("<h2></h2>").addClass("title-real-name").append(student.name).appendTo($ancestor);
            var $imgParent = $("<div></div>").addClass("row center-block placeholders");
            $("<img/>").addClass("img-thumbnail first-image").attr("width", "100").attr("height", "150").attr("src", notLoginImageUrl).attr("alt", "健康码图片链接已失效").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail second-image").attr("width", "100").attr("height", "150").attr("src", notLoginImageUrl).attr("alt", "行程码图片链接已失效").appendTo($imgParent);
            $("<img/>").addClass("img-thumbnail third-image").attr("width", "100").attr("height", "150").attr("src", notLoginImageUrl).attr("alt", "密接查图片链接已失效").appendTo($imgParent);
            // Split line between students
            $("<hr/>").appendTo($imgParent);
            $imgParent.appendTo($ancestor);
        });
    }

    // Get the upload record history of the class by specified date
    function getClassUploadRecord(date) {
        $.ajax({
            url: contextPath + "admin/record/class/" + date,
            dataType: "json",
            type: "get",
            success: function (response) {
                if (CODE_SUCCESS === response.code) {
                    // Build the students list who not signed in the system
                    buildNotLoginStudentList(response);
                    // Build the students list who signed in but not upload the images
                    buildNotUploadStudentList(response);
                    // Build the students list who upload the images successfully
                    buildUploadedStudentsList(response);
                    // Build all students images preview
                    buildImagesPreview(response);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_ERROR, "请求获取班级上传记录失败，请稍后重试");
            }
        })
    }

    // After page loaded successfully, get today class upload record as default behavior
    getClassUploadRecord(parseDate(new Date()));

    /* ====================================== Display the date ahead of today ======================================= */

    // Generate now date go back to the specified days before, return the date array descending
    function goBackDaysBefore(date, dayNums) {
        var year = date.getFullYear()
        var month = date.getMonth() + 1
        var day = date.getDate()
        var result = []
        for (var i = 0; i < dayNums; i++) {
            if (i > 0) {
                day--
                if (day <= 0) {
                    month--
                    if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                        day = 31
                    }
                    if (month == 4 || month == 6 || month == 9 || month == 11) {
                        day = 30
                    }
                    if (month == 2) {
                        // Determining whether it is a leap year
                        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                            day = 29
                        } else {
                            day = 28
                        }
                    }
                    if (month <= 0) {
                        year--
                        month = 12
                    }
                }
            }
            result[i] = year + '-'
            if (month < 10) {
                result[i] += '0'
            }
            result[i] += month + '-'
            if (day < 10) {
                result[i] += '0'
            }
            result[i] += day
        }
        return result
    }

    // Build the date of the top navigation bar
    function buildTopHistoryDate(currentDate, days) {
        var $ancestor = $(".nav-history-view");
        $ancestor.empty();

        // Generate a time string in days ahead of the current date, traverse, build elements then display
        var dateArray = goBackDaysBefore(new Date(), days);
        for (var i = 0; i < dateArray.length; i++) {
            var $span = $("<span></span>").addClass("glyphicon glyphicon-time").attr("aria-hidden", "true");
            var $a = $("<a></a>").addClass("nav-record-history").attr("role", "button").append($span).append(" " + dateArray[i]);
            $("<li></li>").addClass("text-center").append($a).appendTo($ancestor);
        }

        // Check the specified day history click event
        $(".nav-record-history").click(function () {
            var $selectedDate = $(this).text();
            $(".page-header").text($selectedDate);
            getClassUploadRecord($selectedDate);
        });
    }

    // Build the date of the left navigation bar
    function buildLeftHistoryDate(currentDate, days) {
        var $ancestor = $(".left-history-view");
        $ancestor.empty();

        // Generate a time string in days ahead of the current date, traverse, build elements then display
        var dateArray = goBackDaysBefore(new Date(), days);
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
            var $selectedDate = $(this).text();
            $(".page-header").text($selectedDate);
            getClassUploadRecord($selectedDate);
        });
    }

    // After page loaded successfully, display the date ahead of today
    $(".page-header").text(parseDate(new Date()));
    buildTopHistoryDate(new Date(), 5);
    buildLeftHistoryDate(new Date(), 15);
});