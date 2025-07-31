package com.nmmc.hrms.Utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Component
public class DocumentUtil {
    @Value("${app.documentStorageFinal}")
    private String DOCUMENT_STORAGE_FINAL;

    @Value("${app.filePathSeparator}")
    private String FILE_PATH_SEPARATOR;

    public String writeFileToPremanentLocation(MultipartFile file) throws IOException {

        String filePath = getAbsolutePathOfFinalLocation(file.getOriginalFilename());
        File targetFile = new File(filePath);
        if (targetFile.exists()) {
            FileUtils.deleteQuietly(targetFile);
        }
        FileUtils.writeByteArrayToFile(targetFile, file.getBytes());
        return filePath;
    }

    public String getAbsolutePathOfFinalLocation(String fileName) {

        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1, fileName.length());
        }
        String regIdPath = DOCUMENT_STORAGE_FINAL;

        File directory = new File(regIdPath);
        if (!directory.exists()) {
            // create aadhar folder
            directory.mkdir();
        }
        return regIdPath + "_" + Helper.getFileDate() + "_" + fileName;

    }

}
