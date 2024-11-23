/*
 * =====================================================================================================================
 * Constants
 * =====================================================================================================================
 */
var contextPath = "/images-gather/";
// Images' url
var notChooseImageUrl = contextPath + "static/img/notChoose.png";
var invalidImageUrl = contextPath + "static/img/invalid.png";
var notLoginImageUrl = contextPath + "static/img/notLogin.png";
var notUploadImageUrl = contextPath + "static/img/notUpload.png";
// Response code
var CODE_SUCCESS = 0;
var CODE_INFO = 1;
var CODE_ERROR = 2;
var CODE_WARN = 3;
// User type
var USER_TYPE_STUDENT = 1;
var USER_TYPE_MONITOR = 2;
var USER_TYPE_HEAD_TEACHER = 3;
var USER_TYPE_GRADE_TEACHER = 4;
var USER_TYPE_ADMIN = 5;
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
    } else if (USER_TYPE_HEAD_TEACHER === userType || USER_TYPE_GRADE_TEACHER === userType) {
        // Teacher home page
        $(".navbar-brand").attr("href", contextPath + "static/html/teacher.html");
    }
}

// Dispatch to different page according to user type when click the page link
function pageDispatcher(userType) {
    // Class upload record history
    $(".link-class").click(function () {
        if (userType != -1) {
            $(this).attr("href", contextPath + "static/html/class.html?type=" + userType);
        }
    });
    // Personal data including login and upload
    $(".link-record").click(function () {
        if (userType != -1) {
            $(this).attr("href", contextPath + "static/html/record.html?type=" + userType);
        }
    });
    // Personal profile
    $(".link-personal-profile").click(function () {
        if (userType != -1) {
            $(this).attr("href", contextPath + "static/html/profile.html?type=" + userType);
        }
    });
}