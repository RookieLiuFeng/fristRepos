package com.sharewin.modules.sys.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.orm.hibernate.HibernateDao;
import com.sharewin.common.orm.hibernate.Parameter;
import com.sharewin.common.utils.DateUtils;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.utils.collections.Collections3;
import com.sharewin.common.utils.mapper.JsonMapper;
import com.sharewin.modules.sys.entity.Survey;
import com.sharewin.modules.sys.entity.SurveyRecord;
import com.sharewin.modules.sys.entity.SurveyRecordDetail;
import com.sharewin.modules.sys.vo.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class SurveyManager extends EntityManager<Survey, Integer> {

    private HibernateDao<Survey, Integer> surveyDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        surveyDao = new HibernateDao<Survey, Integer>(sessionFactory,
                Survey.class);
    }


    @Override
    protected HibernateDao getEntityDao() {
        return surveyDao;
    }


    /**
     * 数据页
     *
     * @param surveyName
     * @param page
     * @return
     */
    public Page<Survey> getSurveyByQuery(String surveyName, Page<Survey> page) {
        Parameter parameter = new Parameter();
        StringBuilder hql = new StringBuilder();
        hql.append("select u from Survey u where u.status=:status ");
        parameter.put("status", StatusState.normal.getValue());
        if (StringUtils.isNotBlank(surveyName)) {
            hql.append("and u.surveyName like :surveyName");
            parameter.put("surveyName", "%" + surveyName + "%");
        }
        //设置分页
        page = surveyDao.findPage(page, hql.toString(), parameter);
        return page;
    }


    /**
     * ======================================================================
     * 微网页
     * ======================================================================
     */


    public List<Survey> getWxSurveyList(int userid) {
        Parameter parameter = new Parameter();
        parameter.put("curTime",DateUtils.getSysTimestamp());
        String hql = "select u from Survey u where u.status=1 and (u.surveyType=(select a.userType as surveytype from User a where a.status=:status and a.id=:userid) or u.surveyType=-1) and u.surveyStarttm<=:curTime and u.surveyEndtm>=:curTime order by u.createTime desc";
        return getEntityDao().find(hql,parameter);
    }

    public int checkIsSurvey(int surveyId, int userid) {
        Parameter parameter = new Parameter();
        parameter.put("surveyId", surveyId);
        parameter.put("userId", userid);
        Object o = getEntityDao().getByHql("select count(*) from SurveyRecord u where u.status=1 and u.userId=:userId and u.surveyId = :surveyId", parameter);
        return Integer.parseInt(o.toString());
    }

    @Transactional(readOnly = false)
    public void addSurveyRecord(int surveyId, int userid, String jsonString) throws IOException {
        SurveyRecord surveyRecord = new SurveyRecord();
        surveyRecord.setSurveyId(surveyId);
        surveyRecord.setUserId(userid);
        surveyRecord.setStatus(1);
        surveyRecord.setSurveyTime(DateUtils.getSysTimestamp());
        getEntityDao().saveEntity(surveyRecord);
        // logger.info("记录ID："+surveyRecord.getId());

        List<SurveyResult> list = JsonMapper.getInstance().readValue(jsonString, new TypeReference<List<SurveyResult>>() {
        });
        for (int i = 0; i < list.size(); i++) {
            SurveyRecordDetail surveyRecordDetail = new SurveyRecordDetail();
            surveyRecordDetail.setRecordId(surveyRecord.getId());
            surveyRecordDetail.setQuestionId(list.get(i).getQid());
            surveyRecordDetail.setAnswerId(list.get(i).getAid());
            getEntityDao().saveEntity(surveyRecordDetail);
        }

        //更新问卷次数
        Parameter parameter = new Parameter();
        parameter.put("surveyId", surveyId);
        String sql = "update survey u set u.survey_count = (select count(*)+1 from survey_record where survey_id=:surveyId and status=1) where id=:surveyId";
        getEntityDao().updateBySql(sql, parameter);
    }

    /**
     * ======================================================================
     *                               问卷统计
     * ======================================================================
     */

    /**
     * 通过问卷id获取问题列表
     *
     * @param surveyId 问卷id
     * @return
     */
    public List<QuestionVo> getQuestionList(Integer surveyId) {
        StringBuilder sql = new StringBuilder();
        Parameter parameter = new Parameter();
        sql.append("SELECT sq.`id` AS questionId,sq.`question_name` AS questionName,CASE sq.`question_type` WHEN 0 THEN '单选' ELSE '多选' END AS questionType " +
                "FROM survey s INNER JOIN survey_question sq ON s.`id`=sq.`survey_id` WHERE s.`status`=1 AND sq.`status`=1 AND s.`id`=:surveyId ORDER BY sq.`question_order`");
        parameter.put("surveyId", surveyId);
        List<QuestionVo> questionVoList = surveyDao.findBySqlForVoWithExcludeProperties(sql.toString(), parameter, QuestionVo.class, new String[]{"answerVoList"});

        sql.delete(0, sql.length());
        parameter.clear();
        sql.append("SELECT s.`survey_count` FROM survey s WHERE s.`status`=1 AND s.`id`=:surveyId");
        parameter.put("surveyId", surveyId);
        Integer surveyCount = (Integer) surveyDao.findBySql(sql.toString(), parameter).get(0);

        for (QuestionVo questionVo : questionVoList) {
            List<AnswerVo> answerVoList = getAnswerList(questionVo.getQuestionId(), surveyCount);
            questionVo.setAnswerVoList(answerVoList);
        }
        return questionVoList;
    }

    /**
     * 通过问题id获取答案列表
     *
     * @param questionId
     * @return
     */
    public List<AnswerVo> getAnswerList(Integer questionId, Integer surveyCount) {
        StringBuilder sql = new StringBuilder();
        Parameter parameter = new Parameter();
        sql.append("SELECT sa.`id` AS answerId,sa.`answer_name` AS answerName FROM survey_question sq INNER JOIN survey_answer sa ON sq.`id`=sa.`question_id` " +
                "  WHERE sq.`status`=1 AND sa.`status`=1 AND sq.`id`=:questionId ");
        parameter.put("questionId", questionId);
        List<AnswerVo> answerVoList = surveyDao.findBySqlForVoWithExcludeProperties(sql.toString(), parameter, AnswerVo.class, new String[]{"chooseNumber", "ratio"});

        sql.delete(0, sql.length());
        parameter.clear();
        sql.append("SELECT srd.`id` AS recordDetailId,srd.`answer_id` AS answerIds FROM survey_record_detail srd INNER JOIN survey_record sr ON srd.`record_id`=sr.`id` " +
                " WHERE sr.`status`=1 AND srd.`question_id`=:questionId");
        parameter.put("questionId", questionId);
        List<RecordDetailVo> recordDetailVoList = surveyDao.findBySqlForVo(sql.toString(), parameter, RecordDetailVo.class);

        for (AnswerVo answerVo : answerVoList) {
            int answerCount = 0;
            for (RecordDetailVo recordDetailVo : recordDetailVoList) {
                String[] answerIds = recordDetailVo.getAnswerIds().split(",");
                for (String answerId : answerIds) {
                    if (answerId.equals(answerVo.getAnswerId().toString())) {
                        answerCount++;
                    }
                }
            }
            Double ratio = Double.valueOf(answerCount * 100.0f / surveyCount);
            answerVo.setChooseNumber(answerCount);
            BigDecimal b = new BigDecimal(ratio);
            Double mRatio = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            logger.info(mRatio+"");
            answerVo.setRatio(mRatio);
        }
        return answerVoList;
    }

    /**
     * 获取参与者名单
     * @param surveyId
     * @return
     */
    public Page<RecordVo> getResult(int surveyId,String name,Page<RecordVo> page) {
        logger.info(name);
        Parameter parameter = new Parameter();
        StringBuilder sql = new StringBuilder();
        sql.append("select z.id as id,z.realname as name,z.userType,z.sex,z.finishTime,z.statu,z.score,z.completeness from ( " +
                "  SELECT b.`id` as id,a.`realname`,CASE a.`user_type` WHEN 0 THEN '学生' WHEN 1 THEN '老师' ELSE '家长' END AS userType,\n" +
                "  CASE a.`sex`  WHEN 0 THEN '女' WHEN 1 THEN '男' ELSE ' ' END AS sex,DATE_FORMAT(b.`survey_time`,'%Y-%m-%d %H:%i:%s') AS finishTime,\n" +
                "  CASE b.`status` WHEN 1 THEN '有' ELSE '无' END AS statu,IFNULL(b.`score`,' ') AS score,\n" +
                "  CONCAT(CAST((COUNT(DISTINCT d.`question_id`) / (SELECT COUNT(1) FROM survey_question e WHERE e.`survey_id`=c.`id` AND e.`status`=1)) AS DECIMAL(5,2)) * 100,'%') AS completeness\n" +
                "FROM USER a \n" +
                "INNER JOIN survey_record b ON a.`id`=b.`user_id`\n" +
                "INNER JOIN survey_record_detail d ON b.`id`=d.`record_id`\n" +
                "INNER JOIN survey c ON b.`survey_id`=c.`id` AND a.`user_type`=c.`survey_type`\n" +
                "WHERE a.`status`=1 AND b.`status`=1 AND c.`id`=:surveyId AND d.`answer_id` IS NOT NULL\n");
        if (name != null) {
            sql.append(" and a.`realname` like '%" + name + "%'");
        }
        sql.append(" GROUP BY a.`realname`,a.`user_type`,a.`sex`,b.`survey_time`,b.`status`,b.`score`,b.`id`) z ORDER BY z.id \n");
        parameter.put("surveyId",surveyId);
        Page<RecordVo> mapList = surveyDao.findBySqlForVo(page,sql.toString(),parameter,RecordVo.class);
        logger.info("222");
        if (mapList != null) {
            return mapList;
        }
        return null;
    }

    /**
     * 获取参与者名单 (List)
     * @param surveyId
     * @return
     */
    public List<Map<String,Object>> getResult(int surveyId) {
        Parameter parameter = new Parameter();
        StringBuilder sql = new StringBuilder();
        sql.append("select z.realname as name,z.userType,z.sex,z.finishTime,z.statu,z.score,z.completeness from ( " +
                "  SELECT b.`id` as id,a.`realname`,CASE a.`user_type` WHEN 0 THEN '学生' WHEN 1 THEN '老师' ELSE '家长' END AS userType,\n" +
                "  CASE a.`sex`  WHEN 0 THEN '女' WHEN 1 THEN '男' ELSE ' ' END AS sex,DATE_FORMAT(b.`survey_time`,'%Y-%m-%d %H:%i:%s') AS finishTime,\n" +
                "  CASE b.`status` WHEN 1 THEN '有' ELSE '无' END AS statu,IFNULL(b.`score`,' ') AS score,\n" +
                "  CONCAT(CAST((COUNT(DISTINCT d.`question_id`) / (SELECT COUNT(1) FROM survey_question e WHERE e.`survey_id`=c.`id` AND e.`status`=1)) AS DECIMAL(5,2)) * 100,'%') AS completeness\n" +
                "FROM USER a \n" +
                "INNER JOIN survey_record b ON a.`id`=b.`user_id`\n" +
                "INNER JOIN survey_record_detail d ON b.`id`=d.`record_id`\n" +
                "INNER JOIN survey c ON b.`survey_id`=c.`id` AND a.`user_type`=c.`survey_type`\n" +
                "WHERE a.`status`=1 AND b.`status`=1 AND c.`id`=:surveyId AND d.`answer_id` IS NOT NULL\n");
        sql.append(" GROUP BY a.`realname`,a.`user_type`,a.`sex`,b.`survey_time`,b.`status`,b.`score`,b.`id`) z ORDER BY z.id \n");
        parameter.put("surveyId",surveyId);
        List<Map<String, Object>> mapList = surveyDao.findBySql(sql.toString(),parameter,Map.class);
        logger.info("222");
        if (mapList != null) {
            return mapList;
        }
        return null;
    }

    /**
     * 获取答卷明细
     * @param recordid
     * @return
     */
    public List<Map<String,Object>> getRecordDetail(Integer recordid) {
        Parameter parameter = new Parameter();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.`answer_id` as answerid FROM survey_record_detail a INNER JOIN survey_record b ON a.`record_id`=b.`id`  WHERE b.`status`=1 AND b.`id`=:recordid");
        parameter.put("recordid",recordid);
        List<Map<String,Object>> answerList = surveyDao.findBySql(sql.toString(),parameter,Map.class);
        if (Collections3.isNotEmpty(answerList)) {
            return answerList;
        }
        return null;
    }

}
