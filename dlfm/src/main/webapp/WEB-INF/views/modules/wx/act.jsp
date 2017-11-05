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
<div id="actList" class="act-content">
<%--
  <div class="act-cell">
        <div class="act-proctm-view">
            <span class="act-proctm">2017年12月12日 11:11</span>
        </div>
        <div class="act-main">
            <a class="act-root">
                <img src="${ctxStatic}/images/zanwu.jpg"/>
                <div class="act-root-title">
                    有一种骄傲，叫我是徐汇人！
                </div>
            </a>
            <a class="act-child-item">
                <p class="act-child-item-title">徐汇免费体检套餐领取，含家庭专家体检</p>
                <img class="act-child-item-img" src="${ctxStatic}/images/90.jpg"/>
            </a>
            <a class="act-child-item">
                <p class="act-child-item-title">徐汇免费体检套餐领取，含家庭专家体检</p>
                <img class="act-child-item-img" src="${ctxStatic}/images/90.jpg"/>
            </a>
            <a class="act-child-item">
                <p class="act-child-item-title">徐汇免费体检套餐领取，含家庭专家体检</p>
                <img class="act-child-item-img" src="${ctxStatic}/images/90.jpg"/>
            </a>

        </div>
    </div>
--%>

</div>

<script src="https://cdn.bootcss.com/jquery_lazyload/1.9.7/jquery.lazyload.min.js"></script>
<script>
    $(function () {
        var pageMnager = {
            $actList: $("#actList"),
            pageIndex: 0,
            hasMore: false,
            init: function () {
                var self = this;
                var loading = false;
                $(document.body).infinite().on("infinite", function () {
                    if (loading || (!self.hasMore)) return;
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

                });

                setTimeout(function () {
                    self.getActList();
                }, 200);
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
                         console.log(JSON.stringify(data));
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
                    c.push('<div class="act-cell">');
                    c.push('<div class="act-proctm-view"><span class="act-proctm">' + item.createTime + '</span></div>');
                    c.push('<div class="act-main">');
                    c.push('<a class="act-root" href="${ctxfront}/actRoot/'+item.id+'"><img class="lazy" src="${ctxStatic}/images/wximage_default.jpg" data-original="${ctx}' + item.actCoverUrl + '"/><div class="act-root-title">'+item.actName+'</div></a>');
                   $.each(item.actList,function (index2,item2) {
                        c.push(' <a class="act-child-item" href="${ctxfront}/act/'+item2.id+'"><p class="act-child-item-title">'+item2.actName+'</p><img class="act-child-item-img lazy" src="${ctxStatic}/images/wximage_default.jpg" data-original="${ctx}' + item2.actCoverUrl + '"/></a>');
                    });
                    c.push('</div>');
                    c.push('</div>');
                });
                if (n < 10) {
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
