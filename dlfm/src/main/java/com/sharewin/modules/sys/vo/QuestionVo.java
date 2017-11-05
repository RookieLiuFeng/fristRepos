package com.sharewin.modules.sys.vo;

import java.util.List;

public class QuestionVo {
    private Integer questionId;
    private String questionName;
    private String questionType;
    private List<AnswerVo> answerVoList;

    public QuestionVo() {
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<AnswerVo> getAnswerVoList() {
        return answerVoList;
    }

    public void setAnswerVoList(List<AnswerVo> answerVoList) {
        this.answerVoList = answerVoList;
    }

    @Override
    public String toString() {
        return "QuestionVo{" +
                "questionId=" + questionId +
                ", questionName='" + questionName + '\'' +
                ", questionType='" + questionType + '\'' +
                ", answerVoList=" + answerVoList +
                '}';
    }
}
