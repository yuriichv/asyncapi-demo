package local.asyncapidemo.changelog;

import io.minio.*;
import io.minio.messages.Event;
import io.minio.messages.Item;
import io.minio.messages.NotificationRecords;
import local.asyncapidemo.s3Client.S3Props;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChangelogListener{

    //https://docs.min.io/docs/minio-bucket-notification-guide.html
    String[] eventsNew = {"s3:ObjectCreated:Put"};
    String[] eventsAll = {"s3:ObjectCreated:*", "s3:ObjectAccessed:*", "s3:ObjectRemoved:*"};

    MinioClient mc;
    S3Props props;

    @Autowired
    ChangelogService changelogService;

    public ChangelogListener(@Autowired MinioClient mc, @Autowired S3Props props) {
        this.mc = mc;
        this.props=props;
        new Thread(() ->checkNew()).start();
        new Thread(() ->listenEvents()).start();
    }

    public void checkNew() {
        //TODO: если будет ошибка (например нет бакета) то свалится
        //get current state on startup
        Iterable<Result<Item>> results = mc.listObjects(
                ListObjectsArgs.builder()
                        .bucket(props.getS3_bucket())
                        .prefix(props.NEW_DIR)
                        .recursive(true)
                        .build());

        for (Result<Item> object:results){
            try {
                if (!object.get().isDir()) {
                    changelogService.processNewEvent(props.getS3_bucket(), object.get().objectName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       // results.forEach(result -> changelogService.processEvent(result.get().objectName()));

        // listen for new events
        try (CloseableIterator<Result<NotificationRecords>> ci =
                     mc.listenBucketNotification(
                             ListenBucketNotificationArgs.builder()
                                     .bucket(props.getS3_bucket())
                                     .prefix(props.NEW_DIR)
                                     .suffix("")
                                     .events(eventsNew)
                                     .build())) {
            // ci.forEachRemaining();
            while (ci.hasNext()) {
                NotificationRecords records = ci.next().get();
                for (Event event : records.events()) {
                    System.out.println("Event " + event.eventType() + " occurred at " + event.eventTime()
                            + " for " + event.bucketName() + "/" + event.objectName());
                    changelogService.processNewEvent(props.getS3_bucket(), event.objectName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listenEvents(){
        //TODO: этот try может завершить loop если что-то внутри пойдет не так. добавить снаружи while(true)?
        while (true) {
            // listen for new events
            try (CloseableIterator<Result<NotificationRecords>> ci =
                         mc.listenBucketNotification(
                                 ListenBucketNotificationArgs.builder()
                                         .bucket(props.getS3_bucket())
                                         .prefix("")
                                         .suffix("")
                                         .events(eventsAll)
                                         .build())) {
                while (ci.hasNext()) {
                    NotificationRecords records = ci.next().get();
                    for (Event event : records.events()) {
                        changelogService.processEvent(event.eventType().toString(), event.eventTime(), event.bucketName(), event.objectName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
