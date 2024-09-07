package com.example;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;

public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:13306/forum";
        String username = "root";
        String password = "mysql";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("杨梦格") // 设置作者
                            .outputDir("C:\\idea\\project\\forum\\admin-backend\\src\\main\\java"); // 输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.example") // 设置父包名
                            .entity("entity") // 设置实体类包名
                            .mapper("mapper") // 设置 Mapper 接口包名
                            .service("service") // 设置 Service 接口包名
                            .serviceImpl("service.impl") // 设置 Service 实现类包名
                            .xml("mappers"); // 设置 Mapper XML 文件包名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("db_account", "db_account_details", "db_topic", "db_topic_comment", "db_topic_type") // 设置需要生成的表名
                            .addTablePrefix("db_")
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .enableTableFieldAnnotation() // 启用字段注解
                            .controllerBuilder()
                            .enableRestStyle(); // 启用 REST 风格
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用 Freemarker 模板引擎
                .execute(); // 执行生成
    }
}
