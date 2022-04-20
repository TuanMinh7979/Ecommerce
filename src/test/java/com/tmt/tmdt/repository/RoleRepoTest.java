package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class RoleRepoTest {

//    @Autowired
//    private RoleRepo roleRepo;
//
//    @Test
//    public void addMasterRole() {
//        Role masterrole = new Role("ROLE_CUSTOMER");
//
//        Role savedRole = roleRepo.save(masterrole);
//
//        assertThat(savedRole.getId() > 0);
//
//    }



//
}