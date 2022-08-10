/* =================================================== Commons ================================================== */
// TODO replace the context path
var contextPath = "http://localhost:8686/";
// Images access url
var notChooseImageUrl = "https://s1.ax1x.com/2022/07/01/jMb7E4.png";
var invalidImageUrl = "https://s1.ax1x.com/2022/07/06/jU0fW6.png";
var notLoginImageUrl = "https://s1.ax1x.com/2022/07/06/jUBDht.png";
var notUploadImageUrl = "https://s1.ax1x.com/2022/07/06/jUBy1f.png";
// Response code agreed with the server
var CODE_SUCCESS = 0;
var CODE_INFO = 1;
var CODE_WARN = 2;
var CODE_ERROR = 3;
// User type
var USER_TYPE_STUDENT = 0;
var USER_TYPE_MONITOR = 1;
var USER_TYPE_TEACHER = 2;

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


// Get params' value at the address
function getUrlParam(paraName) {
    var url = document.location.toString();
    var arrObj = url.split("?");
    if (arrObj.length > 1) {
        var arrPara = arrObj[1].split("&");
        var arr;
        for (var i = 0; i < arrPara.length; i++) {
            arr = arrPara[i].split("=");
            if (arr != null && arr[0] == paraName) {
                return arr[1];
            }
        }
        return "";
    }
    return "";
}