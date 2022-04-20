package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.entities.Permission;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.repository.PermissionRepo;
import com.tmt.tmdt.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepo permissionRepo;

    @Override
    public List<Permission> getPermissions() {
        return permissionRepo.findAll();
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepo.save(permission);
    }

    @Override
    public Permission getPermission(Integer id) {
        return permissionRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Permission with is " + id + " not found")
                );
    }

    @Override
    public List<Permission> getPermissionByParent(Integer parentId) {
        return permissionRepo.getPermissionsByParent(parentId);
    }

    //for api
    @Override
    public List<Integer> getPermissionIdsByRoleId(Integer roleId) {
        return permissionRepo.getPermissionIdsByRoleId(roleId);
    }


}
