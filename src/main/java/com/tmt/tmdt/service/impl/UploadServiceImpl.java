package com.tmt.tmdt.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.service.ImageService;
import com.tmt.tmdt.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
    private final Cloudinary cloudinary;


    private final ImageService imageService;


    @Override
    public Map<String, String> simpleUpload(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));

    }

    @Override
    public void deleteFromCloud(Long id) throws IOException {
        Image image = imageService.getImage(id);
        String publicId = image.getPublicId();
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }


}
