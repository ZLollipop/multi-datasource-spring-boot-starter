package work.lollipops.multi.datasource.spring.boot.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import work.lollipops.multi.datasource.ds.MultiDataSource;
import work.lollipops.multi.datasource.ds.provider.MultiDataSourceProvider;
import work.lollipops.multi.datasource.ds.provider.YmlMultiDataSourceProvider;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhaohaoren
 */
@Slf4j
@Configuration
@AllArgsConstructor
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import(value = {MultiDataSourceCreatorAutoConfiguration.class})
@EnableConfigurationProperties(MultiDataSourceProperties.class)
public class MultiDataSourceAutoConfiguration/* implements BeanFactoryPostProcessor*/ {

    private final MultiDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public MultiDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
        // 创建这个类的时候，需要creator的Bean，所以才使用了Creator的AutoConfig，但是我觉得这个应该是否要去掉？
        MultiDataSourceProvider sourceProvider = new YmlMultiDataSourceProvider(datasourceMap);
        System.out.println("你好阿");
        return sourceProvider;
    }


    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(MultiDataSourceProvider multiDataSourceProvider) {
        MultiDataSource dataSource = new MultiDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setProvider(multiDataSourceProvider);
        return dataSource;
    }

//    @Override
//    @SneakyThrows
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//
//        MultiDataSource dataSource = beanFactory.getBean(MultiDataSource.class);
//        dataSource.getDataSourceMap().forEach((k, v) -> {
//            try {
//                MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
//                factory.setDataSource(v);
//                String mapperLocations = properties.getDatasource().get(k).getMapperLocations();
//                Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocations);
//                factory.setMapperLocations(resources);
//                beanFactory.registerSingleton(k + "SqlSessionFactory", Objects.requireNonNull(factory.getObject()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
}