package com.sharewin.modules.sys.web;
import com.google.common.collect.Lists;
import com.sharewin.common.model.Datagrid;
import com.sharewin.common.model.Result;
import com.sharewin.common.orm.Page;
import com.sharewin.common.utils.collections.Collections3;
import com.sharewin.common.web.springmvc.SpringMVCHolder;
import com.sharewin.modules.sys.entity.SurveyAnswer;
import com.sharewin.modules.sys.entity.SurveyQuestion;
import com.sharewin.modules.sys.service.QuestionManager;
import com.sharewin.modules.sys.service.SurveyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "${adminPath}/question")
public class QuestionController{

    @Autowired
    private QuestionManager questionManager;
    @Autowired
    private SurveyManager surveyManager;

    @RequestMapping("{surveyid}_index")
    public  String pageQuestion(@PathVariable("surveyid") Integer surveyid, Model model){
        model.addAttribute("survey",surveyManager.getById(surveyid));
        return "modules/sys/survey-question";
    }


    /**
     *  题目集合
     * @param surveyid  问卷ID
     * @param sc_questionName 题目关键字
     * @return
     */
    @RequestMapping("{surveyid}_questionDatagrid")
    @ResponseBody
    public Datagrid<SurveyQuestion> questionDatagrid(@PathVariable("surveyid") Integer surveyid, String sc_questionName){
        Page<SurveyQuestion> p = new Page<SurveyQuestion>(SpringMVCHolder.getRequest());
        p = questionManager.getSurveyQuestionByQuery(surveyid,sc_questionName,p);
        Datagrid<SurveyQuestion> dg = new Datagrid<SurveyQuestion>(p.getTotalCount(), p.getResult());
        return dg;
    }

    /**
     * 题目添加和修改
     * @param surveyid
     * @param id
     * @param model
     * @return
     * @throws Exception
     */


    @RequestMapping(value = {"{surveyid}_input"})
    public String questionsInput(@PathVariable("surveyid") Integer surveyid, Integer id, Model model) throws Exception {

        if(id == null){
            //新增
            SurveyQuestion surveyQuestion = new SurveyQuestion();
            List<SurveyAnswer> answers = Lists.newArrayList();
            for(int i=0;i<6;i++){
                answers.add(new SurveyAnswer());
            }
            surveyQuestion.setSurveyAnswers(answers);
            model.addAttribute("question", surveyQuestion);
        }else{
            SurveyQuestion surveyQuestion = questionManager.getById(id);
            if(Collections3.isNotEmpty(surveyQuestion.getSurveyAnswers())){
                int s = 6-surveyQuestion.getSurveyAnswers().size();
                for(int i=0;i<s;i++){
                    surveyQuestion.getSurveyAnswers().add(new SurveyAnswer());
                }

            }else{
                List<SurveyAnswer> answers = Lists.newArrayList();
                for(int i=0;i<6;i++){
                    answers.add(new SurveyAnswer());
                }
                surveyQuestion.setSurveyAnswers(answers);
            }
            model.addAttribute("question", surveyQuestion);
        }
        return "modules/sys/survey-question-input";
    }

    /**
     * 保存or修改
     */
    @RequestMapping(value = {"{surveyid}_save"})
    @ResponseBody
    public Result questionsSave(@PathVariable("surveyid") Integer surveyid, @ModelAttribute("model") SurveyQuestion surveyQuestion) {

        questionManager.saveQuestion(surveyManager.getById(surveyid),surveyQuestion);
        return Result.successResult();
    }

    /**
     * 根据ID删除
     *
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = {"delete/{id}"})
    @ResponseBody
    public Result delete(@PathVariable Integer id) {
        questionManager.deleteById(id);
        return Result.successResult();
    }

}
