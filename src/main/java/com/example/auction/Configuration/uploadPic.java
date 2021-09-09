package com.example.auction.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import net.coobird.thumbnailator.Thumbnails;

@CrossOrigin
@Component
public class uploadPic {





   //@Value("${pic.address}")
    private  static String address;
    //这是本地存入的格式，上传到服务器的话，格式类似于，"/root/images/pc/"
   // private static String UPLOAD_FOLDER = address;
    // private Logger logger = LoggerFactory.getLogger(picUtil.class);打日志用的
    //Thread.currentThread().getContextClassLoader().getResource("").getPath();//(获取当前的绝对路径的方法，这里不用，得到的是这样的东西:file:/D:/java/eclipse32/workspace/jbpmtest3/bin/)
   //@Bean
    public static String singleFileUpload(MultipartFile pc1,String UPLOAD_FOLDER,String count) throws IOException {
        // logger.debug("传入的文件参数：{}", JSON.toJSONString(file, true));

        if (Objects.isNull(pc1) || pc1.isEmpty()) {//判断非空
            // logger.error("文件为空");
            return "文件为空，请重新上传";
        }
        try {
            byte[] bytes = pc1.getBytes();
            //要存入本地的地址放到path里面
            Path path = Paths.get(UPLOAD_FOLDER + "/");
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(path);
            }
            String extension = getFileExtension(pc1);  //获取文件后缀

            UUID uuid = UUID.randomUUID();  //这里调用了UUID，得到全宇宙唯一的命名
            String str = uuid.toString(); // 真正的UUID打印出来是这样的：xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx (8-4-4-4-12)
            //所以我们可以去掉去掉"-"符号
            String picname = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
            String relativeAddr = picname + extension;  //唯一的名字接上后缀

            if("1".equals(count)){
                File imageFile=new File(path + "/" + relativeAddr);
                pc1.transferTo(imageFile);
            }else {
                 Thumbnails.of(pc1.getInputStream()).size(200, 200)    //写入
                    .outputQuality(0.8f).toFile(path + "/" + relativeAddr);
            }


            //logger.debug("文件写入成功...");
            return Paths.get(path + "/" + relativeAddr).toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "后端异常...";
        }
    }
    private static String getFileExtension(MultipartFile File) {
        String originalFileName = File.getOriginalFilename();
        return originalFileName.substring(originalFileName.lastIndexOf("."));

    }

}
