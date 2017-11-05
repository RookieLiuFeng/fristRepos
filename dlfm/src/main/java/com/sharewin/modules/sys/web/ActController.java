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
import com.sharewin.modules.sys.entity.DlfmAct;
import com.sharewin.modules.sys.service.ActManager;
import com.sharewin.modules.sys.service.ActRootManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping(value = "${adminPath}/act")
public class ActController extends BaseController<DlfmAct, Integer> {

    @Autowired
    private ActManager actManager;

    @Autowired
    private ActRootManager actRootManager;

    @Override
    public EntityManager getEntityManager() {
        return actManager;
    }

    @RequestMapping("{actRootId}_index")
    public String pageAct(@PathVariable("actRootId") Integer actRootId, Model model) {
        model.addAttribute("actRoot", actRootManager.getById(actRootId));
        return "/modules/sys/act";
    }


    /**
     * 用户集合
     *
     * @param sc_actName
     * @return
     */
    @RequestMapping("{actRootId}_actDatagrid")
    @ResponseBody
    public Datagrid<DlfmAct> actDatagrid(@PathVariable("actRootId") Integer actRootId, String sc_actName) {
        Page<DlfmAct> p = new Page<DlfmAct>(SpringMVCHolder.getRequest());
        logger.info("actRoot:"+actRootId);
        p = actManager.getActByQuery(actRootId, sc_actName, p);
        Datagrid<DlfmAct> dg = new Datagrid<DlfmAct>(p.getTotalCount(), p.getResult());
        return dg;
    }

    @RequestMapping(value = {"input"})
    public String actInput(@ModelAttribute("model") DlfmAct act,Model model) {
        if(act.getId()!=null){
            model.addAttribute("act",act);
        }
        return "modules/sys/act-input";

    }

    /**
     * 新增或修改.
     *
     * @param act
     * @return
     */
    @RequestMapping(value = {"bsave/{actRootId}"})
    @ResponseBody
    public Result save(@PathVariable("actRootId") Integer actRootId, HttpServletRequest request, @RequestParam(required = false, value = "file") MultipartFile file, @ModelAttribute("model") DlfmAct act) {
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
                act.setActCoverUrl(xdpath + imageName);
                multipartFile.transferTo(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = Result.errorResult().setMsg("上传活动封面失败!");
        }
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        if (act.getId() == null) {
            act.setDlfmActRoot(actRootManager.getById(actRootId));
            act.setCreateTime(DateUtils.getSysTimestamp());
            act.setCreateUserid(sessionInfo.getUserId());
            act.setStatus(StatusState.normal.getValue());
        }
        getEntityManager().saveEntity(act);
        return Result.successResult();
    }
}
