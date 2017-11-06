package com.sharewin.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.sharewin.common._enum.SexType;
import com.sharewin.common.excel.annotation.Excel;
import com.sharewin.common.orm.PropertyType;
import com.sharewin.common.orm.annotation.Delete;
import com.sharewin.common.utils.ConvertUtils;
import com.sharewin.common.utils.DateUtils;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.utils.collections.Collections3;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by yomg on 2017/5/12 0012.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Delete(propertyName = "status",type = PropertyType.I)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" , "handler","fieldHandler" ,
        "roles","roleIds"})
public class User {
    private Integer id;
    @Excel(exportName="用户名", exportFieldWidth = 18)
    private String loginName;
    private String password;
    @Excel(exportName="姓名", exportFieldWidth = 12)
    private String realname;
    @Excel(exportName="身份证号", exportFieldWidth = 25)
    private String idcardno;
    @Excel(exportName="入学时间", exportFieldWidth = 25)
    private Date enterSchoolTime;
    @Excel(exportName="联系电话", exportFieldWidth = 20)
    private String telphone;
    @Excel(exportName="监护人", exportFieldWidth = 12)
    private String guardianName;
    @Excel(exportName="性别", exportFieldWidth = 6,importConvert = true,exportConvert = true)
    private Integer sex;
    @Excel(exportName="生日", exportFieldWidth = 15,exportConvert = true)
    private Date birthday;
    private Integer status;
    private Timestamp createTime;

    @Excel(exportName="用户类型", exportFieldWidth = 15,importConvert = true,exportConvert = true)
    private Integer userType;

    private List<Role> roles = Lists.newArrayList();

    /**
     * 有序的关联Role对象id集合
     */
    private List<Integer> roleIds = Lists.newArrayList();

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
    @Column(name = "login_name")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "realname")
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Basic
    @Column(name = "idcardno")
    public String getIdcardno() {
        return idcardno;
    }

    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enter_school_time")
    public Date getEnterSchoolTime() {
        return enterSchoolTime;
    }

    public void setEnterSchoolTime(Date enterSchoolTime) {
        this.enterSchoolTime = enterSchoolTime;
    }

    @Basic
    @Column(name = "telphone")
    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    @Basic
    @Column(name = "guardian_name")
    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    @Basic
    @Column(name = "sex")
    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "user_type")
    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    @OrderBy("id")
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @SuppressWarnings("unchecked")
    @Transient
    public List<Integer> getRoleIds() {
        if (!Collections3.isEmpty(roles)) {
            roleIds = ConvertUtils.convertElementPropertyToList(roles, "id");
        }
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    /**
     * 性别描述.
     */
    @Transient
    public String getSexView() {
        SexType ss = SexType.getSexType(sex);
        String str = "";
        if(ss != null){
            str = ss.getDescription();
        }
        return str;
    }

   public void convertSetSex(String text){
        int p = 2;
        if(StringUtils.isNotEmpty(text)){
            if(text.equals("男")){
                p = 1;
            }else if(text.equals("女")){
                p=0;
            }
        }
        this.sex = p;
   }

   public String convertGetSex(){
       String sexStr = "";
       if(sex!=null){
          if(sex == 0){
             sexStr = "女";
          }else if(sex == 1){
              sexStr =  "男";
          }
       }
       return sexStr;
   }

   public String convertGetBirthday(){
       if(birthday!=null){
           return DateUtils.formatDate(birthday,"yyyy-MM-dd");
       }
       return "";
   }

    public void convertSetUserType(String text){
        int p = -1;
        if(StringUtils.isNotEmpty(text)){
            if(text.equals("学生")){
                p = 0;
            }else if(text.equals("家长")){
                p=2;
            }else if (text.equals("老师")){
                p = 1;
            }
        }
        this.userType = p;
    }

    public String convertGetUserType(){
        String userTypeStr = "";
        if(userType!=null){
            if(userType == 0){
                userTypeStr = "学生";
            }else if(userType == 1){
                userTypeStr =  "老师";
            }else if(userType ==2){
                userTypeStr = "家长";
            }
        }
        return userTypeStr;
    }

    /**
     * 类型描述.
     */
    @Transient
    public String getUserTypeView() {
        String userTypeStr = "";
        if(userType!=null){
            if(userType == 0){
                userTypeStr = "学生";
            }else if(userType == 1){
                userTypeStr =  "老师";
            }else if(userType ==2){
                userTypeStr = "家长";
            }
        }
        return userTypeStr;
    }
}
