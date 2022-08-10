$(function () {

    /* =========================================== Images upload to qiniu =========================================== */
    // Prevent the default behavior of the password update form
    $(".images-form").on("submit", function () {
        return false;
    });

    // Three image files object
    var healthImageFileObj;
    var scheduleImageFileObj;
    var closedImageFileObj;
    // Suffix of the three image files
    var healthFileNameSuffix = "";
    var scheduleFileNameSuffix = "";
    var closedFileNameSuffix = "";

    // Health image choose event
    $('.health-image').on('change', function (e) {
        // Get the file source stream
        healthImageFileObj = e.target.files[0];

        // Get the file suffix
        var fileName = $('.health-image').val();
        healthFileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        // If choose anything, clear the existed image preview
        if (healthFileNameSuffix.length <= 0) {
            $(this).parent().prev().attr('src', notChooseImageUrl).attr("未选择健康码图片");
            $("#btn-images-upload").attr("disabled", true);
            return false;
        }
        // Display the image user chosen by user
        $(this).parent().prev().attr('src', window.URL.createObjectURL(this.files[0]));

        // Unlock the upload button
        $("#btn-images-upload").attr("disabled", false);
    });
    // Schedule image choose event
    $('.schedule-image').on('change', function (e) {
        scheduleImageFileObj = e.target.files[0];
        var fileName = $('.schedule-image').val();
        scheduleFileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (scheduleFileNameSuffix.length <= 0) {
            $(this).parent().prev().attr('src', notChooseImageUrl).attr("未选择行程码图片");
            $("#btn-images-upload").attr("disabled", true);
            return false;
        }
        $(this).parent().prev().attr('src', window.URL.createObjectURL(this.files[0]));
        $("#btn-images-upload").attr("disabled", false);
    });
    // Closed image choose event
    $('.closed-image').on('change', function (e) {
        closedImageFileObj = e.target.files[0];
        var fileName = $('.closed-image').val();
        closedFileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (closedFileNameSuffix.length <= 0) {
            $(this).parent().prev().attr('src', notChooseImageUrl).attr("未选择密接查图片");
            $("#btn-images-upload").attr("disabled", true);
            return false;
        }
        $(this).parent().prev().attr('src', window.URL.createObjectURL(this.files[0]));
        $("#btn-images-upload").attr("disabled", false);
    });

    // Before submit the form, do some verify
    $("#btn-images-upload").click(function () {
        // Ensure the three images are chosen by user
        if (healthFileNameSuffix.length <= 0) {
            showNoticeModal(CODE_WARN, "请选择您的健康码");
            return false;
        }
        if (scheduleFileNameSuffix.length <= 0) {
            showNoticeModal(CODE_WARN, "请选择您的行程码");
            return false;
        }
        if (closedFileNameSuffix.length <= 0) {
            showNoticeModal(CODE_WARN, "请选择您的密接查");
            return false;
        }

        $(this).attr("disabled", "disabled");
        showNoticeModal(CODE_INFO, "图片文件上传中，请稍等···");

        // Get the image files data then combine to form data
        var formData = new FormData();
        formData.append("healthImage", healthImageFileObj);
        formData.append("scheduleImage", scheduleImageFileObj);
        formData.append("closedImage", closedImageFileObj);

        $.ajax({
            url: contextPath + "transfer.do",
            type: "post",
            dataType: "json",
            data: formData,
            cache: false,
            processData: false,
            contentType: false,
            success: function (response) {
                showNoticeModal(response.code, response.msg);
            },
            error: function () {
                showNoticeModal(CODE_WARN, "请求上传图片失败，请稍后重试");
            }
        })
    });
});