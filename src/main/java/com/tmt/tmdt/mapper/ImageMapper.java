package com.tmt.tmdt.mapper;

import com.tmt.tmdt.dto.request.ImageRequestDto;
import com.tmt.tmdt.entities.Image;

public interface ImageMapper {
    Image toModel(ImageRequestDto imageDto);

}
