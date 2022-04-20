package com.tmt.tmdt.service;


import com.tmt.tmdt.entities.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getPermissions();

    Permission save(Permission permission);

    Permission getPermission(Integer id);

    List<Permission> getPermissionByParent(Integer parentId);

    //for api
    List<Integer> getPermissionIdsByRoleId(Integer roleId);

}
