package com.sharewin.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "dlfm_book", schema = "dlfm")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"
        , "dlfmBookPeriod"})
public class DlfmBook {
    private Integer id;
    private String bookName;
    private String bookAuthor;
    private String bookDesc;
    private Integer istj;
    private Timestamp createTime;
    private Integer createUserid;
    private Integer status;
    private Integer browseCount;
    private String bookCoverUrl;
    private Integer bookType;
    private String linkUrl;

    private DlfmBookPeriod dlfmBookPeriod;

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
    @Column(name = "book_name")
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Basic
    @Column(name = "book_author")
    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    @Basic
    @Column(name = "book_desc")
    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    @Basic
    @Column(name = "istj")
    public Integer getIstj() {
        return istj;
    }

    public void setIstj(Integer istj) {
        this.istj = istj;
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
    @Column(name = "book_cover_url")
    public String getBookCoverUrl() {
        return bookCoverUrl;
    }

    public void setBookCoverUrl(String bookCoverUrl) {
        this.bookCoverUrl = bookCoverUrl;
    }

    @Basic
    @Column(name = "book_type")
    public Integer getBookType() {
        return bookType;
    }

    public void setBookType(Integer bookType) {
        this.bookType = bookType;
    }

    @Basic
    @Column(name = "link_url")
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_period_id")
    public DlfmBookPeriod getDlfmBookPeriod() {
        return dlfmBookPeriod;
    }

    public void setDlfmBookPeriod(DlfmBookPeriod dlfmBookPeriod) {
        this.dlfmBookPeriod = dlfmBookPeriod;
    }

    @Transient
    public String getBrowseUrl(){
        return AppConstants.BASEURL + AppConstants.getFrontPath()+"/book/"+id;
    }



}
