package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.iceberg.CatalogType;
import io.duzzy.plugin.serializer.IcebergParquetSerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.io.FileIO;
import org.apache.iceberg.io.OutputFile;

public class IcebergSink extends Sink<OutputFile> {

  public static final String CATALOG_NAME = "CATALOG_NAME";

  private final String namespace;
  private final String table;
  private final CatalogType catalogType;
  private final Map<String, String> catalogProperties;

  private Table icebergTable;
  private FileIO io;

  @JsonCreator
  public IcebergSink(
      @JsonProperty("serializer")
      IcebergParquetSerializer serializer,
      @JsonProperty("namespace")
      String namespace,
      @JsonProperty("table")
      String table,
      @JsonProperty("catalog_type")
      @JsonAlias({"catalogType", "catalog-type"})
      CatalogType catalogType,
      @JsonProperty("catalog_properties")
      @JsonAlias({"catalogProperties", "catalog-properties"})
      Map<String, String> catalogProperties
  ) {
    super(serializer);
    this.namespace = namespace;
    this.table = table;
    this.catalogType = catalogType;
    this.catalogProperties =
        catalogProperties == null ? new HashMap<>() : new HashMap<>(catalogProperties);
  }

  @Override
  public OutputFile outputSupplier() {
    final Namespace space = Namespace.of(namespace);
    final Catalog catalog = catalogType.getCatalog(space, catalogProperties);
    final TableIdentifier tableIdentifier = TableIdentifier.of(space, table);
    icebergTable = catalog.tableExists(tableIdentifier) ? catalog.loadTable(tableIdentifier) :
        catalog.createTable(tableIdentifier, getSerializer().getSchema(),
            PartitionSpec.unpartitioned());
    final String filepath = icebergTable.location() + "/" + UUID.randomUUID() + ".parquet";
    io = icebergTable.io();
    return io.newOutputFile(filepath);
  }

  @Override
  public IcebergSink fork(Long threadId) throws Exception {
    return new IcebergSink(
        (IcebergParquetSerializer) serializer.fork(threadId),
        namespace,
        table,
        catalogType,
        catalogProperties
    );
  }

  @Override
  public void close() throws Exception {
    super.close();
    if (io != null) {
      io.close();
    }
    if (icebergTable != null) {
      icebergTable.newAppend().appendFile(getSerializer().getDataFile()).commit();
    }
  }

  @Override
  public IcebergParquetSerializer getSerializer() {
    return (IcebergParquetSerializer) serializer;
  }

  public Table getIcebergTable() {
    return icebergTable;
  }
}
