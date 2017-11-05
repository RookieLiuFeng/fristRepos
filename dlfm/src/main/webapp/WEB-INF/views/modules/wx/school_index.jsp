<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>董李凤美</title>
    <%@include file="/common/wxmeta.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/js/swiper/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/app/modules/dlfm/css/default.css?v=${sysInitTime}">

    <style>
        html, body {
            position: relative;
            height: 100%;
        }
        body {
            background: #f2f2f2;
            font-family: Helvetica Neue, Helvetica, Arial, sans-serif;
            font-size: 14px;
            color:#000;
            margin: 0;
            padding: 0;
        }
        .swiper-container {
            width: 100%;
            height: 180px;
        }
        .swiper-slide {
            text-align: center;
            font-size: 18px;
            background: #000;

        }
        .swiper-slide img {
            width: 100%;
            height: 180px;
            max-width: 100%;
            max-height: 100%;
            -ms-transform: translate(-50%, -50%);
            -webkit-transform: translate(-50%, -50%);
            -moz-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);
            position: absolute;
            left: 50%;
            top: 50%;
        }
        
    .school-tree{
    	position: relative;
    	margin-top: 15px;
    	width: 100%;
    	height: calc(100% - 195px);
    }
    .school-tree img{
    	width: 100%;
    	height: 100%;
    }
    .ztn-a1{
    	position: absolute;
    	top:36% ;
    	left: 5%;
    	background: transparent;
    	width: 26%;
    	height: 26%;
    	display: block;
    }
    
    .ztn-a2{
    	position: absolute;
    	top:6% ;
    	left: 19%;
    	background: transparent;
    	width: 24%;
    	height: 25%;
    	display: block;	
    }
    
    .ztn-a3{
    	position: absolute;
    	top:2% ;
    	left: 45%;
    	background: transparent;
    	width: 21%;
    	height: 25%;
    	display: block;
    }
    .ztn-a4{
    	position: absolute;
    	top:22% ;
    	right: 13%;
    	background: transparent;
    	width: 21%;
    	height: 21%;
    	display: block;
    }
    
     .ztn-a5{
    	position: absolute;
    	top:42% ;
    	right: 3%;
    	background: transparent;
    	width: 23%;
    	height: 21%;
    	display: block;
    }
    
    </style>
</head>
<body ontouchstart>
<!-- Swiper -->
<div class="swiper-container">
    <div class="swiper-wrapper">
        <c:forEach var="banner" items="${banners}">
            <a href="${banner.linkUrl}" class="swiper-slide">
                <img data-src="${ctx}${banner.bannerUrl}" class="swiper-lazy">
                <div class="swiper-lazy-preloader swiper-lazy-preloader-white"></div>
            </a>
        </c:forEach>
    </div>
    <!-- Add Pagination -->
    <div class="swiper-pagination swiper-pagination-white"></div>
</div>
    <div class="school-tree">
    	<img src="${ctxStatic}/images/school_tree.png" />
    	<a href="http://www.xxjyz.net:8087/h5/dlfm/zgfc.html" class="ztn-a1"></a>
    	<a href="http://www.xxjyz.net:8087/h5/dlfm/xuxin.html" class="ztn-a2"></a>
    	<a href="http://www.xxjyz.net:8087/h5/dlfm/yange.html" class="ztn-a3"></a>
    	<a href="http://www.xxjyz.net:8087/h5/dlfm/kcfz.html" class="ztn-a4"></a>
    	<a href="http://www.xxjyz.net:8087/h5/dlfm/xxpmt.html" class="ztn-a5"></a>
    </div>

<!-- Swiper JS -->
<script type="text/javascript" src="${ctxStatic}/js/swiper/js/swiper.min.js"></script>

<!-- Initialize Swiper -->
<script>
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
        // Disable preloading of all images
        preloadImages: false,
        // Enable lazy loading
        lazyLoading: true,
        autoplay: 3000,
        autoplayDisableOnInteraction: false,
        //超链接
        preventClicks:false
    });
</script>
</body>
</html>
