/*
 * Copyright 2022 yoga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yoga.jarvis.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Types;
import java.util.Collections;

/**
 * @Description: 代码生成器工具类
 * @Author: yoga
 * @Date: 2024/8/20 11:32
 */
@Slf4j
public class CodeGenerator {

    public static void main(String[] args) {
        // 需要生成的表名，英文逗号分割
        String tableNames = "user";
        // 数据库schema
        String schemaName = "jarvis";
        // 模块名
        String moduleName = "";
        // 生成文件的父包名
        String packageParentName = "org.yoga.jarvis.repository";

        // 项目路径
        Path projectPath = Paths.get(System.getProperty("user.dir"));
        log.info("当前项目路径为:{}", projectPath);
        // 生成文件
        FastAutoGenerator.create(
                        // 数据源配置，dbUrl username password
                        new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/" + schemaName, "root", "123456")
                                .schema(schemaName)
                                // 增加数据库连接属性
                                // 是否使用 Unicode 转换
                                .addConnectionProperty("useUnicode", "true")
                                // 指定字符编码方式
                                .addConnectionProperty("characterEncoding", "UTF-8")
                                // 指定是否对数据库连接使用SSL加密
                                .addConnectionProperty("useSSL", "false")
                                // 将'0000-00-00 00:00:00'或'0000-00-00'转换为NULL
                                .addConnectionProperty("zeroDateTimeBehavior", "convertToNull")
                                // 指定服务器时区
                                .addConnectionProperty("serverTimezone", "GMT+8")
                                // 在指定的数据库中查找需要的表
                                .addConnectionProperty("nullCatalogMeansCurrent", "true")
                                // 添加下面两个配置，可以读取数据库表备注
                                .addConnectionProperty("remarks", "true")
                                .addConnectionProperty("useInformationSchema", "true")
                                // 定义转换类型
                                // smallint和tinyint都转为integer
                                .typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                                    log.info("字段名 {}, 类型 {}, {}", metaInfo.getColumnName(),
                                            metaInfo.getJdbcType().TYPE_CODE, typeRegistry.getColumnType(metaInfo));
                                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                                    if (typeCode == Types.SMALLINT || typeCode == Types.TINYINT) {
                                        // 自定义类型转换
                                        return DbColumnType.INTEGER;
                                    }
                                    return typeRegistry.getColumnType(metaInfo);
                                }))
                // 全局配置
                .globalConfig(builder -> builder
                        // 作者名称
                        .author("yoga")
                        // 输出目录
                        .outputDir(projectPath + "/src/main/java")
                        // 时间类型对应策略
                        .dateType(DateType.TIME_PACK)
                        // 禁止打开输出目录
                        .disableOpenDir())
                // 包配置
                .packageConfig(builder -> builder
                        // 指定模块名
                        .moduleName(moduleName)
                        // 指定父包名
                        .parent(packageParentName)
                        // 指定xml文件路径
                        .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper")))
                // 策略撇脂
                .strategyConfig(builder -> builder
                        // 开启大写命名
                        .enableCapitalMode()
                        // 需要生成的表名
                        .addInclude(tableNames)
                        // 启用 schema
                        .enableSchema()
                        // Entity配置
                        .entityBuilder()
                        // 开启lombok模型
                        .enableLombok()
                        // 开启链式模型
                        .enableChainModel()
                        // 开启生成字段常量
                        .enableColumnConstant()
                        // 数据库表映射到实体的命名策略，下划线->驼峰
                        .naming(NamingStrategy.underline_to_camel)
                        // Entity名称
                        .formatFileName("%sDO")
                        // 指定生成的主键的ID类型
                        .idType(IdType.AUTO)
                        // 覆盖已有文件
                        .enableFileOverride()
                        // Mapper+xml配置
                        .mapperBuilder()
                        // 标记 Mapper 注解
                        .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class)
                        // xml生成baseResultMap
                        .enableBaseResultMap()
                        // Mapper名称
                        .formatMapperFileName("%sMapper")
                        // xml名称
                        .formatXmlFileName("%sMapper")
                        // 覆盖已有文件
                        .enableFileOverride()
                        // service+impl配置
                        .serviceBuilder()
//                        // service名称
//                        .formatServiceFileName("%sDaoService")
//                        // serviceImpl名称
//                        .formatServiceImplFileName("%sDaoServiceImpl")
                        // 不生成service
                        .disable()
                        // controller配置
                        .controllerBuilder()
                        // 不生成controller
                        .disable())
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

}
