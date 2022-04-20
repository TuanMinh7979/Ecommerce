package com.tmt.tmdt.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UploadService {
    Map<String, String> simpleUpload(MultipartFile file) throws IOException;

    void deleteFromCloud(Long id) throws IOException;




}
