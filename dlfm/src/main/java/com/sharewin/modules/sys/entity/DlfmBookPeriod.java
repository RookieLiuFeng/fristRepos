package com.sharewin.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.sharewin.common.orm.PropertyType;
import com.sharewin.common.orm.annotation.Delete;
import com.sharewin.common.utils.collections.Collections3;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by yomg on 2017/6/26.
 */
@Entity
@JsonFilter("jsonFilter")
@Delete(propertyName = "status", type = PropertyType.I)
@Table(name = "dlfm_book_period", schema = "dlfm")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"
        , "dlfmBooks"})
public class DlfmBookPeriod {
    private Integer id;
    private String periodName;
    private Integer createUserid;
    private Timestamp createTime;
    private Integer status;

    private List<DlfmBook> dlfmBooks = Lists.newArrayList();

    private Timestamp releaseTime;
    private Integer periodType;
    private String periodDesc;

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
    @Column(name = "period_name")
    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
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
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dlfmBookPeriod")
    @Where(clause = "status=1")
    public List<DlfmBook> getDlfmBooks() {
        return dlfmBooks;
    }

    public void setDlfmBooks(List<DlfmBook> dlfmBooks) {
        this.dlfmBooks = dlfmBooks;
    }

    @Basic
    @Column(name = "release_time")
    public Timestamp getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Basic
    @Column(name = "period_type")
    public Integer getPeriodType() {
        return periodType;
    }

    public void setPeriodType(Integer periodType) {
        this.periodType = periodType;
    }

    @Basic
    @Column(name = "period_desc")
    public String getPeriodDesc() {
        return periodDesc;
    }

    public void setPeriodDesc(String periodDesc) {
        this.periodDesc = periodDesc;
    }

    @Transient
    public int getBookCount() {
        if (Collections3.isNotEmpty(dlfmBooks)) {
            return dlfmBooks.size();
        }
        return 0;
    }

    @Transient
    public String getPeriodTypeView() {
        String p = "";
        if (periodType != null) {
            if (periodType == 1) {
                p = "好书推荐";
            } else {
                p = "新书列表";
            }
        }
        return p;
    }

    @Transient
    public String getStatusView(){
        String p = "";
        if(status!=null){
            if(status==1){
                p = "已发布";
            }else{
                p = "未发布";
            }
        }
        return p;
    }
}
