package com.sharewin.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by yomg on 2017/5/21 0021.
 */
@Entity
@Table(name = "survey_answer", schema = "dlfm")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"
        , "surveyQuestion"})
public class SurveyAnswer {
    private Integer id;
    private String answerName;
    private Integer score;
    private Integer status;

    private SurveyQuestion surveyQuestion;

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
    @Column(name = "answer_name")
    public String getAnswerName() {
        return answerName;
    }

    public void setAnswerName(String answerName) {
        this.answerName = answerName;
    }

    @Basic
    @Column(name = "score")
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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
    @JoinColumn(name = "question_id")
    public SurveyQuestion getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    @Override
    public String toString() {
        return "SurveyAnswer{" +
                "id=" + id +
                ", answerName='" + answerName + '\'' +
                ", score=" + score +
                ", status=" + status +
//                ", surveyQuestion=" + surveyQuestion +
                '}';
    }
}
