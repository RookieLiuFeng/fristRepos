package com.sharewin.modules.sys.service;

import com.google.common.collect.Lists;
import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.orm.hibernate.HibernateDao;
import com.sharewin.common.orm.hibernate.Parameter;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.modules.sys.entity.Survey;
import com.sharewin.modules.sys.entity.SurveyAnswer;
import com.sharewin.modules.sys.entity.SurveyQuestion;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionManager extends EntityManager<SurveyQuestion, Integer> {

    private HibernateDao<SurveyQuestion, Integer> surveyQuestionDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        surveyQuestionDao = new HibernateDao<SurveyQuestion, Integer>(sessionFactory,
                SurveyQuestion.class);
    }

    @Override
    protected HibernateDao getEntityDao() {
        return surveyQuestionDao;
    }


    /**
     * 数据页
     *
     * @param questionName
     * @param page
     * @return
     */
    public Page<SurveyQuestion> getSurveyQuestionByQuery(Integer surveyid,String questionName, Page<SurveyQuestion> page) {
        Parameter parameter = new Parameter();
        StringBuilder hql = new StringBuilder();
        hql.append("select u from SurveyQuestion u where u.survey.id=:surveyid and u.status=:status ");
        parameter.put("status", StatusState.normal.getValue());
        parameter.put("surveyid",surveyid);
        if (StringUtils.isNotBlank(questionName)) {
            hql.append("and u.questionName like :questionName");
            parameter.put("questionName", "%" + questionName + "%");
        }
        //设置分页
        page = surveyQuestionDao.findPage(page, hql.toString(), parameter);
        return page;
    }

    @Transactional(readOnly = false)
    public void saveQuestion(Survey survey, SurveyQuestion surveyQuestion) {
        surveyQuestion.setStatus(1);
        List<SurveyAnswer> oList = Lists.newArrayList();
        if(surveyQuestion.getSurveyAnswers()!=null){
            for(SurveyAnswer answer : surveyQuestion.getSurveyAnswers()){
                if(StringUtils.isNotBlank(answer.getAnswerName())){
                    answer.setStatus(1);
                    answer.setSurveyQuestion(surveyQuestion);
                    oList.add(answer);
                }else{
                    //删除内容为空的答案
                    if(answer.getId()!=null){
                        getEntityDao().batchExecute("delete from SurveyAnswer where id=?", answer.getId());
                    }
                }
            }
        }
        surveyQuestion.setSurvey(survey);
        surveyQuestion.setSurveyAnswers(oList);
        this.saveOrUpdate(surveyQuestion);
    }


}
