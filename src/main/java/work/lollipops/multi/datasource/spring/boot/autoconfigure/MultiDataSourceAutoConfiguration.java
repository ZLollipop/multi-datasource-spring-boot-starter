package work.lollipops.multi.datasource.spring.boot.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;

/**
 * @author zhaohaoren
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(MultiDataSourceProperties.class)
public class MultiDataSourceAutoConfiguration implements BeanFactoryPostProcessor {

    private final MultiDataSourceProperties properties;

    @SneakyThrows
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, DataSourceProperty> dataSourcePropertyMap = properties.getDatasource();

        for (Map.Entry<String, DataSourceProperty> entry : dataSourcePropertyMap.entrySet()) {
            String dsName = entry.getKey();
            DataSourceProperty dsProperty = entry.getValue();
            DataSource dataSource = DataSourceBuilder.create().url(dsProperty.getUrl())
                    .username(dsProperty.getUsername())
                    .password(dsProperty.getPassword())
                    .type(dsProperty.getType())
                    .driverClassName(dsProperty.getDriverClassName())
                    .build();
            beanFactory.registerSingleton(dsName, dataSource);

            MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
            factory.setDataSource(dataSource);
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(dsProperty.getMapperLocations());
            factory.setMapperLocations(resources);

            beanFactory.registerSingleton("ds1", new DruidDataSource());


        }
    }
}
