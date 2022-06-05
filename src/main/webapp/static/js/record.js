$(function () {
    /* ====================================================== Commons =============================================== */
    // Context path and notice modal
    var contextPath = $("#span-context-path").text();
    // Response code from server
    var SUCCESS_CODE = 0;
    var ERROR_CODE = 1;
    var INFO_CODE = 2;
    var WARNING_CODE = 3;

    // Prevent the default submit action of form
    $("form").on("submit", function () {
        return false;
    });

    // Show the notice modal
    var showNoticeModal = function (code, msg) {
        var $noticeContent = $("#h-notice-content");
        // Clear the existed style of the notice object
        $noticeContent.parent().removeClass("alert-info alert-success alert-warning alert-danger");
        if (INFO_CODE === code) {
            $noticeContent.parent().addClass("alert-info");
        } else if (SUCCESS_CODE === code) {
            $noticeContent.parent().addClass("alert-success");
        } else if (WARNING_CODE === code) {
            $noticeContent.parent().addClass("alert-warning");
        } else if (ERROR_CODE === code) {
            $noticeContent.parent().addClass("alert-danger");
        }
        $noticeContent.text(msg);
        $("#div-notice-modal").modal('show');
    };

    function parseDate() {
        var date = new Date();
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

    var DATE = parseDate();

    function buildImagePreview(realName, firstImage, secondImage, thirdImage) {
        var $ancestor = $(".container-image-preview");
        // Title
        $("<h2></h2>").addClass("page-header title-real-name").append(realName).appendTo($ancestor);
        // Image parent
        var $parent = $("<div></div>").addClass("row center-block placeholders");
        // Three images
        $("<img/>").addClass("img-thumbnail first-image").attr("width", "100").attr("height", "150").attr("src", firstImage).attr("alt", "健康码").appendTo($parent);
        $("<img/>").addClass("img-thumbnail second-image").attr("width", "100").attr("height", "150").attr("src", secondImage).attr("alt", "行程码").appendTo($parent);
        $("<img/>").addClass("img-thumbnail third-image").attr("width", "100").attr("height", "150").attr("src", thirdImage).attr("alt", "密接查").appendTo($parent);
        // The <br/>
        $("<br/>").appendTo($parent);
        $parent.appendTo($ancestor);
    }

    function buildNotUploadedUserList(notLoginUserList, notUploadedUserList) {
        var $ancestor = $(".container-record-list");

        var notLoginNumbers = notLoginUserList.length;
        var notUploadedNumbers = notUploadedUserList.length;
        var totalNotCompleteNumbers = notLoginNumbers + notUploadedNumbers;
        // Build the not uploaded user list
        if (notLoginNumbers <= 0 && notUploadedNumbers <= 0) {
            $("<div></div>").addClass("alert alert-success").append("今日【两码一查】所有人已完成").attr("role", "alert").appendTo($ancestor);
        } else {
            var $unUploaded = $("<div></div>").addClass("alert alert-danger").append("未完成人员名单【" + totalNotCompleteNumbers + "】：").attr("role", "alert").appendTo($ancestor);

            // Not login user list
            $.each(notLoginUserList, function (index, item) {
                $unUploaded.append(item + " ");
            });
            // Not uploaded user list
            $.each(notUploadedUserList, function (index, item) {
                $unUploaded.append(item + " ");
            });
        }
    }

    function buildRecordContentTable(response) {
        var recordList = response.resultMap.recordList;
        var notLoginUserList = response.resultMap.notLoginUserList;
        var notUploadedUserList = response.resultMap.notUploadedUserList;

        var $ancestor = $(".container-record-list");

        // Title
        $("<h2></h2>").addClass("page-header").append(DATE).appendTo($ancestor);

        // Build the record not uploaded list at first
        buildNotUploadedUserList(notLoginUserList, notUploadedUserList);

        // Parent
        var $parent = $("<div></div>").addClass("table-responsive center-block").appendTo($ancestor);
        // Table
        var $table = $("<table></table>").addClass("table table-striped text-center table-hover").appendTo($parent);

        // Table head
        var $thead = $("<thead></thead>").appendTo($table);
        // Table head tr
        var $theadTr = $("<tr></tr>").appendTo($thead);
        $("<th></th>").append("序号").appendTo($theadTr);
        $("<th></th>").append("学号").appendTo($theadTr);
        $("<th></th>").append("姓名").appendTo($theadTr);
        $("<th></th>").append("完成").appendTo($theadTr);

        // Table body
        var $tbody = $("<tbody></tbody>").appendTo($table);

        // Build the table line
        $.each(recordList, function (index, item) {
            var $tbodyTr = $("<tr></tr>").appendTo($tbody);
            // Number
            $("<td></td>").append(index + 1).appendTo($tbodyTr);
            // Student number
            $("<td></td>").append(item.studentNumber).appendTo($tbodyTr);
            // Real name
            $("<td></td>").append(item.realName).appendTo($tbodyTr);
            // Is uploaded
            var isUploaded = item.uploaded == 0 ? "是" : "否";
            $("<td></td>").append(isUploaded).appendTo($tbodyTr);
        });

        // Build the images preview
        $.each(recordList, function (index, item) {
            buildImagePreview(item.realName, item.healthImageUrl, item.scheduleImageUrl, item.closedImageUrl);
        });
    }

    // After page loaded, get data from server then build element to display it
    $.ajax({
        url: contextPath + "record/" + DATE,
        type: "get",
        dataType: "json",
        success: function (response) {
            buildRecordContentTable(response);
        },
        error: function () {
            showNoticeModal(WARNING_CODE, "请求用户上传记录数据失败");
        }
    })
});