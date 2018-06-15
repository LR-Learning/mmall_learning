package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


/**
 * @Author: LR
 * @Descriprition:
 * @Date: Created in 16:58 2018/6/15
 * @Modified By:
 **/

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path){
        String fileName = file.getOriginalFilename();
        //扩展名
        String fileExtensionFile = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionFile;
        // 打印日志
        logger.info("开始上传文件,上传的文件名:{},上传的路径:{},新文件名:{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        // 创建文件
        if (!fileDir.exists()) {
            fileDir.setWritable(true); // 获取写入权限
            fileDir.mkdirs();  // 会创建包含必须但不存在的父目录
        }
        File targetFile = new File(path, uploadFileName); // 一个完整的路径  路径+文件名
        try {
            // 将file移动到targetFile目录下
            file.transferTo(targetFile);// 文件已经上传成功
            // 将targetFile上传到我们的FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // 上传完之后删除upload下的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();

    }

}
