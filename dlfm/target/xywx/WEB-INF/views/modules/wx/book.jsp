<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>董李凤美</title>
    <%@include file="/common/wxmeta.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/app/modules/dlfm/css/default_new.css?v=${sysInitTime}">
</head>
<body ontouchstart>
<div class="top-tabbar-view">
    <a class="book top-tabber-item ztn-active" data-index="newbook" href="javascript:void(0);">
        <span>新书发布</span>
    </a>
    <a class="book top-tabber-item" data-index="booktj" href="javascript:void(0);">
        <span>好书推荐</span>
    </a>
</div>
<div id="bookContent" class="book-content">

</div>
<div class="weui-loadmore" style="display: none">
    <i class="weui-loading"></i>
    <span class="weui-loadmore__tips">正在加载</span>
</div>
<script src="https://cdn.bootcss.com/jquery_lazyload/1.9.7/jquery.lazyload.min.js"></script>
<script>
    $(function () {
        var pageMnager = {
            $bookContent: $("#bookContent"),
            pageIndex: 0,
            hasMore: false,
            booktype: 0,
            init: function () {
                var self = this;
                $(".book.top-tabber-item").on("click", function () {
                    $(this).addClass('ztn-active').siblings('.ztn-active').removeClass('ztn-active');
                    var m = $(this).data("index");
                    $(window).scrollTop(0);
                    self.pageIndex = 0;
                    self.hasMore = false;
                    switch (m) {
                        case "booktj":
                            self.booktype = 1;
                            break;
                        case "newbook":
                            self.booktype = 0;
                            break;
                    }
                    setTimeout(function () {
                        self.getBookList();
                    }, 200);
                });
                var loading = false;
                $(document.body).infinite().on("infinite", function () {
                    if (loading || (!self.hasMore)) return;
                    $.ajax({
                        type: 'GET',
                        url: ctxFront + '/book',
                        dataType: 'json',
                        data: {page: self.pageIndex, booktype: self.booktype},
                        async: true,
                        success: function (data) {
                            // console.log(JSON.stringify(data));
                            if (data != null) {
                                self.$bookContent.append(self.joinBook(data));
                                $("img.lazy").lazyload();
                            }
                        },
                        error: function (xhr, type) {
                            loading = false;
                        },
                        beforeSend: function (xhr, settings) {
                            loading = true;
                        },
                        complete: function (xhr, status) {
                            loading = false;
                        }
                    });

                });

                setTimeout(function () {
                    self.getBookList();
                }, 200);
            },
            getBookList: function () {
                var self = this;
                $.ajax({
                    type: 'GET',
                    url: ctxFront + '/book',
                    dataType: 'json',
                    data: {page: self.pageIndex, booktype: self.booktype},
                    async: true,
                    success: function (data) {
                        // console.log(JSON.stringify(data));
                        if (data != null) {
                            self.$bookContent.html(self.joinBook(data));
                            $("img.lazy").lazyload();
                        }
                    },
                    error: function (xhr, type) {
                        $.hideLoading();
                    },
                    beforeSend: function (xhr, settings) {
                        $.showLoading();
                    },
                    complete: function (xhr, status) {
                        $.hideLoading();
                    }

                });
            },
            joinBook: function (data) {
                var self = this;
                var c = [];
                var n = 0;
                $.each(data, function (index2, item2) {
                    n++;
                    c.push('<div class="module"><div class="module-header tbline"><span class="module-title" >' + item2.periodName + '</span></div>');
                    c.push('<div class="book-list btmline">');
                    $.each(item2.dlfmBooks, function (index, item) {
                        c.push('<a class="book-list-item" href="${ctxfront}/book/' + item.id + '"><img class="lazy" style="vertical-align:middle" src="${ctxStatic}/images/90.jpg" data-original="${ctx}' + item.bookCoverUrl + '"> <span class="book-name">' + item.bookName + '</span></a>');
                    });
                    c.push('</div></div>');

                });
                if (n < 3) {
                    self.hasMore = false;
                    $(".weui-loadmore").hide();
                } else {
                    self.pageIndex++;
                    self.hasMore = true;
                    $(".weui-loadmore").show();
                }
                return c.join('');
            }
        };
        pageMnager.init();
    });
</script>
</body>

</html>
