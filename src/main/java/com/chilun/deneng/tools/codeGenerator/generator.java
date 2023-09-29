package com.chilun.deneng.tools.codeGenerator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

import java.sql.Types;
import java.util.Collections;

/**
 * @auther 齿轮
 * @create 2023-09-09-11:02
 */
public class generator {
    public static void main(String[] args) {
        FastAutoGenerator.create(new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/deneng_space", "root", "20030322"))
                .dataSourceConfig(
                        builder -> {
                            builder
                                    .schema("mybatis-plus")
                                    .keyWordsHandler(new MySqlKeyWordsHandler())
                                    .typeConvertHandler(
                                            (globalConfig, typeRegistry, metaInfo) -> {
                                                int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                                                if (typeCode == Types.SMALLINT) {
                                                    // 自定义类型转换
                                                    return DbColumnType.INTEGER;
                                                }
                                                return typeRegistry.getColumnType(metaInfo);
                                            }
                                    );
                        }
                )
                .globalConfig(
                        builder -> {
                            builder
                                    .author("chilun")
//                                    .enableSwagger()
                                    .commentDate("2023-09-28")//注释日期
                                    .outputDir("D:\\编程文件\\Idea_文件\\workspace_Idea\\others\\deneng_space\\src\\main\\java");
                        }
                )
                .packageConfig(
                        builder -> {
                            builder
                                    .parent("com.chilun.deneng")
                                    .entity("pojo")
                                    .mapper("dao")
                                    .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\编程文件\\Idea_文件\\workspace_Idea\\others\\deneng_space\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                        }
                )
                .templateConfig(builder -> {
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .injectionConfig(builder -> {
                })
                .strategyConfig(
                        builder -> {
                            builder
                                    .enableCapitalMode()
                                    .enableSkipView()
                                    .addInclude("article","recycled_task","sendback_task","study_record","task","task_force","task_pool","task_user","user");
                            builder.entityBuilder()
                                    .enableFileOverride()
                                    .enableLombok();
                            builder.controllerBuilder()
                                    .enableFileOverride()
                                    .enableRestStyle();
                            builder.serviceBuilder()
                                    .enableFileOverride();
                        }
                )
                .execute();
    }
}
