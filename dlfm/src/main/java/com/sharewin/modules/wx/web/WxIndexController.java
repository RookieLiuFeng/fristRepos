package com.sharewin.modules.wx.web;

import com.sharewin.common.model.Result;
import com.sharewin.core.security.SecurityConstants;
import com.sharewin.core.security.SecurityType;
import com.sharewin.core.security.SecurityUtils;
import com.sharewin.core.security.SessionInfo;
import com.sharewin.core.security.annotation.RequiresUser;
import com.sharewin.core.security.annotation.WxSite;
import com.sharewin.modules.sys.entity.DlfmAct;
import com.sharewin.modules.sys.entity.DlfmActRoot;
import com.sharewin.modules.sys.entity.DlfmBook;
import com.sharewin.modules.sys.entity.DlfmNews;
import com.sharewin.modules.sys.service.*;
import com.sharewin.modules.wx.service.WxDlfmManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yomg on 2017/6/2 0002.
 */
@Controller
@RequestMapping(value = "${frontPath}")
@WxSite(isWx = true)
public class WxIndexController {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserManager userManager;

    @Autowired
    private NewsManager newsManager;

    @Autowired
    private BookManager bookManager;

    @Autowired
    private ActManager actManager;

    @Autowired
    private SurveyManager surveyManager;

    @Autowired
    private BannerManager bannerManager;

    @Autowired
    private BookPeriodManager bookPeriodManager;

    @Autowired
    private ActRootManager actRootManager;

    @Autowired
    private WxDlfmManager wxDlfmManager;

    /**
     * 用户登录页
     *
     * @return
     * @throws Exception
     */
    @RequiresUser(required = false)
    @RequestMapping(value = {"index/welcome", ""})
    public String welcome() throws Exception {
        return "/modules/wx/login";
    }

    /**
     * 主页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"index"})
    public String index(Model model) {
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        model.addAttribute("user", userManager.getById(sessionInfo.getUserId()));
        return "/modules/wx/wx_index";
    }

    /**
     * 新闻页
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "news")
    @ResponseBody
    @RequiresUser(required = false)
    public List<Map<String, Object>> getWxNewsList(Integer page) {
        return newsManager.getWxNewsList(page);
    }

    /**
     * 新闻详情
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "news/{id}")
    @RequiresUser(required = false)
    public String pageNewsDisplay(@PathVariable(value = "id") Integer id, Model model) {
        newsManager.addBrowseCount(id);
        DlfmNews dlfmNews = newsManager.getById(id);
        if (dlfmNews.getNewsType() == 1) {
            return "redirect:" + dlfmNews.getLinkUrl();
        } else {
            model.addAttribute("news", dlfmNews);
            return "/modules/wx/news_display";
        }

    }

    /**
     * 好书推荐
     *
     * @param page
     * @param booktype
     * @return
     */
    @RequestMapping(value = "book")
    @ResponseBody
    @RequiresUser(required = false)
    public List<Map<String, Object>> getWxBookList(Integer page, Integer booktype) {
        return bookPeriodManager.getWxBookList(page, booktype);
    }

    /**
     * 图书详情页
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "book/{id}")
    @RequiresUser(required = false)
    public String pageBookDisplay(@PathVariable(value = "id") Integer id, Model model) {
        bookManager.addBrowseCount(id);
        DlfmBook dlfmBook = bookManager.getById(id);
        if (dlfmBook.getBookType() == 1) {
            return "redirect:" + dlfmBook.getLinkUrl();
        } else {
            model.addAttribute("book", bookManager.getById(id));
            return "/modules/wx/book_display";
        }

    }

    /**
     * 活动
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "act")
    @ResponseBody
    @RequiresUser(required = false)
    public List<Map<String, Object>> getWxactList(Integer page) {
        return actRootManager.getWxActRoot(page);
    }

    /**
     * 活动详情页
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "actRoot/{id}")
    @RequiresUser(required = false)
    public String pageActRootDisplay(@PathVariable(value = "id") Integer id, Model model) {

        DlfmActRoot dlfmActRoot = actRootManager.getById(id);
        if (dlfmActRoot.getActType() == 1) {
            return "redirect:" + dlfmActRoot.getLinkUrl();
        } else {
            model.addAttribute("act", dlfmActRoot);
            return "/modules/wx/act_display";
        }

    }

    /**
     * 活动详情页
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "act/{id}")
    @RequiresUser(required = false)
    public String pageActDisplay(@PathVariable(value = "id") Integer id, Model model) {

        DlfmAct dlfmAct = actManager.getById(id);
        if (dlfmAct.getActType() == 1) {
            return "redirect:" + dlfmAct.getLinkUrl();
        } else {
            model.addAttribute("act", dlfmAct);
            return "/modules/wx/act_display";
        }

    }


    /**
     * 校园首页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "school/index")
    @RequiresUser(required = false)
    public String schoolIndex(Model model) {
        model.addAttribute("banners", bannerManager.getBannerByQuery(""));
        return "/modules/wx/school_index";
    }

    /*  *
      * *==============================================================================
      * *                                       分离
      * *==============================================================================
      * *
     */

