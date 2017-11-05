package com.sharewin.modules.sys.web;

import com.sharewin.common.model.Datagrid;
import com.sharewin.common.model.Result;
import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.utils.DateUtils;
import com.sharewin.common.web.springmvc.BaseController;
import com.sharewin.common.web.springmvc.SpringMVCHolder;
import com.sharewin.common.web.utils.WebUtils;
import com.sharewin.core.security.SecurityUtils;
import com.sharewin.core.security.SessionInfo;
import com.sharewin.modules.sys.entity.Survey;
import com.sharewin.modules.sys.service.SurveyManager;
import com.sharewin.modules.sys.vo.QuestionVo;
import com.sharewin.modules.sys.vo.RecordVo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = "${adminPath}/survey")
public class SurveyController extends BaseController<Survey,Integer>{


    @Autowired
    private SurveyManager surveyManager;

    @Override
    public EntityManager<Survey, Integer> getEntityManager() {
        return surveyManager;
    }

    @RequestMapping("index")
    public String pageBook(){
        return "/modules/sys/survey";
    }


    /**
     * 问卷集合
     * @param sc_surveyName
     * @return
     */
    @RequestMapping("surveyDatagrid")
    @ResponseBody
    public Datagrid<Survey> surveyDatagrid(String sc_surveyName){
        Page<Survey> p = new Page<Survey>(SpringMVCHolder.getRequest());
        p = surveyManager.getSurveyByQuery(sc_surveyName,p);
        Datagrid<Survey> dg = new Datagrid<Survey>(p.getTotalCount(), p.getResult());
        return dg;
    }

    @RequestMapping(value = {"input"})
    public String surveyInput(@ModelAttribute("model") Survey survey){
        return "modules/sys/survey-input";
    }

    /**
     * 新增或修改.
     *
     * @param survey
     * @return
     */
    @Override
    @RequestMapping(value = {"save"})
    @ResponseBody
    public Result save(@ModelAttribute("model") Survey survey) {
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        if(survey.getId()==null){
            survey.setCreateTime(DateUtils.getSysTimestamp());
            survey.setCreateUserid(sessionInfo.getUserId());
            survey.setStatus(StatusState.normal.getValue());
        }
        getEntityManager().saveEntity(survey);
        return Result.successResult();
    }

    /**
     * 跳转到问卷统计页面
     *
     * @param
     * @return
     */
    @RequestMapping(value = {"/stat/{surveyid}_index"})
    public String statistics(@PathVariable("surveyid") Integer surveyid, Model model) {
        model.addAttribute("survey",surveyManager.getById(surveyid));
        return "modules/sys/survey-stat";
    }


    /**
     * 问卷统计结果
     * @param surveyid
     * @return
     */
    @RequestMapping(value = {"/stat/question"})
    @ResponseBody
    public Result getQuestionList(@RequestParam Integer surveyid){
        List<QuestionVo> questionVoList = surveyManager.getQuestionList(surveyid);
        Result result=Result.successResult();
        result.setObj(questionVoList);
        return result;
    }

    /**
     * Excel导出问卷结果
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = {"/stat/exportExcelForSurvey"})
    public void exportExcel(HttpServletRequest request, HttpServletResponse response,Integer surveyid) throws Exception {
        logger.info("导出用户信息");
        // 生成提示信息，
        logger.info(surveyid.toString()+"*******************************************************");
        Survey survey = surveyManager.getById(surveyid);
        List<QuestionVo> questionVoList = surveyManager.getQuestionList(surveyid);

        final String fileName = "问卷结果-"+survey.getSurveyName()+".xls";
        OutputStream outStream = null;

        try {
            //设置文件类型
            response.setContentType(WebUtils.EXCEL_TYPE);
            //设置下载弹出对话框
            WebUtils.setDownloadableHeader(request, response, fileName);

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(survey.getSurveyName());
            HSSFRow row = null;
            HSSFCell cell = null;
            int rowNum = 0;
            for (int i=0;i<questionVoList.size()-1;i++) {
                rowNum++;
                row = sheet.createRow(rowNum);
                cell = row.createCell(0);
                cell.setCellValue("第"+(i+1)+"题:"+questionVoList.get(i).getQuestionName()+"?["+questionVoList.get(i).getQuestionType()+"]");
                sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,0,3));
                rowNum++;
                row = sheet.createRow(rowNum);
                cell = row.createCell(0);
                cell.setCellValue("选项");
                cell = row.createCell(1);
                cell.setCellValue("小计");
                cell = row.createCell(2);
                cell.setCellValue("比例");
                rowNum++;
                logger.info(Integer.valueOf(i).toString());
                for (int j=0;j<questionVoList.get(i).getAnswerVoList().size();j++){
                    row = sheet.createRow(rowNum);
                    cell = row.createCell(0);
                    cell.setCellValue(questionVoList.get(i).getAnswerVoList().get(j).getAnswerName());
                    cell = row.createCell(1);
                    cell.setCellValue(questionVoList.get(i).getAnswerVoList().get(j).getChooseNumber());
                    cell = row.createCell(2);
                    cell.setCellValue(questionVoList.get(i).getAnswerVoList().get(j).getRatio()+"%");
                    rowNum++;
                }
                row = sheet.createRow(rowNum);
                cell = row.createCell(0);
                cell.setCellValue("本题有效填写人次");
                cell = row.createCell(1);
                cell.setCellValue(survey.getSurveyCount());
                cell = row.createCell(2);
                cell.setCellValue("");
                rowNum++;
            }

            sheet.autoSizeColumn((short)0);
            sheet.autoSizeColumn((short)1);
            sheet.autoSizeColumn((short)2);

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
                e.printStackTrace();
            }
        }
    }

    /**
     * 跳转到参与者名单页面
     *
     * @param
     * @return
     */
    @RequestMapping(value = {"/record/{surveyid}_index"})
    public String recordPage(@PathVariable("surveyid") Integer surveyid, Model model) {
        model.addAttribute("survey",surveyManager.getById(surveyid));
        return "modules/sys/survey-record";
    }

