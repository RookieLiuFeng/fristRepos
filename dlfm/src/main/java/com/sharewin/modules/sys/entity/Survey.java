package com.sharewin.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.sharewin.common.orm.PropertyType;
import com.sharewin.common.orm.annotation.Delete;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by yomg on 2017/5/21 0021.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Delete(propertyName = "status",type = PropertyType.I)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler"
        ,"surveyQuestions"})
public class Survey {
    private Integer id;
    private String surveyName;
    private String surveyDesc;
    private Timestamp surveyStarttm;
    private Timestamp surveyEndtm;
    private Timestamp createTime;
    private Integer createUserid;
    private Integer status;
    private Integer surveyCount;
    private Integer surveyType;

    private String surveyTip;

    private List<SurveyQuestion> surveyQuestions = Lists.newArrayList();

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
    @Column(name = "survey_name")
    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    @Basic
    @Column(name = "survey_desc")
    public String getSurveyDesc() {
        return surveyDesc;
    }

    public void setSurveyDesc(String surveyDesc) {
        this.surveyDesc = surveyDesc;
    }

    @Basic
    @Column(name = "survey_starttm")
    public Timestamp getSurveyStarttm() {
        return surveyStarttm;
    }

    public void setSurveyStarttm(Timestamp surveyStarttm) {
        this.surveyStarttm = surveyStarttm;
    }

    @Basic
    @Column(name = "survey_endtm")
    public Timestamp getSurveyEndtm() {
        return surveyEndtm;
    }

    public void setSurveyEndtm(Timestamp surveyEndtm) {
        this.surveyEndtm = surveyEndtm;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "create_userid")
    public Integer getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(Integer createUserid) {
        this.createUserid = createUserid;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "survey_count")
    public Integer getSurveyCount() {
        return surveyCount;
    }

    public void setSurveyCount(Integer surveyCount) {
        this.surveyCount = surveyCount;
    }

    @Basic
    @Column(name = "survey_type")
    public Integer getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(Integer surveyType) {
        this.surveyType = surveyType;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "survey")
    @Where(clause = "status=1")
    public List<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
    }

    @Transient
    public int getQuestionCount(){
        if(surveyQuestions!=null){
            return surveyQuestions.size();
        }
        return 0;
    }

    @Basic
    @Column(name = "survey_tip")
    public String getSurveyTip() {
        return surveyTip;
    }

    public void setSurveyTip(String surveyTip) {
        this.surveyTip = surveyTip;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "id=" + id +
                ", surveyName='" + surveyName + '\'' +
                ", surveyDesc='" + surveyDesc + '\'' +
                ", surveyStarttm=" + surveyStarttm +
                ", surveyEndtm=" + surveyEndtm +
                ", createTime=" + createTime +
                ", createUserid=" + createUserid +
                ", status=" + status +
                ", surveyCount=" + surveyCount +
                ", surveyType=" + surveyType +
                ", surveyTip='" + surveyTip + '\'' +
                ", surveyQuestions=" + surveyQuestions +
                '}';
    }
}
