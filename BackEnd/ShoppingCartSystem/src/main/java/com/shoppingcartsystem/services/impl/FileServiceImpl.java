package com.shoppingcartsystem.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shoppingcartsystem.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Value("${project.image}")
	private String path;
	
	@Override
	public String uploadImage(MultipartFile file) throws IOException {
		
		//File Name
		String name = file.getOriginalFilename();
		
		//Random Name Generate
		String randomID = UUID.randomUUID().toString();
		String fileName = randomID.concat(name.substring(name.lastIndexOf(".")));
		
		
		//Full path
		String filePath = path+File.separator+fileName;
		
		
		//Create folder if not created
		File f = new File(path);
		if(!f.exists()) {
			f.mkdir();
		}
		
		//File copy
		Files.copy(file.getInputStream(), Paths.get(filePath));
		
		
		return fileName;
	}

	@Override
	public InputStream getResource(String fileName) throws FileNotFoundException {
		String fullPath = path+File.separator+fileName;
		InputStream is = new FileInputStream(fullPath);
		return is;
	}

	@Override
	public void deleteImage(String fileName) {
		String fullPath = path+File.separator+fileName;
		File imageFile = new File(fullPath);
		imageFile.delete();
		
	}

	
}
