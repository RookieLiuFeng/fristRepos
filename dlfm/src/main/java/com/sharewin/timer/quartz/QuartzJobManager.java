package com.sharewin.timer.quartz;


import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.utils.mapper.JsonMapper;
import com.sharewin.common.weixin.AccessToken;
import com.sharewin.common.weixin.JsapiTicket;
import com.sharewin.common.weixin.WxUtils;
import com.sharewin.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzJobManager {

    private static final Logger logger = LoggerFactory
            .getLogger(QuartzJobManager.class);

    public static AccessToken  accessToken = null;

    public static JsapiTicket jsapiTicket = null;

    public void weixinAccessToken(){
        AccessToken token = WxUtils.getAccessToken(AppConstants.appid,AppConstants.appsecret);
        logger.info("定时获取(董李凤美)token:"+ JsonMapper.getInstance().toJson(token));
        if(token!=null&& StringUtils.isNotEmpty(token.getAccess_token())){
            accessToken = token;
            jsapiTicket = WxUtils.getJsapiTicket(accessToken.getAccess_token());
            logger.info("定时获取(董李凤美)jsapiTicket:"+ JsonMapper.getInstance().toJson(jsapiTicket));
        }
    }
}
