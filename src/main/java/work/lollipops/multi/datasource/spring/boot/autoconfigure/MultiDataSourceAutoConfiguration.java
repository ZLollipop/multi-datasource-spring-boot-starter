package work.lollipops.multi.datasource.spring.boot.autoconfigure;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import work.lollipops.multi.datasource.ds.MultiDataSource;
import work.lollipops.multi.datasource.ds.provider.MultiDataSourceProvider;
import work.lollipops.multi.datasource.ds.provider.YmlMultiDataSourceProvider;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author zhaohaoren
 */
@Slf4j
@Configuration
@AllArgsConstructor
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import(value = {MultiDataSourceCreatorAutoConfiguration.class})
@EnableConfigurationProperties(MultiDataSourceProperties.class)
public class MultiDataSourceAutoConfiguration {

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


}
