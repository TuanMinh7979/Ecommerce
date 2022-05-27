package com.tmt.tmdt.service;


import com.tmt.tmdt.entities.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionService {
    List<Permission> getPermissions();

    Permission save(Permission permission);

    Permission getPermission(Integer id);



    //for api
    List<Integer> getPermissionIdsByRoleId(Integer roleId);

    Set<Permission> getPermissionByParent(Integer i);
}
