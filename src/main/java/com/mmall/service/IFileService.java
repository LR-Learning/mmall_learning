package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 16:58 2018/6/15
 * @Modified By:
 **/
public interface IFileService {
    String upload(MultipartFile file, String path);
}
