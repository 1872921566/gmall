package com.zxshare.gmall.web.util;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadFileUtil {



    public static String fileUpload(MultipartFile file) {
        // 上传图片到服务器
        // 配置fdfs的全局链接地址
        String imgUrl = "http://192.168.50.138";

        String tracker = PmsUploadFileUtil.class.getResource("/tracker.conf").getPath();// 获得配置文件的路径

        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrackerClient trackerClient = new TrackerClient();

        // 获得一个trackerServer的实例
        TrackerServer trackerServer = null;
        try {
            trackerServer = trackerClient.getTrackerServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 通过tracker获得一个Storage链接客户端
        StorageClient storageClient = new StorageClient(trackerServer, null);

        try {

            byte[] bytes = file.getBytes();// 获得上传的二进制对象

            // 获得文件后缀名
            String originalFilename = file.getOriginalFilename();// a.jpg
            System.out.println("上传文件的名字:"+originalFilename);
            int i = originalFilename.lastIndexOf(".");
            String extName = originalFilename.substring(i + 1);

            String[] uploadInfos = storageClient.upload_file(bytes, extName, null);

            for (String uploadInfo : uploadInfos) {
                imgUrl += "/" + uploadInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("上传文件的url地址:"+imgUrl);

        return imgUrl;

    }


}

