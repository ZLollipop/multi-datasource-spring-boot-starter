package work.lollipops.multi.datasource.ds.provider;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 专门用来处理数据源的，从properties 里面的配置加载数据源
 *
 * @author zhaohaoren
 */
public interface MultiDataSourceProvider {

    /**
     * 加载所有数据源
     *
     * @return 所有数据源，key为数据源名称
     */
    Map<String, DataSource> loadDataSources();
}
