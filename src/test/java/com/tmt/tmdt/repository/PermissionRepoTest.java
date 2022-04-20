package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Permission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class PermissionRepoTest {

//    @Autowired
//    private PermissionRepo permissionRepo;
//
//    @Test
//    public void addMasterPermission() {
//        Permission masterPermission = new Permission(1,"All");
//
//
//
//        Permission savedPermission = permissionRepo.save(masterPermission);
//
//        assertThat(savedPermission.getId() > 0);
//
//    }
//
//    @Test
//    public void addSubPermission() {
//        Permission masterPermission = new Permission(1);
//        Permission permission = new Permission("Category:all", masterPermission);
//        Permission permission1 = new Permission("Product:all", masterPermission);
//
//        Permission savedPermission = permissionRepo.save(permission);
//        Permission savedPermission1 = permissionRepo.save(permission1);
//
//        assertThat(savedPermission.getId() > 0);
//
//    }
//
//    @Test
//    public void addSubPermission1() {
////        Permission masterPermission1 = new Permission(2);
//        Permission masterPermission2 = new Permission(3);
//
//
//        Permission permission = new Permission("Product:read", masterPermission2);
//        Permission permission1 = new Permission("Product:write", masterPermission2);
//
//        Permission savedPermission = permissionRepo.save(permission);
//        Permission savedPermission1 = permissionRepo.save(permission1);
//
//        assertThat(savedPermission.getId() > 0);
//
//    }

}