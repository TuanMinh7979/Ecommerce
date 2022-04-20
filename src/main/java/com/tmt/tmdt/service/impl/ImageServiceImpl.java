package com.tmt.tmdt.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.repository.ImageRepo;
import com.tmt.tmdt.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepo imageRepo;
    private final Cloudinary cloudinary;

    @Override
    public Image save(Image image) {
        Image savedImage = imageRepo.save(image);
        return savedImage;
    }

    @Override
    public Image getImage(Long id) {
        return imageRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image with id " + id + " not found"));
    }

    @Override
    public boolean existById(Long id) {
        return imageRepo.existsById(id);
    }



    @Override
    public void deleteById(Long id) throws IOException {

        Image image = getImage(id);
        String publicId = image.getPublicId();
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        imageRepo.deleteById(id);
    }





}
