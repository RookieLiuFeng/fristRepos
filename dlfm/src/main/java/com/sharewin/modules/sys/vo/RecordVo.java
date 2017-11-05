package com.sharewin.modules.sys.vo;

public class RecordVo {
    private Integer id;
    private String name;
    private String userType;
    private String sex;
    private String finishTime;
    private String statu;
    private String score;
    private String completeness;

    public RecordVo() {
    }

    public RecordVo(Integer id, String name, String userType, String sex, String finishTime, String statu, String score, String completeness) {
        this.id = id;
        this.name = name;
        this.userType = userType;
        this.sex = sex;
        this.finishTime = finishTime;
        this.statu = statu;
        this.score = score;
        this.completeness = completeness;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCompleteness() {
        return completeness;
    }

    public void setCompleteness(String completeness) {
        this.completeness = completeness;
    }
}
