package io.duzzy.plugin.iceberg;

import static io.duzzy.plugin.sink.IcebergSink.CATALOG_NAME;

import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.CatalogUtil;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.SupportsNamespaces;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.hive.HiveCatalog;
import org.apache.iceberg.jdbc.JdbcCatalog;
import org.apache.iceberg.nessie.NessieCatalog;
import org.apache.iceberg.rest.RESTCatalog;

public enum CatalogType {
  REST,
  JDBC,
  HADOOP,
  HIVE, // Keep ?
  NESSIE;

  public Catalog getCatalog(Namespace namespace, Map<String, String> properties) {
    return switch (this) {
      case HADOOP -> buildCatalog(HadoopCatalog.class, namespace, properties);
      case HIVE -> buildCatalog(HiveCatalog.class, namespace, properties);
      case JDBC -> buildCatalog(JdbcCatalog.class, namespace, properties);
      case NESSIE -> buildCatalog(NessieCatalog.class, namespace, properties);
      case REST -> buildCatalog(RESTCatalog.class, namespace, properties);
    };
  }

  private static Catalog buildCatalog(
      Class<? extends Catalog> implementation,
      Namespace namespace,
      Map<String, String> properties
  ) {
    properties.put(CatalogProperties.CATALOG_IMPL, implementation.getName());
    final Catalog catalog = CatalogUtil.buildIcebergCatalog(
        properties.get(CATALOG_NAME),
        properties,
        new Configuration()
    );
    if (catalog instanceof SupportsNamespaces) {
      if (!((SupportsNamespaces) catalog).namespaceExists(namespace)) {
        ((SupportsNamespaces) catalog).createNamespace(namespace);
      }
    }
    return catalog;
  }
}
