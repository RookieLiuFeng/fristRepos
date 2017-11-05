<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>董李凤美</title>
    <%@include file="/common/wxmeta.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/app/modules/dlfm/css/default.css?v=${sysInitTime}">
</head>
<body ontouchstart>
<div hidden>
    <svg xmlns="http://www.w3.org/2000/svg">
        <symbol id="icon-arrow-l" viewBox="0 0 8 16">
            <path d="M.146 7.646a.5.5 0 0 0 0 .708l7 7a.5.5 0 0 0 .708-.708l-7-7v.708l7-7a.5.5 0 0 0-.708-.708l-7 7z"></path>
        </symbol>
        <symbol id="icon-arrow-r" viewBox="0 0 7 12">
            <path d="M6.146 6.354v-.708l-5.5 5.5a.5.5 0 0 0 .708.708l5.5-5.5a.5.5 0 0 0 0-.708l-5.5-5.5a.5.5 0 1 0-.708.708l5.5 5.5z"></path>
        </symbol>
        <symbol id="icon-more" viewBox="0 0 18 13">
            <g>
                <rect width="18" height="1" rx=".5"></rect>
                <rect y="6" width="18" height="1" rx=".5"></rect>
                <rect y="12" width="18" height="1" rx=".5"></rect>
            </g>
        </symbol>
    </svg>
</div>

<nav class="ztn-bar ztn-bar-tab">
    <a class="ztn-tab-item ztn-active" data-panel="news" href="javascript:void(0);">
        <span class="ztn-tab-icon ztn-tab-icon_news"></span>
        <span class="ztn-tab-label">爱儿有方</span>
    </a>
    <a class="ztn-tab-item" data-panel="book" href="javascript:void(0);">
        <span class="ztn-tab-icon ztn-tab-icon_book"></span>
        <span class="ztn-tab-label">好书推荐</span>
    </a>
    <a class="ztn-tab-item" data-panel="act" href="javascript:void(0);">

        <span class="ztn-tab-icon ztn-tab-icon_act"></span>
        <span class="ztn-tab-label">主题活动</span>
    </a>
    <a class="ztn-tab-item" data-panel="personal" href="javascript:void(0);">
        <span class="ztn-tab-icon ztn-tab-icon_personal"></span>
        <span class="ztn-tab-label">个人中心</span>
    </a>
</nav>
<div class="ztn-content">
    <%--爱儿有方--%>
    <div id="ztn-page_news" class="ztn-tab_panel panel_on" data-panel="news">
        <div id="newsList" class="module module-nomargin btmline">

        </div>
    </div>

    <%--好书推荐--%>
    <div id="ztn-page_book" class="ztn-tab_panel" data-panel="book">
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
    </div>

    <%--主题活动--%>
    <div id="ztn-page_act" class="ztn-tab_panel" data-panel="book">
        <div id="actList" class="act-content">

        </div>
    </div>

    <%--个人中心--%>
    <div id="ztn-page_personal" class="ztn-tab_panel" data-panel="book">
        <div class="module module-nomargin">
            <div class="module-user_info">
                <img class="user_photo" src="${ctxStatic}/images/avatar_default.png"/>
                <div class="user_layout">
                    <p class="user_name">姓名:${user.realname}</p>
                    <p class="user_goz">上海市董李凤美康健学校</p>
                </div>
                <div class="user_right_view">
                    <p>三年级二班</p>
                    <svg class="icon user-arrow" aria-hidden="true">
                        <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
                    </svg>
                </div>
            </div>
        </div>
        <div class="module tbline">
            <a href="${ctxfront}/survey" class="module-cell personal-cell btmline">
                <img src="${ctxStatic}/images/my_value.png"/>
                <div class="module-cell_bd">
                    <p class="personal-title">问卷调查</p>
                </div>
                <div class="personal-cell_fr">
                    <svg class="icon personal-cell_arrow" aria-hidden="true">
                        <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
                    </svg>
                </div>
            </a>
            <div class="module-cell personal-cell btmline">
                <img src="${ctxStatic}/images/my_shc.png"/>
                <div class="module-cell_bd">
                    <p class="personal-title">我的收藏</p>
                </div>
                <div class="personal-cell_fr">
                    <svg class="icon personal-cell_arrow" aria-hidden="true">
                        <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
                    </svg>
                </div>
            </div>
            <a href="${ctxfront}/school/index" class="module-cell personal-cell btmline">
                <img src="${ctxStatic}/images/my_yh.png"/>
                <div class="module-cell_bd">
                    <p class="personal-title">校园首页</p>
                </div>
                <div class="personal-cell_fr">
                    <svg class="icon personal-cell_arrow" aria-hidden="true">
                        <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
                    </svg>
                </div>
            </a>
            <div class="module-cell personal-cell btmline">
                <img src="${ctxStatic}/images/my_password.png"/>
                <div class="module-cell_bd">
                    <p class="personal-title">密码修改</p>
                </div>
                <div class="personal-cell_fr">
                    <svg class="icon personal-cell_arrow" aria-hidden="true">
                        <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
                    </svg>
                </div>
            </div>
            <div class="module-cell personal-cell btmline">
                <img src="${ctxStatic}/images/my_setting.png"/>
                <div class="module-cell_bd">
                    <p class="personal-title">设置</p>
                </div>
                <div class="personal-cell_fr">
                    <svg class="icon personal-cell_arrow" aria-hidden="true">
                        <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use>
                    </svg>
                </div>
            </div>
        </div>

        <div class="module tbline">
            <a class="module-cell personal-cell flex-jc-c">
                <div class="module-cell_bd tc" style="font-size: 16px;color: #E64340;">
                    <span>退出</span>
                </div>
            </a>
        </div>
    </div>


