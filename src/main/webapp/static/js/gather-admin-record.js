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
    };

    // Context path
    var contextPath = $("#context-path").val();

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
            alert($selectedDate);
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
            alert($selectedDate);
        });
    }

    // Display the date ahead of today
    buildTopHistoryDate(new Date(), 5);
    buildLeftHistoryDate(new Date(), 15);
});