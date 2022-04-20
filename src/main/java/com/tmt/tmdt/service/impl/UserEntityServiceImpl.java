package com.tmt.tmdt.service.impl;

import com.cloudinary.Cloudinary;
import com.tmt.tmdt.dto.request.FileRequestDto;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.entities.Role;
import com.tmt.tmdt.entities.UserEntity;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.mapper.ImageMapper;
import com.tmt.tmdt.repository.UserRepo;
import com.tmt.tmdt.service.ImageService;
import com.tmt.tmdt.service.UploadService;
import com.tmt.tmdt.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService {

    private final UserRepo userRepo;
    private final UploadService uploadService;
    private final ImageMapper imageMapper;
    private final Cloudinary cloudinary;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;


    private List<String> setRoleNameListHelper(List<String> currentRoleNameList, Set<Role> roles) {
        //use for 2 case add new or remove all
        if (!currentRoleNameList.isEmpty()) {
            //if currentRoleNameListNotEmpty clear it first
            for (String rolenamei : currentRoleNameList) {
                currentRoleNameList.remove(rolenamei);
            }
        }
        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                currentRoleNameList.add(role.getName());
            }
        }
        return currentRoleNameList;

    }

    @Override
    public UserEntity getUserEntity(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public List<UserEntity> getUserEntitys() {
        return userRepo.findAll();
    }

    @Override
    @Transactional
    public void update(UserEntity userEntity, FileRequestDto fileRequestDto, String delImageId) throws IOException {

        //delete old image if have

        //+ delete old image(del !null, dto ==null)
        //3 case : + add new image from none (del = null, dto !=null)

        ///this case is connecting of 2 previous case
        //+ del old image and add new image(del !=null, dto != null )

        //+ del = null  && dto ==null
        if (delImageId != null && !delImageId.isEmpty()) {
            //delete old imag
            delImageId = delImageId.trim();
            Long imageIdToDel = Long.parseLong(delImageId);

            uploadService.deleteFromCloud(imageIdToDel);
            userEntity.setImage(null);
            userEntity.setImageLink(userEntity.defaultImage());
            imageService.deleteById(imageIdToDel);
        }
        if (!fileRequestDto.getFile().isEmpty()) {
            //add new image
            fileRequestDto.setUploadRs(uploadService.simpleUpload(fileRequestDto.getFile()));
            Image image = imageMapper.toModel(fileRequestDto);

            image.setUserEntity(userEntity);
            Image savedImage = imageService.save(image);

            userEntity.setImageLink(savedImage.getLink());
        }
        if ((delImageId == null || delImageId.isEmpty()) && fileRequestDto.getFile().isEmpty()) {
            userEntity.setImage(getUserEntity(userEntity.getId()).getImage());
        }

        save(userEntity);


    }

    public UserEntity save(UserEntity userEntity) {

        userEntity.setRoleNameList(setRoleNameListHelper(userEntity.getRoleNameList(), userEntity.getRoles()));
        return userRepo.save(userEntity);

    }


    @Transactional
    @Override
    public void add(UserEntity userEntity, FileRequestDto fileRequestDto) throws IOException {


        //add image if have , imageRequestDto away !=null but for scable -> execute fully checking
        if (fileRequestDto != null && !fileRequestDto.getFile().isEmpty()) {
            fileRequestDto.setUploadRs(uploadService.simpleUpload(fileRequestDto.getFile()));
            Image image = imageMapper.toModel(fileRequestDto);

            Image imageSaved = imageService.save(image);
            imageSaved.setUserEntity(userEntity);
            //persistence
            userEntity.setImageLink(imageSaved.getLink());
        }



//        userEntity.setImage(null);
        save(userEntity);

    }


    @Override
    public Page<UserEntity> getUserEntitysByRole(Long roleId, Pageable pageable) {
        return userRepo.getUserEntitysByRole(roleId, pageable);
    }


    @Override
    public Page<UserEntity> getUserEntitys(Pageable pageable) {
        return userRepo.findAll(pageable);
    }


    @Override
    public Page<UserEntity> getUserEntitysByRoleAndUserNameLike(Long roleId, String searchNameTerm, Pageable pageable) {
        return userRepo.getUserEntitysByRoleAndUserNameLike(roleId, searchNameTerm, pageable);
    }

    @Override
    public Page getUserEntityByUserName(String searchNameTerm, Pageable pageable) {
        return userRepo.getUserEntitysByUserName(searchNameTerm, pageable);
    }

    @Override
    public boolean existByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public boolean existById(Long id) {
        return userRepo.existsById(id);
    }

    @Override
    public UserEntity getUserEntityWithImage(Long id) {
        return userRepo.getUserEntityWithImage(id).
                orElseThrow(() -> new ResourceNotFoundException("user with id " + id + " not found"));
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public void deletes(Long[] ids) {
        for (Long idi : ids) userRepo.deleteById(idi);


    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepo.getUserEntityByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
    }

    @Override
    public UserEntity getUserEntityWithRoles(String username) {
        return userRepo.getUserEntitysByUserNameWithRoles(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
    }
}
