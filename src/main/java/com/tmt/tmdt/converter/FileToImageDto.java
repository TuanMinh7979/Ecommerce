package com.tmt.tmdt.converter;

import com.tmt.tmdt.dto.request.FileRequestDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

public class FileToImageDto implements Converter<MultipartFile, FileRequestDto> {
    @Override
    public FileRequestDto convert(MultipartFile source) {
        return new FileRequestDto(source);
    }



}
