package com.example.travelnode.S3.uploadtest;

import com.example.travelnode.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3ImageService {

    private final S3ImageRepository repository;
    @Autowired
    private S3Uploader s3Uploader;

    @Transactional
    public String keepImage(MultipartFile image) throws IOException {
        S3Image s3Image = new S3Image();
        if(!image.isEmpty()){
            String filename = s3Uploader.upload(image, "images");
            System.out.println("file : "+filename);
            s3Image.setImageUrl(filename);
        }
        repository.save(s3Image);
        return "uploaded";
    }

}
