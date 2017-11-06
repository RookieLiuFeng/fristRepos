package com.sharewin.utils;


import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.utils.SysConstants;
import com.sharewin.common.utils.io.PropertiesLoader;

/**
 * Created by yomg on 2017/4/1 0001.
 */
public class AppConstants extends SysConstants{

    /**
     * 系统初始化时间
     */
    public static long SYS_INIT_TIME = System.currentTimeMillis();

    private static PropertiesLoader config = null;
    public static final String APPCONFIG_FILE_PATH = "appconfig.properties";
    public static final String CONFIG_FILE_PATH = "config.properties";

    public static String BASEURL = "http://wlgl.share-win.net/dlfm";

    public static final String appid = "wxf616ddc274817a00";

    public static final String appsecret = "dc4ece666f881f534f2f3a84fc7f97e1";

    /**
     * 修改用户密码 个人(需要输入原始密码)
     */
    public static final String USER_UPDATE_PASSWORD_YES = "1";
    /**
     * 修改用户密码 个人(不需要输入原始密码)
     */
    public static final String USER_UPDATE_PASSWORD_NO = "0";


    /**
     * 获取配置
     */
    public static String getAppConfig(String key) {
        return getAppConfig().getProperty(key);
    }

    /**
     * 获取配置
     */
    public static String getAppConfig(String key,String defaultValue) {
        return getAppConfig().getProperty(key,defaultValue);
    }

    /**
     * 获取管理端根路径
     */
    public static String getAdminPath() {
        return getAppConfig("adminPath","/home");
    }

    /**
     * 获取管理端根路径
     */
    public static String getFrontPath() {
        return getAppConfig("frontPath","/wx");
    }


    /**
     * 配置文件(config.properties)
     */
    public static PropertiesLoader getConfig() {
        if(config == null){
            config = new PropertiesLoader(CONFIG_FILE_PATH);
        }
        return config;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        return getConfig().getProperty(key);
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key,String defaultValue) {
        return getConfig().getProperty(key,defaultValue);
    }

    /**
     * 启用安全检查
     * @return
     */
    public static boolean getIsSecurityOn() {
        String code = "security.on";
        String value = getConfig(code, "false");
        return "true".equals(value) || "1".equals(value);
    }

    /**
     * 系统最大登录用户数
     * @return
     */
    public static int getSessionUserMaxSize() {
        String code = "sessionUser.MaxSize";
        String value = getConfig(code, "0");
        return Integer.valueOf(value);
    }

    /**
     * 获取用户可创建会话数量 默认值：0
     * 0 无限制
     * @return
     */
    public static int getUserSessionSize() {
        String code = "sessionUser.UserSessionSize";
        String value = getConfig(code);
        return StringUtils.isBlank(value) ? 0:Integer.valueOf(value);
    }



}
