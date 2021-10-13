package local.asyncapidemo.changelog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Controller("/")
public class ChangelogController {

    @Autowired
    ChangelogEntity changelogEntity;

    @GetMapping(path="/changelog", produces = "application/json")
    public @ResponseBody String getChangeLog() {
        return changelogEntity.getChanges().stream().map(Object::toString).collect(Collectors.joining(", "));
    }

}