    /**
     * 获取参与问卷者信息
     * @param surveyId
     * @param name
     * @return
     */
    @RequestMapping(value = {"/record/datagrid"})
    @ResponseBody
    public Datagrid<RecordVo> getResult(@RequestParam int surveyId,@RequestParam String name) {
        logger.info(surveyId + "###" + name + "###");
        Page<RecordVo> p = new Page<RecordVo>(SpringMVCHolder.getRequest());
        p = surveyManager.getResult(surveyId,name,p);
        Datagrid<RecordVo> dg = new Datagrid<RecordVo>(p.getTotalCount(), p.getResult());
        return dg;
    }

    /**
     * 跳转到参与者问卷调研明细
     * @param recordid
     * @param model
     * @return
     */
    @RequestMapping(value = {"/record/detail/{recordid}_index/{surveyid}"})
    public String recordDetailPage(@PathVariable("recordid") Integer recordid, @PathVariable("surveyid") Integer surveyid, Model model) {
        model.addAttribute("recordid",recordid);
        model.addAttribute("survey", surveyManager.getById(surveyid));
        model.addAttribute("recordDetail", surveyManager.getRecordDetail(recordid));
        logger.info(surveyManager.getById(surveyid).toString());
        return "modules/sys/survey_record_detail";
    }

    /**
     * 获取参与者问卷调研明细
     * @param recordid
     * @return
     */
    @RequestMapping(value = {"/record/detail"})
    @ResponseBody
    public Result recordDetail(@RequestParam Integer recordid) {
        Result result = Result.successResult();
        result.setObj(surveyManager.getRecordDetail(recordid));
        return result;
    }

    /**
     * Excel导出参与者名单
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = {"/record/exportExcel"})
    public void exportExcelForRecord(HttpServletRequest request, HttpServletResponse response,Integer surveyid) throws Exception {
        logger.info("导出参与者名单");
        // 生成提示信息，
        logger.info(surveyid.toString()+"*******************************************************");
        Survey survey = surveyManager.getById(surveyid);
        List<Map<String, Object>> mapList = surveyManager.getResult(surveyid);

        final String fileName = "参与者名单-"+survey.getSurveyName()+".xls";
        OutputStream outStream = null;

        try {
            //设置文件类型
            response.setContentType(WebUtils.EXCEL_TYPE);
            //设置下载弹出对话框
            WebUtils.setDownloadableHeader(request, response, fileName);

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(survey.getSurveyName());
            HSSFRow row = null;
            HSSFCell cell = null;
            int rowNum = 0;
            row = sheet.createRow(rowNum);//表头行
            String[] strArr = new String[]{"序号","姓名","身份","性别","完成时间","是否有效","成绩","完成度"};
            for (int i=0;i<strArr.length;i++) {
                cell = row.createCell(i);
                cell.setCellValue(strArr[i]);
            }
            rowNum++;

            for (int i=0;i<mapList.size();i++) {
                row = sheet.createRow(rowNum);//内容行
                int j = 0;
                Map<String, Object> map = mapList.get(i);
                cell = row.createCell(0);
                cell.setCellValue(i+1);
                cell = row.createCell(1);
                cell.setCellValue((String) map.get("name"));
                cell = row.createCell(2);
                cell.setCellValue((String) map.get("userType"));
                cell = row.createCell(3);
                cell.setCellValue((String) map.get("sex"));
                cell = row.createCell(4);
                cell.setCellValue((String) map.get("finishTime"));
                cell = row.createCell(5);
                cell.setCellValue((String) map.get("statu"));
                cell = row.createCell(6);
                cell.setCellValue((String) map.get("score"));
                cell = row.createCell(7);
                cell.setCellValue((String) map.get("completeness"));
                rowNum++;
            }

            sheet.autoSizeColumn((short)0);
            sheet.autoSizeColumn((short)1);
            sheet.autoSizeColumn((short)2);
            sheet.autoSizeColumn((short)3);
            sheet.autoSizeColumn((short)4);
            sheet.autoSizeColumn((short)5);
            sheet.autoSizeColumn((short)6);
            sheet.autoSizeColumn((short)7);

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
                e.printStackTrace();
            }
        }
    }
}
