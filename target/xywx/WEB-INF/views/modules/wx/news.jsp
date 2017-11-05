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
 <div id="newsList" class="module module-nomargin btmline">

 </div>
 <div class="weui-loadmore" style="display: none">
     <i class="weui-loading"></i>
     <span class="weui-loadmore__tips">正在加载</span>
 </div>
 <script src="https://cdn.bootcss.com/jquery_lazyload/1.9.7/jquery.lazyload.min.js"></script>
</body>
<script>
    $(function () {
        var pageMnager = {
            $newsList: $("#newsList"),
            pageIndex: 0,
            hasMore: false,
            init:function () {
                var self = this;
                var loading = false;
                $(document.body).infinite().on("infinite", function () {
                    if (loading || (!self.hasMore)) return;
                        $.ajax({
                            type: 'GET',
                            url: ctxFront + '/news',
                            dataType: 'json',
                            data: {page: self.pageIndex},
                            async: true,
                            success: function (data) {
                                // console.log(JSON.stringify(data));
                                if (data != null) {
                                    self.$newsList.append(self.joinNews(data));
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
                    self.getNewsList();
                }, 200);
            },
            getNewsList: function () {
                var self = this;
                $.ajax({
                    type: 'GET',
                    url: ctxFront + '/news',
                    dataType: 'json',
                    data: {page: self.pageIndex},
                    async: true,
                    success: function (data) {
                        // console.log(JSON.stringify(data));
                        if (data != null) {
                            self.$newsList.html(self.joinNews(data));
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
            joinNews: function (data) {
                var self = this;
                var c = [];
                var n = 0;
                $.each(data, function (index, item) {
                    n++;
                    c.push('<a class="dlfm-news-cell btmline" href="${ctxfront}/news/'+item.id+'"><img class="lazy" src="${ctxStatic}/images/wximage_default.jpg" data-original="${ctx}' + item.newsCover + '"><div class="news-info-view"><p class="news-title">' + item.newsName + '</p><p class="news-time">' + item.createTime + '</p></div></a>');
                });
                if (n < 15) {
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
</html>
