package habittracker.configservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PropertySource {
    private String name;
    private Source source;

    public PropertySource(String name, Source source) {
        this.name = name;
        this.source = source;
    }
}
