package local.asyncapidemo.s3Client;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Autowired
    S3Props props;

    @Bean
    MinioClient minioClient(){
        MinioClient client =  MinioClient.builder()
                .endpoint(props.getS3_endpoint())
                .credentials(props.getS3_client(), props.getS3_secret())
                .region("ap-northeast-1")
                .build();
        return client;
    }
/*
    S3Client s3Client(){
        AWSCredentials credentials = new BasicAWSCredentials("YOUR-ACCESSKEYID", "YOUR-SECRETACCESSKEY");
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");
        URI s3URI = null;
        try {
            s3URI = new URI(props.getS3_endpoint());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        S3Client s3Client = S3Client.builder()
                .endpointOverride(s3URI)
                .
                .credentialsProvider()
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
                .region(region)
                .build();

    }

 */
}
