package com.sharewin.modules.sys.web;

import com.google.common.collect.Lists;
import com.sharewin.common.model.TreeNode;
import com.sharewin.common.utils.DateUtils;
import com.sharewin.common.utils.mapper.JsonMapper;
import com.sharewin.common.web.springmvc.SimpleController;
import com.sharewin.common.web.utils.WebUtils;
import com.sharewin.core.security.SecurityUtils;
import com.sharewin.core.security.SessionInfo;
import com.sharewin.core.security.annotation.RequiresUser;
import com.sharewin.modules.sys.entity.User;
import com.sharewin.modules.sys.service.PermissionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "${adminPath}")
public class IndexController  extends SimpleController{

    @Autowired
    private PermissionManager permissionManager;

    /**
     * 欢迎页面 登录页
     * @return
     * @throws Exception
     */
    @RequiresUser(required = false)
    @RequestMapping(value = {"index/welcome", ""})
    public String welcome() throws Exception {
        return "login";
    }
    @RequestMapping(value = {"index"})
    public String index() {
       return "layout/index";
    }

    @RequestMapping("index/west")
    public ModelAndView west() {
        logger.info("loading west");
        ModelAndView modelAnView = new ModelAndView("layout/west");
        User sessionUser = SecurityUtils.getCurrentUser();
        modelAnView.addObject("user", sessionUser);
        return modelAnView;
    }
    @RequestMapping("index/north")
    public ModelAndView north(){
        ModelAndView modelAndView = new ModelAndView("layout/north");
        User sessionUser = SecurityUtils.getCurrentUser();
        modelAndView.addObject("user", sessionUser);
        modelAndView.addObject("sysdate", DateUtils.getDate("yyyy年MM月dd日"));
        return modelAndView;
    }

    /**
     * 导航菜单.
     */

    @RequestMapping(value = {"index/navTree"})
    @ResponseBody
    public List<TreeNode> navTree(HttpServletResponse response) {

        WebUtils.setNoCacheHeader(response);
        List<TreeNode> treeNodes = Lists.newArrayList();
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        if (sessionInfo != null) {
            treeNodes = permissionManager.getPermissionTreeByUserId(sessionInfo.getUserId());
            logger.info(JsonMapper.getInstance().toJson(treeNodes));
        }
        return treeNodes;
    }
}
