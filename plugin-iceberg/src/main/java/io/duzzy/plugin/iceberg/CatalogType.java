package io.duzzy.plugin.iceberg;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.hive.HiveCatalog;
import org.apache.iceberg.jdbc.JdbcCatalog;
import org.apache.iceberg.nessie.NessieCatalog;
import org.apache.iceberg.rest.RESTCatalog;

public enum CatalogType {
  REST,
  JDBC,
  HADOOP,
  HIVE,
  NESSIE;

  public static final String NAMESPACE = "NAMESPACE";

  public Catalog getCatalog(Map<String, String> properties) {
    return switch (this) {
      case REST -> new RESTCatalog();
      case JDBC -> new JdbcCatalog();
      case HADOOP -> hadoopCatalog(properties);
      case HIVE -> new HiveCatalog();
      case NESSIE -> new NessieCatalog();
    };
  }

  private static Catalog hadoopCatalog(Map<String, String> properties) {
    final Configuration conf = new Configuration();
    final HadoopCatalog hadoopCatalog =
        new HadoopCatalog(conf, properties.get(CatalogProperties.WAREHOUSE_LOCATION));
    final Namespace space = Namespace.of(properties.get(NAMESPACE));
    if (!hadoopCatalog.namespaceExists(space)) {
      hadoopCatalog.createNamespace(space);
    }
    return hadoopCatalog;
  }
}
