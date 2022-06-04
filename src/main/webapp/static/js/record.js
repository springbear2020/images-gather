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

    // Response record list data from server
    // var RECORD_LIST;
    // var LIST_LENGTH;
    // var curListIndex = 0;

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
        // The next user button
        // $("<button></button>").addClass("btn btn-primary btn-lg btn-next-student").append("上一位").appendTo($parent);
        // $("<button></button>").addClass("btn btn-primary btn-lg btn-next-student").append("下一位").appendTo($parent);
        $parent.appendTo($ancestor);

        // // Next student button click event
        // $(".btn-next-student").click(function () {
        //     curListIndex++;
        //     $(".title-real-name").text(RECORD_LIST[curListIndex].realName);
        //     $(".first-image").attr("src", RECORD_LIST[curListIndex].healthImageUrl);
        //     $(".second-image").attr("src", RECORD_LIST[curListIndex].scheduleImageUrl);
        //     $(".third-image").attr("src", RECORD_LIST[curListIndex].closedImageUrl);
        //     if (curListIndex >= LIST_LENGTH) {
        //         curListIndex = 0;
        //         showNoticeModal(INFO_CODE, "所有记录已预览完毕");
        //     }
        // });
    }

    var IS_ALL_UPLOADED = true;

    function buildRecordContentTable(response) {
        var recordList = response.resultMap.recordList;
        var size = response.resultMap.size;
        // RECORD_LIST = recordList;
        // LIST_LENGTH = RECORD_LIST.length;

        var $ancestor = $(".container-record-list");

        // Title
        $("<h2></h2>").addClass("page-header").append(DATE).appendTo($ancestor);

        // Build the images preview and judge whether all users upload the three images
        $.each(recordList, function (index, item) {
            // Build the images preview
            buildImagePreview(item.realName, item.healthImageUrl, item.scheduleImageUrl, item.closedImageUrl);
            var isUploaded = item.uploaded;
            if (isUploaded == 1) {
                IS_ALL_UPLOADED = false;
            }
        });

        // Build the not uploaded user list
        if (IS_ALL_UPLOADED) {
            $("<div></div>").addClass("alert alert-success").append("今日【两码一查】所有人已完成").attr("role", "alert").appendTo($ancestor);
        } else {
            // Not uploaded user list
            var $unUploaded = $("<div></div>").addClass("alert alert-danger").append("未上传人员名单：").attr("role", "alert").appendTo($ancestor);
            $.each(recordList, function (index, item) {
                var isUploaded = item.uploaded;
                if (isUploaded == 1) {
                    IS_ALL_UPLOADED = false;
                    $unUploaded.append(item.realName + " ");
                }
            });
        }

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
        // $("<th></th>").append("日期").appendTo($theadTr);
        // $("<th></th>").append("图片查看").attr("colspan", 3).appendTo($theadTr);

        // Table body
        var $tbody = $("<tbody></tbody>").appendTo($table);

        // Build the table line
        $.each(recordList, function (index, item) {
            var $tbodyTr = $("<tr></tr>").appendTo($tbody);
            // Number
            $("<td></td>").append(index + 1).appendTo($tbodyTr);
            // Student number
            $("<td></td>").append(item.username).appendTo($tbodyTr);
            // Real name
            $("<td></td>").append(item.realName).appendTo($tbodyTr);
            // Is uploaded
            var isUploaded = item.uploaded == 0 ? "是" : "否";
            $("<td></td>").append(isUploaded).appendTo($tbodyTr);
            // Date
            // $("<td></td>").append(item.uploadDate).appendTo($tbodyTr);
            // // Health image
            // var healthUploadId = item.healthUploadId;
            // if (healthUploadId == -1) {
            //     $("<td></td>").append("未上传").appendTo($tbodyTr);
            // } else {
            //     var $td = $("<td></td>").appendTo($tbodyTr);
            //     var $link = $("<a></a>").attr("href", item.healthImageUrl).attr("target", "_blank");
            //     var $span = $("<span></span>").addClass("glyphicon glyphicon-eye-open").attr("aria-hidden", "true").appendTo($link);
            //     $link.append(" 健康码").appendTo($td);
            // }
            // // Schedule image
            // var scheduleUploadId = item.scheduleUploadId;
            // if (scheduleUploadId == -1) {
            //     $("<td></td>").append("未上传").appendTo($tbodyTr);
            // } else {
            //     var $td = $("<td></td>").appendTo($tbodyTr);
            //     var $link = $("<a></a>").attr("href", item.scheduleImageUrl).attr("target", "_blank");
            //     var $span = $("<span></span>").addClass("glyphicon glyphicon-eye-open").attr("aria-hidden", "true").appendTo($link);
            //     $link.append(" 行程码").appendTo($td);
            // }
            // // Closed image
            // var closedUploadId = item.closedUploadId;
            // if (closedUploadId == -1) {
            //     $("<td></td>").append("未上传").appendTo($tbodyTr);
            // } else {
            //     var $td = $("<td></td>").appendTo($tbodyTr);
            //     var $link = $("<a></a>").attr("href", item.closedImageUrl).attr("target", "_blank");
            //     var $span = $("<span></span>").addClass("glyphicon glyphicon-eye-open").attr("aria-hidden", "true").appendTo($link);
            //     $link.append(" 密接查").appendTo($td);
            // }
        });
    }

    // After page loaded, get data from server then build element to display it
    $.ajax({
        url: contextPath + "record/" + DATE,
        type: "get",
        dataType: "json",
        success: function (response) {
            if (SUCCESS_CODE === response.code) {
                buildRecordContentTable(response);
            } else {
                showNoticeModal(response.code, response.msg);
            }
        },
        error: function () {
            showNoticeModal(WARNING_CODE, "请求用户上传记录数据失败");
        }
    })
});