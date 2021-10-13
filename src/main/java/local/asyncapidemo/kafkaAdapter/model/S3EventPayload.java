package local.asyncapidemo.kafkaAdapter.model;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;


public class S3EventPayload {
    
    private @Valid long timestamp;
    
    public enum EventTypeEnum {
            
        S3_OBJECTCREATED_PUT(String.valueOf("s3_ObjectCreated_Put")),
            
        S3_OBJECTCREATED_POST(String.valueOf("s3_ObjectCreated_Post")),
            
        S3_OBJECTCREATED_COPY(String.valueOf("s3_ObjectCreated_Copy")),
            
        S3_OBJECTREMOVED_DELETE(String.valueOf("s3_ObjectRemoved_Delete")),
            
        S3_OBJECTACCESSED_GET(String.valueOf("s3_ObjectAccessed_Get")),
            
        S3_OBJECTACCESSED_HEAD(String.valueOf("s3_ObjectAccessed_Head"));
            
        private String value;

        EventTypeEnum (String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static EventTypeEnum fromValue(String value) {
            for ( EventTypeEnum b :  EventTypeEnum.values()) {
                if (Objects.equals(b.value, value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    private @Valid EventTypeEnum eventType;
    
    private @Valid String bucket;
    
    private @Valid String object;
    

    

    /**
     * timestamp.
     */
    @JsonProperty("timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    

    /**
     * event types as they appear in minio. https://docs.min.io/docs/minio-bucket-notification-guide.html
     */
    @JsonProperty("event_type")
    public EventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEnum eventType) {
        this.eventType = eventType;
    }
    

    
    @JsonProperty("bucket")
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    

    
    @JsonProperty("object")
    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        S3EventPayload s3EventPayload = (S3EventPayload) o;
        return 
            Objects.equals(this.timestamp, s3EventPayload.timestamp) &&
            Objects.equals(this.eventType, s3EventPayload.eventType) &&
            Objects.equals(this.bucket, s3EventPayload.bucket) &&
            Objects.equals(this.object, s3EventPayload.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, eventType, bucket, object);
    }

    @Override
    public String toString() {
        return "class S3EventPayload {\n" +
        
                "    timestamp: " + toIndentedString(timestamp) + "\n" +
                "    eventType: " + toIndentedString(eventType) + "\n" +
                "    bucket: " + toIndentedString(bucket) + "\n" +
                "    object: " + toIndentedString(object) + "\n" +
                "}";
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
           return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}