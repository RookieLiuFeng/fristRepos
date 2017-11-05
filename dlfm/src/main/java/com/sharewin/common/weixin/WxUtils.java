package com.sharewin.common.weixin;

import com.sharewin.common.utils.Identities;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.utils.encode.Encrypt;
import com.sharewin.common.utils.mapper.JsonMapper;
import com.sharewin.timer.quartz.QuartzJobManager;
import com.sharewin.utils.AppConstants;
import com.sharewin.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WxUtils {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 用户授权验证
     * @param appId
     * @param appSecret
     * @param code
     * @return
     */
    public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        WeixinOauth2Token wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        Map<String,String> params = new HashMap<String, String>();
        params.put("appid",appId);
        params.put("secret",appSecret);
        params.put("code",code);
        params.put("grant_type","authorization_code");
        String result = HttpUtils.URLGet(requestUrl,params,"UTF-8");
        if(StringUtils.isNotEmpty(result)){
            wat = JsonMapper.getInstance().fromJson(result,WeixinOauth2Token.class);
        }
        return  wat;
    }

    /**
     * 获取用户信息
     * @param accessToken
     * @param openId
     * @return
     */
    public static SNSUserInfo getSNSUserInfo(String accessToken,String openId){
        SNSUserInfo snsUserInfo = null;
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo";
        Map<String,String> params = new HashMap<String, String>();
        params.put("access_token",accessToken);
        params.put("openid",openId);
        String result = HttpUtils.URLGet(requestUrl,params,"UTF-8");
        if(StringUtils.isNotEmpty(result)){
            snsUserInfo = JsonMapper.getInstance().fromJson(result,SNSUserInfo.class);
        }
        return snsUserInfo;
    }

    public static AccessToken getAccessToken(String appid,String appsecret){
        AccessToken accessToken = null;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token";
        Map<String,String> parameters = new HashMap<String, String>();
        parameters.put("grant_type","client_credential");
        parameters.put("appid",appid);
        parameters.put("secret",appsecret);
        String result = HttpUtils.URLGet(requestUrl,parameters,"UTF-8");
        if(StringUtils.isNotEmpty(result)){
            accessToken = JsonMapper.getInstance().fromJson(result,AccessToken.class);
        }
        return accessToken;

    }

    public static JsapiTicket getJsapiTicket(String accessToken){
        JsapiTicket jsapiTicket = null;
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
        Map<String,String> params = new HashMap<String, String>();
        params.put("access_token",accessToken);
        params.put("type","jsapi");
        String result = HttpUtils.URLPost(requestUrl,params,"UTF-8");
        if(result!=null&&result.trim().length()>0){
            jsapiTicket = JsonMapper.getInstance().fromJson(result,JsapiTicket.class);
            System.out.println("jsapiTicket结果:"+result);
        }
        return jsapiTicket;
    }


    public static Map<String, Object> getWxConfig(String requestUrl){
        Map<String, Object> config = new HashMap<String, Object>();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000); // 必填，生成签名的时间戳
        String nonceStr = Identities.uuid2();
        // 注意这里参数名必须全部小写，且必须有序
        String sign = "jsapi_ticket=" + QuartzJobManager.jsapiTicket.getTicket() + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + requestUrl;
        String signature = Encrypt.sha(sign);

        config.put("appid", AppConstants.appid);
        config.put("timestamp",timestamp);
        config.put("nonceStr",nonceStr);
        config.put("signature",signature);

        return config;

    }
}
