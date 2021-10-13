package local.asyncapidemo.changelog;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChangelogEntity {
    private List<ChangeEntity> changes;

    public ChangelogEntity() {
        this.changes = new ArrayList<ChangeEntity>();
    }

    public List<ChangeEntity> getChanges() {
        return changes;
    }

    public void setChanges(List<ChangeEntity> changes) {
        this.changes = changes;
    }
}
