//package com.example.Jewelry.service.ServiceImpl;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import lombok.RequiredArgsConstructor;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class CloudinaryService {
//    private final Cloudinary cloudinary;
//
//    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
//        List<String> urls = new ArrayList<>();
//        for (MultipartFile file : files) {
//            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//            urls.add((String) uploadResult.get("secure_url"));
//        }
//        return urls;
//    }
//
//
//}
//
