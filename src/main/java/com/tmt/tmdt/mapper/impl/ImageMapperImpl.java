package com.tmt.tmdt.mapper.impl;

import com.tmt.tmdt.dto.request.ImageRequestDto;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageMapperImpl implements ImageMapper {

    @Override
    public Image toModel(ImageRequestDto imageDto) {
        if (imageDto == null) return null;
        Image image = new Image();
        image.setColor(imageDto.getColor());
        if (imageDto.getIsMain() != null) {
            image.setMain(imageDto.getIsMain().equals("true") ? true : false);
        }
        if (imageDto.getUploadRs() != null) {
            image.setPublicId(imageDto.getUploadRs().get("public_id"));
            image.setLink(imageDto.getUploadRs().get("url"));
        }

        return image;

    }
}
