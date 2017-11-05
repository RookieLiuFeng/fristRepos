package com.sharewin.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.sharewin.common.orm.PropertyType;
import com.sharewin.common.orm.annotation.Delete;
import com.sharewin.common.utils.collections.Collections3;
import com.sharewin.utils.AppConstants;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by yomg on 2017/7/10.
 */
@Entity
@Delete(propertyName = "status", type = PropertyType.I)
@Table(name = "dlfm_act_root", schema = "dlfm")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"
        , "dlfmActs"})
public class DlfmActRoot {
    private Integer id;
    private String actName;
    private String actDesc;
    private String actCoverUrl;
    private Date actTime;
    private Timestamp createTime;
    private Integer createUserid;
    private Integer status;
    private Integer actType;
    private String linkUrl;

    private List<DlfmAct> dlfmActs = Lists.newArrayList();

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
    @Column(name = "act_name")
    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    @Basic
    @Column(name = "act_desc")
    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    @Basic
    @Column(name = "act_cover_url")
    public String getActCoverUrl() {
        return actCoverUrl;
    }

    public void setActCoverUrl(String actCoverUrl) {
        this.actCoverUrl = actCoverUrl;
    }

    @Basic
    @Column(name = "act_time")
    public Date getActTime() {
        return actTime;
    }

    public void setActTime(Date actTime) {
        this.actTime = actTime;
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
    @Column(name = "act_type")
    public Integer getActType() {
        return actType;
    }

    public void setActType(Integer actType) {
        this.actType = actType;
    }

    @Basic
    @Column(name = "link_url")
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dlfmActRoot")
    @Where(clause = "status=1")
    public List<DlfmAct> getDlfmActs() {
        return dlfmActs;
    }

    public void setDlfmActs(List<DlfmAct> dlfmActs) {
        this.dlfmActs = dlfmActs;
    }

    @Transient
    public int getActCount(){
        if(Collections3.isNotEmpty(dlfmActs)){
            return dlfmActs.size();
        }
        return 0;
    }

    @Transient
    public String getBrowseUrl(){
        return AppConstants.BASEURL + AppConstants.getFrontPath()+"/actRoot/"+id;
    }
}
