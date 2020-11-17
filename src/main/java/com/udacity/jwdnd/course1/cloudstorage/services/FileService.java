package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void saveFile(Integer userId, MultipartFile multipartFile) throws IOException {
        File file = new File(null,
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                String.valueOf(multipartFile.getSize()),
                userId,
                multipartFile.getBytes());

        fileMapper.insert(file);
    }

    public List<File> getUserFiles(Integer userId) {
        return fileMapper.getUserFiles(userId);
    }


    public File getUserFileByFileName(Integer userId, String fileName) {
        return fileMapper.getUserFile(userId, fileName);
    }

    public int deleteUserFile(Integer userId, String fileName) {
        return fileMapper.deleteUserFile(userId, fileName);
    }

    public boolean isFileNameAvailable(Integer userId, String fileName) {
        return getUserFileByFileName(userId, fileName) == null;
    }
}
