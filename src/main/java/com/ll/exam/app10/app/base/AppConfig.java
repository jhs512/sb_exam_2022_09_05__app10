package com.ll.exam.app10.app.base;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class AppConfig {
    public static String GET_FILE_DIR_PATH;

    @Value("${custom.genFileDirPath}")
    public void setFileDirPath(String genFileDirPath) {
        GET_FILE_DIR_PATH = genFileDirPath;
    }
}
