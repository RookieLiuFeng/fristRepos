package com.sharewin.modules.sys.web;

import com.sharewin.common.model.Result;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.utils.DateUtils;
import com.sharewin.common.web.springmvc.BaseController;
import com.sharewin.core.security.SecurityUtils;
import com.sharewin.core.security.SessionInfo;
import com.sharewin.modules.sys.entity.DlfmBanner;
import com.sharewin.modules.sys.service.BannerManager;
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
import java.util.List;

@Controller
@RequestMapping(value = "${adminPath}/banner")
public class BannerController extends BaseController<DlfmBanner, Integer> {

    @Autowired
    private BannerManager bannerManager;

    @Override
    public EntityManager<DlfmBanner, Integer> getEntityManager() {
        return bannerManager;
    }

    @RequestMapping("index")
    public String pageNews() {
        return "/modules/sys/banner";
    }

    @RequestMapping("list")
    @ResponseBody
    public List<DlfmBanner> bannerList(String sc_bannerDesc) {
        return bannerManager.getBannerByQuery(sc_bannerDesc);
    }

    @RequestMapping(value = {"input"})
    public String bannerInput(@ModelAttribute("model") DlfmBanner banner, Model model) {
        if(banner.getId()!=null){
            model.addAttribute("banner",banner);
        }
        return "modules/sys/banner-input";
    }


    /**
     * 新增或修改.
     *
     * @param banner
     * @return
     */
    @RequestMapping(value = {"bsave"})
    @ResponseBody
    public Result save(HttpServletRequest request,@RequestParam(required=false,value = "file")MultipartFile file,@ModelAttribute("model") DlfmBanner banner) {
        Result result = null;
        try {
            if (request instanceof MultipartHttpServletRequest && file != null && file.getSize() > 0) {
                String xdpath = "/static/upload/bannerImages/";

                String path = request.getSession().getServletContext().getRealPath(xdpath);
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile multipartFile = multipartRequest.getFile("file");
                File dirs = new File(path);
                if (!dirs.exists()) {
                    new File(path).mkdirs();
                }
                String imageName = DateUtils.getDateRandom() + ".png";
                File out = new File(path + "/" + imageName);
                banner.setBannerUrl(xdpath+imageName);
                multipartFile.transferTo(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = Result.errorResult().setMsg("上传banner失败!");
        }
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        if(banner.getId()==null){
            banner.setCreateTime(DateUtils.getSysTimestamp());
            banner.setCreateUserid(sessionInfo.getUserId());
            banner.setStatus(StatusState.normal.getValue());
        }
        getEntityManager().saveEntity(banner);
        return Result.successResult();
    }
}
