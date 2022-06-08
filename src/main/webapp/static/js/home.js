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

    /* =================================================== 修改密码 ================================================== */
    // 显示密码修改模态框
    $(".link-update-password").click(function () {
        // 除非点击 x 关闭按钮，否则一直
        $("#modal-update-password").modal({
            backdrop: "static"
        })
    });

    // 重置修改密码模态框中的内容
    function resetFormItem() {
        $("#input-old-password").val("");
        $("#input-new-password").val("");
        $("#input-new-password-again").val("");
    }

    // 密码修改模态框关闭按钮点击事件
    $(".modal-update-close").click(function () {
        resetFormItem();
    });

    // 确认按钮
    $("#btn-update-password").click(function () {
        var oldPassword = $("#input-old-password").val();
        var newPassword = $("#input-new-password").val();
        var newPasswordAgain = $("#input-new-password-again").val();

        // 原密码、新密码不允许为空
        if (oldPassword.length == 0) {
            showNoticeModal(WARNING_CODE, "请输入原密码");
            return false;
        }
        if (newPassword.length == 0) {
            showNoticeModal(WARNING_CODE, "请输入新密码");
            return false;
        }
        if (newPasswordAgain.length == 0) {
            showNoticeModal(WARNING_CODE, "请确认新密码");
            return false;
        }

        if (newPassword !== newPasswordAgain) {
            showNoticeModal(WARNING_CODE, "两次输入的新密码不一致");
            return false;
        }

        // 请求服务器修改用户密码
        $.ajax({
            url: contextPath + "update",
            method: "POST",
            data: "_method=PUT&" + $("#form-update-password").serialize(),
            dataType: "json",
            success: function (response) {
                if (SUCCESS_CODE == response.code) {
                    // 密码修改成功，关闭模态框，重置内容
                    $("#modal-update-password").modal('hide');
                    resetFormItem();
                    // 显示来自服务器的密码修改成功信息
                    showNoticeModal(response.code, response.msg);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(WARNING_CODE, "请求修改密码失败");
            }
        });
    });

    /* ===================================================== 图片上传 ================================================ */
    // 健康码文件对象
    var HEALTH_IMAGE_FILE;
    // 行程码文件对象
    var SCHEDULE_IMAGE_FILE;
    // 密接查文件对象
    var CLOSED_IMAGE_FILE;
    // 健康码文件后缀
    var healthFileNameSuffix = "";
    // 行程码文件后缀
    var scheduleFileNameSuffix = "";
    // 密接查文件后缀
    var closedFileNameSuffix = "";

    // 选择健康码事件
    $('.health-image').on('change', function (e) {
        // 文件对象
        HEALTH_IMAGE_FILE = e.target.files[0];
        // 获取图片显示容器 <img/>
        let $img = $(this).parent().prev();

        // 文件后缀
        var fileName = $('.health-image').val();
        healthFileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        // 未选择任何图片，清除原有展示的图片
        if (healthFileNameSuffix.length <= 0) {
            $img.attr('src', "");
            return false;
        }

        // 显示用户选择的图片
        let src = window.URL.createObjectURL(this.files[0]);
        $img.attr('src', src);

        // 解锁上传按钮
        $(".image-submit").attr("disabled", false);
    })
    // 选择行程码事件
    $('.schedule-image').on('change', function (e) {
        SCHEDULE_IMAGE_FILE = e.target.files[0];
        let $img = $(this).parent().prev();
        var fileName = $('.schedule-image').val();
        scheduleFileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (scheduleFileNameSuffix.length <= 0) {
            $img.attr('src', "");
            return false;
        }
        let src = window.URL.createObjectURL(this.files[0]);
        $img.attr('src', src);
        $(".image-submit").attr("disabled", false);
    })
    // 选择密接查事件
    $('.closed-image').on('change', function (e) {
        CLOSED_IMAGE_FILE = e.target.files[0];
        let $img = $(this).parent().prev();
        var fileName = $('.closed-image').val();
        closedFileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        if (closedFileNameSuffix.length <= 0) {
            $img.attr('src', "");
            return false;
        }
        let src = window.URL.createObjectURL(this.files[0]);
        $img.attr('src', src);
        $(".image-submit").attr("disabled", false);
    })

    // 上传文件到七牛云
    function uploadFileToQiniuServer(file, key, token, mimeType, successMsg, errorMsg) {
        var putExtra = {
            fname: {key},
            params: {},
            mimeType: [mimeType]
        }
        var config = {
            shouldUseQiniuFileName: false,
            region: qiniu.region.z2,
            forceDirect: true,
            useCdnDomain: true,
        };

        // 使用七牛云前台 API 上传文件到七牛云服务器
        var observable = qiniu.upload(file, key, token, putExtra, config);

        // 打印实时上传信息
        var observer = {
            next(msg) {
                // 上传进度，百分比
                var rate = msg.total.percent + "";
                // console.log(rate.substring(0, rate.indexOf(".") + 3));
            },
            error(msg) {
                console.log(msg);
                showNoticeModal(ERROR_CODE, errorMsg);
            },
            complete(msg) {
                // showNoticeModal(SUCCESS_CODE, successMsg);
            }
        }
        // 开始上传
        observable.subscribe(observer);
    };

    // 依次上传文件到七牛云
    function uploadFilesInOrder(keyList, tokenList) {
        // key 的个数与 token 个数不相等，不允许上传
        if (keyList.length !== tokenList.length) {
            showNoticeModal(ERROR_CODE, "请求获取文件上传验证信息失败");
            return false;
        }

        // 依次上传健康码、行程码、密接查图片文件
        uploadFileToQiniuServer(HEALTH_IMAGE_FILE, keyList[0], tokenList[0], "image/*", "健康码图片文件上传成功", "健康码上传失败，请稍后重试");
        uploadFileToQiniuServer(SCHEDULE_IMAGE_FILE, keyList[1], tokenList[1], "image/*", "行程码图片文件上传成功", "行程码上传失败，请稍后重试");
        uploadFileToQiniuServer(CLOSED_IMAGE_FILE, keyList[2], tokenList[2], "image/*", "密接查图片文件上传成功", "密接查上传失败，请稍后重试");

        //  请求服务器保存上传记录 Upload、更新今日记录 Record
        var status = 0;

        $.ajax({
            url: contextPath + "record/upload/" + status,
            dataType: "json",
            data: "healthKey=" + keyList[0] + "&scheduleKey=" + keyList[1] + "&closedKey=" + keyList[2],
            type: "POST",
            async: "false",
            success: function (response) {
                showNoticeModal(response.code, response.msg);
            },
            error: function () {
                showNoticeModal(ERROR_CODE, "请求更新记录失败");
            }
        })
    }

    // 文件上传点击按钮
    $(".image-submit").click(function () {
        // 验证保证三张图片均已选择
        if (healthFileNameSuffix.length <= 0) {
            showNoticeModal(WARNING_CODE, "请选择您的健康码");
            return false;
        }
        if (scheduleFileNameSuffix.length <= 0) {
            showNoticeModal(WARNING_CODE, "请选择您的行程码");
            return false;
        }
        if (closedFileNameSuffix.length <= 0) {
            showNoticeModal(WARNING_CODE, "请选择您的密接查");
            return false;
        }

        // 点击上传后禁用按钮
        $(this).attr("disabled", "disabled");

        // 从服务器获取 keyList 和 tokenList 完成七牛校验
        $.ajax({
            url: contextPath + "transfer/upload/images",
            dataType: "json",
            type: "get",
            success: function (response) {
                if (SUCCESS_CODE === response.code) {
                    uploadFilesInOrder(response.resultMap.keyList, response.resultMap.tokenList);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(ERROR_CODE, "请求获取文件上传验证信息失败");
            }
        })
    });

    /* ============================================== 已上传 ======================================================== */
    // 页面加载完成之后，从服务器查询用户今日记录状态信息，已上传则提示用户并回显图片信息
    $.ajax({
        url: contextPath + "record/user/today",
        dataType: "json",
        type: "get",
        success: function (response) {
            // 用户今日已经上传则显示提示信息
            if (INFO_CODE === response.code) {
                var userRecordToday = response.resultMap.userRecordToday;
                // 回显用户今日已上传图片
                $(".img-health").attr("src", userRecordToday.healthImageUrl);
                $(".img-schedule").attr("src", userRecordToday.scheduleImageUrl);
                $(".img-closed").attr("src", userRecordToday.closedImageUrl);
                showNoticeModal(response.code, response.msg);
            } else if (ERROR_CODE === response.code) {
                showNoticeModal(response.code, response.msg);
            }
        },
        error: function () {
            showNoticeModal(ERROR_CODE, "请求获取今日上传记录信息失败");
        }
    })
});