package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.FileSink;
import io.duzzy.core.sink.Sink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;

@Documentation(
    identifier = "io.duzzy.plugin.sink.GoogleCloudStorageSink",
    description = "Sink data to Google Cloud Storage (GCS)",
    module = "io.duzzy.plugin-gcp",
    duzzyType = DuzzyType.SINK,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "serializer",
            description = "The serializer to use"
        ),
        @Parameter(
            name = "project_id",
            aliases = {"projectId", "project-id"},
            description = "The GCP project ID"
        ),
        @Parameter(
            name = "credentials_file",
            aliases = {"credentialsFile", "credentials-file"},
            description = "Path to the GCP credentials file"
        ),
        @Parameter(
            name = "bucket_name",
            aliases = {"bucketName", "bucket-name"},
            description = "The GCS bucket name"
        ),
        @Parameter(
            name = "object_name",
            aliases = {"objectName", "object-name"},
            description = "The GCS object name"
        ),
        @Parameter(
            name = "compression_algorithm",
            aliases = {"compressionAlgorithm", "compression-algorithm"},
            description = "The compression algorithm to use for the file, "
                + "available options are: NONE, BZIP2, GZIP, ZSTD. "
                + "If not specified, no compression will be applied.",
            defaultValue = "NONE"
        ),
        @Parameter(
            name = "size_of_file",
            aliases = {"sizeOfFile", "size-of-file"},
            description = "The size of the file in bytes. "
                + "If not specified, the file will not be limited by size."
        ),
        @Parameter(
            name = "rows_per_file",
            aliases = {"rowsPerFile", "rows-per-file"},
            description = "The number of rows per file. "
                + "If not specified, the file will not be limited by number of rows."
        )
    },
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.GoogleCloudStorageSink"
          project_id: "my-project-id"
          credentials_file: "/path/to/credentials.json"
          bucket_name: "my-bucket"
          object_name: "data/output.json.gz"
          compression_algorithm: "GZIP"
          serializer:
            identifier: "io.duzzy.plugin.serializer.JsonSerializer"
        """
)
public class GoogleCloudStorageSink extends FileSink {

  private WriteChannel writer;
  private Storage storage;
  private final String bucketName;
  private final String projectId;
  private final String credentialsFile;

  @JsonCreator
  public GoogleCloudStorageSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("project_id")
      @JsonAlias({"projectId", "project-id"})
      String projectId,
      @JsonProperty("credentials_file")
      @JsonAlias({"credentialsFile", "credentials-file"})
      String credentialsFile,
      @JsonProperty("bucket_name")
      @JsonAlias({"bucketName", "bucket-name"})
      String bucketName,
      @JsonProperty("object_name")
      @JsonAlias({"objectName", "object-name"})
      String objectName,
      @JsonProperty("compression_algorithm")
      @JsonAlias({"compressionAlgorithm", "compression-algorithm"})
      CompressionAlgorithm compressionAlgorithm,
      @JsonProperty("size_of_file")
      @JsonAlias({"sizeOfFile", "size-of-file"})
      Long sizeOfFile,
      @JsonProperty("rows_per_file")
      @JsonAlias({"rowsPerFile", "rows-per-file"})
      Long rowsPerFile
  ) {
    super(serializer, objectName, compressionAlgorithm, sizeOfFile, rowsPerFile);
    this.bucketName = bucketName;
    this.projectId = projectId;
    this.credentialsFile = credentialsFile;
  }

  @Override
  protected OutputStream createOutputStream() throws IOException {
    storage = buildStorage();
    final BlobId blobId = BlobId.of(bucketName, incrementedName());
    final BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
    writer = storage.writer(blobInfo, Storage.BlobWriteOption.doesNotExist());
    return Channels.newOutputStream(writer);
  }

  @Override
  public void close() throws Exception {
    super.close();
    if (writer != null) {
      writer.close();
    }
    if (storage != null) {
      storage.close();
    }
  }

  @Override
  public Sink fork(long id) throws Exception {
    return new GoogleCloudStorageSink(
        getSerializer().fork(id),
        projectId,
        credentialsFile,
        bucketName,
        forkedName(id),
        getCompressionAlgorithm(),
        getSizeOfFile(),
        getRowsPerFile()
    );
  }

  Storage buildStorage() throws IOException {
    final StorageOptions options = StorageOptions.getDefaultInstance();
    final StorageOptions.Builder builder = StorageOptions.newBuilder();
    final Credentials credentials = credentialsFile == null ? options.getCredentials() :
        GoogleCredentials.fromStream(new FileInputStream(credentialsFile));
    if (credentials != null) {
      builder.setCredentials(credentials);
    }
    return builder
        .setProjectId(projectId == null ? options.getProjectId() : projectId)
        .build()
        .getService();
  }
}
