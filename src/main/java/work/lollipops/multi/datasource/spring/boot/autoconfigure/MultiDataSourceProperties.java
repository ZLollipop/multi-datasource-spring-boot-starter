package work.lollipops.multi.datasource.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import work.lollipops.multi.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import work.lollipops.multi.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author zhaohaoren
 */

@Data
@ConfigurationProperties(prefix = MultiDataSourceProperties.PREFIX)
public class MultiDataSourceProperties {

    public static final String PREFIX = "spring.datasource.multi";

    /**
     * 必须设置默认的库,默认master
     */
    private String primary = "master";
    /**
     * 每一个数据源
     */
    private Map<String, DataSourceProperty> datasource = new LinkedHashMap<>();
    /**
     * 是否启用严格模式,默认不启动. 严格模式下未匹配到数据源直接报错, 非严格模式下则使用默认数据源primary所设置的数据源
     */
    private Boolean strict = false;
    /**
     * Druid全局参数配置
     */
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();
    /**
     * HikariCp全局参数配置
     */
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();
}
