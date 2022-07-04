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
    };

    // Context path
    var contextPath = $("#context-path").val();

    /* ================================================= Pagination ================================================= */
    var loginLogDataType = 1;
    var uploadDataType = 2;

    // Build the pagination
    function buildPagination(pageData, whoClickedMe) {
        // Parent: login data display container
        var $parentElement = $(".data-table-container");

        // Navigation
        var $nav = $("<nav></nav>").attr("aria-label", "Page navigation").addClass("text-center").appendTo($parentElement);
        var $ul = $("<ul></ul>").addClass("pagination").appendTo($nav);
        var curPageNum = pageData.pageNum;

        // Not the first page, build the previous page element
        if (!pageData.isFirstPage) {
            var $previousPageLi = $("<li></li>").appendTo($ul);
            var $previousPageLink = $("<a></a>").addClass("previous-page").attr("role", "button").attr("aria-label", "Previous").appendTo($previousPageLi);
            $("<span></span>").attr("aria-hidden", "true").append("&laquo;").appendTo($previousPageLink);
        }

        // Build the pagination numbers
        var pageNumArray = pageData.navigatepageNums;
        $.each(pageNumArray, function (index, val) {
            var $li;
            // Current page num add the active style, prevent click it and give it a highlight style
            if (val == curPageNum) {
                $li = $("<li></li>").addClass("active").appendTo($ul);
            } else {
                $li = $("<li></li>").appendTo($ul);
            }
            // Give the page number a click event except the current page number
            var $pageTurnLink = $("<a></a>").attr("role", "button").append(val).appendTo($li);
            if (val != curPageNum) {
                $pageTurnLink.addClass("page-turn");
            }
        });

        // Not the last page, build the next page element
        if (!pageData.isLastPage) {
            var $nextPageLi = $("<li></li>").appendTo($ul);
            var $nextPageLink = $("<a></a>").attr("role", "button").addClass("next-page").attr("aria-label", "Next").appendTo($nextPageLi);
            $("<span></span>").attr("aria-hidden", "true").append("&raquo;").appendTo($nextPageLink);
        }

        // Previous page click event
        $(".previous-page").click(function () {
            var pageNum = curPageNum - 1;
            if (whoClickedMe == loginLogDataType) {
                getLoginLogPageData(pageNum);
            } else if (whoClickedMe == uploadDataType) {
                getUploadHistoryPageData(pageNum);
            }
        });

        // Next page click event
        $(".next-page").click(function () {
            var pageNum = curPageNum + 1;
            if (whoClickedMe == loginLogDataType) {
                getLoginLogPageData(pageNum);
            } else if (whoClickedMe == uploadDataType) {
                getUploadHistoryPageData(pageNum);
            }
        });

        // Go to specified page click event
        $(".page-turn").click(function () {
            var pageNum = $(this).text();
            if (whoClickedMe == loginLogDataType) {
                getLoginLogPageData(pageNum);
            } else if (whoClickedMe == uploadDataType) {
                getUploadHistoryPageData(pageNum);
            }
        });
    }

    /* ================================================== Login log ================================================= */

    // Get the user's login log page data from the server
    function getLoginLogPageData(pageNum) {
        $.ajax({
            url: contextPath + "record/login/" + pageNum,
            type: "get",
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS === response.code) {
                    buildLoginLogDateTable(response);
                    buildPagination(response.resultMap.loginPageData, loginLogDataType);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_ERROR, "请求个人登录记录失败");
            }
        });
    }

    // Build the user's login record page data display table
    var buildLoginLogDateTable = function (response) {
        // Parent: login data display container
        var $loginDataTable = $(".data-table-container");
        $loginDataTable.empty();

        var loginLogList = response.resultMap.loginPageData.list;
        // Head title of the page
        $("<h2></h2>").append("登录记录").addClass("sub-header").appendTo($loginDataTable);
        // Parent of table
        var $tableParent = $("<div></div>").addClass("table-responsive").appendTo($loginDataTable);
        var $table = $("<table></table>").addClass("table table-hover").appendTo($tableParent);
        // Table thead and tbody
        var $thead = $("<thead></thead>").appendTo($table);
        var $tbody = $("<tbody></tbody>").appendTo($table);
        var $trOfThead = $("<tr></tr>").appendTo($thead);
        $("<th></th>").append("序号").appendTo($trOfThead);
        $("<th></th>").append("时间").appendTo($trOfThead);
        $("<th></th>").append("IP").appendTo($trOfThead);
        $("<th></th>").append("地点").appendTo($trOfThead);
        // Go though the record list in loop to display the data
        $.each(loginLogList, function (index, data) {
            var $trOfBody = $("<tr></tr>").appendTo($tbody);
            var pageSize = response.resultMap.loginPageData.pageSize;
            var pageNum = response.resultMap.loginPageData.pageNum;
            var num = (pageNum - 1) * pageSize + index + 1;
            $("<td></td>").append(num).appendTo($trOfBody);
            $("<td></td>").append(data.loginDateTime).appendTo($trOfBody);
            $("<td></td>").append(data.ip).appendTo($trOfBody);
            $("<td></td>").append(data.location).appendTo($trOfBody);
        });
    };

    // After page loaded successfully, get the user's login log data default
    getLoginLogPageData(1);

    /* ================================================== Upload history ================================================= */

    // Get the user's upload record history page data from the server
    function getUploadHistoryPageData(pageNum) {
        $.ajax({
            url: contextPath + "record/upload/" + pageNum,
            type: "get",
            dataType: "json",
            success: function (response) {
                if (CODE_SUCCESS === response.code) {
                    buildUploadDateTable(response);
                    buildPagination(response.resultMap.uploadPageData, uploadDataType);
                } else {
                    showNoticeModal(response.code, response.msg);
                }
            },
            error: function () {
                showNoticeModal(CODE_ERROR, "请求个人上传记录失败");
            }
        });
    }

    // Build the user's upload record page data display table
    var buildUploadDateTable = function (response) {
        // Parent: data display container
        var $dataTable = $(".data-table-container");
        $dataTable.empty();

        var uploadList = response.resultMap.uploadPageData.list;
        // Head title of the page
        $("<h2></h2>").append("上传记录").addClass("sub-header").appendTo($dataTable);
        // Parent of table
        var $tableParent = $("<div></div>").addClass("table-responsive").appendTo($dataTable);
        var $table = $("<table></table>").addClass("table table-hover").appendTo($tableParent);
        // Table thead and tbody
        var $thead = $("<thead></thead>").appendTo($table);
        var $tbody = $("<tbody></tbody>").appendTo($table);
        var $trOfThead = $("<tr></tr>").appendTo($thead);
        $("<th></th>").append("序号").appendTo($trOfThead);
        $("<th></th>").append("时间").appendTo($trOfThead);
        $("<th></th>").append("健康码").appendTo($trOfThead);
        $("<th></th>").append("行程码").appendTo($trOfThead);
        $("<th></th>").append("密接查").appendTo($trOfThead);
        // Go though the record list in loop to display the data
        $.each(uploadList, function (index, data) {
            var $trOfBody = $("<tr></tr>").appendTo($tbody);
            var pageSize = response.resultMap.uploadPageData.pageSize;
            var pageNum = response.resultMap.uploadPageData.pageNum;
            var num = (pageNum - 1) * pageSize + index + 1;
            $("<td></td>").append(num).appendTo($trOfBody);
            $("<td></td>").append(data.uploadDateTime).appendTo($trOfBody);
            // Health image access url
            var localHealthUrl = data.localHealthUrl;
            var cloudHealthUrl = data.cloudHealthUrl;
            var healthAccessUrl = cloudHealthUrl.length == 0 ? contextPath + localHealthUrl : cloudHealthUrl;
            var healthContent = healthAccessUrl == contextPath ? "已失效" : $("<a></a>").append("查看").attr("href", healthAccessUrl).attr("target", "blank");
            $("<td></td>").append(healthContent).appendTo($trOfBody);
            // Schedule image access url
            var localScheduleUrl = data.localScheduleUrl;
            var cloudScheduleUrl = data.cloudScheduleUrl;
            var scheduleAccessUrl = cloudScheduleUrl.length == 0 ? contextPath + localScheduleUrl : cloudScheduleUrl;
            var scheduleContent = scheduleAccessUrl == contextPath ? "已失效" : $("<a></a>").append("查看").attr("href", scheduleAccessUrl).attr("target", "blank");
            $("<td></td>").append(scheduleContent).appendTo($trOfBody);
            // Closed image access url
            var localClosedUrl = data.localClosedUrl;
            var cloudClosedUrl = data.cloudClosedUrl;
            var closedAccessUrl = cloudClosedUrl.length == 0 ? contextPath + localClosedUrl : cloudClosedUrl;
            var closedContent = closedAccessUrl == contextPath ? "已失效" : $("<a></a>").append("查看").attr("href", closedAccessUrl).attr("target", "blank");
            $("<td></td>").append(closedContent).appendTo($trOfBody);
        });
    };

    /* ============================================= Record type change ============================================= */
    // Record change click event
    $(".personal-record").click(function () {
        $(".personal-record-li").removeClass("active");
        $(this).parent().addClass("active");
        var $recordType = $(this).text();
        if ("登录记录" === $recordType) {
            getLoginLogPageData(1);
        } else if ("上传记录" === $recordType) {
            getUploadHistoryPageData(1);
        }
    });
});