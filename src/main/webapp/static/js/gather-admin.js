$(function () {
    /*
    * =================================================================================================================
    * Management item change event
    * =================================================================================================================
    */
    $(".manage-item").click(function () {
        // Highlight the current item
        $(".li-management-item").removeClass("active");
        $(this).parent().addClass("active");
        // Set text for page header
        $(".page-header").text($(this).text());
    });

    /*
    * =================================================================================================================
    * Class data import
    * =================================================================================================================
    */
    // File content change event
    $(".excel-file").on('change', function (e) {
        // Check the format of the file user chosen
        var fileName = $(this).val();
        var fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (!(fileSuffix == ".xls" || fileSuffix == ".xlsx")) {
            showNoticeModal(CODE_WARN, "请选择 Excel 格式文件");
            return false;
        }

        var fileObj = e.target.files[0];
        var formData = new FormData();
        formData.append("file", fileObj);

        showNoticeModal(CODE_INFO, "文件上传处理中，请稍等···");

        // Ask server for save the excel file and import the data
        $.ajax({
            url: contextPath + "transfer/class.do",
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
                showNoticeModal(CODE_WARN, "请求上传文件失败，请稍后重试");
            }
        })
    });
});