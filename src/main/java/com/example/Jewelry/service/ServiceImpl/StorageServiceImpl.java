package com.example.Jewelry.service.ServiceImpl;


import com.example.Jewelry.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class StorageServiceImpl implements StorageService {

    @Value("${com.lms.profile.image.folder.path}")
    private String PROFILE_PIC_BASEPATH;

    @Value("${com.lms.course.video.folder.path}")
    private String PRODUCT_BASEPATH;

    @Value("${com.lms.course.notes.folder.path}")
    private String CATEGORY_BASEPATH;

    @Override
    public List<String> loadAll() {
        File dirPath = new File(PROFILE_PIC_BASEPATH);
        return Arrays.asList(dirPath.list());
    }

    @Override
    public String store(MultipartFile file) {
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
        File filePath = new File(PROFILE_PIC_BASEPATH, fileName);
        // Fix for non-existing parent directory
        filePath.getParentFile().mkdirs();
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            FileCopyUtils.copy(file.getInputStream(), out);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Resource load(String fileName) {
        File filePath = new File(PROFILE_PIC_BASEPATH, fileName);
        if (filePath.exists())
            return new FileSystemResource(filePath);
        return null;
    }

    @Override
    public void delete(String fileName) {
        File filePath = new File(PROFILE_PIC_BASEPATH, fileName);
        if (filePath.exists())
            filePath.delete();
    }

    @Override
    public List<String> loadAllProductImage() {
        File dirPath = new File(PRODUCT_BASEPATH);
        return Arrays.asList(dirPath.list());
    }

    @Override
    public String storeProductImage(MultipartFile file) {
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
        File filePath = new File(PRODUCT_BASEPATH, fileName);
        // Fix for non-existing parent directory
        filePath.getParentFile().mkdirs();
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            FileCopyUtils.copy(file.getInputStream(), out);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Resource loadProductImage(String fileName) {
        File filePath = new File(PRODUCT_BASEPATH, fileName);
        if (filePath.exists())
            return new FileSystemResource(filePath);
        return null;
    }

    @Override
    public void deleteProductImage(String fileName) {
        File filePath = new File(PRODUCT_BASEPATH, fileName);
        if (filePath.exists())
            filePath.delete();
    }

    @Override
    public List<String> loadAllCategoryImage() {
        File dirPath = new File(CATEGORY_BASEPATH);
        return Arrays.asList(dirPath.list());
    }

    @Override
    public String storeCategoryImage(MultipartFile file) {

        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
        File filePath = new File(CATEGORY_BASEPATH, fileName);
        // Fix for non-existing parent directory
        filePath.getParentFile().mkdirs();
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            FileCopyUtils.copy(file.getInputStream(), out);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Resource loadCategoryImage(String fileName) {
        File filePath = new File(CATEGORY_BASEPATH, fileName);
        if (!filePath.exists()) {
            System.err.println("File not found: " + filePath.getAbsolutePath());
            return null;
        }
        return new FileSystemResource(filePath);
    }

    @Override
    public void deleteCategoryImage(String fileName) {
        File filePath = new File(CATEGORY_BASEPATH, fileName);
        if (filePath.exists())
            filePath.delete();
    }



}

