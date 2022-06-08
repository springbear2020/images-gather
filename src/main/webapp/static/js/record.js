$(function () {
    /* ============================================================================================================== */
    // TODO 更新前台工程上下文路径
    // var contextPath = "http://localhost:8080/gather/";
    var contextPath = "http://whut.springbear2020.cn/";
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
    var projectBirth = "2022-06-02";

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
            $("<div></div>").addClass("alert alert-success").append("【两码一查】所有人已完成").attr("role", "alert").appendTo($ancestor);
        } else {
            // 排行：未完成人员名单
            var $unUploaded = $("<div></div>").addClass("alert alert-danger").append("未完成人员名单【" + totalNotCompleteNumbers + "】：").attr("role", "alert").appendTo($ancestor);

            // 首先展示未登录人员
            $.each(notLoginUserList, function (index, item) {
                $unUploaded.append(item.realName + " ");
            });
            // 接着显示登录未上传人员
            $.each(notUploadedUserList, function (index, item) {
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
        $("<img/>").addClass("img-thumbnail first-image").attr("width", "100").attr("height", "150").attr("src", firstImage).attr("alt", "健康码图片不存在，请管理员联系该同学重新上传").appendTo($parent);
        $("<img/>").addClass("img-thumbnail second-image").attr("width", "100").attr("height", "150").attr("src", secondImage).attr("alt", "行程码图片不存在，请管理员联系该同学重新上传").appendTo($parent);
        $("<img/>").addClass("img-thumbnail third-image").attr("width", "100").attr("height", "150").attr("src", thirdImage).attr("alt", "密接查图片不存在，请管理员联系该同学重新上传").appendTo($parent);
        // 分割线
        $("<hr/>").appendTo($parent);
        $parent.appendTo($ancestor);
    }

    // 解析记录内容并显示
    function buildRecordContent(response, date) {
        // 清除原有内容
        $(".container-record-list").empty();
        $(".container-image-preview").empty();

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
        $("<h2></h2>").addClass("page-header").append(date).appendTo($ancestor);

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

        // 一行一行依次显示所有人信息表格（正序），依次为未登录、未上传、已上传人员
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

        // 构建所有人员图片预览（逆序），依次为已上传、未上传、未登录人员名单
        var lastIndex = classRecordList.length - 1;
        for (var i = lastIndex; i >= 0; i--) {
            var item = classRecordList[i];
            buildImagePreview(item.realName, item.healthImageUrl, item.scheduleImageUrl, item.closedImageUrl);
        }
    }

    // 查询指定日期的记录
    function getRecordByDate(date) {
        $.ajax({
            url: contextPath + "admin/record/class/" + date,
            type: "get",
            dataType: "json",
            success: function (response) {
                buildRecordContent(response, date);
            },
            error: function () {
                showNoticeModal(WARNING_CODE, "请求用户上传记录数据失败");
            }
        })
    }

    // 页面加载完成之后，从服务器获取本班学生今日上传数据
    getRecordByDate(DATE);

    /* ============================================= 历史记录 ======================================================== */

    // 从当前日期到指定天数前的日期，返回数组类型
    function nowDateReduce(date, dayNums) {
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
                        // 判断是否为闰年
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
            // result[i] = year + '-' + month + '-' + day
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

    // 从当前日期到指定日期（递减）
    function showAllDate(startDate, endDate) {
        // 将字符串转化为日期对象
        var startD = new Date(startDate)
        var endD = new Date(endDate)

        if (startD < endD) {
            return false
        }

        // 接收起始日期的年月日
        var year = startD.getFullYear()
        var month = startD.getMonth() + 1
        var day = startD.getDate()

        // 接收结束日期的年月日
        var eyear = endD.getFullYear()
        var emonth = endD.getMonth() + 1
        var eday = endD.getDate()

        // 接收得到的字符串
        var result = []

        // 循环判断
        var check = true

        var i = 0

        while (check) {
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
                        // 判断是否为闰年
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
            // result[i] = year + '-' + month + '-' + day;
            result[i] = year + '-'
            if (month < 10) {
                result[i] += '0'
            }
            result[i] += month + '-'
            if (day < 10) {
                result[i] += '0'
            }
            result[i] += day
            i++

            // 循环结束判断
            if (year == eyear && month == emonth && day == eday) {
                check = false
            }
        }

        return result
    }

    // 标题栏默认显示近 5 天的日期
    function buildNavHistoryDate(currentDate) {
        var $ancestor = $(".nav-history-view");
        $ancestor.empty();

        // 生成当前日期往前 5 天的时间字符串，遍历、构建元素并显示
        var dateArray = nowDateReduce(new Date(), 5);
        for (var i = 0; i < dateArray.length; i++) {
            var $span = $("<span></span>").addClass("glyphicon glyphicon-time").attr("aria-hidden", "true");
            var $a = $("<a></a>").addClass("nav-record-history").attr("role", "button").append($span).append(" " + dateArray[i]);
            $("<li></li>").addClass("text-center").append($a).appendTo($ancestor);
        }

        // 跳转到指定日期
        $(".nav-record-history").click(function () {
            var $selectedDate = $(this).text();
            getRecordByDate($selectedDate);
        });
    }

    // 左侧导航栏显示 10 天记录
    function buildLeftHistoryDate(currentDate) {
        var $ancestor = $(".left-history-view");
        $ancestor.empty();

        // 创建历史记录并显示
        var $liHistory = $("<li></li>").addClass("active");
        $("<a></a>").append("历史记录 ").append($("<span></span>").addClass("sr-only")).appendTo($liHistory);
        $liHistory.appendTo($ancestor);

        // 左侧导航栏显示 10 天记录
        var dateArray = nowDateReduce(new Date(), 10);

        for (var i = 0; i <= dateArray.length; i++) {
            var $li = $("<li></li>").addClass("left-date-li");
            $("<a></a>").addClass("left-record-history").append(dateArray[i]).attr("role", "button").appendTo($li);
            $li.appendTo($ancestor);
        }

        // 查看指定日期的记录
        $(".left-record-history").click(function () {
            $(".left-date-li").removeClass("active");
            $(this).parent().addClass("active");
            var $selectedDate = $(this).text();
            getRecordByDate($selectedDate);
        });
    }

    // 页面加载完成之后，即刻创建日期并显示
    buildNavHistoryDate(DATE);
    buildLeftHistoryDate(DATE);
});