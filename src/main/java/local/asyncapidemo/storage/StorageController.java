package local.asyncapidemo.storage;

import io.minio.*;
import io.minio.messages.Item;
import local.asyncapidemo.s3Client.S3Props;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StorageController {

    @Autowired
    MinioClient mc;

    @Autowired
    S3Props props;

    @PostMapping(path="/storage", produces = "application/json")
    public @ResponseBody String store(@RequestParam("file")MultipartFile[] files) {
        checkBucket();
        Map<String,String> metadata = new HashMap<>();
        metadata.put("username","yuriichv");
        metadata.put("type","TopSecret");

        for (MultipartFile file : files) {
            try {
                mc.putObject(
                        PutObjectArgs.builder()
                                .bucket(props.getS3_bucket())
                                .object(props.NEW_DIR+"/" +file.getOriginalFilename())
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .userMetadata(metadata)
                                .build());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GetMapping(path="/documents/")
    @ResponseBody
    public List<FileEntity> listNew(){
            List<FileEntity> fileEntities = new ArrayList<>();
            //get current state on startup
            Iterable<Result<Item>> results = mc.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(props.getS3_bucket())
                            .prefix("")
                            .recursive(true)
                            .includeUserMetadata(true)
                            .includeVersions(true)
                            .useApiVersion1(false)
                            .build());
        for (Result<Item> result:results){
            try {
                fileEntities.add(new FileEntity(result.get().objectName(), result.get().userMetadata()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileEntities;
    }

    private void checkBucket(){
        try {
            if (!mc.bucketExists(BucketExistsArgs.builder()
                    .bucket(props.getS3_bucket())
                    .build()))
                mc.makeBucket(MakeBucketArgs.builder()
                        .bucket(props.getS3_bucket())
                        .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
