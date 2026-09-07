package com.snvec.student;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 遂宁工程职业学院一站式学生工作智能平台
 * 启动类
 * 
 * @author SNVEC Development Team
 * @since 2024
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableAsync
@EnableScheduling
@MapperScan("com.snvec.student.modules.*.mapper")
public class StudentPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentPlatformApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  遂宁工程职业学院一站式学生工作智能平台启动成功  ");
        System.out.println("  校训: 厚德精技 求实创新                      ");
        System.out.println("==============================================");
    }
}
