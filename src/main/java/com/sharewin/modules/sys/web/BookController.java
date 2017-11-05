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
import com.sharewin.modules.sys.entity.DlfmBookPeriod;
import com.sharewin.modules.sys.service.BookManager;
import com.sharewin.modules.sys.service.BookPeriodManager;
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
@RequestMapping(value = "${adminPath}/book")
public class BookController extends BaseController<DlfmBook,Integer>{

    @Autowired
    private BookManager  bookManager;

    @Autowired
    private BookPeriodManager bookPeriodManager;

    @Override
    public EntityManager getEntityManager() {
        return bookManager;
    }

    @RequestMapping("{bookPeriodId}_index")
    public String pageBook(@PathVariable("bookPeriodId") Integer bookPeriodId,Model model){
        model.addAttribute("bookPeriod",bookPeriodManager.getById(bookPeriodId));
        return "/modules/sys/book";
    }


    /**
     * 书籍集合
     * @param sc_bookName
     * @return
     */
    @RequestMapping("{bookPeriodId}_bookDatagrid")
    @ResponseBody
    public Datagrid<DlfmBook> bookDatagrid(@PathVariable("bookPeriodId") Integer bookPeriodId,String sc_bookName){
        Page<DlfmBook> p = new Page<DlfmBook>(SpringMVCHolder.getRequest());
        p = bookManager.getBookByQuery(bookPeriodId,sc_bookName,p);
        Datagrid<DlfmBook> dg = new Datagrid<DlfmBook>(p.getTotalCount(), p.getResult());
        return dg;
    }

    @RequestMapping(value = {"input"})
    public String bookInput(@ModelAttribute("model") DlfmBook book, Model model){
        if(book.getId()!=null){
            model.addAttribute("book",book);
        }
        return "modules/sys/book-input";

    }

    /**
     * 新增或修改.
     *
     * @param book
     * @return
     */
    @RequestMapping(value = {"bsave/{bookPeriodId}"})
    @ResponseBody
    public Result save(HttpServletRequest request, @PathVariable("bookPeriodId") Integer bookPeriodId,@RequestParam(required=false,value = "file")MultipartFile file, @ModelAttribute("model") DlfmBook book) {
        Result result = null;
        try {
            if (request instanceof MultipartHttpServletRequest && file != null && file.getSize() > 0) {
                String xdpath = "/static/upload/bookImages/";

                String path = request.getSession().getServletContext().getRealPath(xdpath);
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultipartFile multipartFile = multipartRequest.getFile("file");
                File dirs = new File(path);
                if (!dirs.exists()) {
                    new File(path).mkdirs();
                }
                String imageName = DateUtils.getDateRandom() + ".png";
                File out = new File(path + "/" + imageName);
                book.setBookCoverUrl(xdpath+imageName);
                multipartFile.transferTo(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = Result.errorResult().setMsg("上传书籍封面失败!");
        }
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        if(book.getId()==null){
            DlfmBookPeriod  dlfmBookPeriod = bookPeriodManager.getById(bookPeriodId);
            book.setDlfmBookPeriod(dlfmBookPeriod);
            book.setCreateTime(DateUtils.getSysTimestamp());
            book.setCreateUserid(sessionInfo.getUserId());
            book.setStatus(StatusState.normal.getValue());
            book.setIstj(dlfmBookPeriod.getPeriodType());
        }
        getEntityManager().saveEntity(book);
        return Result.successResult();
    }
}