</div>

<div class="weui-loadmore" style="display: none">
    <i class="weui-loading"></i>
    <span class="weui-loadmore__tips">正在加载</span>
</div>
</div>
<script src="https://cdn.bootcss.com/jquery_lazyload/1.9.7/jquery.lazyload.min.js"></script>
<script type="text/javascript">
    $(function () {
        var pageManager = {
            $newsList: $("#newsList"),
            $bookContent: $("#bookContent"),
            $actList: $("#actList"),
            pageIndex: 0,
            hasMore: false,
            booktype: 0,
            pageTag:0,
            init: function () {
                var self = this;
                self.initTab();
                var loading = false;
                $(document.body).infinite().on("infinite", function () {
                    if (loading || (!self.hasMore)) return;
                    if(self.pageTag == 0){
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
                    }else if(self.pageTag==1){
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
                    }else if(self.pageTag==2){
                        $.ajax({
                            type: 'GET',
                            url: ctxFront + '/act',
                            dataType: 'json',
                            data: {page: self.pageIndex},
                            async: true,
                            success: function (data) {
                                // console.log(JSON.stringify(data));
                                if (data != null) {
                                    self.$actList.append(self.joinAct(data));
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
                    }

                });

                setTimeout(function () {
                    self.getNewsList();
                }, 200);

            },
            initTab: function () {
                var self = this;
                $('.ztn-tab-item').on('click', function () {
                    $(this).addClass('ztn-active').siblings('.ztn-active').removeClass('ztn-active');
                    $(".ztn-content .ztn-tab_panel").removeClass("panel_on");
                    var m = $(this).data("panel");
                    $(window).scrollTop(0);
                    self.pageIndex = 0;
                    self.hasMore = false;
                    switch (m) {
                        case "news":
                            $("#ztn-page_news").addClass("panel_on");
                            self.pageTag = 0;
                            setTimeout(function () {
                                self.getNewsList();
                            }, 200);
                            break;
                        case "book":
                            $("#ztn-page_book").addClass("panel_on");
                            self.pageTag = 1;
                            setTimeout(function () {
                                self.getBookList();
                            }, 200);
                            break;
                        case "act":
                            $("#ztn-page_act").addClass("panel_on");
                            self.pageTag = 2;
                            setTimeout(function () {
                                self.getActList();
                            }, 200);
                            break;
                        case "personal":
                            $("#ztn-page_personal").addClass("panel_on");
                            break;
                    }
                });
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
                })
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
                    c.push('<div class="module"><div class="module-header tbline"><span class="module-title" >'+item2.periodName+'</span></div>');
                    c.push('<div class="book-list btmline">');
                    $.each(item2.dlfmBooks,function (index,item) {
                        c.push('<a class="book-list-item" href="${ctxfront}/book/'+item.id+'"><img class="lazy" style="vertical-align:middle" src="${ctxStatic}/images/90.jpg" data-original="${ctx}' + item.bookCoverUrl + '"> <span class="book-name">' + item.bookName + '</span></a>');
                    });
                    c.push('</div></div>');

                });
                console.log("期刊数量:"+n);
                if (n < 3) {
                    self.hasMore = false;
                    $(".weui-loadmore").hide();
                } else {
                    self.pageIndex++;
                    self.hasMore = true;
                    $(".weui-loadmore").show();
                }
                return c.join('');
            },
            getActList: function () {
                var self = this;
                $.ajax({
                    type: 'GET',
                    url: ctxFront + '/act',
                    dataType: 'json',
                    data: {page: self.pageIndex},
                    async: true,
                    success: function (data) {
                        // console.log(JSON.stringify(data));
                        if (data != null) {
                            self.$actList.html(self.joinAct(data));
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
            joinAct: function (data) {
                var self = this;
                var c = [];
                var n = 0;
                $.each(data, function (index, item) {
                    n++;
                    c.push('<a class="act-cell" href="${ctxfront}/act/'+item.id+'"><div class="act-proctm-view"><span class="act-proctm">' + item.createTime + '</span></div><div class="act-item"><p class="act-title">' + item.actName + '</p><p class="act-time">' + item.actTime + '</p><img class="lazy act-img" src="${ctxStatic}/images/zanwu.jpg" data-original="${ctx}' + item.actCoverUrl + '"><div class="act-diver"></div><div class="act-read-view"><p>查看全文</p><svg class="icon act-arrow" aria-hidden="true"><use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#icon-arrow-r"></use></svg></div></div></a>');
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
        pageManager.init();
    });

</script>
</body>
</html>
