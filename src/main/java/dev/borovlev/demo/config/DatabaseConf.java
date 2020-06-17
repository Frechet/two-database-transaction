package dev.borovlev.demo.config;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class DatabaseConf {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurerMysql() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setAnnotationClass(Mapper.class);
        configurer.setBasePackage("dev.borovlev.demo.dao.mysql");
        configurer.setSqlSessionTemplateBeanName("sqlMysqlSessionTemplate");
        return configurer;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurerPostgres() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setAnnotationClass(Mapper.class);
        configurer.setBasePackage("dev.borovlev.demo.dao.postgres");
        configurer.setSqlSessionTemplateBeanName("sqlPostgresSessionTemplate");
        return configurer;
    }

    @Bean(name = "sqlPostgresSessionTemplate")
    public SqlSessionTemplate sqlPostgresSessionTemplate(
            ResourcePatternResolver applicationContext,
            DataSource dataSource
    ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(
                applicationContext.getResources("classpath:mybatis/postgres/mappers/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(
                applicationContext.getResource("classpath:mybatis/postgres/mybatis-config.xml"));
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        return new SqlSessionTemplate(Objects.requireNonNull(sqlSessionFactory));
    }

    @Bean(name = "sqlMysqlSessionTemplate")
    public SqlSessionTemplate sqlMysqlSessionTemplate(
            ResourcePatternResolver applicationContext,
            DataSource mysqlDataSource
    ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mysqlDataSource);
        sqlSessionFactoryBean.setMapperLocations(
                applicationContext.getResources("classpath:mybatis/mysql/mappers/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(
                applicationContext.getResource("classpath:mybatis/mysql/mybatis-config.xml"));
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        return new SqlSessionTemplate(Objects.requireNonNull(sqlSessionFactory));
    }

    @Bean
    public ChainedTransactionManager transactionManager(DataSource dataSource, DataSource mysqlDataSource) {
        DataSourceTransactionManager mysqlManager = new DataSourceTransactionManager(mysqlDataSource);
        DataSourceTransactionManager postgresManager = new DataSourceTransactionManager(dataSource);
        return new ChainedTransactionManager(postgresManager, mysqlManager);
    }

    @Bean
    public DataSource dataSource(Environment env) {
        return DataSourceBuilder.create()
                .url(env.getProperty("spring.datasource.url"))
                .username(env.getProperty("spring.datasource.username"))
                .password(env.getProperty("spring.datasource.password"))
                .build();
    }

    @Bean
    public DataSource mysqlDataSource(Environment env) {
        return DataSourceBuilder.create()
                .url(env.getProperty("spring.second-datasource.url"))
                .username(env.getProperty("spring.second-datasource.username"))
                .password(env.getProperty("spring.second-datasource.password"))
                .build();
    }
}
