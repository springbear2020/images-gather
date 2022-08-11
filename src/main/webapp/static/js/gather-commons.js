/*
 * =====================================================================================================================
 * Constants
 * =====================================================================================================================
 */
// TODO Replace the context path when environment changed
var contextPath = "http://localhost:8686/";
// Images' url
var notChooseImageUrl = "https://s1.ax1x.com/2022/07/01/jMb7E4.png";
var invalidImageUrl = "https://s1.ax1x.com/2022/07/06/jU0fW6.png";
var notLoginImageUrl = "https://s1.ax1x.com/2022/07/06/jUBDht.png";
var notUploadImageUrl = "https://s1.ax1x.com/2022/07/06/jUBy1f.png";
// Response code
var CODE_SUCCESS = 0;
var CODE_INFO = 1;
var CODE_ERROR = 2;
var CODE_WARN = 3;
// User type
var USER_TYPE_STUDENT = 0;
var USER_TYPE_MONITOR = 1;
var USER_TYPE_HEAD_TEACHER = 2;
var USER_TYPE_GRADE_TEACHER = 3;
// User status
var USER_STATUS_NORMAL = 0;
// Verify code length
var VERIFY_CODE_LEN = 6;

/*
 * =====================================================================================================================
 * Methods
 * =====================================================================================================================
 */
// System notice for user
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

// Dispatch to the different home page according to the user type
function homePageDispatch(type) {
    var userType = parseInt(type);
    if (USER_TYPE_STUDENT === userType || USER_TYPE_MONITOR === userType) {
        // Student home page
        $(".navbar-brand").attr("href", contextPath + "static/html/student.html");
    } else if (USER_TYPE_HEAD_TEACHER === userType) {
        // Teacher home page
        $(".navbar-brand").attr("href", contextPath + "static/html/teacher.html");
    }
}