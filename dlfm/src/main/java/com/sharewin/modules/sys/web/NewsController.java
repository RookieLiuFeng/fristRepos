package com.sharewin.modules.sys.web;

import com.sharewin.common.model.Datagrid;
import com.sharewin.common.model.Result;
import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.utils.DateUtils;
import com.sharewin.common.web.springmvc.BaseController;
import com.sharewin.common.web.springmvc.SpringMVCHolder;
import com.sharewin.core.security.SecurityUtils;
import com.sharewin.core.security.SessionInfo;
import com.sharewin.modules.sys.entity.DlfmBook;
import com.sharewin.modules.sys.entity.DlfmNews;
import com.sharewin.modules.sys.service.NewsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping(value = "${adminPath}/news")
public class NewsController extends BaseController<DlfmNews,Integer>{

    @Autowired
    private NewsManager newsManager;

    @Override
    public EntityManager<DlfmNews, Integer> getEntityManager() {
        return newsManager;
    }



    @RequestMapping("index")
    public String pageNews(){
        return "/modules/sys/news";
    }
    /**
     * 新闻集合
     * @param titleName
     * @return
     */
    @RequestMapping("newsDatagrid")
    @ResponseBody
    public Datagrid<DlfmNews> newsDatagrid(String titleName){
        Page<DlfmNews> p = new Page<DlfmNews>(SpringMVCHolder.getRequest());
        p = newsManager.getNewsByQuery(titleName,p);
        Datagrid<DlfmNews> dg = new Datagrid<DlfmNews>(p.getTotalCount(), p.getResult());
        return dg;
    }

    @RequestMapping(value = {"input"})
    public String newsInput(@ModelAttribute("model") DlfmNews news, Model model){
        if(news.getId()!=null){
            model.addAttribute("news",news);
        }
        return "modules/sys/news-input";

    }

    /**
     * 新增或修改.
     *
     * @param dlfmNews
     * @return
     */
    @RequestMapping(value = {"bsave"})
    @ResponseBody
    public Result save(HttpServletRequest request, @RequestParam(required=false,value = "file")MultipartFile file, @ModelAttribute("model") DlfmNews dlfmNews) {
        Result result = null;
        try {
            if (request instanceof MultipartHttpServletRequest && file != null && file.getSize() > 0) {
                String xdpath = "/static/upload/newsImages/";

                String path = request.getSession().getServletContext().getRealPath(xdpath);
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile multipartFile = multipartRequest.getFile("file");
                File dirs = new File(path);
                if (!dirs.exists()) {
                    new File(path).mkdirs();
                }
                String imageName = DateUtils.getDateRandom() + ".png";
                File out = new File(path + "/" + imageName);
                dlfmNews.setNewsCoverUrl(xdpath+imageName);
                multipartFile.transferTo(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = Result.errorResult().setMsg("上传新闻封面失败!");
        }
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        if(dlfmNews.getId()==null){
            dlfmNews.setCreateTime(DateUtils.getSysTimestamp());
            dlfmNews.setCreateUserid(sessionInfo.getUserId());
            dlfmNews.setStatus(StatusState.normal.getValue());
        }
        getEntityManager().saveEntity(dlfmNews);
        return Result.successResult();
    }
}
