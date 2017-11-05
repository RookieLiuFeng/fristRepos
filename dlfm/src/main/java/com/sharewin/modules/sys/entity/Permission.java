package com.sharewin.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by yomg on 2017/5/12 0012.
 */
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler" ,  "parentPermission",
        "roles", "subPermissions" })
public class Permission {
    private Integer id;
    private Integer _parentId;
    private String name;
    private String type;
    private String url;
    private String icon;
    private Integer status;
    private String remark;
    private Integer ordernum;

    /**
     * 父级Resource
     */
    private Permission parentPermission;
    /**
     * 有序的关联对象集合
     */
    private List<Role> roles = Lists.newArrayList();

    /**
     * 子Permission集合
     */
    private List<Permission> subPermissions = Lists.newArrayList();

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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission", joinColumns = { @JoinColumn(name = "permission_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "parent_id")
    public Permission getParentPermission() {
        return parentPermission;
    }

    public void setParentPermission(Permission parentPermission) {
        this.parentPermission = parentPermission;
    }

    @OneToMany(mappedBy = "parentPermission",cascade = {CascadeType.REMOVE})
    @OrderBy("ordernum asc")
    public List<Permission> getSubPermissions() {
        return subPermissions;
    }

    public void setSubPermissions(List<Permission> subPermissions) {
        this.subPermissions = subPermissions;
    }

    @Transient
    public Integer get_parentId() {
        if (parentPermission != null) {
            _parentId = parentPermission.getId();
        }
        return _parentId;
    }

    public void set_parentId(Integer _parentId) {
        this._parentId = _parentId;
    }


    @Basic
    @Column(name = "ordernum")
    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }
}
