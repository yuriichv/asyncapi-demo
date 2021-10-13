package local.asyncapidemo.storage;

import java.util.Map;

public class FileEntity {
    private String filename;
    private Map<String, String> metadata;

    public FileEntity(String filename, Map<String, String> metadata) {
        this.filename = filename;
        this.metadata= metadata;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
