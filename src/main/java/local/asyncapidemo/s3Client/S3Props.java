package local.asyncapidemo.s3Client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "demos3.s3")
public class S3Props {

    public final String NEW_DIR = "new";
    public final String COMPLETED_DIR = "completed";
    public final String ERROR_DIR = "errors";

    private String s3_endpoint;
    private String s3_client;
    private String s3_secret;
    private String s3_bucket;

    public String getS3_bucket() {
        return s3_bucket;
    }

    public void setS3_bucket(String s3_bucket) {
        this.s3_bucket = s3_bucket;
    }

    public String getS3_endpoint() {
        return s3_endpoint;
    }

    public void setS3_endpoint(String s3_endpoint) {
        this.s3_endpoint = s3_endpoint;
    }

    public String getS3_client() {
        return s3_client;
    }

    public void setS3_client(String s3_client) {
        this.s3_client = s3_client;
    }

    public String getS3_secret() {
        return s3_secret;
    }

    public void setS3_secret(String s3_secret) {
        this.s3_secret = s3_secret;
    }
}
