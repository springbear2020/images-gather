$(function () {
    /* ============================================================================================================== */
    // TODO 更新前台工程上下文路径
    var contextPath = "http://localhost:8080/gather/";
    // 与服务器约定的响应码
    var SUCCESS_CODE = 0;
    var ERROR_CODE = 1;
    var INFO_CODE = 2;
    var WARNING_CODE = 3;

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

    /* ============================================== 年级记录 ======================================================= */

    // 构建年级记录并显示
    function buildRecordModalContent(response) {
        var userList = response.resultMap.userList;
        var userSize = userList.length;
        var recordList = response.resultMap.recordList;
        var recordSize = recordList.length;
        var unCompletedNumbers = userSize + recordSize;

        var $ancestor = $(".record-content")
        var $contentParent = $("<div></div>").addClass("alert alert-danger").attr("role", "alert");

        // 展示首行：今日年级未上传人数
        $contentParent.append("【" + DATE + "】两码一查共有 " + unCompletedNumbers + " 人未完成");
        $contentParent.append($("<hr/>"));

        /*
            TODO 分班级显示未上传人员信息
            1	软件1901
            2	软件1902
            3	软件1903
            4	软件zy1901
            5	软件zy1902
            6	软件sy1901
            7	大数据1901
            8	大数据1902
            9	人工智能1901
            10	人工智能1902
         */
        var classOne = [];
        var classOneSize = 0;
        var classTwo = [];
        var classTwoSize = 0;
        var classThree = [];
        var classThreeSize = 0;
        var classFour = [];
        var classFourSize = 0;
        var classFive = [];
        var classFiveSize = 0;
        var classSix = [];
        var classSixSize = 0;
        var classSeven = [];
        var classSevenSize = 0;
        var classEight = [];
        var classEightSize = 0;
        var classNine = [];
        var classNineSize = 0;
        var classTen = [];
        var classTenSize = 0;

        // 分班分类未登录用户
        $.each(userList, function (index, item) {
            var classNumber = item.classNumber;
            var studentName = item.realName;
            switch (classNumber) {
                case 1:
                    classOne[classOneSize++] = studentName;
                    break;
                case 2:
                    classTwo[classTwoSize++] = studentName;
                    break;
                case 3:
                    classThree[classThreeSize++] = studentName;
                    break;
                case 4:
                    classFour[classFourSize++] = studentName;
                    break;
                case 5:
                    classFive[classFiveSize++] = studentName;
                    break;
                case 6:
                    classSix[classSixSize++] = studentName;
                    break;
                case 7:
                    classSeven[classSevenSize++] = studentName;
                    break;
                case 8:
                    classEight[classEightSize++] = studentName;
                    break;
                case 9:
                    classNine[classNineSize++] = studentName;
                    break;
                case 10:
                    classTen[classTenSize++] = studentName;
                    break;
            }
        });
        // 分别分类登录未上传用户
        $.each(recordList, function (index, item) {
            var classNumber = item.classNumber;
            var studentName = item.realName;
            switch (classNumber) {
                case 1:
                    classOne[classOneSize++] = studentName;
                    break;
                case 2:
                    classTwo[classTwoSize++] = studentName;
                    break;
                case 3:
                    classThree[classThreeSize++] = studentName;
                    break;
                case 4:
                    classFour[classFourSize++] = studentName;
                    break;
                case 5:
                    classFive[classFiveSize++] = studentName;
                    break;
                case 6:
                    classSix[classSixSize++] = studentName;
                    break;
                case 7:
                    classSeven[classSevenSize++] = studentName;
                    break;
                case 8:
                    classEight[classEightSize++] = studentName;
                    break;
                case 9:
                    classNine[classNineSize++] = studentName;
                    break;
                case 10:
                    classTen[classTenSize++] = studentName;
                    break;
            }
        });

        // 1【软件1901 31】
        $contentParent.append("【软件1901 " + classOneSize + "/31】");
        if (classOneSize <= 0) {
            $contentParent.append("今日两码一查所有同学已完成");
        }
        for (var i = 0; i < classOneSize; i++) {
            $contentParent.append(classOne[i] + " ");
        }
        $contentParent.append($("<hr/>"));
        // 2【软件1902 32】
        $contentParent.append("【软件1902 " + classTwoSize + "/32】");
        if (classTwoSize <= 0) {
            $contentParent.append("今日两码一查所有同学已完成");
        }
        for (var i = 0; i < classTwoSize; i++) {
            $contentParent.append(classTwo[i] + " ");
        }
        $contentParent.append($("<hr/>"));
        // 3【软件1903 30】
        $contentParent.append("【软件1903 " + classThreeSize + "/30】");
        if (classThreeSize <= 0) {
            $contentParent.append("今日两码一查所有同学已完成");
        }
        for (var i = 0; i < classThreeSize; i++) {
            $contentParent.append(classThree[i] + " ");
        }
        $contentParent.append($("<hr/>"));
        // 4【软件zy1901 26】
        $contentParent.append("【软件zy1901 " + classFourSize + "/26】");
        if (classFourSize <= 0) {
            $contentParent.append("今日两码一查所有同学已完成");
        }
        for (var i = 0; i < classFourSize; i++) {
            $contentParent.append(classFour[i] + " ");
        }
        $contentParent.append($("<hr/>"));
        // 5【软件zy1902 26】
        $contentParent.append("【软件zy1902 " + classFiveSize + "/26】");
        if (classFiveSize <= 0) {
            $contentParent.append("今日两码一查所有同学已完成");
        }
        for (var i = 0; i < classFiveSize; i++) {
            $contentParent.append(classFive[i] + " ");
        }
        $contentParent.append($("<hr/>"));
        // 6【软件sy1901 24】
        $contentParent.append("【软件sy1901 " + classSixSize + "/24】");
        if (classSixSize <= 0) {
            $contentParent.append("今日两码一查所有同学已完成");
        }
        for (var i = 0; i < classSixSize; i++) {
            $contentParent.append(classSix[i] + " ");
        }
        $contentParent.append($("<hr/>"));
        // 7【大数据1901 35】
        $contentParent.append("【大数据1901 " + classSevenSize + "/35】");
        if (classSevenSize <= 0) {
            $contentParent.append("今日两码一查所有同学已完成");
        }
        for (var i = 0; i < classSevenSize; i++) {
            $contentParent.append(classSeven[i] + " ");
        }
        $contentParent.append($("<hr/>"));
        // 8【大数据1902 36】
        $contentParent.append("【大数据1902 " + classEightSize + "/36】");
        if (classEightSize <= 0) {
            $contentParent.append("今日两码一查所有同学已完成");
        }
        for (var i = 0; i < classEightSize; i++) {
            $contentParent.append(classEight[i] + " ");
        }
        $contentParent.append($("<hr/>"));
        // 9【人工智能1901 35】
        $contentParent.append("【人工智能1901 " + classNineSize + "/35】");
        if (classNineSize <= 0) {
            $contentParent.append("今日两码一查所有同学已完成");
        }
        for (var i = 0; i < classNineSize; i++) {
            $contentParent.append(classNine[i] + " ");
        }
        $contentParent.append($("<hr/>"));
        // 10【人工智能1902 34】
        $contentParent.append("【人工智能1902 " + classTenSize + "/34】");
        if (classTenSize <= 0) {
            $contentParent.append("今日两码一查所有同学已完成");
        }
        for (var i = 0; i < classTenSize; i++) {
            $contentParent.append(classTen[i] + " ");
        }
        // $contentParent.append($("<hr/>"));


        $contentParent.appendTo($ancestor);
        // 打开模态框
        $("#modal-all-class-record").modal({
            backdrop: "static"
        })
    }

    // 打开上传记录模态框
    $(".record-grade").click(function () {
        // 清除原有信息
        $(".record-content").empty();

        // 从服务器获取年级上传记录
        $.ajax({
            url: contextPath + "admin/record/grade/" + DATE,
            method: "get",
            dataType: "json",
            async: "false",
            success: function (response) {
                if (SUCCESS_CODE == response.code) {
                    // 根据响应构建内容并显示
                    buildRecordModalContent(response);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(WARNING_CODE, "请求年级上传记录失败");
            }
        });
    });
});