package com.example.travelnode.S3.uploadtest;

import com.example.travelnode.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class S3ImageController {

    @Autowired
    S3ImageService service;
    private final S3Uploader s3Uploader;

    // 이미지 업로드 테스트 O - s3 서버에 올라가는지 테스트
    @PostMapping(value = "/image")
    public String saveImage(HttpServletRequest request,
                            @RequestParam(value = "image")MultipartFile image) throws IOException {
        System.out.println("save Image");
        return service.keepImage(image);
    }

}
