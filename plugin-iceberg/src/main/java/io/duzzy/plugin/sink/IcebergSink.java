package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.iceberg.CatalogType;
import io.duzzy.plugin.serializer.IcebergParquetSerializer;
import java.util.Map;
import java.util.UUID;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.DataFile;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.io.FileIO;
import org.apache.iceberg.io.OutputFile;

public class IcebergSink extends Sink<OutputFile> {

  private final String namespace;
  private final String table;
  private final String warehouseLocation;
  private final CatalogType catalogType;

  private Table icebergTable;
  private FileIO io;

  @JsonCreator
  public IcebergSink(
      @JsonProperty("serializer") IcebergParquetSerializer serializer,
      @JsonProperty("namespace") String namespace,
      @JsonProperty("table") String table,
      @JsonProperty("warehouse_location")
      @JsonAlias({"warehouseLocation", "warehouse-location"})
      String warehouseLocation,
      @JsonProperty("catalog_type")
      @JsonAlias({"catalogType", "catalog-type"})
      CatalogType catalogType
  ) {
    super(serializer);
    this.namespace = namespace;
    this.table = table;
    this.warehouseLocation = warehouseLocation;
    this.catalogType = catalogType;
  }

  @Override
  public OutputFile outputSupplier() {
    final Map<String, String> properties = Map.of(
        CatalogType.NAMESPACE, namespace,
        CatalogProperties.WAREHOUSE_LOCATION, warehouseLocation
    );
    final Catalog catalog = catalogType.getCatalog(properties);
    final Namespace space = Namespace.of(namespace);
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
        warehouseLocation,
        catalogType
    );
  }

  @Override
  public void close() throws Exception {
    super.close();
    if (io != null) {
      io.close();
    }
    if (icebergTable != null) {
      final DataFile dataFile = ((IcebergParquetSerializer) serializer).getDataFile();
      icebergTable.newAppend().appendFile(dataFile).commit();
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
