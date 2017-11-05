package com.sharewin.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.utils.ConvertUtils;
import com.sharewin.common.utils.collections.Collections3;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by yomg on 2017/5/12 0012.
 */
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler" ,
        "users","permissions"})
public class Role {
    private Integer id;
    private String roleName;
    private String roleCode;
    private String roleDesc;
    private Integer status;

    /**
     * 关联的用户
     */
    private List<User> users = Lists.newArrayList();

    /**
     * 关联用户ID集合    @Transient
     */
    private List<Integer> userIds = Lists.newArrayList();

    /**
     * 关联的资源
     */
    private List<Permission> permissions = Lists.newArrayList();

    /**
     * 关联权限ID集合    @Transient
     */
    private List<Integer> permissionIds = Lists.newArrayList();

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
    @Column(name = "role_name")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Basic
    @Column(name = "role_code")
    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @Basic
    @Column(name = "role_desc")
    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    // 中间表定义,表名采用默认命名规则
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
    @Where(clause = "status = 1")
    @OrderBy("id")
    public List<User> getUsers() {
        return users;
    }


    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Transient
    public List<Integer> getUserIds() {
        if (!Collections3.isEmpty(users)) {
            userIds = ConvertUtils.convertElementPropertyToList(users, "id");
        }
        return userIds;
    }


    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id") })
    @OrderBy("id")
    public List<Permission> getPermissions() {
        return permissions;
    }


    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Transient
    public List<Integer> getPermissionIds() {
        if (!Collections3.isEmpty(permissions)) {
            permissionIds = ConvertUtils.convertElementPropertyToList(permissions, "id");
        }
        return permissionIds;
    }


    public void setPermissionIds(List<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }

    /**
     * 角色拥有的权限字符串,多个之间以","分割
     *
     * @return
     */
    @Transient
    public String getpermissionNames() {
        List<Permission> ms = Lists.newArrayList();
        for(Permission m: permissions){
            if(m.getStatus().equals(StatusState.normal.getValue())){
                ms.add(m);
            }
        }
        return ConvertUtils.convertElementPropertyToString(ms, "name",
                ", ");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != role.id) return false;
        if (roleName != null ? !roleName.equals(role.roleName) : role.roleName != null) return false;
        if (roleCode != null ? !roleCode.equals(role.roleCode) : role.roleCode != null) return false;
        if (roleDesc != null ? !roleDesc.equals(role.roleDesc) : role.roleDesc != null) return false;
        if (status != null ? !status.equals(role.status) : role.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (roleCode != null ? roleCode.hashCode() : 0);
        result = 31 * result + (roleDesc != null ? roleDesc.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
