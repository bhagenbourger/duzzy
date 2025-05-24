package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.IcebergParquetSerializer;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.DataFile;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.Table;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.io.OutputFile;
import org.apache.iceberg.types.Types;

public class IcebergSink extends Sink<OutputFile> {

  private final String namespace;
  private final String table;
  private final String path;

  private Table icebergTable;

  @JsonCreator
  public IcebergSink(
      @JsonProperty("serializer") IcebergParquetSerializer serializer,
      @JsonProperty("namespace") String namespace,
      @JsonProperty("table") String table,
      @JsonProperty("path") String path
  ) {
    super(serializer);
    this.namespace = namespace;
    this.table = table;
    this.path = path;
  }

  @Override
  public OutputFile outputSupplier() {
    final Catalog catalog = hadoopCatalog();
    final Namespace space = Namespace.of(namespace);
    final TableIdentifier tableIdentifier = TableIdentifier.of(space, table);
    Schema schema = new Schema(
        Types.NestedField.optional(1, "c1", Types.IntegerType.get()),
        Types.NestedField.optional(2, "c2", Types.StringType.get())
    );
    icebergTable = catalog.tableExists(tableIdentifier) ? catalog.loadTable(tableIdentifier) :
        catalog.createTable(tableIdentifier, schema, PartitionSpec.unpartitioned());
    return icebergTable.io().newOutputFile(path);
  }

  @Override
  public IcebergSink fork(Long threadId) {
    return new IcebergSink(
        (IcebergParquetSerializer) serializer,
        namespace,
        table,
        path
    );
  }

  @Override
  public void close() throws Exception {
    super.close();
    final DataFile dataFile = ((IcebergParquetSerializer) serializer).getWriter().toDataFile();
    icebergTable.newAppend().appendFile(dataFile).commit();
  }

  private Catalog hadoopCatalog() {
    final Configuration conf = new Configuration();
    final String warehousePath = "/tmp/test-iceberg";
    final HadoopCatalog hadoopCatalog = new HadoopCatalog(conf, warehousePath);
    final Namespace space = Namespace.of(namespace);
    if (!hadoopCatalog.namespaceExists(space)) {
      hadoopCatalog.createNamespace(space);
    }
    return hadoopCatalog;
  }

  public Table getIcebergTable() {
    return icebergTable;
  }
}
