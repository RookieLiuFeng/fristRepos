package com.sharewin.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.sharewin.common.orm.PropertyType;
import com.sharewin.common.orm.annotation.Delete;
import com.sharewin.common.utils.ConvertUtils;
import com.sharewin.common.utils.collections.Collections3;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by yomg on 2017/5/21 0021.
 */
@Entity
@Delete(propertyName = "status", type = PropertyType.I)
@Table(name = "survey_question", schema = "dlfm")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"
        , "surveyAnswers", "survey"})
public class SurveyQuestion {
    private Integer id;
    private String questionName;
    private Integer questionType;
    private Integer status;
    private Survey survey;
    private Integer questionOrder;

    private List<SurveyAnswer> surveyAnswers = Lists.newArrayList();

    private String answerNames;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "question_name")
    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    @Basic
    @Column(name = "question_type")
    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "surveyQuestion")
    public List<SurveyAnswer> getSurveyAnswers() {
        return surveyAnswers;
    }

    public void setSurveyAnswers(List<SurveyAnswer> surveyAnswers) {
        this.surveyAnswers = surveyAnswers;
    }

    @Basic
    @Column(name = "question_order")
    public Integer getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(Integer questionOrder) {
        this.questionOrder = questionOrder;
    }


    @Transient
    public String getQtypeView() {
        String typeView = "未知";
        if (questionType != null) {
            if (questionType == 0) {
                typeView = "单选";
            } else if (questionType == 1) {
                typeView = "多选";
            }
        }

        return typeView;
    }

    @Transient
    public String getAnswerNames() {
        if (Collections3.isNotEmpty(surveyAnswers)) {
            answerNames = ConvertUtils.convertElementPropertyToString(surveyAnswers, "answerName", "  |  ");
        }
        return answerNames;
    }

    public void setAnswerNames(String answerNames) {
        this.answerNames = answerNames;
    }
}
