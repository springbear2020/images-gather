$(function () {
    /* ============================================================================================================== */
    // TODO 更新前台工程上下文路径
    var contextPath = "http://localhost:8080/gather/";
    // 与服务器约定的响应码
    var SUCCESS_CODE = 0;
    var ERROR_CODE = 1;
    var INFO_CODE = 2;
    var WARNING_CODE = 3;

    // 阻止表单的提交跳转页面问题
    $("form").on("submit", function () {
        return false;
    });

    // 提示信息
    var showNoticeModal = function (code, msg) {
        var $noticeContent = $("#h-notice-content");
        // 清除原有样式
        $noticeContent.parent().removeClass("alert-info alert-success alert-warning alert-danger");
        // 根据不同的响应码添加不同的样式
        if (INFO_CODE === code) {
            $noticeContent.parent().addClass("alert-info");
        } else if (SUCCESS_CODE === code) {
            $noticeContent.parent().addClass("alert-success");
        } else if (WARNING_CODE === code) {
            $noticeContent.parent().addClass("alert-warning");
        } else if (ERROR_CODE === code) {
            $noticeContent.parent().addClass("alert-danger");
        }
        // 设置通知的内容
        $noticeContent.text(msg);
        // 显示
        $("#div-notice-modal").modal('show');
    };

    /* ============================================= 记录查看 ======================================================== */

    // 解析时间为 2022-06-07 格式
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

    // 获得当前日期
    var DATE = parseDate();

    // 构建未上传人员名单
    function buildNotUploadedUserList(notLoginUserList, notUploadedUserList) {
        var $ancestor = $(".container-record-list");

        // 未登录人数
        var notLoginNumbers = notLoginUserList.length;
        // 登录未上传人数
        var notUploadedNumbers = notUploadedUserList.length;
        // 未上传总人数 = 未登录人数 + 登录未上传人数
        var totalNotCompleteNumbers = notLoginNumbers + notUploadedNumbers;

        // 所有人已上传
        if (notLoginNumbers <= 0 && notUploadedNumbers <= 0) {
            $("<div></div>").addClass("alert alert-success").append("今日【两码一查】所有人已完成").attr("role", "alert").appendTo($ancestor);
        } else {
            // 排行：未完成人员名单
            var $unUploaded = $("<div></div>").addClass("alert alert-danger").append("未完成人员名单【" + totalNotCompleteNumbers + "】：").attr("role", "alert").appendTo($ancestor);

            // 首先展示未登录人员
            $.each(notLoginUserList, function (index, item) {
                $unUploaded.append(item.realName + " ");
            });
            // 接着显示登录未上传人员
            $.each(notUploadedUserList, function (index, item) {
                console.log(index);
                $unUploaded.append(item.realName + " ");
            });
        }
    }

    // 构建所有人员图片预览
    function buildImagePreview(realName, firstImage, secondImage, thirdImage) {
        var $ancestor = $(".container-image-preview");

        // 姓名 page-header
        $("<h2></h2>").addClass("title-real-name").append(realName).appendTo($ancestor);

        // 三张图片的父容器
        var $parent = $("<div></div>").addClass("row center-block placeholders");

        // 三张图片
        $("<img/>").addClass("img-thumbnail first-image").attr("width", "100").attr("height", "150").attr("src", firstImage).attr("alt", "健康码").appendTo($parent);
        $("<img/>").addClass("img-thumbnail second-image").attr("width", "100").attr("height", "150").attr("src", secondImage).attr("alt", "行程码").appendTo($parent);
        $("<img/>").addClass("img-thumbnail third-image").attr("width", "100").attr("height", "150").attr("src", thirdImage).attr("alt", "密接查").appendTo($parent);
        // 分割线
        $("<hr/>").appendTo($parent);
        $parent.appendTo($ancestor);
    }

    // 解析记录内容并显示
    function buildRecordContent(response) {
        // 本班所有人员记录
        var classRecordList = response.resultMap.classRecordList;
        // 未登录人员记录
        var unLoginUserList = response.resultMap.unLoginUserList;
        // 登录未上传人员记录
        var loginNotUploadList = response.resultMap.loginNotUploadList;

        // 未登录人数
        var unLoginNumbers = unLoginUserList.length;
        // 登录未上传人数
        var loginNotUploadNumbers = loginNotUploadList.length;
        // 已上传人数
        var uploadedNumbers = response.resultMap.uploadedNumbers;

        var $ancestor = $(".container-record-list");

        // 标题：显示今日日期
        $("<h2></h2>").addClass("page-header").append(DATE).appendTo($ancestor);

        // 首先显示未上传人员名单
        buildNotUploadedUserList(unLoginUserList, loginNotUploadList);

        // 所有人员信息表格
        var $parent = $("<div></div>").addClass("table-responsive center-block").appendTo($ancestor);
        var $table = $("<table></table>").addClass("table table-striped text-center table-hover").appendTo($parent);

        // 表头
        var $thead = $("<thead></thead>").appendTo($table);
        var $theadTr = $("<tr></tr>").appendTo($thead);
        $("<th></th>").append("序号").appendTo($theadTr);
        $("<th></th>").append("学号").appendTo($theadTr);
        $("<th></th>").append("姓名").appendTo($theadTr);
        $("<th></th>").append("完成").appendTo($theadTr);

        // 表体
        var $tbody = $("<tbody></tbody>").appendTo($table);

        // 一行一行依次显示所有人信息表格
        $.each(classRecordList, function (index, item) {
            var $tbodyTr = $("<tr></tr>").appendTo($tbody);
            // 序号
            $("<td></td>").append(index + 1).appendTo($tbodyTr);
            // 学号
            $("<td></td>").append(item.studentNumber).appendTo($tbodyTr);
            // 姓名
            $("<td></td>").append(item.realName).appendTo($tbodyTr);
            // 是否完成
            var isComplete = item.status == 0 ? "是" : "否";
            $("<td></td>").append(isComplete).appendTo($tbodyTr);
        });

        // 构建所有人员图片预览
        $.each(classRecordList, function (index, item) {
            buildImagePreview(item.realName, item.healthImageUrl, item.scheduleImageUrl, item.closedImageUrl);
        });
    }

    // 页面加载完成之后，从服务器获取本班学生今日上传数据
    $.ajax({
        url: contextPath + "admin/record/class/" + DATE,
        type: "get",
        dataType: "json",
        success: function (response) {
            buildRecordContent(response);
        },
        error: function () {
            showNoticeModal(WARNING_CODE, "请求用户上传记录数据失败");
        }
    })
});