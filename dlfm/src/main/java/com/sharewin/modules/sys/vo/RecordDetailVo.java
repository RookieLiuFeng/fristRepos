package com.sharewin.modules.sys.vo;

public class RecordDetailVo {
    private Integer recordDetailId;
    private String answerIds;

    public RecordDetailVo() {
    }

    public Integer getRecordDetailId() {
        return recordDetailId;
    }

    public void setRecordDetailId(Integer recordDetailId) {
        this.recordDetailId = recordDetailId;
    }

    public String getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(String answerIds) {
        this.answerIds = answerIds;
    }

    @Override
    public String toString() {
        return "RecordDetailVo{" +
                "recordDetailId=" + recordDetailId +
                ", answerIds='" + answerIds + '\'' +
                '}';
    }
}
