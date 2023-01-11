package com.hss.reggie.controller;

import com.hss.reggie.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${path.img}")
    private String ImgPath;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        //截取文件后缀
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //利用UUID创建新文件名
        String fileName= UUID.randomUUID().toString() + suffix;
        //创建文件夹
        File dir = new File(ImgPath);
        if(!dir.exists())dir.mkdirs();
        //文件转存
        file.transferTo(new File(ImgPath+fileName));

        return R.success(fileName);
    }
    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        //通过输入流读取服务器文件
        FileInputStream fileInputStream = new FileInputStream(ImgPath+name);
        //输出流，浏览器展示图片
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("image/jpeg");
        //数据传输
        int len = 0;
        byte[] bytes=new byte[1024];
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
            outputStream.flush();//刷新
        }
        //关闭资源
        outputStream.close();
        fileInputStream.close();
    }
}
