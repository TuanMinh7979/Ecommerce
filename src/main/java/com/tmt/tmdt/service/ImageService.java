package com.tmt.tmdt.service;

import com.tmt.tmdt.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Image save(Image image);

    Image getImage(Long id);

    boolean existById(Long id);

    void deleteById(Long id) throws IOException;




}
