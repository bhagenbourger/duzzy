package io.duzzy.plugin.sink;

import static io.duzzy.core.sink.FileSink.addFilePart;

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
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

@Documentation(
    identifier = "io.duzzy.plugin.sink.GcsSink",
    description = "Sink data to Google Cloud Storage (GCS)",
    module = "io.duzzy.plugin-gcp-gcs",
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
        )
    },
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.GcsSink"
          project_id: "my-project-id"
          credentials_file: "/path/to/credentials.json"
          bucket_name: "my-bucket"
          object_name: "data/output.json"
          serializer:
            identifier: "io.duzzy.plugin.serializer.JsonSerializer"
        """
)
public class GcsSink extends Sink {

  private WriteChannel writer;
  private Storage storage;
  private final String bucketName;
  private final String objectName;
  private final String projectId;
  private final String credentialsFile;

  @JsonCreator
  public GcsSink(
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
      String objectName
  ) {
    super(serializer);
    this.bucketName = bucketName;
    this.objectName = objectName;
    this.projectId = projectId;
    this.credentialsFile = credentialsFile;
  }

  GcsSink(
      Serializer<?> serializer,
      String bucketName,
      String objectName,
      String projectId,
      String credentialsFile,
      Storage storage
  ) {
    super(serializer);
    this.bucketName = bucketName;
    this.objectName = objectName;
    this.projectId = projectId;
    this.credentialsFile = credentialsFile;
    this.storage = storage;
  }

  @Override
  public OutputStream outputStreamSupplier() {
    return new ByteArrayOutputStream();
  }

  @Override
  public Sink fork(Long threadId) throws Exception {
    return new GcsSink(
        serializer.fork(threadId),
        bucketName,
        addFilePart(objectName, threadId),
        projectId,
        credentialsFile,
        storage
    );
  }

  @Override
  protected ByteArrayOutputStream getOutputStream() throws IOException {
    return (ByteArrayOutputStream) super.getOutputStream();
  }

  @Override
  public void init(DuzzySchema duzzySchema, Long rowCount) throws Exception {
    if (storage == null) {
      storage = buildStorage(projectId, credentialsFile);
    }
    writer = buildWriteChannel(bucketName, objectName);
    super.init(duzzySchema, rowCount);
  }

  @Override
  public void write(DuzzyRow row) throws Exception {
    super.write(row);
    writer.write(ByteBuffer.wrap(getOutputStream().toByteArray()));
    getOutputStream().reset();
  }

  @Override
  public void close() throws Exception {
    serializer.close();
    if (getOutputStream().size() > 0) {
      writer.write(ByteBuffer.wrap(getOutputStream().toByteArray()));
    }
    getOutputStream().close();
    writer.close();
    storage.close();
  }

  private WriteChannel buildWriteChannel(String bucketName, String objectName) {
    final BlobId blobId = BlobId.of(bucketName, objectName);
    final BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
    return storage.writer(blobInfo, Storage.BlobWriteOption.doesNotExist());
  }

  private static Storage buildStorage(String projectId, String credentialsFile) throws IOException {
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
