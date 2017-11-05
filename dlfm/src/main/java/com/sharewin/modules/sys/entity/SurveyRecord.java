package com.sharewin.modules.sys.entity;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by yomg on 2017/6/7 0007.
 */
@Entity
@Table(name = "survey_record", schema = "dlfm")
public class SurveyRecord {
    private Integer id;
    private Integer surveyId;
    private Integer userId;
    private Timestamp surveyTime;
    private Integer status;
    private Integer score;

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
    @Column(name = "survey_id")
    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "survey_time")
    public Timestamp getSurveyTime() {
        return surveyTime;
    }

    public void setSurveyTime(Timestamp surveyTime) {
        this.surveyTime = surveyTime;
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
    @Column(name = "score")
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyRecord that = (SurveyRecord) o;

        if (id != that.id) return false;
        if (surveyId != null ? !surveyId.equals(that.surveyId) : that.surveyId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (surveyTime != null ? !surveyTime.equals(that.surveyTime) : that.surveyTime != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (score != null ? !score.equals(that.score) : that.score != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (surveyId != null ? surveyId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (surveyTime != null ? surveyTime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}
