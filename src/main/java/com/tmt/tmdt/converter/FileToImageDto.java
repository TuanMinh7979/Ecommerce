package com.tmt.tmdt.converter;

import com.tmt.tmdt.dto.request.ImageRequestDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

public class FileToImageDto implements Converter<MultipartFile, ImageRequestDto> {
    @Override
    public ImageRequestDto convert(MultipartFile source) {
        return new ImageRequestDto(source);
    }



}
