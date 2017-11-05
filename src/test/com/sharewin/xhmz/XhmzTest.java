package com.sharewin.xhmz;

import com.google.common.collect.Lists;
import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.jdbc.JdbcDao;
import com.sharewin.common.utils.mapper.JsonMapper;
import com.sharewin.common.web.springmvc.SpringMVCHolder;
import com.sharewin.modules.sys.entity.DlfmNews;
import com.sharewin.modules.sys.entity.Survey;
import com.sharewin.modules.sys.entity.User;
import com.sharewin.modules.sys.service.*;
import com.sharewin.modules.sys.vo.RecordVo;
import com.sharewin.modules.sys.vo.SurveyResult;
import com.sharewin.modules.wx.service.WxDlfmManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yomg on 2017/4/12 0012.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class XhmzTest {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserManager userManager;

    @Autowired
    private NewsManager newsManager;

    @Autowired
    private SurveyManager surveyManager;

    @Autowired
    private BookPeriodManager bookPeriodManager;

    @Autowired
    private ActRootManager actRootManager;

    @Autowired
    private WxDlfmManager wxDlfmManager;


    @Test
    public void wxNewsList(){
        List<Map<String,Object>> list = newsManager.getWxNewsList(0);
        logger.info(JsonMapper.getInstance().toJson(list));
    }

    @Test
    public void checkIsSurvey(){
        int count = surveyManager.checkIsSurvey(4,1);
        logger.info("是否问卷:"+count);
    }

    @Test
    public void wxBookList(){
        List<Map<String,Object>> list = bookPeriodManager.getWxBookList(1,0);
        logger.info(JsonMapper.getInstance().toJson(list));
    }
    @Test
    public void wxget(){
        List<SurveyResult> results = Lists.newArrayList();
        SurveyResult result = new SurveyResult();
        result.setAid("232323");
        result.setQid(1);
        results.add(result);
        SurveyResult result2 = new SurveyResult();
        result.setAid("sdsdsd");
        result.setQid(2);
        results.add(result2);
        logger.info(JsonMapper.getInstance().toJson(results));
        HashMap<String,Object> map = new HashMap<String, Object>();
    }

    @Test
    public void getActRoot(){
        logger.info(JsonMapper.getInstance().toJson(actRootManager.getWxActRoot(0)));
    }

    @Test
    public void getUsers(){
        List<User> users = userManager.getUsers();
        logger.info(JsonMapper.getInstance().toJson(users));
    }

    @Test
    public void getUser2(){
        logger.info(JsonMapper.getInstance().toJson(wxDlfmManager.getUsers(1)));
    }
    @Test
    public void getSurvey(){
        logger.info(JsonMapper.getInstance().toJson(surveyManager.getWxSurveyList(2)));
    }

    @Test
    public void getResult(){
        logger.info(JsonMapper.getInstance().toJson(surveyManager.getResult(4,null,new Page<RecordVo>(SpringMVCHolder.getRequest()))));
    }
    @Test
    public void ja(){
        List<Map<String,Object>> m1 = new ArrayList<Map<String, Object>>();
        Map<String,Object> t1 = new HashMap<String,Object>();
        t1.put("name","123");
        t1.put("age",234);
        Map<String,Object> t2 = new HashMap<String,Object>();
        t2.put("name","gher");
        t2.put("age",567);
        m1.add(t1);
        m1.add(t2);
        logger.info(JsonMapper.getInstance().toJson(m1));
    }

    @Test
    public void getRecord(){
        logger.info(JsonMapper.getInstance().toJson(surveyManager.getResult(4)));
    }
}