    /**
     * 新闻页
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "news/list")
    @RequiresUser(required = false)
    public String getWxNewsPage(Integer page) {
        return "/modules/wx/news";
    }

    /**
     * 活动页
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "act/list")
    @RequiresUser(required = false)
    public String getWxActPage(Integer page) {
        return "/modules/wx/act";
    }

    @RequestMapping(value = "act/list/root")
    @RequiresUser(required = false)
    public List<Map<String, Object>> getWxActRoot(int pageNo) {
        return actRootManager.getWxActRoot(pageNo);
    }

    /**
     * 书籍页
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "book/list")
    @RequiresUser(required = false)
    public String getWxPage(Integer page) {
        return "/modules/wx/book";
    }


   /* ***********************************************************************
   *                                                                        *
   *                            个人中心                                     *
   *                                                                        *
   * ************************************************************************/

    @RequestMapping(value = "mycenter")
    public String pageToMyCenter(Model model) {
        model.addAttribute("user", wxDlfmManager.getUsers(SecurityUtils.getCurrentSessionInfo().getUserId()));
        return "modules/wx/mycenter";
    }

    @RequestMapping(value = "mycenter/editpassword")
    public String updatePasswordPage() {
        return "modules/wx/editpd";
    }

    @RequestMapping(value = "mycenter/editpassword.json", method = RequestMethod.POST)
    @ResponseBody
    public Result updatePassword(String orgpassword, String newpassword) {
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        return wxDlfmManager.updatePassword(sessionInfo.getUserId(), orgpassword, newpassword);
    }

    @RequestMapping(value = "mycenter/removeBindWx.json", method = RequestMethod.POST)
    @ResponseBody
    public Result removeBindWx(HttpServletRequest request){
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        Result result = wxDlfmManager.removeBindWx(sessionInfo.getUserId());
        if(result.getCode()==1){
            String sessionId = request.getSession().getId();
            SecurityUtils.removeUserFromSession(sessionId, false, SecurityType.logout);
            result.setObj(SecurityConstants.WX_UNAUTHORITY_LOGIN_PAGE);
        }
        return result;
    }

    /**
     * 问卷调查
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "survey")
    public String pageSurvey(Model model) {
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        model.addAttribute("surveys", surveyManager.getWxSurveyList(sessionInfo.getUserId()));
        return "/modules/wx/survey_index";
    }

    /**
     * 问卷调查详情页
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "survey/{id}")
    public String pageSurveyDisplay(@PathVariable(value = "id") Integer id, Model model) {
        model.addAttribute("survey", surveyManager.getById(id));
        return "/modules/wx/survey_display";
    }

    /**
     * 提交问卷
     *
     * @param id
     * @param jsonString
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "survey/submit")
    @ResponseBody
    public Result submitSurvey(Integer id, String jsonString) throws IOException {
        Result result = null;
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        if (surveyManager.checkIsSurvey(id, sessionInfo.getUserId()) > 0) {
            result = new Result(Result.WARN, "你已参加过本次问卷！", null);
        } else {
            surveyManager.addSurveyRecord(id, sessionInfo.getUserId(), jsonString);
            result = Result.successResult();
        }

        return result;
    }

}
