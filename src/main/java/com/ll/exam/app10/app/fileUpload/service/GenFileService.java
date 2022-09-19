package com.ll.exam.app10.app.fileUpload.service;

import com.ll.exam.app10.app.article.entity.Article;
import com.ll.exam.app10.app.fileUpload.entity.GenFile;
import com.ll.exam.app10.app.fileUpload.repository.GenFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenFileService {
    private final GenFileRepository genFileRepository;

    public void saveFiles(Article article, Map<String, MultipartFile> fileMap) {
        String relTypeCode = "article";
        long relId = article.getId();

        for (String inputName : fileMap.keySet()) {
            MultipartFile multipartFile = fileMap.get(inputName);

            String typeCode = "common";
            String type2Code = "inBody";
            String fileExt = "jpg";
            String fileExtTypeCode = "img";
            String fileExtType2Code = "jpg";
            int fileNo = 1;
            int fileSize = 1000;
            String fileDir = "article/2022_09_19";
            String originFileName = "??";

            GenFile genFile = GenFile
                    .builder()
                    .relTypeCode(relTypeCode)
                    .relId(relId)
                    .typeCode(typeCode)
                    .type2Code(type2Code)
                    .fileExtTypeCode(fileExtTypeCode)
                    .fileExtType2Code(fileExtType2Code)
                    .fileNo(fileNo)
                    .fileSize(fileSize)
                    .fileDir(fileDir)
                    .fileExt(fileExt)
                    .originFileName(originFileName)
                    .build();

            genFileRepository.save(genFile);
        }
    }
}
