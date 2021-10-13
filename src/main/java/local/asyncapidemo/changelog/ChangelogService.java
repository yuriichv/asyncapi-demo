package local.asyncapidemo.changelog;

import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import local.asyncapidemo.kafkaAdapter.model.S3EventPayload;
import local.asyncapidemo.kafkaAdapter.service.PublisherService;
import local.asyncapidemo.s3Client.S3Props;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Service
public class ChangelogService {

    @Autowired
    MinioClient mc;

    @Autowired
    S3Props props;

    @Autowired
    PublisherService kafkaService;

    public void processNewEvent(String bucket, String objectName){
        //do some logic here
        //...
        moveObject(bucket,objectName, props.COMPLETED_DIR);
    }

    public void moveObject(String bucket, String objectName, String targetPath){
        CopySource src = CopySource.builder()
                .bucket(bucket)
                .object(objectName)
                .build();
        try {
            mc.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucket)
                            .object(targetPath+objectName.substring(props.NEW_DIR.length()))
                            .source(src)
                            .build()
            );
            mc.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processEvent(String event, ZonedDateTime eventTime, String bucket, String objectName){
        //do some logic here
        //...
       // S3Event s3Event = new S3Event();
        S3EventPayload s3EventPayload = new S3EventPayload();
        s3EventPayload.setBucket(bucket);
        s3EventPayload.setObject(objectName);
        s3EventPayload.setEventType(S3EventPayload.EventTypeEnum.fromValue(event.replaceAll(":","_")));
        s3EventPayload.setTimestamp(Timestamp.from(eventTime.toInstant()).getTime());
        kafkaService.publishEvent(1,s3EventPayload);
    }
}
