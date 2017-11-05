package com.sharewin.modules.sys.web;

import com.google.common.collect.Lists;
import com.sharewin.common._enum.SelectType;
import com.sharewin.common._enum.SexType;
import com.sharewin.common.excel.ExcelUtil;
import com.sharewin.common.excel.ExportExcel;
import com.sharewin.common.exception.ActionException;
import com.sharewin.common.model.Combobox;
import com.sharewin.common.model.Datagrid;
import com.sharewin.common.model.Result;
import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.utils.DateUtils;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.utils.collections.Collections3;
import com.sharewin.common.utils.encode.Encrypt;
import com.sharewin.common.utils.mapper.JsonMapper;
import com.sharewin.common.web.springmvc.BaseController;
import com.sharewin.common.web.springmvc.SpringMVCHolder;
import com.sharewin.common.web.utils.WebUtils;
import com.sharewin.modules.sys.entity.User;
import com.sharewin.modules.sys.service.UserManager;
import com.sharewin.utils.AppConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by yomg on 2017/5/16 0016.
 */
@Controller
@RequestMapping(value = "${adminPath}/user")
public class UserController extends BaseController<User, Integer> {

    @Autowired
    public UserManager userManager;

    @Override
    public EntityManager<User, Integer> getEntityManager() {
        return userManager;
    }

    @RequestMapping("index")
    public String pageUser() {
        return "/modules/sys/user";
    }


    /**
     * 用户集合
     *
     * @param realname
     * @return
     */
    @RequestMapping("userDatagrid")
    @ResponseBody
    public Datagrid<User> userDatagrid(String realname) {
        Page<User> p = new Page<User>(SpringMVCHolder.getRequest());
        p = userManager.getUserByQuery(realname, p);
        Datagrid<User> dg = new Datagrid<User>(p.getTotalCount(), p.getResult());
        logger.info(JsonMapper.getInstance().toJson(dg));
        return dg;
    }

    @RequestMapping(value = {"input"})
    public String userInput(@ModelAttribute("model") User user) throws Exception {
        return "modules/sys/user-input";
    }

    /**
     * 新增或修改.
     *
     * @param user
     * @return
     */
    @RequestMapping(value = {"save"})
    @ResponseBody
    public Result save(@ModelAttribute("model") User user) {
        if (user.getId() == null) {
            user.setPassword(Encrypt.md5(user.getPassword()));
            user.setCreateTime(DateUtils.getSysTimestamp());
        }
        logger.info("保存用户");
        getEntityManager().saveEntity(user);
        return Result.successResult();
    }


    /**
     * 修改用户密码.
     *
     * @param id           用户ID
     * @param upateOperate 需要密码"1" 不需要密码"0".
     * @param password     原始密码
     * @param newPassword  新密码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"updateUserPassword"})
    @ResponseBody
    public Result updateUserPassword(@RequestParam(value = "id", required = true) Integer id, @RequestParam(value = "upateOperate", required = true) String upateOperate, String password, @RequestParam(value = "newPassword", required = true) String newPassword) throws Exception {
        Result result;
        User u = userManager.loadById(id);
        if (u != null) {
            boolean isCheck = true;
            //需要输入原始密码
            if (AppConstants.USER_UPDATE_PASSWORD_YES.equals(upateOperate)) {
                String originalPassword = u.getPassword(); //数据库存储的原始密码
                String pagePassword = password; //页面输入的原始密码（未加密）
                if (!originalPassword.equals(Encrypt.e(pagePassword))) {
                    isCheck = false;
                }
            }
            //不需要输入原始密码
            if (AppConstants.USER_UPDATE_PASSWORD_NO.equals(upateOperate)) {
                isCheck = true;
            }
            if (isCheck) {
                u.setPassword(Encrypt.e(newPassword));
                userManager.saveEntity(u);
                result = Result.successResult();
            } else {
                result = new Result(Result.WARN, "原始密码输入错误.", "password");
            }
        } else {
            throw new ActionException("用户【" + id + "】不存在或已被删除.");
        }
        logger.debug(result.toString());
        return result;
    }

    /**
     * 性别下拉框
     *
     * @throws Exception
     */
    @RequestMapping(value = {"sexTypeCombobox"})
    @ResponseBody
    public List<Combobox> sexTypeCombobox(String selectType) throws Exception {
        List<Combobox> cList = Lists.newArrayList();
        //为combobox添加  "---全部---"、"---请选择---"
        if (!StringUtils.isBlank(selectType)) {
            SelectType s = SelectType.getSelectTypeValue(selectType);
            if (s != null) {
                Combobox selectCombobox = new Combobox("", s.getDescription());
                cList.add(selectCombobox);
            }
        }
        SexType[] _enums = SexType.values();
        for (int i = 0; i < _enums.length; i++) {
            Combobox combobox = new Combobox(_enums[i].getValue().toString(), _enums[i].getDescription());
            cList.add(combobox);
        }
        return cList;
    }

    /**
     * 导入数据页
     *
     * @return
     */
    @RequestMapping("import")
    public String importPage() {
        return "/modules/sys/user-import";
    }

    /**
     * Excel导入
     * @param file
     * @return
     */
    @RequestMapping(value = {"importExcel"})
    @ResponseBody
    public Result importExcel(@RequestParam(value = "filedata") MultipartFile file) {
        return userManager.importExcel(file);
    }

    /**
     * Excel导出
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = {"exportExcel"})
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("导出用户信息");
        // 生成提示信息，
        final String fileName = "用户信息.xls";
        OutputStream outStream = null;
        try {
            //设置文件类型
            response.setContentType(WebUtils.EXCEL_TYPE);
            //设置下载弹出对话框
            WebUtils.setDownloadableHeader(request, response, fileName);
            List<User> users =  users = userManager.getUsers();

            HSSFWorkbook workbook = new ExportExcel<User>().exportExcel("用户信息",
                    User.class, users);
            outStream = response.getOutputStream();
            workbook.write(outStream);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outStream.flush();
                outStream.close();
            } catch (IOException e) {

            }
        }
    }

}
