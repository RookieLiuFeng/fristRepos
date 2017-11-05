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
import com.sharewin.modules.sys.entity.DlfmActRoot;
import com.sharewin.modules.sys.service.ActRootManager;
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
@RequestMapping(value = "${adminPath}/actRoot")
public class ActRootController extends BaseController<DlfmActRoot,Integer>{

    @Autowired
    private ActRootManager actRootManager;

    @Override
    public EntityManager getEntityManager() {
        return actRootManager;
    }

    @RequestMapping("index")
    public String pageActRoot(){
        return "/modules/sys/act-root";
    }


    /**
     * 用户集合
     * @param sc_actRootName
     * @return
     */
    @RequestMapping("actRootDatagrid")
    @ResponseBody
    public Datagrid<DlfmActRoot> actRootDatagrid(String sc_actRootName){
        Page<DlfmActRoot> p = new Page<DlfmActRoot>(SpringMVCHolder.getRequest());
        p = actRootManager.getActRootByQuery(sc_actRootName,p);
        Datagrid<DlfmActRoot> dg = new Datagrid<DlfmActRoot>(p.getTotalCount(), p.getResult());
        return dg;
    }

    @RequestMapping(value = {"input"})
    public String actInput(@ModelAttribute("model") DlfmActRoot actRoot,Model model){
        if(actRoot.getId()!=null){
            model.addAttribute("actRoot",actRoot);
        }
        return "modules/sys/act-root-input";

    }

    /**
     * 新增或修改.
     *
     * @param actRoot
     * @return
     */
    @RequestMapping(value = {"bsave"})
    @ResponseBody
    public Result save(HttpServletRequest request, @RequestParam(required=false,value = "file")MultipartFile file, @ModelAttribute("model") DlfmActRoot actRoot) {
        Result result = null;
        try {
            if (request instanceof MultipartHttpServletRequest && file != null && file.getSize() > 0) {
                String xdpath = "/static/upload/acyImages/";

                String path = request.getSession().getServletContext().getRealPath(xdpath);
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile multipartFile = multipartRequest.getFile("file");
                File dirs = new File(path);
                if (!dirs.exists()) {
                    new File(path).mkdirs();
                }
                String imageName = DateUtils.getDateRandom() + ".png";
                File out = new File(path + "/" + imageName);
                actRoot.setActCoverUrl(xdpath+imageName);
                multipartFile.transferTo(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = Result.errorResult().setMsg("上传活动封面失败!");
        }
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        if(actRoot.getId()==null){
            actRoot.setCreateTime(DateUtils.getSysTimestamp());
            actRoot.setCreateUserid(sessionInfo.getUserId());
            actRoot.setStatus(StatusState.normal.getValue());
        }
        getEntityManager().saveEntity(actRoot);
        return Result.successResult();
    }
}
