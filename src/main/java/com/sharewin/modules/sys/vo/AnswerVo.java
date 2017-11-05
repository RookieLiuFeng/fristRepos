package com.sharewin.modules.sys.vo;

public class AnswerVo {
    private Integer answerId;
    private String AnswerName;
    private Integer chooseNumber;
    private Double ratio;

    public AnswerVo() {
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getAnswerName() {
        return AnswerName;
    }

    public void setAnswerName(String answerName) {
        AnswerName = answerName;
    }

    public Integer getChooseNumber() {
        return chooseNumber;
    }

    public void setChooseNumber(Integer chooseNumber) {
        this.chooseNumber = chooseNumber;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "AnswerVo{" +
                "answerId=" + answerId +
                ", AnswerName='" + AnswerName + '\'' +
                ", chooseNumber=" + chooseNumber +
                ", ratio=" + ratio +
                '}';
    }
}
