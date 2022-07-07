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

        // $("#notice-progress").attr("style", "width: " + countingTime + "%");
        // <div class="progress" style="max-height: 3px">
        //      <div id="notice-progress" class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%"></div>
        // </div>
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

    /* =========================================== Get images of today ============================================== */
    $.ajax({
        url: contextPath + "admin/record/upload/today",
        dataType: "json",
        type: "get",
        success: function (response) {
            // Display the images that the admin upload before
            if (CODE_SUCCESS === response.code) {
                var imagesUrlArray = response.resultMap.imagesUrl;
                $(".img-health").attr("src", imagesUrlArray[0]);
                $(".img-schedule").attr("src", imagesUrlArray[1]);
                $(".img-closed").attr("src", imagesUrlArray[2]);
                showNoticeModal(response.code, response.msg);
            }
        },
        error: function () {
            showNoticeModal(CODE_ERROR, "请求获取已上传图片失败，请稍后重试");
        }
    })
});