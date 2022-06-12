package com.fingerchar.db.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.fingerchar.db.base.BaseEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Generator {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/fingernft-db/src/main/java");
        gc.setOpen(false);
        gc.setAuthor("");
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.1.111:3306/github_fingernft?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("github_fingernft");
        dsc.setPassword("github_fingernft");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("db");
        pc.setParent("com.fingerchar");
        pc.setEntity("domain");
        pc.setMapper("dao");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {

            }
        };

        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();


        // 调整 xml生成目录演示
        /*focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
            	return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });*/
        cfg.setFileCreate((configBuilder, fileType, filePath) -> {
            //如果是Entity则直接返回true表示写文件
            if (fileType == FileType.ENTITY) {
                return true;
            }
            //否则先判断文件是否存在
            File file = new File(filePath);
            boolean exist = file.exists();
            if (!exist) {
                file.getParentFile().mkdirs();
            }
            //文件不存在或者全局配置的fileOverride为true才写文件
            return !exist || configBuilder.getGlobalConfig().isFileOverride();
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
        tc.setService(null);
        tc.setServiceImpl(null);
        tc.setEntity("entity.java"); //domain
        tc.setMapper("mapper.java");
        tc.setController(null);
        tc.setXml(null);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //设置命名规则（驼峰式）
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //设置表列命名规则（驼峰式）
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //设置entity父类
        strategy.setSuperEntityClass(BaseEntity.class);
        //设置lombok（看个人习惯）
        strategy.setEntityLombokModel(false);
        //是否生成@Resetcontroller
        strategy.setRestControllerStyle(false);
        // 公共父类
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns(new String[]{"id", "deleted", "create_time", "update_time"});
        //设置需要生成的表
        strategy.setInclude(new String[]{
                "fc_admin_department",
                "fc_admin_job",
                "fc_admin_log",
                "fc_admin_notice",
                "fc_admin_permission",
                "fc_admin_role",
                "fc_admin_storage",
                "fc_admin_token",
                "fc_admin_user",
                "fc_contract",
                "fc_contract_nft",
                "fc_nft_category",
                "fc_nft_items",
                "fc_nft_like",
                "fc_notice",
                "fc_order",
                "fc_order_log",
                "fc_pay_token",
                "fc_storage",
                "fc_system",
                "fc_tx_order",
                "fc_user",
                "fc_user_follow",
                "fc_user_log",
                "fc_user_token"
        });
        strategy.setEntitySerialVersionUID(false);
        strategy.setEntityColumnConstant(true);
        //strategy.setControllerMappingHyphenStyle(true);
        //strategy.setTablePrefix("vr_");
        strategy.setEntityTableFieldAnnotationEnable(true);
        mpg.setStrategy(strategy);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        mpg.setTemplate(tc);
        // 执行生成
        mpg.execute();
    }
}
