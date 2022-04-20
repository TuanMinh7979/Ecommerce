package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.entities.Permission;
import com.tmt.tmdt.entities.Role;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.repository.RoleRepo;
import com.tmt.tmdt.service.PermissionService;
import com.tmt.tmdt.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;

    @Override
    public Role save(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public boolean existByName(String name) {
        return roleRepo.existsByName(name);
    }

    @Override
    public Page<Role> getRolesByNameLike(String searchNameTerm, Pageable pageable) {
        return roleRepo.getRolesByNameLike(searchNameTerm, pageable);
    }

    @Override
    public Page<Role> getRoles(Pageable pageable) {
        return roleRepo.findAll(pageable);
    }

    @Override
    public void deleteById(Integer id) {
        roleRepo.deleteById(id);

    }


    public void deleteRoles(Integer[] ids) {
        for (Integer id : ids) {
            roleRepo.deleteById(id);
        }

    }

    @Override
    public Role getRole(Integer id) {
        return roleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " not found"));
    }


    //for show update form, from id
    @Override
    public Role getRoleWithPermissions(Integer id) {
        return roleRepo.getRoleWithPermissions(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " not found"));

    }

    //for show update form, from name keyword
    @Override
    public Role getRoleByNameWithPermissions(String name) {
        return roleRepo.getRoleByNameWithPermissions(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role with name " + name + "not found"));
    }

    //for autocomplete data
    @Override
    public List<String> getRoleNamesByKw(String kw) {
        return roleRepo.getRoleNamesByKw(kw);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @Override
    public List<Integer> getRoleIdsByUserId(Long userId) {
        return roleRepo.getRoleIdsByUserId(userId);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepo.getRoleByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Role with name " + name + " not found"));
    }


}
