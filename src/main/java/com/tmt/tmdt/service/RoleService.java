package com.tmt.tmdt.service;

import com.tmt.tmdt.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    Role save(Role role);

    boolean existByName(String name);

    Page<Role> getRolesByNameLike(String searchNameTerm, Pageable pageable);

    Page<Role> getRoles(Pageable pageable);


    void deleteById(Integer id);

    void deleteRoles(Integer[] ids);

    Role getRole(Integer id);

    Role getRoleWithPermissions(Integer id);

    Role getRoleByNameWithPermissions(String name);


    List<String> getRoleNamesByKw(String kw);

    List<Role> getRoles();

    List<Integer> getRoleIdsByUserId(Long userId);

    Role getRoleByName(String name);
}
