package com.sharewin.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sharewin.common.exception.DaoException;
import com.sharewin.common.exception.ServiceException;
import com.sharewin.common.exception.SystemException;
import com.sharewin.common.model.TreeNode;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.orm.hibernate.HibernateDao;
import com.sharewin.common.orm.hibernate.Parameter;
import com.sharewin.modules.sys.entity.Permission;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PermissionManager extends EntityManager<Permission, Integer> {

    @Autowired
    private UserManager userManager;

    private HibernateDao<Permission, Integer> permissionDao;// 默认的泛型DAO成员变量.

    /**
     * 通过注入的sessionFactory初始化默认的泛型DAO成员变量.
     */
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        permissionDao = new HibernateDao<Permission, Integer>(sessionFactory, Permission.class);
    }

    @Override
    protected HibernateDao<Permission, Integer> getEntityDao() {
        // TODO Auto-generated method stub
        return permissionDao;
    }


    /**
     * 根据用户ID得到导航栏资源（权限控制）.
     *
     * @param userId 用户ID
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    public List<TreeNode> getPermissionTreeByUserId(Integer userId)
            throws DaoException, SystemException, ServiceException {
        // Assert.notNull(userId, "参数[userId]为空!");
        List<TreeNode> nodes = Lists.newArrayList();
        List<Permission> userPermissions = this.getPermissionByUserId(userId);

        for (Permission permission : userPermissions) {
            if (permission != null && permission.getParentPermission() == null) {

                TreeNode node = this.permissionToTreeNode(userPermissions, permission, null, true);
                if (node != null) {
                    nodes.add(node);
                }
            }
        }


        return nodes;
    }

    @SuppressWarnings("unchecked")
    public List<Permission> getPermissionByUserId(Integer userId) throws DaoException, SystemException, ServiceException {
        Assert.notNull(userId, "userId不能为空");
        Parameter parameter = new Parameter(userId, StatusState.normal.getValue());
        // 角色权限
        List<Permission> rolePermissions = permissionDao.distinct(permissionDao.createQuery(
                "select ms from User u left join u.roles rs left join rs.permissions ms where u.id= :p1 and ms.status = :p2 order by ms.ordernum asc",
                parameter)).list();
        return rolePermissions;
    }

    /**
     * 将Permission 转化为 TreeNode
     *
     * @param repositoryPermission
     * @param permission
     * @param permissionType
     * @param isCascade
     * @return
     * @throws DaoException
     * @throws SystemException
     * @throws ServiceException
     */
    private TreeNode permissionToTreeNode(List<Permission> repositoryPermission, Permission permission, Integer permissionType, boolean isCascade) throws DaoException, SystemException,
            ServiceException {
        if (permission == null || !repositoryPermission.contains(permission)) {
            return null;
        }
        if (permissionType != null) {
            if (!permissionType.equals(permission.getType())) {
                return null;
            }
        }
        TreeNode treeNode = new TreeNode(permission.getId().toString(),
                permission.getName(), permission.getIcon());
        // 自定义属性 url
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("url", permission.getUrl());
        attributes.put("type", permission.getType());
        treeNode.setAttributes(attributes);
        if (isCascade) {
            List<TreeNode> childrenTreeNodes = Lists.newArrayList();
            for (Permission subPermission : permission.getSubPermissions()) {
                TreeNode node = permissionToTreeNode(repositoryPermission, subPermission, permissionType, isCascade);
                if (node != null) {
                    childrenTreeNodes.add(node);
                }
            }
            treeNode.setChildren(childrenTreeNodes);
        }

        return treeNode;
    }





}
