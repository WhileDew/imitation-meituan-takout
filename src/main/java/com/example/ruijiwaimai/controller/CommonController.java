package com.example.ruijiwaimai.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import com.example.ruijiwaimai.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${ruijiwaimai.path}")
    private String basePath;

    /**
     * 前端下载图片
     */
    @GetMapping("/download")
    public void download(@RequestParam String name, HttpServletResponse response) {
        try {
            // 通过输入流读取文件信息
            FileInputStream inputStream = new FileInputStream(basePath + name);
            response.setContentType("img/jpg");
            // 通过输出流将文件写回浏览器
            IoUtil.read(inputStream).writeTo(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 前端上传图片
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        // 获取源文件名
        String name = file.getOriginalFilename();
        System.out.println("路径：" + basePath + name);
        assert name != null;
        String suffix = name.substring(name.lastIndexOf("."));
        // 使用uuid拼接文件名
        String fileName = UUID.randomUUID() + suffix;
        // 存储文件
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 注意此处上传成功后返回的是文件路径
        return Result.success(fileName);
    }

}
