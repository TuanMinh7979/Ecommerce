package com.tmt.tmdt.mapper.impl;

import com.tmt.tmdt.dto.request.FileRequestDto;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageMapperImpl implements ImageMapper {

    @Override
    public Image toModel(FileRequestDto imageDto) {
        if (imageDto == null) return null;
        Image image = new Image();
        image.setPublicId(imageDto.getUploadRs().get("public_id"));
        image.setLink(imageDto.getUploadRs().get("url"));
        return image;

    }
}
