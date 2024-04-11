package com.company.reports.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class CompanyIOUtils {
	private static final Logger logger = LoggerFactory.getLogger(CompanyIOUtils.class);

	public void createFileFromByteArray(byte[] data, String fileLocation,String subDirectory,String fileName, String fileExtension)
			throws Exception {
		StringBuilder fileDirectory = new StringBuilder(fileLocation).append(subDirectory);
		File directory = new File(fileDirectory.toString());
		if (!directory.exists()) {
			directory.mkdirs();
		}
		StringBuilder fileNameWithExtension = new StringBuilder(fileName).append(fileExtension);

		File file = new File(directory, fileNameWithExtension.toString());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
		} catch (FileNotFoundException e) {
			logger.error("Error: {}, Cause: {}", e.getMessage(), e.getCause());
			throw new Exception("Couldn't create a file!");
		} catch (IOException e) {
			logger.error("Error: {}, Cause:", e.getMessage(), e.getCause());
			throw new Exception("Couldn't write on created file!");
		} finally {
			if (fos != null)
				fos.close();
		}
	}

	public byte[] readFileAsBytes(String fileLocation,String subDirectory, String fileName, String fileExtension) throws Exception {

		StringBuilder fileNameWithExtension = new StringBuilder(fileName).append(fileExtension);
		StringBuilder fileDirectory = new StringBuilder(fileLocation).append(subDirectory);
		File directory = new File(fileDirectory.toString());
		if (!directory.exists()) {
			directory.mkdirs();
		}
		FileInputStream fis = null;
		byte[] data = null;
		try {
			File file = new File(directory, fileNameWithExtension.toString());
			fis = new FileInputStream(file);
			data = fis.readAllBytes();
		} finally {
			if (fis != null)
				fis.close();
		}
		return data;
	}

}
