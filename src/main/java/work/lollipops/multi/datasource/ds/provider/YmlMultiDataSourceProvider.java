package work.lollipops.multi.datasource.ds.provider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import work.lollipops.multi.datasource.spring.boot.autoconfigure.DataSourceProperty;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author zhaohaoren
 */
@Slf4j
@AllArgsConstructor
public class YmlMultiDataSourceProvider extends AbstractDataSourceProvider {

    /**
     * 所有数据源
     */
    private final Map<String, DataSourceProperty> dataSourcePropertiesMap;

    @Override
    public Map<String, DataSource> loadDataSources() {
        return createDataSourceMap(dataSourcePropertiesMap);
    }
}
