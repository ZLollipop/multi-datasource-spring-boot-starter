package work.lollipops.multi.datasource.ds;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import work.lollipops.multi.datasource.ds.provider.MultiDataSourceProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhaohaoren
 */

@Slf4j
@Data
public class MultiDataSource extends AbstractDataSource implements InitializingBean, DisposableBean {

    private String primary = "master";

    @Setter
    private MultiDataSourceProvider provider;

    /**
     * 所有数据库
     */
    private final Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();


    /**
     * 添加数据源
     *
     * @param ds         数据源名称
     * @param dataSource 数据源
     */
    public synchronized void addDataSource(String ds, DataSource dataSource) {
        if (!dataSourceMap.containsKey(ds)) {
            dataSourceMap.put(ds, dataSource);
            log.info("multi-datasource - load a datasource named [{}] success", ds);
        } else {
            log.warn("multi-datasource - load a datasource named [{}] failed, because it already exist", ds);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加并分组数据源
        Map<String, DataSource> dataSources = provider.loadDataSources();
        for (Map.Entry<String, DataSource> dsItem : dataSources.entrySet()) {
            addDataSource(dsItem.getKey(), dsItem.getValue());
        }
    }


    @Override
    public void destroy() throws Exception {
        log.info("dynamic-datasource start closing ....");
        for (Map.Entry<String, DataSource> item : dataSourceMap.entrySet()) {
            closeDataSource(item.getValue());
        }
        log.info("dynamic-datasource all closed success,bye");
    }

    /**
     * 关闭数据源。
     * 这里只处理hikari的，其他暂时不处理
     */
    private void closeDataSource(DataSource dataSource) throws Exception {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSourceMap.get(primary).getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return dataSourceMap.get(primary).getConnection(username, password);
    }
}
