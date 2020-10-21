package work.lollipops.multi.datasource.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.Ordered;
import work.lollipops.multi.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import work.lollipops.multi.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author zhaohaoren
 */

@Data
@EnableConfigurationProperties(MultiDataSourceProperties.class)
@ConfigurationProperties(prefix = MultiDataSourceProperties.PREFIX)
public class MultiDataSourceProperties {

    public static final String PREFIX = "spring.datasource.multi";
//    public static final String HEALTH = PREFIX + ".health";

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
     * 是否使用p6spy输出，默认不输出
     */
    private Boolean p6spy = false;
    /**
     * 是否使用开启seata，默认不开启
     */
    private Boolean seata = false;
//    /**
//     * seata使用模式，默认AT
//     */
//    private SeataMode seataMode = SeataMode.AT;
    /**
     * 是否使用 spring actuator 监控检查，默认不检查
     */
    private boolean health = false;
//    /**
//     * 多数据源选择算法clazz，默认负载均衡算法
//     */
//    private Class<? extends DynamicDataSourceStrategy> strategy = LoadBalanceDynamicDataSourceStrategy.class;
    /**
     * aop切面顺序，默认优先级最高
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;
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

//    /**
//     * 全局默认publicKey
//     */
//    private String publicKey = CryptoUtils.DEFAULT_PUBLIC_KEY_STRING;
    /**
     * aop 切面是否只允许切 public 方法
     */
    private boolean allowedPublicOnly = true;
}
