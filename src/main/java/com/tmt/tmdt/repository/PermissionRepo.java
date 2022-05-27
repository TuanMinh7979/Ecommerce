package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Integer> {

    @Query("from Permission p left join fetch p.childs where p.parent.id = :id ")
    Set<Permission> getPermissionsByParent(@Param("id") Integer id);

    //for api
    @Query("select p.id from Permission p join p.roles r where r.id = :roleId")
    List<Integer> getPermissionIdsByRoleId(@Param("roleId") Integer roleId);
}
