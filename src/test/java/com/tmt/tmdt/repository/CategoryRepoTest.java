package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.util.TextUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class CategoryRepoTest {

    @Autowired
    private CategoryRepo categoryRepo;
//


    @Test
    public void test() {

//        Category category = categoryRepo.getCategoryWithChilds(3).get();
//        System.out.println(category.getName());
//        System.out.println(category.getChildren().size());


    }
//
//    @Test
//    public void addCategory() {
//        Category category1 = new Category("Phone");
//        category1.setCode(TextUtil.generateCode(category1.getName()));
//        Category category2 = new Category("Computer");
//        category2.setCode(TextUtil.generateCode(category2.getName()));
//        Category category3 = new Category("Tablet");
//        category3.setCode(TextUtil.generateCode(category3.getName()));
//
//
//        categoryRepo.save(category1);
//        categoryRepo.save(category2);
//        categoryRepo.save(category3);
//
//        assertThat(category1.getId() > 0);
//
//    }
//
//    @Test
//    public void addSubCategory1() {
//
//        Category com= new Category(2);
//
//
//        //subcategory
//        ///o phone:
//        Category lt= new Category("Laptop", com);
//        lt.setCode(TextUtil.generateCode(lt.getName()));
//        Category lk= new Category("Desktop",com);
//        lk.setCode(TextUtil.generateCode(lk.getName()));
//
//              //o pc
////
////        Category apple= new Category("Mac",phone)
//
//        categoryRepo.save(lt);
//        categoryRepo.save(lk);
//
//    }
//
//    @Test
//    public void addSubCategory2() {
//        Category lt= new Category(10);
//        //subcategory
//        ///o phone:
//        Category lk= new Category("Offical",lt);
//        lk.setCode(TextUtil.generateCode(lk.getName()));
//        Category gm= new Category("Laptop Gamming",lt);
//        gm.setCode(TextUtil.generateCode(gm.getName()));
//
//
//        categoryRepo.save(lk);
//        categoryRepo.save(gm);
//
//    }
}