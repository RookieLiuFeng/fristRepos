package com.sharewin.modules.sys.web;

import com.google.common.collect.Lists;
import com.sharewin.common._enum.SelectType;
import com.sharewin.common.model.Combobox;
import com.sharewin.common.model.Datagrid;
import com.sharewin.common.model.Result;
import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.utils.DateUtils;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.web.springmvc.BaseController;
import com.sharewin.common.web.springmvc.SpringMVCHolder;
import com.sharewin.core.security.SecurityUtils;
import com.sharewin.core.security.SessionInfo;
import com.sharewin.modules.sys.entity.DlfmBookPeriod;
import com.sharewin.modules.sys.service.BookPeriodManager;
import com.sharewin.modules.sys.service.BookRecommendManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping(value = "${adminPath}/bookRecommend")
public class BookRecommendController extends BaseController<DlfmBookPeriod,Integer>{

    @Autowired
    private BookRecommendManager bookRecommendManager;

    @Override
    public EntityManager getEntityManager() {
        return bookRecommendManager;
    }

    @RequestMapping("index")
    public String bookRecommendPage(){
        return "/modules/sys/book-recommend";
    }

    /**
     * 好书推荐 集合
     * @param bookRecommendName
     * @param publishType
     * @return
     */
    @RequestMapping(value = {"bookRecommendDatagrid"})
    @ResponseBody
    public Datagrid<DlfmBookPeriod> bookRecommendDatagrid(String bookRecommendName,String publishType){
        Page<DlfmBookPeriod> p = new Page<DlfmBookPeriod>(SpringMVCHolder.getRequest());
        p = bookRecommendManager.getBookRecommendByQuery(bookRecommendName,publishType,p);
        Datagrid<DlfmBookPeriod> dg = new Datagrid<DlfmBookPeriod>(p.getTotalCount(), p.getResult());
        return dg;
    }

    @RequestMapping(value = {"input"})
    public String bookRecommendInput(@ModelAttribute("model") DlfmBookPeriod bookPeriod, Model model){
        return "modules/sys/book-recommend-input";
    }

    /**
     * 新增或修改.
     *
     * @param bookPeriod
     * @return
     */
    @RequestMapping(value = {"save"})
    @ResponseBody
    public Result save(@ModelAttribute("model") DlfmBookPeriod bookPeriod) {
        Result result = null;

        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        if(bookPeriod.getId()==null){
            bookPeriod.setCreateTime(DateUtils.getSysTimestamp());
            bookPeriod.setCreateUserid(sessionInfo.getUserId());
            bookPeriod.setStatus(StatusState.normal.getValue());
        }
        getEntityManager().saveEntity(bookPeriod);
        return Result.successResult();
    }

    @RequestMapping(value = {"publish/{id}"})
    @ResponseBody
    public Result publish(@PathVariable Integer id) {
        return bookRecommendManager.publish(id);
    }

    /**
     * 发布类型下拉框
     *
     * @throws Exception
     */
    @RequestMapping(value = {"publishTypeCombobox"})
    @ResponseBody
    public List<Combobox> publishTypeCombobox(String selectType) throws Exception {
        List<Combobox> cList = Lists.newArrayList();
        //为combobox添加  "---全部---"、"---请选择---"
        if (!StringUtils.isBlank(selectType)) {
            SelectType s = SelectType.getSelectTypeValue(selectType);
            if (s != null) {
                Combobox selectCombobox = new Combobox("", s.getDescription());
                cList.add(selectCombobox);
            }
        }
        Combobox combobox = new Combobox("1", "已发布");
        cList.add(combobox);
        Combobox combobox2 = new Combobox("0", "未发布");
        cList.add(combobox2);
        return cList;
    }
}
