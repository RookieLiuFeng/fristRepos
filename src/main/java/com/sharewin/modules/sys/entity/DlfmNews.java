package com.sharewin.modules.sys.entity;

import com.sharewin.common.orm.PropertyType;
import com.sharewin.common.orm.annotation.Delete;
import com.sharewin.utils.AppConstants;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by yomg on 2017/5/21 0021.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Delete(propertyName = "status",type = PropertyType.I)
@Table(name = "dlfm_news", schema = "dlfm")
public class DlfmNews {
    private Integer id;
    private String newsName;
    private String newsContent;
    private Timestamp createTime;
    private Integer createUserid;
    private Integer status;
    private Integer browseCount;
    private String newsCoverUrl;
    private Integer newsType;
    private String linkUrl;

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
    @Column(name = "news_name")
    public String getNewsName() {
        return newsName;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
    }

    @Basic
    @Column(name = "news_content")
    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
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
    @Column(name = "browse_count")
    public Integer getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Integer browseCount) {
        this.browseCount = browseCount;
    }

    @Basic
    @Column(name = "news_cover_url")
    public String getNewsCoverUrl() {
        return newsCoverUrl;
    }

    public void setNewsCoverUrl(String newsCoverUrl) {
        this.newsCoverUrl = newsCoverUrl;
    }

    @Basic
    @Column(name = "news_type")
    public Integer getNewsType() {
        return newsType;
    }

    public void setNewsType(Integer newsType) {
        this.newsType = newsType;
    }

    @Basic
    @Column(name = "link_url")
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Transient
    public String getBrowseUrl(){
        return AppConstants.BASEURL + AppConstants.getFrontPath()+"/news/"+id;
    }
}
